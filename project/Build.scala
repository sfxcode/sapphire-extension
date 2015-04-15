import sbt._

object ProjectDependencies {

  val sapphire_core = RootProject(file("../../sapphire/sapphire-core"))

}

object ProjectBuild extends Build {

  import ProjectDependencies._

  lazy val root = Project(id = "sapphire-extension", base = file(".")).dependsOn(sapphire_core)

}