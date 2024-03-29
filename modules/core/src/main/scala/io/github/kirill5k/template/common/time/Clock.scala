package io.github.kirill5k.template.common.time

import cats.effect.Temporal
import cats.syntax.functor.*
import io.github.kirill5k.template.common.time.syntax.*

import java.time.Instant
import scala.concurrent.duration.FiniteDuration

trait Clock[F[_]]:
  def now: F[Instant]
  def durationBetweenNowAnd(time: Instant): F[FiniteDuration]
  def sleep(duration: FiniteDuration): F[Unit]

final private class LiveClock[F[_]](using F: Temporal[F]) extends Clock[F]:
  override def now: F[Instant]                                         = F.realTimeInstant
  override def durationBetweenNowAnd(time: Instant): F[FiniteDuration] = now.map(_.durationBetween(time))
  override def sleep(duration: FiniteDuration): F[Unit]                = F.sleep(duration)

object Clock:
  given [F[_]: Temporal]: Clock[F] = Clock.make[F]

  def apply[F[_]](using C: Clock[F]): C.type = C
  def make[F[_]: Temporal]: Clock[F]         = new LiveClock[F]()
