package bluejam.hobby.gekitsui.webapp.controller.api

import bluejam.hobby.gekitsui.judge.tool.JudgeStatus
import bluejam.hobby.gekitsui.webapp.GekitsuiWebappApplication
import bluejam.hobby.gekitsui.webapp.controller.BadRequestException
import bluejam.hobby.gekitsui.webapp.controller.ForbiddenException
import bluejam.hobby.gekitsui.webapp.controller.ServiceUnavailableException
import bluejam.hobby.gekitsui.webapp.controller.UnauthorizedException
import bluejam.hobby.gekitsui.webapp.entity.ProblemRepository
import bluejam.hobby.gekitsui.webapp.entity.Submission
import bluejam.hobby.gekitsui.webapp.entity.SubmissionRepository
import bluejam.hobby.gekitsui.webapp.repository.UserRepository
import bluejam.hobby.gekitsui.webapp.util.isAccessibleToProblem
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

data class SubmissionPayload(val problemName: String, val testcase: String)

@Controller
class SubmitController(
        val userRepository: UserRepository,
        val problemRepository: ProblemRepository,
        val submissionRepository: SubmissionRepository,
        val rabbitTemplate: RabbitTemplate,
        @Value("\${gekitsui.submission.max-testcase-size:2000}") val maxTestcaseSize: Long
) {
    @PostMapping("/api/submit")
    fun post(
            @RequestParam(required = false) contestName: String?,
            @ModelAttribute submissionPayload: SubmissionPayload,
            @AuthenticationPrincipal principal: OAuth2AuthenticationToken
    ): String {
        if (principal.principal !is OAuth2User) {
            return "redirect:/"
        }

        val oAuth2User = principal.principal as OAuth2User
        val githubId = oAuth2User.name?.toInt() ?: return "redirect:/"

        val user = userRepository.findByGithubId(githubId)
                ?: throw UnauthorizedException("The user with name = $githubId has not signed up")
        val problem = problemRepository.findByName(submissionPayload.problemName)
                ?: throw BadRequestException("There is no problem with name = ${submissionPayload.problemName}")

        if (!isAccessibleToProblem(principal, problem)) {
            throw ForbiddenException("The user doesn't have permission to see the problem")
        }

        val testcase = submissionPayload.testcase.replace("\r\n", "\n")

        if (testcase.length > maxTestcaseSize) {
            throw BadRequestException("Test case is too large.")
        }

        val submission = submissionRepository.save(Submission(
                user,
                problem,
                testcase,
                JudgeStatus.WAITING_JUDGE
        ))
        val submissionId = submission.id ?: throw ServiceUnavailableException("Submission ID is null.")

        rabbitTemplate.convertAndSend(
                GekitsuiWebappApplication.TOPIC_EXCHANGE_NAME,
                "",
                submissionId.toString()
        )

        return if (contestName == null) {
            "redirect:/submission/${submission.id}"
        } else {
            "redirect:/contest/${contestName}/submission/${submission.id}"
        }
    }
}
