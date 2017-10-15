
name := "sapphire-extension"

organization := "com.sfxcode.sapphire"

version := "0.6.3"

crossScalaVersions := Seq("2.12.3",  "2.11.11")

scalaVersion := "2.12.3"


scalacOptions += "-deprecation"

javacOptions += "-Dorg.apache.deltaspike.ProjectStage=Test"

parallelExecution in Test := false

javacOptions ++= Seq("-source", "1.8")

javacOptions ++= Seq("-target", "1.8")

scalacOptions += "-target:jvm-1.8"

// resolvers

resolvers += "sonatype-snapshots" at "http://oss.sonatype.org/content/repositories/snapshots"

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

resolvers += "sfxcode-bintray" at "https://dl.bintray.com/sfxcode/maven"

resolvers += "bintray" at "https://jcenter.bintray.com"

// Test

libraryDependencies += "org.specs2" %% "specs2-core" % "4.0.1" % "test"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.4" % "test"


libraryDependencies += "org.json4s" %% "json4s-native" % "3.5.3" % "test"

// Compile

libraryDependencies += "com.sfxcode.sapphire" %% "sapphire-core" % "1.2.3"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.5.6"

libraryDependencies += "org.controlsfx" % "controlsfx" % "8.40.14" intransitive()

libraryDependencies += "de.jensd" % "fontawesomefx-commons" % "8.15"
libraryDependencies += "de.jensd" % "fontawesomefx-controls" % "8.15"

libraryDependencies += "de.jensd" % "fontawesomefx-fontawesome" % "4.7.0"
libraryDependencies += "de.jensd" % "fontawesomefx-materialicons" % "2.2.0"
libraryDependencies += "de.jensd" % "fontawesomefx-materialdesignfont" % "1.7.22"

licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.html"))

bintrayReleaseOnPublish in ThisBuild := false
