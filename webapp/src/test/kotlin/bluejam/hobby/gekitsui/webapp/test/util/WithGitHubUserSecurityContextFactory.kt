package bluejam.hobby.gekitsui.webapp.test.util

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority
import org.springframework.security.test.context.support.WithSecurityContextFactory

class WithGitHubUserSecurityContextFactory: WithSecurityContextFactory<WithGitHubUser> {
    override fun createSecurityContext(annotation: WithGitHubUser?): SecurityContext {
        val securityContext = SecurityContextHolder.createEmptyContext()

        if (annotation == null) {
            return securityContext
        }

        val attributes = mutableMapOf(
                Pair("id", annotation.githubId),
                Pair("login", annotation.username)
        )
        val principal = object : OAuth2User {
            override fun getName(): String {
                return annotation.githubId.toString()
            }

            override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
                return mutableListOf(
                        SimpleGrantedAuthority("ROLE_USER"),
                        SimpleGrantedAuthority("SCOPE_read:user")
                )
            }

            override fun getAttributes(): MutableMap<String, Any> {
                return attributes
            }
        }

        securityContext.authentication = OAuth2AuthenticationToken(
                principal,
                listOf(
                        OAuth2UserAuthority("ROLE_USER", attributes),
                        SimpleGrantedAuthority("SCOPE_read:user")
                ),
                "github"
        )

        return securityContext
    }
}
