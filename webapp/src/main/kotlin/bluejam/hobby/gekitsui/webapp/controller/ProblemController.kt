package bluejam.hobby.gekitsui.webapp.controller

import bluejam.hobby.gekitsui.webapp.entity.Problem
import bluejam.hobby.gekitsui.webapp.entity.ProblemRepository
import bluejam.hobby.gekitsui.webapp.util.checkIsAccessibleToProblem
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
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

        if (!checkIsAccessibleToProblem(principal, problem)) {
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
                .filter { checkIsAccessibleToProblem(principal, it) }

        return "problems"
    }
}
