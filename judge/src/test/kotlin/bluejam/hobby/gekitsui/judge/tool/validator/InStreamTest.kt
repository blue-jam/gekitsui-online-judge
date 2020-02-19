package bluejam.hobby.gekitsui.judge.tool.validator

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class InStreamTest {
    @Test
    fun `accepts only ASCII characters`() {
        assertThrows(InvalidFormatException::class.java) { InStream("１２３ ＡＢＣ") }
    }

    @Test
    fun `read simple integer input`() {
        val inStream = InStream("""
            3 4
            5

        """.trimIndent())

        assertEquals(3, inStream.readInt(Int.MIN_VALUE, Int.MAX_VALUE))
        inStream.readSpace()
        assertEquals(4, inStream.readInt(Int.MIN_VALUE, Int.MAX_VALUE))
        inStream.readLineFeed()
        assertEquals(5, inStream.readInt(Int.MIN_VALUE, Int.MAX_VALUE))
        inStream.readLineFeed()
        inStream.expectEndOfInput()
    }

    @Test
    fun `read int value with range`() {
        val inStream = InStream("111")
        assertEquals(111, inStream.readInt(-42, 111))
    }

    @Test
    fun `read max int value with range`() {
        val inStream = InStream("123")
        assertEquals(123, inStream.readInt(-42, 123))
    }

    @Test
    fun `read min int value with range`() {
        val inStream = InStream("-42")
        assertEquals(-42, inStream.readInt(-42, 123))
    }

    @Test
    fun `read space`() {
        val inStream = InStream(" a")
        inStream.readSpace()
    }

    @Test
    fun `fail on read space with a wrong character`() {
        val inStream = InStream("a")
        assertThrows(InvalidFormatException::class.java) { inStream.readSpace() }
    }

    @Test
    fun `fail on read space with end of input`() {
        val inStream = InStream("")
        assertThrows(InvalidFormatException::class.java) { inStream.readSpace() }
    }

    @Test
    fun `expect end of input`() {
        val inStream = InStream("")
        inStream.expectEndOfInput()
    }

    @Test
    fun `fail on expect end of input`() {
        val inStream = InStream("123")
        assertThrows(InvalidFormatException::class.java) { inStream.expectEndOfInput() }
    }

    @Test
    fun `expect line feed`() {
        val inStream = InStream("\n")
        inStream.readLineFeed()
    }

    @Test
    fun `fail on expect line feed`() {
        val inStream = InStream("123")
        assertThrows(InvalidFormatException::class.java) { inStream.readLineFeed() }
    }
}
