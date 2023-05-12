package io.github.kirill5k.template.common.time

import io.github.kirill5k.template.common.time.syntax.*

import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec

import java.time.format.DateTimeParseException
import java.time.Instant
import scala.concurrent.duration.*

class TimeSyntaxSpec extends AnyWordSpec with Matchers {

  "A String extension" should {
    "convert str to instant" in {
      "2020-01-01".toInstant mustBe Right(Instant.parse("2020-01-01T00:00:00Z"))
      "2020-01-01T01:30:00".toInstant mustBe Right(Instant.parse("2020-01-01T01:30:00Z"))
      "2020-01-01T02:30:00Z".toInstant mustBe Right(Instant.parse("2020-01-01T02:30:00Z"))
      "foo".toInstant.left.map(_.getMessage) mustBe Left("Text 'foo' could not be parsed at index 0")
    }
  }

  "A FiniteDuration extension" should {

    "convert fd to readable string" in {
      100.millis.toReadableString mustBe "0s"
      30.minutes.toReadableString mustBe "30m"
      60.minutes.toReadableString mustBe "1h"
      90.minutes.toReadableString mustBe "1h30m"
      27.hours.toReadableString mustBe "1d3h"
    }
  }

}
