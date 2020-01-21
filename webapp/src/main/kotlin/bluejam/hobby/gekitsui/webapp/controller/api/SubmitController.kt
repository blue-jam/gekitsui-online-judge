package bluejam.hobby.gekitsui.webapp.controller.api

import bluejam.hobby.gekitsui.judge.tool.JudgeStatus
import bluejam.hobby.gekitsui.webapp.GekitsuiWebappApplication
import bluejam.hobby.gekitsui.webapp.entity.ProblemRepository
import bluejam.hobby.gekitsui.webapp.entity.Submission
import bluejam.hobby.gekitsui.webapp.entity.SubmissionRepository
import bluejam.hobby.gekitsui.webapp.repository.UserRepository
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping

data class SubmissionPayload (val problemName: String, val testcase: String)

@Controller
class SubmitController(
        val userRepository: UserRepository,
        val problemRepository: ProblemRepository,
        val submissionRepository: SubmissionRepository,
        val rabbitTemplate: RabbitTemplate
) {
    @PostMapping("/api/submit")
    fun post(
            @ModelAttribute submissionPayload: SubmissionPayload,
            @AuthenticationPrincipal oAuth2User: OAuth2User
    ): String {
        val githubId = oAuth2User.name?.toInt() ?: return "redirect:/"

        val user = userRepository.findByGithubId(githubId)
        val problem = problemRepository.findByName(submissionPayload.problemName)

        if (user == null || problem == null) {
            throw Exception()
        }

        val testcase = submissionPayload.testcase.replace("\r\n", "\n")
        val status = JudgeStatus.WAITING_JUDGE //judgeSuite.run(testcase)

        val submission = submissionRepository.save(Submission(
                user,
                problem,
                testcase,
                status
        ))
        val submissionId = submission.id ?: throw Exception("Submission ID is null")

        rabbitTemplate.convertAndSend(
                GekitsuiWebappApplication.TOPIC_EXCHANGE_NAME,
                "",
                submissionId.toString()
        )

        return "redirect:/submission/${submission.id}"
    }
}
