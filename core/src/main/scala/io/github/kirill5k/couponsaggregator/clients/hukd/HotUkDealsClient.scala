package io.github.kirill5k.couponsaggregator.clients.hukd

import io.github.kirill5k.couponsaggregator.clients.CouponsProvider
import io.github.kirill5k.couponsaggregator.domain.Coupon

import scala.concurrent.duration.FiniteDuration

final private[clients] class HotUkDealsClient[F[_]] extends CouponsProvider[F] {
  override def findLatest(age: FiniteDuration): fs2.Stream[F, Coupon] = ???
}
