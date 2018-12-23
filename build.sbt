import sbt.url

name := "sapphire-extension"

organization := "com.sfxcode.sapphire"

scalaVersion := "2.12.8"

scalacOptions += "-deprecation"

javacOptions += "-Dorg.apache.deltaspike.ProjectStage=Test"

parallelExecution in Test := false

lazy val sapphire_extension_root = Project(id = "sapphire-extension", base = file(".")).
  configs(IntegrationTest).
  settings(Defaults.itSettings: _*)

// resolvers

resolvers += Resolver.jcenterRepo

// Test

libraryDependencies += "org.specs2" %% "specs2-core" % "4.3.6" % Test

libraryDependencies += "org.json4s" %% "json4s-native" % "3.6.2" % Test

// Provided

libraryDependencies += "com.sfxcode.sapphire" %% "sapphire-core" % "1.4.3" % Provided

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.5.19" % Provided

// Compile

libraryDependencies += "org.controlsfx" % "controlsfx" % "9.0.0" intransitive()

libraryDependencies += "org.kordamp.ikonli" % "ikonli-javafx" % "11.0.2"

libraryDependencies += "org.kordamp.ikonli" % "ikonli-fontawesome-pack" % "11.0.2"


licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.html"))

enablePlugins(BuildInfoPlugin)

buildInfoPackage := "com.sfxcode.sapphire.extension"

buildInfoOptions += BuildInfoOption.BuildTime


// publish

releaseCrossBuild := true

bintrayReleaseOnPublish in ThisBuild := true

publishMavenStyle := true

homepage := Some(url("https://github.com/sfxcode/sapphire-core"))

scmInfo := Some(
  ScmInfo(
    url("https://github.com/sfxcode/sapphire-core"),
    "scm:https://github.com/sfxcode/sapphire-core.git"
  )
)

developers := List(
  Developer(
    id    = "sfxcode",
    name  = "Tom Lamers",
    email = "tom@sfxcode.com",
    url   = url("https://github.com/sfxcode")
  )
)


