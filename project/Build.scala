import sbt._
import Keys._
import wartremover._

object BuildSettings {
  val buildSettings = Defaults.defaultSettings ++ Seq(
    organization := "pureirc",
    version := "0.1-SNAPSHOT",
    publishTo := Some(Resolver.file("file", new File("releases"))),
    resolvers ++= Seq(
      Resolver.sonatypeRepo("snapshots")
    ),
    scalaVersion := "2.11.2",
    crossScalaVersions := List("2.10.4", scalaVersion.value),
    scalacOptions ++= Seq(
      "-deprecation",
      "-encoding", "UTF-8",
      "-feature",
      "-language:existentials",
      "-language:higherKinds",
      "-language:implicitConversions",
      "-language:postfixOps",
      "-unchecked",
      "-Xfatal-warnings",
      "-Xlint",
      "-Yno-adapted-args",
      "-Ywarn-dead-code",
      "-Ywarn-numeric-widen",
      "-Ywarn-value-discard",
      "-P:wartremover:only-warn-traverser:org.brianmckenna.wartremover.warts.Unsafe"
    ),
    wartremoverErrors ++= Warts.all
  )
}

object Dependencies {
  val scalazVersion = "7.1.0"
  val monocleVersion = "1.0.0-SNAPSHOT"

  val scalaz            = "org.scalaz" %% "scalaz-core" % scalazVersion
  val scalazEffect      = "org.scalaz" %% "scalaz-effect" % scalazVersion
  val monocle           = "com.github.julien-truffaut" %% "monocle-core" % monocleVersion
  val monocleGeneric    = "com.github.julien-truffaut" %% "monocle-generic" % monocleVersion
  val monocleMacro      = "com.github.julien-truffaut" %% "monocle-macro" % monocleVersion

  // Tests
  val scalaCheck        = "org.scalacheck" %% "scalacheck" % "1.11.4"
  val scalaCheckBinding = "org.scalaz" %% "scalaz-scalacheck-binding" % scalazVersion
  val specs2            = "org.specs2" %% "specs2" % "2.3.11"
  val scalazSpecs2      = "org.typelevel" %% "scalaz-specs2" % "0.2"
  val monocleLaw        = "com.github.julien-truffaut" %% "monocle-law" % monocleVersion
}

object PureIRCBuild extends Build {
  import BuildSettings._
  import Dependencies._

  lazy val root: Project = Project(
    "pureirc",
    file("."),
    settings = buildSettings ++ Seq(
      publishArtifact := false,
      run <<= run in Compile in core)
  ) aggregate(core, test)

  lazy val core: Project = Project(
    "pureirc-core",
    file("core"),
    settings = buildSettings ++ Seq(
      libraryDependencies ++= Seq(
        monocle,
        monocleGeneric,
        monocleMacro,
        scalaz,
        scalazEffect)
    )
  )

  lazy val test: Project = Project(
    "pureirc-test",
    file("tests"),
    settings = buildSettings ++ Seq(
      publishArtifact := false,
      libraryDependencies ++= Seq(
        monocle,
        scalaCheck,
        scalaCheckBinding,
        scalaz,
        scalazEffect,
        scalazSpecs2,
        specs2)
    )
  ) dependsOn(core)
}
