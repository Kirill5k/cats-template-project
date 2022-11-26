package io.github.kirill5k.template.common

import java.time.{Instant, LocalDate}
import scala.concurrent.duration.*
import scala.util.Try

object time {
  extension (dateString: String)
    def toInstant: Either[Throwable, Instant] =
      val localDate = dateString.length match
        case 10 => s"${dateString}T00:00:00Z"
        case 19 => s"${dateString}Z"
        case _  => dateString
      Try(Instant.parse(localDate)).toEither

  extension (ts: Instant)
    def durationBetween(otherTs: Instant): FiniteDuration =
      math.abs(otherTs.toEpochMilli - ts.toEpochMilli).millis

  extension (fd: FiniteDuration)
    def toReadableString: String = {
      val hours   = fd.toHours
      val remMins = fd - hours.hours
      val minutes = remMins.toMinutes
      val remSecs = remMins - minutes.minutes
      val seconds = remSecs.toSeconds
      s"""
         |${if hours > 0 then s"${hours}h" else ""}
         |${if minutes > 0 then s"${minutes}m" else ""}
         |${if seconds > 0 then s"${seconds}s" else ""}
         |""".stripMargin.replaceAll("\n", "")
    }
}
