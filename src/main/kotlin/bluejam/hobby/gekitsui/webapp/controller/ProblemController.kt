package bluejam.hobby.gekitsui.webapp.controller

import bluejam.hobby.gekitsui.webapp.entity.ProblemRepository
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class ProblemController(private val problemRepository: ProblemRepository) {
    @GetMapping("/problem/{problemName}")
    fun get(
            model: Model,
            @PathVariable problemName: String
    ): String {
        model["problem"] = problemRepository.findByName(problemName) ?: throw Exception()
        return "problem"
    }
}
