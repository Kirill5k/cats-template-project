package io.github.kirill5k.template

import cats.effect.{IO}
import cats.effect.unsafe.implicits.global
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import io.circe.*
import io.circe.parser.*
import org.http4s.{Response, Status}
import org.scalatest.Assertion
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.io.Source

trait ControllerSpec extends AnyWordSpec with Matchers {

  given logger: Logger[IO]   = Slf4jLogger.getLogger[IO]

  def verifyJsonResponse(
      actual: IO[Response[IO]],
      expectedStatus: Status,
      expectedBody: Option[String] = None
  ): Assertion = {
    val actualResp = actual.unsafeRunSync()

    actualResp.status must be(expectedStatus)
    expectedBody match {
      case Some(expected) =>
        val actual = actualResp.as[String].unsafeRunSync()
        parse(actual) mustBe parse(expected)
      case None =>
        actualResp.body.compile.toVector.unsafeRunSync() mustBe empty
    }
  }
}
