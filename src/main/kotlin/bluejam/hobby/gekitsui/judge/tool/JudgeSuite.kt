package bluejam.hobby.gekitsui.judge.tool

import bluejam.hobby.gekitsui.judge.tool.validator.InStream
import bluejam.hobby.gekitsui.judge.tool.validator.InvalidFormatException
import java.util.*

class JudgeSuite(
        val problemId: String,
        private val referenceSolution: (Scanner) -> String,
        private val wrongSolutions: List<(Scanner) -> String>,
        private val validator: (InStream) -> Unit
) {
    fun run(testcase: String): JudgeStatus {
        try {
            validator(InStream(testcase))
        } catch (e: InvalidFormatException) {
            return JudgeStatus.INVALID_FORMAT
        }

        val referenceOutput = referenceSolution(Scanner(testcase))
        val wrongOutputs = wrongSolutions.map { it(Scanner(testcase)) }

        return if (wrongOutputs.any { it == referenceOutput }) {
            JudgeStatus.FAILED
        } else {
            JudgeStatus.ACCEPTED
        }
    }
}
