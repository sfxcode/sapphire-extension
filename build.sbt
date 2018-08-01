import sbt.url

name := "sapphire-extension"

organization := "com.sfxcode.sapphire"

scalaVersion := "2.12.6"

crossScalaVersions := Seq("2.12.6", "2.11.12")



scalacOptions += "-deprecation"

javacOptions += "-Dorg.apache.deltaspike.ProjectStage=Test"

parallelExecution in Test := false

lazy val sapphire_extension_root = Project(id = "sapphire-extension", base = file(".")).
  configs(IntegrationTest).
  settings(Defaults.itSettings: _*)

// resolvers

// Test

libraryDependencies += "org.specs2" %% "specs2-core" % "4.3.2" % "test"

libraryDependencies += "org.json4s" %% "json4s-native" % "3.6.0" % "provided"

// Compile

libraryDependencies += "com.sfxcode.sapphire" %% "sapphire-core" % "1.3.5"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.5.14"

libraryDependencies += "org.controlsfx" % "controlsfx" % "8.40.14" intransitive()

libraryDependencies += "de.jensd" % "fontawesomefx-commons" % "8.15"
libraryDependencies += "de.jensd" % "fontawesomefx-controls" % "8.15"

libraryDependencies += "de.jensd" % "fontawesomefx-fontawesome" % "4.7.0-5"
libraryDependencies += "de.jensd" % "fontawesomefx-materialicons" % "2.2.0-5"
libraryDependencies += "de.jensd" % "fontawesomefx-materialdesignfont" % "1.7.22-4"
libraryDependencies += "de.jensd" % "fontawesomefx-emojione" % "2.2.7-2"
libraryDependencies += "de.jensd" % "fontawesomefx-icons525" % "3.0.0-4"
libraryDependencies += "de.jensd" % "fontawesomefx-octicons" % "4.3.0-5"

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


