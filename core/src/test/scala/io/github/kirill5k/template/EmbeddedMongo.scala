package io.github.kirill5k.template

import de.flapdoodle.embed.mongo.MongodStarter
import de.flapdoodle.embed.mongo.config.{MongodConfig, Net}
import de.flapdoodle.embed.mongo.distribution.Version
import de.flapdoodle.embed.process.runtime.Network

import java.io.File
import java.nio.file.Files

trait EmbeddedMongo {

  private def clearResources(): Unit = {
    val tempFile = System.getenv("temp") + File.separator + "extract-" + System.getenv("USERNAME") + "-extractmongod";
    val extension = if (System.getenv("OS") != null && System.getenv("OS").contains("Windows")) ".exe" else ".sh"
    Files.deleteIfExists(new File(s"$tempFile$extension").toPath)
    Files.deleteIfExists(new File(tempFile + ".pid").toPath)
    ()
  }
  
  def withRunningEmbeddedMongo[A](host: String = "localhost", port: Int = 12345)(test: => A): A = {
    clearResources()
    val starter          = MongodStarter.getDefaultInstance
    val mongodConfig     = MongodConfig.builder()
      .version(Version.Main.PRODUCTION)
      .net(new Net(host, port, Network.localhostIsIPv6))
      .build
    val mongodExecutable = starter.prepare(mongodConfig)
    try {
      val _ = mongodExecutable.start
      test
    } finally mongodExecutable.stop()
  }
}

