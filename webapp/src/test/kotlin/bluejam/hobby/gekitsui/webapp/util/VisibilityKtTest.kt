package bluejam.hobby.gekitsui.webapp.util

import bluejam.hobby.gekitsui.webapp.entity.Problem
import bluejam.hobby.gekitsui.webapp.entity.User
import bluejam.hobby.gekitsui.webapp.entity.Visibility
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.user.DefaultOAuth2User

internal class VisibilityKtTest {
    private val writerId = 123

    @Test
    fun `Anonymous user can see public problem`() {
        assertTrue(isAccessibleToProblem(
                principal = null,
                problem = problem(Visibility.PUBLIC)
        ))
    }

    @Test
    fun `Anonymous user cannot see non-public problem`() {
        assertFalse(isAccessibleToProblem(
                principal = null,
                problem = problem(Visibility.PRIVATE)
        ))
    }

    @Test
    fun `User cannot see private problem`() {
        assertFalse(isAccessibleToProblem(principal(gitHubId = writerId + 10), problem(Visibility.PRIVATE)))
    }

    @Test
    fun `Writer can see their private problem`() {
        assertTrue(isAccessibleToProblem(principal(gitHubId = writerId), problem(Visibility.PRIVATE)))
    }

    @Test
    fun `Admin can see any private problem`() {
        assertTrue(isAccessibleToProblem(
                principal(gitHubId = writerId + 10, authorityNames = listOf("ROLE_ADMIN")),
                problem(Visibility.PRIVATE)
        ))
    }

    private fun problem(visibility: Visibility)
            = Problem("name", "title", "statement", visibility, mutableSetOf(User("writer", writerId)))

    private fun principal(gitHubId: Int, authorityNames: List<String> = emptyList()) =
            OAuth2AuthenticationToken(
                    DefaultOAuth2User(
                            listOf(SimpleGrantedAuthority("ROLE_USER")),
                            mapOf(Pair("id", gitHubId)),
                            "id"
                    ),
                    authorityNames.map { SimpleGrantedAuthority(it) },
                    "registrationId"
            )
}
