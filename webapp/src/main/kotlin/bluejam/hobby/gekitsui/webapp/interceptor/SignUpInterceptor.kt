package bluejam.hobby.gekitsui.webapp.interceptor

import bluejam.hobby.gekitsui.webapp.entity.User
import bluejam.hobby.gekitsui.webapp.repository.UserRepository
import org.apache.commons.codec.digest.DigestUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class SignUpInterceptor(
        val userRepository: UserRepository,
        @Value("\${gekitsui.cookie.user-salt}") private val hashSalt: String
): HandlerInterceptor {
    companion object {
        const val COOKIE_NAME = "signed_up"
    }

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (handler is ResourceHttpRequestHandler) {
            return true
        }

        val principal = SecurityContextHolder.getContext().authentication?.principal ?: return true
        val attributes = (principal as DefaultOAuth2User).attributes
        val username = (attributes["login"] as String?) ?: return true
        val githubId = (attributes["id"] as Int?) ?: return true
        val hashedUsername = DigestUtils.sha256Hex(username + hashSalt)

        if (request.cookies.find { it.name == COOKIE_NAME }?.value == hashedUsername) {
            return true
        }

        response.addCookie(Cookie(COOKIE_NAME, hashedUsername))

        if (userRepository.findByGithubId(githubId) != null) {
            return true
        }

        userRepository.save(User(username, githubId))

        return true
    }
}
