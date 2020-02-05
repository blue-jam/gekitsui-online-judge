package bluejam.hobby.gekitsui.webapp.controller

import bluejam.hobby.gekitsui.webapp.entity.ProblemRepository
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class ProblemController(private val problemRepository: ProblemRepository) {
    @GetMapping("/problem/{problemName}")
    fun doGet(
            model: Model,
            @PathVariable problemName: String
    ): String {
        model["problem"] = problemRepository.findByName(problemName) ?:
                throw NotFoundException("There is no problem with name = $problemName")

        return "problem"
    }

    @GetMapping("/problems")
    fun doGetProblems(
            model: Model
    ): String {
        model["problems"] = problemRepository.findAll(Sort.by(Direction.ASC, "id"))

        return "problems"
    }
}
