package bluejam.hobby.gekitsui.webapp.controller

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping

@Controller
class IndexController {
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
        } else {
            model["username"] = username
        }

        return "index"
    }
}
