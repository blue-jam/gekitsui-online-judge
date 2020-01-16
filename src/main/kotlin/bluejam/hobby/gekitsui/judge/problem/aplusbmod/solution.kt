package bluejam.hobby.gekitsui.judge.problem.aplusbmod

import bluejam.hobby.gekitsui.judge.tool.JudgeSuite
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

val A_PLUS_B_MOD_SUITE = JudgeSuite("a_plus_b_mod", "A + B mod 10", ::aPlusBModCorrect, listOf(::aPlusBModWrong))
