import sbt._

object Dependencies {

  object Versions {
    val mongo4cats = "0.7.2"
    val pureConfig = "0.17.6"
    val circe      = "0.14.6"
    val sttp       = "3.9.3"
    val http4s     = "0.23.25"
    val logback    = "1.5.0"
    val log4cats   = "2.6.0"
    val tapir      = "1.9.10"

    val scalaTest = "3.2.18"
    val mockito   = "3.2.17.0"
  }

  object Libraries {

    object mongo4cats {
      val core     = "io.github.kirill5k" %% "mongo4cats-core"     % Versions.mongo4cats
      val circe    = "io.github.kirill5k" %% "mongo4cats-circe"    % Versions.mongo4cats
      val embedded = "io.github.kirill5k" %% "mongo4cats-embedded" % Versions.mongo4cats
    }

    object pureconfig {
      val core = "com.github.pureconfig" %% "pureconfig-core" % Versions.pureConfig
    }

    object logging {
      val logback  = "ch.qos.logback" % "logback-classic" % Versions.logback
      val log4cats = "org.typelevel" %% "log4cats-slf4j"  % Versions.log4cats

      val all = Seq(log4cats, logback)
    }

    object circe {
      val core    = "io.circe" %% "circe-core"    % Versions.circe
      val generic = "io.circe" %% "circe-generic" % Versions.circe
      val parser  = "io.circe" %% "circe-parser"  % Versions.circe

      val all = Seq(core, generic, parser)
    }

    object sttp {
      val core       = "com.softwaremill.sttp.client3" %% "core"  % Versions.sttp
      val circe      = "com.softwaremill.sttp.client3" %% "circe" % Versions.sttp
      val fs2Backend = "com.softwaremill.sttp.client3" %% "fs2"   % Versions.sttp

      val all = Seq(core, circe, fs2Backend)
    }

    object tapir {
      val core   = "com.softwaremill.sttp.tapir" %% "tapir-core"          % Versions.tapir
      val circe  = "com.softwaremill.sttp.tapir" %% "tapir-json-circe"    % Versions.tapir
      val http4s = "com.softwaremill.sttp.tapir" %% "tapir-http4s-server" % Versions.tapir

      val all = Seq(core, circe, http4s)
    }

    object http4s {
      val emberServer = "org.http4s" %% "http4s-ember-server" % Versions.http4s
    }

    val scalaTest = "org.scalatest"     %% "scalatest"   % Versions.scalaTest
    val mockito   = "org.scalatestplus" %% "mockito-4-11" % Versions.mockito
  }

  val core = Seq(
    Libraries.mongo4cats.core,
    Libraries.mongo4cats.circe,
    Libraries.pureconfig.core,
    Libraries.http4s.emberServer
  ) ++
    Libraries.circe.all ++
    Libraries.tapir.all ++
    Libraries.logging.all ++
    Libraries.sttp.all

  val test = Seq(
    Libraries.scalaTest           % Test,
    Libraries.mockito             % Test,
    Libraries.mongo4cats.embedded % Test
  )

}
