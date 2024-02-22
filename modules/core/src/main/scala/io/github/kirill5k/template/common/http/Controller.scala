package io.github.kirill5k.template.common.http

import org.http4s.HttpRoutes

trait Controller[F[_]]:
  def routes: HttpRoutes[F]
