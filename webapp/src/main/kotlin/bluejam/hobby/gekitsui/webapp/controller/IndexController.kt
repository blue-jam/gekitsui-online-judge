package bluejam.hobby.gekitsui.webapp.controller

import bluejam.hobby.gekitsui.webapp.entity.User
import bluejam.hobby.gekitsui.webapp.repository.UserRepository
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import java.security.Principal

@Controller
class IndexController(val userRepository: UserRepository) {

    @GetMapping("/user")
    fun user(
            principal: Principal
    ): Principal {
        return principal
    }

    @GetMapping("/")
    fun index(
            model: Model,
            @AuthenticationPrincipal oauth2User: OAuth2User
    ): String {
        val githubId = oauth2User.attributes["id"]?.toString()?.toInt()
        val username = oauth2User.attributes["login"]?.toString()

        if (githubId == null || username == null) {
            // TODO: Throw exception
            model["username"] = "unknown user"
            model["message"] = "Please try with a valid github account"
        } else {
            model["username"] = username

            if (userRepository.findByGithubId(githubId) == null) {
                userRepository.save(User(username, githubId))

                model["message"] = "Nice to meet you"
            } else {
                model["message"] = "How are you?"
            }
        }

        return "index"
    }
}
