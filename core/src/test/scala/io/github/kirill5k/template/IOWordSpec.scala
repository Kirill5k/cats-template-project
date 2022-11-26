package io.github.kirill5k.template

import cats.effect.IO
import cats.effect.unsafe.IORuntime
import org.scalatest.Assertion
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AsyncWordSpec
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

import scala.concurrent.Future

trait IOWordSpec extends AsyncWordSpec with Matchers with MockitoMatchers {
  given logger: Logger[IO] = Slf4jLogger.getLogger[IO]

  extension [A](io: IO[A])
    def throws(error: Throwable): Future[Assertion] =
      io.attempt.asserting(_ mustBe Left(error))
    def asserting(f: A => Assertion): Future[Assertion] =
      io.map(f).unsafeToFuture()(IORuntime.global)
}
