package bluejam.hobby.gekitsui.judge

import bluejam.hobby.gekitsui.judge.problem.TEST_SUITE_MAP
import bluejam.hobby.gekitsui.judge.tool.JudgeStatus
import bluejam.hobby.gekitsui.webapp.entity.SubmissionRepository
import org.springframework.stereotype.Component

@Component
class JudgeWorker(
        val submissionRepository: SubmissionRepository
) {
    fun judge(submissionId: String) {
        val submission = submissionRepository.findById(submissionId.toLong())
                .orElseThrow { Exception("Passed invalid submission ID to the queue") }

        submission.status = JudgeStatus.EXECUTING

        submissionRepository.save(submission)

        val judgeSuite = TEST_SUITE_MAP[submission.problem.name]
                ?: throw NotImplementedError("JudgeSuite of ${submission.problem.name} is not implemented")

        val status = judgeSuite.run(submission.testcase)

        submission.status = status

        submissionRepository.save(submission)
    }
}
