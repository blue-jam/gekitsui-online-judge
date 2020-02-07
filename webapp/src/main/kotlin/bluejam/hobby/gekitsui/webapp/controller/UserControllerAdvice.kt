package bluejam.hobby.gekitsui.webapp.controller

import bluejam.hobby.gekitsui.webapp.util.GitHubOAuthFields
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ModelAttribute

@ControllerAdvice
class UserControllerAdvice {
    @ModelAttribute
    fun addUserName(model: Model) {
        val principal = SecurityContextHolder.getContext().authentication?.principal

        if (principal !is OAuth2User) {
            return
        }

        model["username"] = GitHubOAuthFields.getUserName(principal) ?: throw Exception("GitHub user doesn't have username.")
    }
}
