package io.github.kirill5k.template.health

import cats.effect.Async
import cats.effect.Temporal
import cats.syntax.flatMap.*
import cats.syntax.functor.*
import io.circe.Codec
import io.github.kirill5k.template.common.http.Controller
import io.github.kirill5k.template.common.time.Clock
import io.github.kirill5k.template.common.time.syntax.*
import org.http4s.HttpRoutes
import sttp.tapir.*
import sttp.tapir.generic.auto.SchemaDerivation
import sttp.tapir.json.circe.{TapirJsonCirce, jsonBody}
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.server.http4s.Http4sServerInterpreter

import java.net.InetAddress
import java.time.Instant

final class HealthController[F[_]: Async](
    private val startupTime: Instant,
    private val ipAddress: String,
    private val appVersion: Option[String]
)(using
    C: Clock[F]
) extends Controller[F] {

  private val statusEndpoint: ServerEndpoint[Any, F] =
    HealthController.statusEndpoint
      .serverLogicSuccess { _ =>
        C.durationBetweenNowAnd(startupTime)
          .map { uptime =>
            HealthController.Status(
              startupTime,
              uptime.toReadableString,
              appVersion,
              ipAddress
            )
          }
      }

  def routes: HttpRoutes[F] = Http4sServerInterpreter[F]().toRoutes(List(statusEndpoint))
}

object HealthController extends TapirJsonCirce with SchemaDerivation {

  final case class Status(
      startupTime: Instant,
      upTime: String,
      appVersion: Option[String],
      serverIpAddress: String
  ) derives Codec.AsObject

  val basePath = "health"

  val statusEndpoint = infallibleEndpoint.get
    .in(basePath / "status")
    .out(jsonBody[HealthController.Status])

  def make[F[_]](using F: Async[F], C: Clock[F]): F[Controller[F]] =
    for
      now     <- C.now
      ip      <- F.delay(InetAddress.getLocalHost.getHostAddress)
      version <- F.delay(sys.env.get("VERSION"))
    yield new HealthController[F](now, ip, version)
}
