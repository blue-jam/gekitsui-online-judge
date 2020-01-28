package bluejam.hobby.gekitsui.webapp.service

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.stereotype.Service

@Service
class SecurityService {
    fun userid(): Int? {
        val principal = SecurityContextHolder.getContext().authentication?.principal ?: return null
        val attributes = (principal as DefaultOAuth2User).attributes

        return attributes["id"] as Int?
    }
}
