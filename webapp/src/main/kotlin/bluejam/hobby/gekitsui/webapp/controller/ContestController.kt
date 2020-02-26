package bluejam.hobby.gekitsui.webapp.controller

import bluejam.hobby.gekitsui.webapp.entity.ContestRepository
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class ContestController(
        val contestRepository: ContestRepository
) {
    @GetMapping("/contests")
    fun contestList(
            model: Model
    ): String {
        model["contests"] = contestRepository.findAll(Sort.by(Sort.Direction.DESC, "startTime"))

        return "contest/contest_list"
    }

    @GetMapping("/contest/{name}")
    fun index(
            @PathVariable name: String,
            model: Model
    ): String {
        val contest = contestRepository.findByName(name) ?: throw NotFoundException("There is no contest with name = $name")

        model["contest"] = contest

        return "contest/contest_index"
    }
}
