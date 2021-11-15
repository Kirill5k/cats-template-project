import sbt._

object Dependencies {

  object Versions {
    val mongo4cats = "0.4.2"
    val pureConfig = "0.17.0"
    val circe      = "0.14.1"
    val sttp       = "3.3.16"
    val http4s     = "1.0.0-M29"
    val logback    = "1.2.7"
    val log4cats   = "2.1.1"
    val tapir      = "0.19.0-M16"

    val scalaTest = "3.2.10"
    val mockito   = "3.2.10.0"
  }

  object Libraries {

    object mongo4cats {
      lazy val core     = "io.github.kirill5k" %% "mongo4cats-core"     % Versions.mongo4cats
      lazy val circe    = "io.github.kirill5k" %% "mongo4cats-circe"    % Versions.mongo4cats
      lazy val embedded = "io.github.kirill5k" %% "mongo4cats-embedded" % Versions.mongo4cats
    }

    object pureconfig {
      lazy val core = "com.github.pureconfig" %% "pureconfig-core" % Versions.pureConfig
    }

    object logging {
      lazy val logback  = "ch.qos.logback" % "logback-classic" % Versions.logback
      lazy val log4cats = "org.typelevel" %% "log4cats-slf4j"  % Versions.log4cats

      lazy val all = Seq(log4cats, logback)
    }

    object circe {
      lazy val core    = "io.circe" %% "circe-core"    % Versions.circe
      lazy val generic = "io.circe" %% "circe-generic" % Versions.circe
      lazy val parser  = "io.circe" %% "circe-parser"  % Versions.circe

      lazy val all = Seq(core, generic, parser)
    }

    object sttp {
      lazy val core        = "com.softwaremill.sttp.client3" %% "core"                           % Versions.sttp
      lazy val circe       = "com.softwaremill.sttp.client3" %% "circe"                          % Versions.sttp
      lazy val catsBackend = "com.softwaremill.sttp.client3" %% "async-http-client-backend-cats" % Versions.sttp

      lazy val all = Seq(core, circe, catsBackend)
    }

    object tapir {
      lazy val core   = "com.softwaremill.sttp.tapir" %% "tapir-core"          % Versions.tapir
      lazy val circe  = "com.softwaremill.sttp.tapir" %% "tapir-json-circe"    % Versions.tapir
      lazy val http4s = "com.softwaremill.sttp.tapir" %% "tapir-http4s-server" % Versions.tapir

      lazy val all = Seq(core, circe, http4s)
    }

    lazy val scalaTest = "org.scalatest"     %% "scalatest"   % Versions.scalaTest
    lazy val mockito   = "org.scalatestplus" %% "mockito-3-4" % Versions.mockito
  }

  lazy val core =
    Seq(
      Libraries.mongo4cats.core,
      Libraries.mongo4cats.circe,
      Libraries.pureconfig.core
    ) ++
      Libraries.circe.all ++
      Libraries.tapir.all ++
      Libraries.logging.all ++
      Libraries.sttp.all

  lazy val test = Seq(
    Libraries.scalaTest           % Test,
    Libraries.mockito             % Test,
    Libraries.mongo4cats.embedded % Test
  )

}
