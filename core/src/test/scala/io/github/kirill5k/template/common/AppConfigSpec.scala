package io.github.kirill5k.template.common

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import io.github.kirill5k.template.IOWordSpec
import io.github.kirill5k.template.common.config.AppConfig

class AppConfigSpec extends IOWordSpec {

  "An AppConfig" should {

    "load itself from reference.conf" in {
      AppConfig.load[IO].asserting { config =>
        config.server.host mustBe "0.0.0.0"
      }
    }
  }
}
