package bluejam.hobby.gekitsui.judge.problem.aplusbmod

import bluejam.hobby.gekitsui.judge.tool.JudgeSuite
import bluejam.hobby.gekitsui.judge.tool.validator.InStream
import java.lang.StringBuilder
import java.util.*

private fun aPlusBModCorrect(sc: Scanner): String {
    val a = sc.nextInt()
    val b = sc.nextInt()

    return StringBuilder()
            .appendln((a + b) % 10)
            .toString()
}

private fun aPlusBModWrong(sc: Scanner): String {
    val a = sc.nextInt()
    val b = sc.nextInt()

    return StringBuilder()
            .appendln(a + b)
            .toString()
}

private fun validate(inStream: InStream) {
    inStream.readInt(0, 9)
    inStream.readSpace()

    inStream.readInt(0, 9)
    inStream.readLineFeed()

    inStream.expectEndOfInput()
}

val A_PLUS_B_MOD_SUITE = JudgeSuite(
        "a_plus_b_mod",
        "A + B mod 10",
        ::aPlusBModCorrect,
        listOf(::aPlusBModWrong),
        ::validate
)
