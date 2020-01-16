package bluejam.hobby.gekitsui.webapp.controller.api

import bluejam.hobby.gekitsui.webapp.repository.UserRepository
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping

data class Submission (val problemId: Int, val testcase: String)

@Controller
class SubmitController(val userRepository: UserRepository) {
    @PostMapping("/api/submit")
    fun post(
            @ModelAttribute submission: Submission,
            @AuthenticationPrincipal oAuth2User: OAuth2User
    ): String {
        val githubId = oAuth2User.name?.toInt() ?: return "redirect:/"
        val user = userRepository.findByGithubId(githubId) ?: return "redirect:/"

        return "redirect:/"
    }
}
