package io.github.kirill5k.template.common.http

import org.http4s.HttpRoutes
import sttp.tapir.generic.auto.SchemaDerivation
import sttp.tapir.json.circe.TapirJsonCirce

trait Controller[F[_]]:
  def routes: HttpRoutes[F]
