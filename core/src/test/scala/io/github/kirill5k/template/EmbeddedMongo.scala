package io.github.kirill5k.template

import cats.effect.kernel.Sync
import cats.effect.{Async, IO, Resource}
import cats.implicits._
import de.flapdoodle.embed.mongo.config.{MongodConfig, Net}
import de.flapdoodle.embed.mongo.distribution.Version
import de.flapdoodle.embed.mongo.{MongodExecutable, MongodProcess, MongodStarter}
import de.flapdoodle.embed.process.runtime.Network

import scala.concurrent.duration._

object EmbeddedMongo {
  private val starter = MongodStarter.getDefaultInstance

  def prepare[F[_]: Async](config: MongodConfig, maxAttempts: Int = 5, attempt: Int = 0): F[MongodExecutable] =
    if (attempt >= maxAttempts)
      Sync[F].raiseError(new RuntimeException("tried to prepare executable far too many times"))
    else
      Async[F].delay(starter.prepare(config)).handleErrorWith { _ =>
        Async[F].sleep(attempt.seconds) *> prepare[F](config, maxAttempts, attempt + 1)
      }

  implicit final class MongodExecutableOps(private val ex: MongodExecutable) extends AnyVal {
    def startWithRetry[F[_]: Async](maxAttempts: Int = 5, attempt: Int = 0): F[MongodProcess] =
      if (attempt < 0) Sync[F].raiseError(new RuntimeException("tried to prepare executable far too many times"))
      else
        Async[F].delay(ex.start()).handleErrorWith { _ =>
          Async[F].sleep(attempt.seconds) *> startWithRetry(maxAttempts, attempt + 1)
        }
  }
}

trait EmbeddedMongo {
  import EmbeddedMongo._

  protected val mongoHost = "localhost"
  protected val mongoPort = 12343

  def withRunningEmbeddedMongo[A](test: => IO[A]): IO[A] = {
    val mongodConfig = MongodConfig
      .builder()
      .version(Version.Main.PRODUCTION)
      .net(new Net(mongoHost, mongoPort, Network.localhostIsIPv6))
      .build

    Resource
      .make(EmbeddedMongo.prepare[IO](mongodConfig))(ex => IO(ex.stop()))
      .flatMap(ex => Resource.make(ex.startWithRetry[IO]())(pr => IO(pr.stop())))
      .use(_ => test)
  }
}
