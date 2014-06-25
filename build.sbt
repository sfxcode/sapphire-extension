name := "sapphire-control"

organization := "com.sfxcode.sapphire"

version := "0.9.1"

scalaVersion := "2.11.1"

scalacOptions += "-deprecation"

parallelExecution in Test := false

(testOptions in Test) += Tests.Argument(TestFrameworks.Specs2, "html")

javacOptions ++= Seq("-source", "1.7")

javacOptions ++= Seq("-target", "1.7")

scalacOptions += "-target:jvm-1.7"


// resolvers


resolvers ++= Seq(
  "sfxcode-releases" at "https://raw.github.com/sfxcode/mvn-repo/master/releases",
  "sfxcode-snapshots" at "https://raw.github.com/sfxcode/mvn-repo/master/snapshots"
)

// Test

libraryDependencies += "org.specs2" %% "specs2" % "2.3.12" % "test"

libraryDependencies += "org.json4s" %% "json4s-native" % "3.2.9" % "test"


libraryDependencies += "com.sfxcode.sapphire" %% "sapphire-core" % "0.9.1"

libraryDependencies += "org.controlsfx" % "controlsfx" % "8.0.6"

publishTo := {
  if (version.value.trim.endsWith("SNAPSHOT"))
    Some(Resolver.file("file", new File("/Users/tom/projects/sfxcode/mvn-repo/snapshots")))
  else
    Some(Resolver.file("file", new File("/Users/tom/projects/sfxcode/mvn-repo/releases")))
}



