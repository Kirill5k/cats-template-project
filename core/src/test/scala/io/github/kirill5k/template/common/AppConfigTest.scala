package io.github.kirill5k.template.common

import io.github.kirill5k.template.CatsSpec
import io.github.kirill5k.template.common.config.AppConfig
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec

class AppConfigSpec extends AnyWordSpec with Matchers {

  "An AppConfig" should {

    "load itself from reference.conf" in {
      val config = AppConfig.load
      config.server.host mustBe "0.0.0.0"
    }
  }
}
