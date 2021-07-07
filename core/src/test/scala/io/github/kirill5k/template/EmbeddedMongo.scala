package io.github.kirill5k.template

import de.flapdoodle.embed.mongo.MongodStarter
import de.flapdoodle.embed.mongo.config.{MongodConfig, Net}
import de.flapdoodle.embed.mongo.distribution.Version
import de.flapdoodle.embed.process.runtime.Network

trait EmbeddedMongo {

  protected val mongoHost = "localhost"
  protected val mongoPort = 12345

  private val starter = MongodStarter.getDefaultInstance
  private val mongodConfig = MongodConfig
    .builder()
    .version(Version.Main.PRODUCTION)
    .net(new Net(mongoHost, mongoPort, Network.localhostIsIPv6))
    .build

  def withRunningEmbeddedMongo[A](test: => A): A = {
    val mongoExecutable = starter.prepare(mongodConfig)
    try {
      val _ = mongoExecutable.start
      test
    } finally mongoExecutable.stop()
  }

