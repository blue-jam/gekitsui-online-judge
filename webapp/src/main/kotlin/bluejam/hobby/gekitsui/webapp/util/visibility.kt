package bluejam.hobby.gekitsui.webapp.util

import bluejam.hobby.gekitsui.webapp.entity.Problem
import bluejam.hobby.gekitsui.webapp.entity.Visibility
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.user.OAuth2User


fun checkIsAccessibleToProblem(
        principal: OAuth2AuthenticationToken?,
        problem: Problem
): Boolean {
    if (principal == null || principal.principal !is OAuth2User) {
        return problem.visibility == Visibility.PUBLIC
    }

    if (principal.authorities.any { it.authority == "ROLE_ADMIN" }) {
        return true
    }

    val gitHubId = GitHubOAuthFields.getUserId(principal.principal as OAuth2User)

    return problem.visibility != Visibility.PRIVATE || problem.writers.map { it.githubId }.contains(gitHubId)
}
