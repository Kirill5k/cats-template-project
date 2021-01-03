package io.github.kirill5k.template

import cats.effect.{ContextShift, IO, Timer}
import io.chrisdavenport.log4cats.Logger
import io.chrisdavenport.log4cats.slf4j.Slf4jLogger
import org.mockito.ArgumentMatchersSugar
import org.mockito.scalatest.AsyncMockitoSugar
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AsyncWordSpec

import scala.concurrent.ExecutionContext

trait CatsSpec extends AsyncWordSpec with Matchers with AsyncMockitoSugar with ArgumentMatchersSugar {

  implicit val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)
  implicit val timer: Timer[IO]     = IO.timer(ExecutionContext.global)
  implicit val logger: Logger[IO]   = Slf4jLogger.getLogger[IO]
}
