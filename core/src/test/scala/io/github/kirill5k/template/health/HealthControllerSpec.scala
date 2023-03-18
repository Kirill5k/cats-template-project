package io.github.kirill5k.template.health

import cats.effect.{IO, Ref}
import io.github.kirill5k.template.ControllerSpec
import org.http4s.implicits.*
import org.http4s.*
import org.http4s.Header.Raw
import org.typelevel.ci.CIString

import java.time.Instant

class HealthControllerSpec extends ControllerSpec {

  val ts = Instant.parse("2021-01-01T00:00:00Z")

  "A HealthController" should {

    "return status on the app" in {
      val controller = new HealthController[IO](ts)

      val request  = Request[IO](uri = uri"/health/status", method = Method.GET, headers = Headers(Raw(CIString("foo"), "bar")))
      val response = controller.routes.orNotFound.run(request)

      response mustHaveStatus (Status.Ok, Some(s"""{"startupTime":"$ts"}"""))
    }
  }
}
