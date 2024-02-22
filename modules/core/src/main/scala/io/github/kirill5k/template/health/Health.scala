package io.github.kirill5k.template.health

import cats.effect.Async
import cats.implicits.*
import io.github.kirill5k.template.common.http.Controller

final class Health[F[_]] private (
    val controller: Controller[F]
)

object Health:
  def make[F[_]: Async]: F[Health[F]] =
    HealthController.make[F].map(Health(_))
