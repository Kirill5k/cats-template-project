package io.github.kirill5k.template.health

import cats.effect.Async
import cats.effect.Temporal
import cats.syntax.flatMap.*
import cats.syntax.functor.*
import io.circe.Codec
import io.github.kirill5k.template.common.http.Controller
import org.http4s.HttpRoutes
import sttp.tapir.*
import sttp.tapir.generic.auto.SchemaDerivation
import sttp.tapir.json.circe.{TapirJsonCirce, jsonBody}
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.server.http4s.Http4sServerInterpreter

import java.time.Instant

final class HealthController[F[_]: Async](
    private val startupTime: Instant
) extends Controller[F] {

  private val statusEndpoint: ServerEndpoint[Any, F] = HealthController.statusEndpoint
    .serverLogicPure(_ => Right(HealthController.AppStatus(startupTime)))

  def routes: HttpRoutes[F] = Http4sServerInterpreter[F]().toRoutes(List(statusEndpoint))
}

object HealthController extends TapirJsonCirce with SchemaDerivation {

  final case class AppStatus(startupTime: Instant) derives Codec.AsObject

  val basePath = "health"

  val statusEndpoint = infallibleEndpoint.get
    .in(basePath / "status")
    .out(jsonBody[HealthController.AppStatus])

  def make[F[_]: Async]: F[Controller[F]] =
    Temporal[F].realTimeInstant
      .map(HealthController[F](_))
}
