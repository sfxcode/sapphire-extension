name := "sapphire-extension"

organization := "com.sfxcode.sapphire"

version := "0.4.1"

scalaVersion := "2.11.6"

scalacOptions += "-deprecation"

parallelExecution in Test := false

(testOptions in Test) += Tests.Argument(TestFrameworks.Specs2, "html")

javacOptions ++= Seq("-source", "1.8")

javacOptions ++= Seq("-target", "1.8")

scalacOptions += "-target:jvm-1.7"


// resolvers


resolvers ++= Seq(
  "sfxcode-releases" at "https://raw.github.com/sfxcode/mvn-repo/master/releases",
  "sfxcode-snapshots" at "https://raw.github.com/sfxcode/mvn-repo/master/snapshots",
  "sonatype-snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"
)

// Test

libraryDependencies += "org.specs2" %% "specs2-core" % "3.6" % "test"

libraryDependencies += "org.json4s" %% "json4s-native" % "3.2.11" % "test"

libraryDependencies += "com.sfxcode.sapphire" %% "sapphire-core" % "1.0.0"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.3.11"

libraryDependencies += "org.controlsfx" % "controlsfx" % "8.40.9" intransitive()

libraryDependencies += "de.jensd" % "fontawesomefx" % "8.4"


publishTo := {
  if (version.value.trim.endsWith("SNAPSHOT"))
    Some(Resolver.file("file", new File("/Users/tom/projects/sfxcode/mvn-repo/snapshots")))
  else
    Some(Resolver.file("file", new File("/Users/tom/projects/sfxcode/mvn-repo/releases")))
}



