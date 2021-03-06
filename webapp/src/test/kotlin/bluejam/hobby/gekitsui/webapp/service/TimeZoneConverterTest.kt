package bluejam.hobby.gekitsui.webapp.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

internal class TimeZoneConverterTest {

    @Test
    fun convertToLocalTime() {
        val timeZoneConverter = TimeZoneConverter("Asia/Tokyo", "yyyy/MM/dd HH:mm:ss")
        val zonedDateTime = ZonedDateTime.of(1985, 10, 26, 1, 21, 0, 0, ZoneId.of("America/Los_Angeles"))

        assertEquals("1985/10/26 17:21:00", timeZoneConverter.convertToLocalTimeString(Timestamp(zonedDateTime.toEpochSecond() * 1000)))
    }

    @Test
    fun convertToServerTime() {
        val timeZoneConverter = TimeZoneConverter("Asia/Tokyo", "yyyy/MM/dd HH:mm:ss")
        val localDateTime = LocalDateTime.of(1985, 10, 26, 17, 21, 0, 0)
        val zonedDateTime = localDateTime.atZone(ZoneId.of("Asia/Tokyo"))

        assertThat(timeZoneConverter.convertToServerTimestamp(localDateTime))
                .isEqualTo(Timestamp(zonedDateTime.toEpochSecond() * 1000))
    }
}
