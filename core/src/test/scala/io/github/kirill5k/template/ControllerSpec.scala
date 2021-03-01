package io.github.kirill5k.template

import cats.effect.{ContextShift, IO}
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import io.circe.parser._
import org.http4s.{Response, Status}
import org.mockito.{ArgumentMatchersSugar, MockitoSugar}
import org.scalatest.Assertion
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.http4s.circe._

import scala.concurrent.ExecutionContext
import scala.io.Source

trait ControllerSpec extends AnyWordSpec with MockitoSugar with ArgumentMatchersSugar with Matchers {

  implicit val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)
  implicit val logger: Logger[IO]   = Slf4jLogger.getLogger[IO]

  def verifyJsonResponse(
      actual: IO[Response[IO]],
      expectedStatus: Status,
      expectedBody: Option[String] = None
  ): Assertion = {
    val actualResp = actual.unsafeRunSync()

    actualResp.status must be(expectedStatus)
    expectedBody match {
      case Some(expected) =>
        val actual = actualResp.asJson.unsafeRunSync()
        actual must be(parse(expected).getOrElse(throw new RuntimeException))
      case None =>
        actualResp.body.compile.toVector.unsafeRunSync() mustBe empty
    }
  }

  def readFileFromResources(path: String): String =
    Source.fromResource(path).getLines().toList.mkString
}
