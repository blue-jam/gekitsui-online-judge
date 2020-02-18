package bluejam.hobby.gekitsui.webapp.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Service
class TimeZoneConverter(
        @Value("\${gekitsui.time.timezone:Asia/Tokyo}") timeZoneName: String,
        @Value("\${gekitsui.time.format:yyyy/MM/dd HH:mm:ss}") pattern: String
) {
    private val zoneId: ZoneId = ZoneId.of(timeZoneName)
    private val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(pattern)

    fun convertToLocalTime(timestamp: Timestamp): String {
        return LocalDateTime.ofInstant(timestamp.toInstant(), zoneId).format(dateTimeFormatter)
    }
}
