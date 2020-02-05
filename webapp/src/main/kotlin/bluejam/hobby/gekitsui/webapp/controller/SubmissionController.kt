package bluejam.hobby.gekitsui.webapp.controller

import bluejam.hobby.gekitsui.webapp.entity.SubmissionRepository
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class SubmissionController(private val submissionRepository: SubmissionRepository) {
    @GetMapping("/submission/{id}")
    fun doGet(
            model: Model,
            @PathVariable id: Long
    ): String {
        val submission = submissionRepository.findById(id)
                .orElseThrow { NotFoundException("There is no submission with ID = $id") }

        model["testcase"] = submission.testcase
        model["authorName"] = submission.author.username
        model["problem"] = submission.problem
        model["submission"] = submission
        model["status"] = submission.status

        return "submission"
    }

    @GetMapping("/submissions")
    fun doGetSubmissions(
            model: Model
    ): String {
        val submissions = submissionRepository.findAll(Sort.by(Direction.DESC, "id"))

        model["submissions"] = submissions

        return "submissions"
    }
}
