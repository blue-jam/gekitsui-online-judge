package bluejam.hobby.gekitsui.webapp.util

import bluejam.hobby.gekitsui.webapp.entity.Problem
import bluejam.hobby.gekitsui.webapp.entity.Visibility
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.user.OAuth2User


fun isAccessibleToProblem(
        principal: OAuth2AuthenticationToken?,
        problem: Problem
): Boolean {
    if (problem.visibility == Visibility.PUBLIC) {
        return true
    }

    if (principal == null || principal.principal !is OAuth2User) {
        return false
    }

    if (principal.authorities.any { it.authority == "ROLE_ADMIN" }) {
        return true
    }

    val oAuth2User = principal.principal as OAuth2User

    if (problem.visibility == Visibility.PRIVATE) {
        val gitHubId = GitHubOAuthFields.getUserId(oAuth2User)

        return problem.writers.map { it.githubId }.contains(gitHubId)
    }

    return true
}
