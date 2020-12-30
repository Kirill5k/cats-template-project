package io.github.kirill5k.couponsaggregator.common

import cats.effect.{Blocker, ContextShift, IO}
import io.github.kirill5k.couponsaggregator.common.config.AppConfig
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AsyncWordSpec

import scala.concurrent.ExecutionContext

class AppConfigSpec extends AsyncWordSpec with Matchers {

  implicit val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)

  "An AppConfig" should {

    "load itself from reference.conf" in {

      Blocker[IO].use(AppConfig.load[IO]).unsafeToFuture().map { c =>
        c.server.host mustBe "0.0.0.0"
      }
    }
  }
}
