package bluejam.hobby.gekitsui.webapp.controller.api

import bluejam.hobby.gekitsui.webapp.controller.NotFoundException
import bluejam.hobby.gekitsui.webapp.entity.Submission
import bluejam.hobby.gekitsui.webapp.entity.SubmissionRepository
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

@Controller
@RequestMapping("/api")
class SubmissionApiController(
        val submissionRepository: SubmissionRepository
) {
    @GetMapping("/submission")
    @ResponseBody
    fun fetchSubmission(
            @RequestParam id: Long
    ): Submission {
        return submissionRepository.findById(id)
                .orElseThrow { NotFoundException("There is no submission with id = $id") }
    }

    @GetMapping("/submissions")
    @ResponseBody
    fun fetchSubmissions(
            @RequestParam username: String
    ): List<Submission> {
        return submissionRepository.findByAuthorUsernameOrderByIdDesc(username)
    }
}
