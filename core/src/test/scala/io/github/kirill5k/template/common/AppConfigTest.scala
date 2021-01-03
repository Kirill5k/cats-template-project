package io.github.kirill5k.template.common

import cats.effect.{Blocker, IO}
import io.github.kirill5k.template.CatsSpec
import io.github.kirill5k.template.common.config.AppConfig

class AppConfigSpec extends CatsSpec {

  "An AppConfig" should {

    "load itself from reference.conf" in {

      Blocker[IO].use(AppConfig.load[IO]).unsafeToFuture().map { c =>
        c.server.host mustBe "0.0.0.0"
      }
    }
  }
}
