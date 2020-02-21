package bluejam.hobby.gekitsui.judge.tool

import bluejam.hobby.gekitsui.judge.tool.validator.InvalidFormatException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class JudgeSuiteTest {
    @Test
    fun `invalid format`() {
        val judgeSuite = JudgeSuite(
                "problemId",
                { "output" },
                listOf { _ -> "output" },
                { throw InvalidFormatException() }
        )

        assertThat(judgeSuite.run("input")).isEqualTo(JudgeStatus.INVALID_FORMAT)
    }

    @Test
    fun `fail on correct answer`() {
        val output = "output"
        val judgeSuite = JudgeSuite(
                "problemId",
                { output },
                listOf { _ -> output },
                { }
        )

        assertThat(judgeSuite.run("input")).isEqualTo(JudgeStatus.FAILED)
    }

    @Test
    fun `fail on one correct answer and one wrong answer`() {
        val output = "output"
        val judgeSuite = JudgeSuite(
                "problemId",
                { output },
                listOf({ _ -> output }, { _ -> "wrong output" }),
                { }
        )

        assertThat(judgeSuite.run("input")).isEqualTo(JudgeStatus.FAILED)
    }

    @Test
    fun success() {
        val judgeSuite = JudgeSuite(
                "problemId",
                { "output" },
                listOf({ _ -> "wrong output1" }, { _ -> "wrong output2" }),
                { }
        )

        assertThat(judgeSuite.run("input")).isEqualTo(JudgeStatus.ACCEPTED)
    }
}
