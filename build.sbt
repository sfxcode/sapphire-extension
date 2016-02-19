import _root_.bintray.BintrayPlugin.autoImport._

name := "sapphire-extension"

organization := "com.sfxcode.sapphire"

version := "0.5.0"

scalaVersion := "2.11.7"

scalacOptions += "-deprecation"

parallelExecution in Test := false

(testOptions in Test) += Tests.Argument(TestFrameworks.Specs2, "html")

javacOptions ++= Seq("-source", "1.8")

javacOptions ++= Seq("-target", "1.8")

scalacOptions += "-target:jvm-1.8"

// resolvers


resolvers ++= Seq(
   "sonatype-snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"
)

// Test

libraryDependencies += "org.specs2" %% "specs2-core" % "3.7.1" % "test"

libraryDependencies += "org.specs2" %% "specs2-html" % "3.7.1" % "test"

libraryDependencies += "org.json4s" %% "json4s-native" % "3.3.0" % "test"

// Compile

libraryDependencies += "com.sfxcode.sapphire" %% "sapphire-core" % "1.1.1"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.4.2"

libraryDependencies += "org.controlsfx" % "controlsfx" % "8.40.10" intransitive()

libraryDependencies += "de.jensd" % "fontawesomefx" % "8.9"

licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.html"))

bintrayReleaseOnPublish in ThisBuild := false
