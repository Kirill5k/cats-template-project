package io.github.kirill5k.template

import cats.effect.IO
import cats.effect.IOApp
import io.github.kirill5k.template.common.config.AppConfig
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.implicits._
import org.http4s.server.Router
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

object Application extends IOApp.Simple:

  given logger: Logger[IO] = Slf4jLogger.getLogger[IO]

  override val run: IO[Unit] =
    for
      config <- AppConfig.load[IO]
      _ <-
        BlazeServerBuilder[IO]
          .withExecutionContext((runtime.compute))
          .bindHttp(config.server.port, config.server.host)
          .withHttpApp(Router[IO]().orNotFound)
          .serve
          .compile
          .drain
    yield ()
