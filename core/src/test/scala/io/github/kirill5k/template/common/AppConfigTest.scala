package io.github.kirill5k.template.common

import cats.effect.unsafe.implicits.global
import io.github.kirill5k.template.CatsSpec
import io.github.kirill5k.template.common.config.AppConfig
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AsyncWordSpec

class AppConfigSpec extends AsyncWordSpec with Matchers {

  "An AppConfig" should {

    "load itself from reference.conf" in {
      AppConfig.load[IO].unsafeToFuture().map { config =>
        config.server.host mustBe "0.0.0.0"
      }
    }
  }
}
