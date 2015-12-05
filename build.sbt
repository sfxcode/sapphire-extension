import _root_.bintray.BintrayPlugin.autoImport._

name := "sapphire-extension"

organization := "com.sfxcode.sapphire"

version := "0.4.7-SNAPSHOT"

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

libraryDependencies += "org.specs2" %% "specs2-core" % "3.6.6" % "test"

libraryDependencies += "org.specs2" %% "specs2-html" % "3.6.6" % "test"

libraryDependencies += "org.json4s" %% "json4s-native" % "3.3.0" % "test"

// Compile

libraryDependencies += "com.sfxcode.sapphire" %% "sapphire-core" % "1.0.8-SNAPSHOT"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.4.1"

libraryDependencies += "org.controlsfx" % "controlsfx" % "8.40.10" intransitive()

libraryDependencies += "de.jensd" % "fontawesomefx" % "8.7"

licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.html"))

bintrayReleaseOnPublish in ThisBuild := false
