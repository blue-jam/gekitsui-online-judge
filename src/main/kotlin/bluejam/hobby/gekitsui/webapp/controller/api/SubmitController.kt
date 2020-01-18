package bluejam.hobby.gekitsui.webapp.controller.api

import bluejam.hobby.gekitsui.judge.problem.aplusbmod.A_PLUS_B_MOD_SUITE
import bluejam.hobby.gekitsui.webapp.entity.ProblemRepository
import bluejam.hobby.gekitsui.webapp.entity.Submission
import bluejam.hobby.gekitsui.webapp.entity.SubmissionRepository
import bluejam.hobby.gekitsui.webapp.repository.UserRepository
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping

data class SubmissionPayload (val problemName: String, val testcase: String)

@Controller
class SubmitController(
        val userRepository: UserRepository,
        val problemRepository: ProblemRepository,
        val submissionRepository: SubmissionRepository
) {
    @PostMapping("/api/submit")
    fun post(
            model: Model,
            @ModelAttribute submissionPayload: SubmissionPayload,
            @AuthenticationPrincipal oAuth2User: OAuth2User
    ): String {
        val githubId = oAuth2User.name?.toInt() ?: return "redirect:/"

        val user = userRepository.findByGithubId(githubId)
        val problem = problemRepository.findByName(submissionPayload.problemName)

        if (user == null || problem == null) {
            throw Exception()
        }

        val status = A_PLUS_B_MOD_SUITE.run(submissionPayload.testcase)

        val submission = submissionRepository.save(Submission(
                user,
                problem,
                submissionPayload.testcase,
                status
        ))

        model["status"] = status.toString()
        model["title"] = problem.title

        return "redirect:/submission/${submission.id}"
    }
}
