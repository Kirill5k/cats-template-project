package io.github.kirill5k.couponsaggregator.clients

import cats.effect.Sync
import fs2.Stream
import io.github.kirill5k.couponsaggregator.clients.hukd.HotUkDealsClient
import io.github.kirill5k.couponsaggregator.domain.Coupon

import scala.concurrent.duration.FiniteDuration

trait CouponsProvider[F[_]] {
  def findLatest(age: FiniteDuration): Stream[F, Coupon]
}

object CouponsProvider {

  def hukd[F[_]: Sync]: F[CouponsProvider[F]] =
    Sync[F].delay(new HotUkDealsClient[F])
}
