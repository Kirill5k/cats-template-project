package io.github.kirill5k.template

import de.flapdoodle.embed.mongo.MongodStarter
import de.flapdoodle.embed.mongo.config.{MongodConfig, Net}
import de.flapdoodle.embed.mongo.distribution.Version
import de.flapdoodle.embed.process.runtime.Network

object EmbeddedMongo {
  val starter = MongodStarter.getDefaultInstance
}

trait EmbeddedMongo {

  protected val mongoHost = "localhost"
  protected val mongoPort = 12343

  def withRunningEmbeddedMongo[A](test: => A): A = {
    val mongodConfig = MongodConfig
      .builder()
      .version(Version.Main.PRODUCTION)
      .net(new Net(mongoHost, mongoPort, Network.localhostIsIPv6))
      .build
    val mongodExecutable = EmbeddedMongo.starter.prepare(mongodConfig)
    try {
      val _ = mongodExecutable.start
      test
    } finally mongodExecutable.stop()
  }

