package io.github.kirill5k.template.health

import cats.Monad
import cats.effect.Async
import cats.syntax.either.*
import cats.syntax.monad.*
import io.circe.Codec
import org.http4s.HttpRoutes
import sttp.capabilities.WebSockets
import sttp.capabilities.fs2.Fs2Streams
import sttp.tapir.*
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.server.http4s.Http4sServerInterpreter
import io.github.kirill5k.template.common.http.Controller

final class HealthController[F[_]: Async] extends Controller[F] {

  implicit val statusSchema: Schema[HealthController.AppStatus] = Schema.string

  private val statusEndpoint: ServerEndpoint[Any, F] =
    infallibleEndpoint.get
      .in("health" / "status")
      .out(jsonBody[HealthController.AppStatus])
      .serverLogicPure(_ => HealthController.AppStatus.UP.asRight[Nothing])

  def routes: HttpRoutes[F] = Http4sServerInterpreter[F]().toRoutes(statusEndpoint)
}

object HealthController {

  final case class AppStatus(status: Boolean) derives Codec.AsObject

  object AppStatus:
    inline def UP: AppStatus = AppStatus(true)

  def make[F[_]: Async]: F[Controller[F]] =
    Monad[F].pure(new HealthController[F])
}