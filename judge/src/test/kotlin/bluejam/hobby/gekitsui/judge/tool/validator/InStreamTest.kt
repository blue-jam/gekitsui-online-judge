package bluejam.hobby.gekitsui.judge.tool.validator

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class InStreamTest {
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
}
