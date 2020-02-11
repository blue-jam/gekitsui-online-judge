package bluejam.hobby.gekitsui.webapp.controller

import bluejam.hobby.gekitsui.webapp.entity.Problem
import bluejam.hobby.gekitsui.webapp.entity.ProblemRepository
import org.springframework.data.domain.Sort
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.*
import javax.transaction.Transactional

@Controller
@RequestMapping("/admin")
class AdminController(val problemRepository: ProblemRepository) {
    @RequestMapping("/")
    fun doGetIndex(model: Model, @AuthenticationPrincipal principal: OAuth2User): String = "admin/admin_index"

    @GetMapping("/problems")
    fun doGetProblems(
            model: Model
    ): String {
        model["problems"] = problemRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))

        return "admin/admin_problems"
    }

    @GetMapping("/problem/{problemName}")
    fun doGetProblem(
            model: Model,
            @PathVariable problemName: String
    ): String {
        model["problem"] = problemRepository.findByName(problemName)
                ?: throw NotFoundException("There is no problem with name = $problemName")

        return "admin/problem_form"
    }

    @GetMapping("/create_problem")
    fun doGetCreateProblems(model: Model): String = "admin/problem_form"

    @PostMapping("/api/create_or_update_problem")
    fun createOrUpdateProblem(@ModelAttribute problem: Problem): String {
        problemRepository.save(problem)

        return "redirect:/admin/problem/${problem.name}"
    }

    // TODO: Use @DeleteMapping
    @PostMapping("/api/delete_problem")
    @Transactional
    fun deleteProblem(@RequestParam problemName: String): String {
        problemRepository.removeByName(problemName)

        return "redirect:/admin/problems"
    }
}
