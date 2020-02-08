package bluejam.hobby.gekitsui.judge

import bluejam.hobby.gekitsui.judge.tool.JudgeStatus
import bluejam.hobby.gekitsui.judge.tool.JudgeSuite
import bluejam.hobby.gekitsui.webapp.entity.SubmissionRepository
import org.apache.commons.logging.LogFactory
import org.springframework.stereotype.Component

@Component
class JudgeWorker(
        val submissionRepository: SubmissionRepository,
        judgeSuites: Array<JudgeSuite>
) {
    private final val judgeSuiteMap: Map<String, JudgeSuite> = mapOf(
            *judgeSuites.map { Pair(it.problemId, it) }.toTypedArray()
    )

    private final val logger = LogFactory.getLog(JudgeWorker::class.java)

    fun judge(submissionId: String) {
        val submission = submissionRepository.findById(submissionId.toLong()).orElse(null)

        if (submission == null) {
            logger.error("Received an invalid submission ID from the queue: $submissionId")

            return
        }

        submission.status = JudgeStatus.EXECUTING

        submissionRepository.save(submission)

        val judgeSuite = judgeSuiteMap[submission.problem.name]
                ?: throw NotImplementedError("JudgeSuite of ${submission.problem.name} is not implemented")

        val status = judgeSuite.run(submission.testcase)

        submission.status = status

        submissionRepository.save(submission)
    }
}
