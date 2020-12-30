package io.github.kirill5k.couponsaggregator.common

import cats.effect.{Blocker, ContextShift, Sync}
import pureconfig.generic.auto._
import pureconfig.module.catseffect.syntax._
import pureconfig.ConfigSource

object config {

  final case class ServerConfig(
      host: String,
      port: Int
  )

  final case class AppConfig(
      server: ServerConfig
  )

  object AppConfig {
    def load[F[_]: Sync: ContextShift](blocker: Blocker): F[AppConfig] =
      ConfigSource.default.loadF[F, AppConfig](blocker)
  }
}
