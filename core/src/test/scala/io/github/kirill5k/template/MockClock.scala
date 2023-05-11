package io.github.kirill5k.template

import cats.Monad
import cats.effect.IO
import io.github.kirill5k.template.common.time.Clock
import io.github.kirill5k.template.common.time.syntax.*

import java.time.Instant
import scala.concurrent.duration.FiniteDuration

final private class MockClock[F[_]: Monad](
    private var timestamp: Instant
)(using
    M: Monad[F]
) extends Clock[F] {
  def set(newTimestamp: Instant): Unit = {
    timestamp = newTimestamp
    ()
  }

  override def now: F[Instant]                                         = M.pure(timestamp)
  override def durationBetweenNowAnd(time: Instant): F[FiniteDuration] = M.pure(time.durationBetween(timestamp))
  override def sleep(duration: FiniteDuration): F[Unit]                = M.pure(set(timestamp.plusNanos(duration.toNanos)))
}

object MockClock {
  def apply[F[_]: Monad](timestamp: Instant): Clock[F] = new MockClock[F](timestamp)
}
