import sbt._

object Dependencies {
  object Versions {
    lazy val mongo4cats = "0.1.5"
    lazy val pureConfig = "0.14.0"
    lazy val circe      = "0.13.0"
    lazy val sttp       = "3.0.0"
    lazy val http4s     = "0.21.19"
    lazy val logback    = "1.2.3"
    lazy val log4cats   = "1.2.0"

    lazy val scalaTest     = "3.2.2"
    lazy val mockito       = "1.10.3"
    lazy val embeddedMongo = "2.2.0"
  }

  object Libraries {
    lazy val mongo4cats = "io.github.kirill5k" %% "mongo4cats-core" % Versions.mongo4cats

    object pureconfig {
      lazy val core       = "com.github.pureconfig" %% "pureconfig"             % Versions.pureConfig
      lazy val catsEffect = "com.github.pureconfig" %% "pureconfig-cats-effect" % Versions.pureConfig

      lazy val all = Seq(core, catsEffect)
    }

    object logging {
      lazy val logback  = "ch.qos.logback" % "logback-classic" % Versions.logback
      lazy val log4cats = "org.typelevel" %% "log4cats-slf4j"  % Versions.log4cats

      lazy val all = Seq(log4cats, logback)
    }

    object circe {
      lazy val core          = "io.circe" %% "circe-core"           % Versions.circe
      lazy val literal       = "io.circe" %% "circe-literal"        % Versions.circe
      lazy val generic       = "io.circe" %% "circe-generic"        % Versions.circe
      lazy val genericExtras = "io.circe" %% "circe-generic-extras" % Versions.circe
      lazy val parser        = "io.circe" %% "circe-parser"         % Versions.circe

      lazy val all = Seq(core, literal, generic, genericExtras, parser)
    }

    object sttp {
      lazy val core        = "com.softwaremill.sttp.client3" %% "core"                           % Versions.sttp
      lazy val circe       = "com.softwaremill.sttp.client3" %% "circe"                          % Versions.sttp
      lazy val catsBackend = "com.softwaremill.sttp.client3" %% "async-http-client-backend-cats" % Versions.sttp

      lazy val all = Seq(core, circe, catsBackend)
    }

    object http4s {
      lazy val core   = "org.http4s" %% "http4s-core"         % Versions.http4s
      lazy val dsl    = "org.http4s" %% "http4s-dsl"          % Versions.http4s
      lazy val server = "org.http4s" %% "http4s-server"       % Versions.http4s
      lazy val blaze  = "org.http4s" %% "http4s-blaze-server" % Versions.http4s
      lazy val circe  = "org.http4s" %% "http4s-circe"        % Versions.http4s

      lazy val all = Seq(core, dsl, server, blaze, circe)
    }

    lazy val scalaTest        = "org.scalatest"      %% "scalatest"                 % Versions.scalaTest
    lazy val mockitoCore      = "org.mockito"        %% "mockito-scala"             % Versions.mockito
    lazy val mockitoScalatest = "org.mockito"        %% "mockito-scala-scalatest"   % Versions.mockito
    lazy val embeddedMongo    = "de.flapdoodle.embed" % "de.flapdoodle.embed.mongo" % Versions.embeddedMongo
  }

  lazy val core = Seq(
    Libraries.mongo4cats
  ) ++
    Libraries.circe.all ++
    Libraries.http4s.all ++
    Libraries.logging.all ++
    Libraries.sttp.all ++
    Libraries.pureconfig.all

  lazy val test = Seq(
    Libraries.scalaTest        % Test,
    Libraries.mockitoCore      % Test,
    Libraries.mockitoScalatest % Test,
    Libraries.embeddedMongo    % Test
  )
}
