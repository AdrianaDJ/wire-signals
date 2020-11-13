// based on http://caryrobbins.com/dev/sbt-publishing/

lazy val scala212 = "2.12.12"
lazy val scala211 = "2.11.12"
lazy val supportedScalaVersions = List(scala212, scala211)

ThisBuild / organization := "com.wire"
ThisBuild / scalaVersion := scala212

scalacOptions ++= Seq("-deprecation", "-feature")

homepage := Some(url("https://github.com/wireapp/wire-signals"))
licenses := Seq("GPL 3.0" -> url("https://www.gnu.org/licenses/gpl-3.0.en.html"))
publishMavenStyle := true
publishArtifact in Test := false
pomIncludeRepository := { _ => false }
ThisBuild / publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

scmInfo := Some(
  ScmInfo(
    url("https://github.com/wireapp/wire-signals"),
    "scm:git:git@github.com:wireapp/wire-signals.git"
  )
)

developers := List(
  Developer("makingthematrix", "Maciej Gorywoda", "maciej.gorywoda@wire.com", url("https://github.com/makingthematrix"))
)

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("public"),
  Resolver.mavenLocal
)

publishMavenStyle := true

publishConfiguration := publishConfiguration.value.withOverwrite(true)
publishLocalConfiguration := publishLocalConfiguration.value.withOverwrite(true)
publishM2Configuration := publishM2Configuration.value.withOverwrite(true)

lazy val root = (project in file("."))
  .settings(
    name := "wire-signals",
    crossScalaVersions := supportedScalaVersions,
    libraryDependencies ++= Seq(
      "org.threeten"                  %  "threetenbp"            % "1.4.4"            % Provided,

      //Test dependencies
      "org.scalatest"                 %% "scalatest"             % "3.0.7"            % Test,
      "org.scalamock"                 %% "scalamock"             % "4.2.0"            % Test,
      "junit"                         %  "junit"                 % "4.8.2"            % Test
    )
  )



