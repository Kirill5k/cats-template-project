import com.typesafe.sbt.packager.docker._
import sbtghactions.JavaSpec

ThisBuild / scalaVersion                        := "3.1.1"
ThisBuild / version                             := scala.sys.process.Process("git rev-parse HEAD").!!.trim.slice(0, 7)
ThisBuild / organization                        := "io.github.kirill5k"
ThisBuild / githubWorkflowPublishTargetBranches := Nil
ThisBuild / githubWorkflowJavaVersions          := Seq(JavaSpec.temurin("17"))

val noPublish = Seq(
  publish         := {},
  publishLocal    := {},
  publishArtifact := false,
  publish / skip  := true
)

val docker = Seq(
  packageName        := moduleName.value,
  version            := version.value,
  maintainer         := "immotional@aol.com",
  dockerBaseImage    := "amazoncorretto:17.0.1-alpine",
  dockerUpdateLatest := true,
  makeBatScripts     := List(),
  dockerCommands := {
    val commands         = dockerCommands.value
    val (stage0, stage1) = commands.span(_ != DockerStageBreak)
    val (before, after)  = stage1.splitAt(4)
    val installBash      = Cmd("RUN", "apk update && apk upgrade && apk add bash")
    stage0 ++ before ++ List(installBash) ++ after
  }
)

val core = project
  .in(file("core"))
  .enablePlugins(JavaAppPackaging, JavaAgent, DockerPlugin)
  .settings(docker)
  .settings(
    name       := "template-project-core",
    moduleName := "template-project-core",
    libraryDependencies ++= Dependencies.core ++ Dependencies.test
  )

val root = project
  .in(file("."))
  .settings(noPublish)
  .settings(
    name := "template-project"
  )
  .aggregate(core)
