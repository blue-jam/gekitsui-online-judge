package bluejam.hobby.gekitsui.judge.tool.validator

import java.lang.NumberFormatException
import java.nio.charset.Charset

class InStream(private val input: String) {
    private var cursor = 0

    init {
        if (input.any { !Charset.forName("US-ASCII").newEncoder().canEncode(input) }) {
            throw InvalidFormatException("Input contains non-ASCII character")
        }
    }

    fun readUntilNextBlank(): String {
        val nextBlankPosition = input.indexOfAny(charArrayOf(' ', '\n'), cursor)
        val end = if (nextBlankPosition < 0) input.length else nextBlankPosition
        val token = input.substring(cursor, end)
        cursor = end

        return token
    }

    fun readInt(min: Int, max: Int): Int {
        val value: Int
        try {
            value = readUntilNextBlank().toInt()
        } catch (e: NumberFormatException) {
            throw InvalidFormatException(e)
        }

        if (value < min || max < value) {
            throw InvalidFormatException("$value is not in [$min, $max].")
        }

        return value
    }

    fun readLong(min: Long, max: Long): Long {
        val value: Long
        try {
            value = readUntilNextBlank().toLong()
        } catch (e: NumberFormatException) {
            throw InvalidFormatException(e)
        }

        if (value < min || max < value) {
            throw InvalidFormatException("$value is not in [$min, $max]")
        }

        return value
    }

    fun readSpace() {
        if (cursor >= input.length || input[cursor] != ' ') {
            throw InvalidFormatException("A space is expected but another character is returned.")
        }
        cursor++
    }

    fun readLineFeed() {
        if (cursor >= input.length || input[cursor] != '\n') {
            throw InvalidFormatException("LF is expected but another character is returned.")
        }
        cursor++
    }

    fun expectEndOfInput() {
        if (cursor != input.length) {
            throw InvalidFormatException("End of input is expected. But there are unread characters.")
        }
    }
}
