package io.github.kirill5k.template

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import io.circe.*
import io.circe.parser.*
import org.http4s.{Request, Response, Status}
import org.scalatest.Assertion
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.io.Source

trait ControllerSpec extends AnyWordSpec with Matchers with MockitoMatchers {

  extension (r: Request[IO])
    def withBody(requestBody: String): Request[IO] = r.withBodyStream(fs2.Stream.emits(requestBody.getBytes().toList))
    def withJsonBody(json: Json): Request[IO]      = withBody(json.noSpaces)

  def verifyJsonResponse(
      response: IO[Response[IO]],
      expectedStatus: Status,
      expectedBody: Option[String] = None
  ): Assertion =
    response
      .flatTap(res => IO(res.status mustBe expectedStatus))
      .flatMap { res =>
        expectedBody match {
          case Some(expectedJson) => res.as[String].map(parse(_) mustBe parse(expectedJson))
          case None               => res.body.compile.toVector.map(_ mustBe empty)
        }
      }
      .unsafeRunSync()

  def parseJson(jsonString: String): Json =
    parse(jsonString).getOrElse(throw new RuntimeException)

  extension (res: IO[Response[IO]])
    def mustHaveStatus(expectedStatus: Status, expectedBody: Option[String] = None): Assertion =
      verifyJsonResponse(res, expectedStatus, expectedBody)
}
