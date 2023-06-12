package io.github.kirill5k.template

import cats.effect.Async
import com.comcast.ip4s.{Ipv4Address, Port}
import fs2.Stream
import org.http4s.HttpApp
import org.http4s.ember.server.EmberServerBuilder
import io.github.kirill5k.template.common.config.ServerConfig

import scala.concurrent.duration.*

object Server:
  def serve[F[_]: Async](config: ServerConfig, routes: HttpApp[F]): Stream[F, Unit] =
    Stream.eval {
      EmberServerBuilder
        .default[F]
        .withHostOption(Ipv4Address.fromString(config.host))
        .withPort(Port.fromInt(config.port).get)
        .withIdleTimeout(1.hour)
        .withHttpApp(routes)
        .build
        .use(_ => Async[F].never)
    }
