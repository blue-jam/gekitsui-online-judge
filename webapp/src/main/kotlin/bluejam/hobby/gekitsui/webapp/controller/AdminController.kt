package bluejam.hobby.gekitsui.webapp.controller

import bluejam.hobby.gekitsui.webapp.GekitsuiWebappApplication
import bluejam.hobby.gekitsui.webapp.entity.*
import bluejam.hobby.gekitsui.webapp.repository.UserRepository
import bluejam.hobby.gekitsui.webapp.service.TimeZoneConverter
import bluejam.hobby.gekitsui.webapp.util.GitHubOAuthFields
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.data.domain.Sort
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.transaction.Transactional

@Controller
@RequestMapping("/admin")
class AdminController(
        val userRepository: UserRepository,
        val problemRepository: ProblemRepository,
        val submissionRepository: SubmissionRepository,
        val contestRepository: ContestRepository,
        val rabbitTemplate: RabbitTemplate,
        val timeZoneConverter: TimeZoneConverter
) {
    @RequestMapping("/")
    fun doGetIndex(model: Model): String = "admin/admin_index"

    @GetMapping("/problems")
    fun doGetProblems(
            model: Model
    ): String {
        model["problems"] = problemRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))

        return "admin/admin_problems"
    }

    @GetMapping("/problem/{problemName}")
    fun doGetProblem(
            model: Model,
            @PathVariable problemName: String
    ): String {
        model["problem"] = problemRepository.findByName(problemName)
                ?: throw NotFoundException("There is no problem with name = $problemName")
        model["visibilityOptions"] = Visibility.values()

        return "admin/problem_form"
    }

    @GetMapping("/create_problem")
    fun doGetCreateProblems(model: Model): String {
        model["visibilityOptions"] = Visibility.values()

        return "admin/problem_form"
    }

    @PostMapping("/api/create_or_update_problem")
    fun createOrUpdateProblem(
            @ModelAttribute problem: Problem,
            @AuthenticationPrincipal oAuth2User: OAuth2User
    ): String {
        if (problem.id == null) {
            val gitHubId = GitHubOAuthFields.getUserId(oAuth2User) ?: throw UnauthorizedException("GitHub ID is missing.")
            val author = userRepository.findByGithubId(gitHubId) ?: throw UnauthorizedException("The user with github ID = $gitHubId has not registered.")
            problem.writers.add(author)
        }

        problemRepository.save(problem)

        return "redirect:/admin/problem/${problem.name}"
    }

    // TODO: Use @DeleteMapping
    @PostMapping("/api/delete_problem")
    @Transactional
    fun deleteProblem(@RequestParam problemName: String): String {
        problemRepository.removeByName(problemName)

        return "redirect:/admin/problems"
    }

    @GetMapping("/rejudge")
    fun doGetRejudge(
            model: Model
    ): String = "admin/rejudge"

    @PostMapping("/api/rejudge")
    fun rejudge(
            @RequestParam(required = false) problemName: String?,
            @RequestParam(required = false) submissionId: Long?
    ): String {
        val submissions: List<Submission> = when {
            problemName != null -> {
                submissionRepository.findByProblemName(problemName)
            }
            submissionId != null -> {
                listOf(
                        submissionRepository.findById(submissionId)
                                .orElseThrow { throw BadRequestException("There is no submission with id=${submissionId}") }
                )
            }
            else -> {
                throw BadRequestException("The request has neither problemName nor submissionId")
            }
        }

        submissions.forEach { submission ->
            rabbitTemplate.convertAndSend(
                    GekitsuiWebappApplication.TOPIC_EXCHANGE_NAME,
                    "",
                    submission.id.toString()
            )
        }

        return "redirect:/admin/rejudge"
    }

    @GetMapping("/create_contest")
    fun doGetContestCreate(): String = "admin/contest_form"

    @GetMapping("/contest/{contestName}")
    fun contestForm(
            model: Model,
            @PathVariable contestName: String
    ): String {
        val contest = contestRepository.findByName(contestName) ?: throw BadRequestException("There is no problem with name = $contestName")

        model["contest"] = contest
        model["problemNames"] = contest.problemSet.joinToString(", ") { it.name }
        model["startTime"] = DateTimeFormatter.ISO_DATE_TIME.format(timeZoneConverter.convertToLocalDateTime(contest.startTime))
        model["endTime"] = DateTimeFormatter.ISO_DATE_TIME.format(timeZoneConverter.convertToLocalDateTime(contest.endTime))

        return "admin/contest_form"
    }

    @PostMapping("/api/create_or_update_contest")
    fun createOrUpdateContest(
            @RequestParam(required = false) id: Long?,
            @RequestParam name: String,
            @RequestParam title: String,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) startTime: LocalDateTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) endTime: LocalDateTime,
            @RequestParam problemNames: String
    ): String {
        val contest: Contest

        val startTimestamp = timeZoneConverter.convertToServerTimestamp(startTime)
        val endTimestamp = timeZoneConverter.convertToServerTimestamp(endTime)
        val problemSet = problemNames.split(",").map {
            val problemName = it.trim()

            problemRepository.findByName(problemName) ?: throw BadRequestException("There is no problem with name = $problemName")
        }.toMutableList()

        if (id == null) {
            contest = Contest(name, title, startTimestamp, endTimestamp, problemSet)
        } else {
            contest = contestRepository.findById(id).orElseThrow { throw BadRequestException("There is no contest with ID = $id") }
            contest.name = name
            contest.title = title
            contest.startTime = startTimestamp
            contest.endTime = endTimestamp
            contest.problemSet = problemSet
        }

        contestRepository.save(contest)

        return "redirect:/admin/contest/$name"
    }
}
