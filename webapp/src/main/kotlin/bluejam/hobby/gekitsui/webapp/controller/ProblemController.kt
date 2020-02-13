package bluejam.hobby.gekitsui.webapp.controller

import bluejam.hobby.gekitsui.webapp.entity.Problem
import bluejam.hobby.gekitsui.webapp.entity.ProblemRepository
import bluejam.hobby.gekitsui.webapp.entity.Visibility
import bluejam.hobby.gekitsui.webapp.util.GitHubOAuthFields
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class ProblemController(
        private val problemRepository: ProblemRepository
) {
    @GetMapping("/problem/{problemName}")
    fun doGet(
            model: Model,
            @PathVariable problemName: String,
            @AuthenticationPrincipal principal: OAuth2AuthenticationToken?
    ): String {
        val problem: Problem = problemRepository.findByName(problemName)
                ?: throw NotFoundException("There is no problem with name = $problemName")

        if (!isAccessibleToProblem(principal, problem)) {
            throw ForbiddenException("The user doesn't have permission to see the problem.")
        }

        model["problem"] = problem

        return "problem"
    }

    @GetMapping("/problems")
    fun doGetProblems(
            model: Model,
            @AuthenticationPrincipal principal: OAuth2AuthenticationToken?
    ): String {
        model["problems"] = problemRepository.findAll(Sort.by(Direction.ASC, "id"))
                .filter { isAccessibleToProblem(principal, it) }

        return "problems"
    }

    private fun isAccessibleToProblem(
            principal: OAuth2AuthenticationToken?,
            problem: Problem
    ): Boolean {
        if (problem.visibility == Visibility.PUBLIC) {
            return true
        }

        if (principal == null || principal.principal !is OAuth2User) {
            return false
        }

        val oAuth2User = principal.principal as OAuth2User

        if (problem.visibility == Visibility.PRIVATE) {
            val gitHubId = GitHubOAuthFields.getUserId(oAuth2User)

            if (!problem.writers.map { it.githubId }.contains(gitHubId)) {
                return false
            }
        }

        return true
    }
}
