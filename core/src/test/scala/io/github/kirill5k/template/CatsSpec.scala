package io.github.kirill5k.template

import cats.effect.IO
import org.mockito.ArgumentMatchersSugar
import org.mockito.scalatest.AsyncMockitoSugar
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AsyncWordSpec
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

trait CatsSpec extends AsyncWordSpec with Matchers with AsyncMockitoSugar with ArgumentMatchersSugar {
  implicit val logger: Logger[IO]   = Slf4jLogger.getLogger[IO]
}
