package bluejam.hobby.gekitsui.judge.problem.floydalgorithmwarshall

import bluejam.hobby.gekitsui.judge.tool.JudgeSuite
import bluejam.hobby.gekitsui.judge.tool.validator.InStream
import bluejam.hobby.gekitsui.judge.tool.validator.InvalidFormatException
import java.util.*
import kotlin.math.min

private const val INF = 10000000

private fun warshallFloyd(d: Array<IntArray>) {
    for (k in d.indices) for (i in d.indices) for (j in d.indices) {
        d[i][j] = min(d[i][j], d[i][k] + d[k][j])
    }
}

private fun warshallFloydIJK(d: Array<IntArray>) {
    for (i in d.indices) for (j in d.indices) for (k in d.indices) {
        d[i][j] = min(d[i][j], d[i][k] + d[k][j])
    }
}

private fun warshallFloydIKJ(d: Array<IntArray>) {
    for (i in d.indices) for (k in d.indices) for (j in d.indices) {
        d[i][j] = min(d[i][j], d[i][k] + d[k][j])
    }
}

private fun solve(scanner: Scanner, warshallFloyd: (Array<IntArray>) -> Unit): String {
    val N = scanner.nextInt()
    val M = scanner.nextInt()

    val d = Array(N) { i -> IntArray(N) { j -> if (i == j) 0 else INF } }

    for (i in 1..M) {
        val u = scanner.nextInt()
        val v = scanner.nextInt()
        val c = scanner.nextInt()

        d[u][v] = c
        d[v][u] = c
    }

    warshallFloyd(d)

    val S = scanner.nextInt()
    val T = scanner.nextInt()
    val ans = if (d[S][T] == INF) -1 else d[S][T]

    return "$ans\n"
}

private fun correctSolution(scanner: Scanner): String = solve(scanner, ::warshallFloyd)

private fun wrongSolution1(scanner: Scanner): String = solve(scanner, ::warshallFloydIJK)

private fun wrongSolution2(scanner: Scanner): String = solve(scanner, ::warshallFloydIKJ)

private const val MAX_N = 10
private const val MIN_C = 1
private const val MAX_C = 100

private fun validate(input: InStream) {
    val N = input.readInt(2, MAX_N)
    input.readSpace()
    val M = input.readInt(1, MAX_N * (MAX_N - 1) / 2)
    input.readLineFeed()

    val used = Array(N) { BooleanArray(N) { false } }

    for (i in 1..M) {
        val u = input.readInt(0, N - 1)
        input.readSpace()
        val v = input.readInt(0, N - 1)
        input.readSpace()
        input.readInt(MIN_C, MAX_C)
        input.readLineFeed()

        if (u == v) {
            throw InvalidFormatException("Self loop is not allowed: ($u, $v)")
        }

        if (used[u][v]) {
            throw InvalidFormatException("Duplicated edges: ($u, $v)")
        }

        used[u][v] = true
        used[v][u] = true
    }

    input.readInt(0, N - 1)
    input.readSpace()
    input.readInt(0, N - 1)
    input.readLineFeed()
    input.expectEndOfInput()
}

val FLOYD_ALGORITHM_WARSHALL = JudgeSuite(
        "floyd_algorithm_warshall",
        ::correctSolution,
        listOf(::wrongSolution1, ::wrongSolution2),
        ::validate
)
