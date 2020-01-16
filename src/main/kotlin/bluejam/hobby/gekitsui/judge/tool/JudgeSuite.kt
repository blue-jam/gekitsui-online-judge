package bluejam.hobby.gekitsui.judge.tool

import java.util.*

class JudgeSuite(
        val problemId: String,
        val title: String,
        private val referenceSolution: (Scanner) -> String,
        private val wrongSolutions: List<(Scanner) -> String>
) {
    fun run(testcase: String): JudgeStatus {
        val referenceOutput = referenceSolution(Scanner(testcase))
        val wrongOutputs = wrongSolutions.map { it(Scanner(testcase)) }

        return if (wrongOutputs.any { it == referenceOutput }) {
            JudgeStatus.FAILED
        } else {
            JudgeStatus.ACCEPTED
        }
    }
}
