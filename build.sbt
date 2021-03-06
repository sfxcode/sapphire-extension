import sbt.url

import scala.xml.transform.{RewriteRule, RuleTransformer}
import scala.xml.{Node => XmlNode, NodeSeq => XmlNodeSeq, _}

name := "sapphire-extension"

organization := "com.sfxcode.sapphire"

crossScalaVersions := Seq("2.13.3", "2.12.12")

scalaVersion := crossScalaVersions.value.head

scalacOptions += "-deprecation"

compileOrder := CompileOrder.JavaThenScala

javacOptions in test += "-Dorg.apache.deltaspike.ProjectStage=Test"

parallelExecution in Test := false

val JavaFXVersion       = "15.0.1"
val SapphireCoreVersion = "2.0.1"
val Json4sVersion       = "3.6.10"
val LogbackVersion      = "1.2.3"
val IkonliVersion       = "11.5.0"

val osName = System.getProperty("os.name") match {
  case n if n.startsWith("Linux")   => "linux"
  case n if n.startsWith("Mac")     => "mac"
  case n if n.startsWith("Windows") => "win"
  case _                            => throw new Exception("Unknown platform!")
}

lazy val sapphire_extension_root =
  Project(id = "sapphire-extension", base = file("."))
    .settings(description := "Sapphire Extension")

lazy val demo_showcase =
  Project(id = "sapphire-extension-showcase", base = file("demos/showcase"))
    .settings(
      scalaVersion := "2.13.3",
      name := "sapphire-extension-showcase",
      description := "Sapphire Extension Showcase",
      libraryDependencies ++= Seq(
            "base",
            "controls",
            "fxml",
            "graphics",
            "media",
            "swing",
            "web"
          ).map(m => "org.openjfx" % s"javafx-$m" % JavaFXVersion classifier osName),
      libraryDependencies += "com.sfxcode.sapphire" %% "sapphire-core"   % SapphireCoreVersion,
      libraryDependencies += "org.json4s"           %% "json4s-native"   % Json4sVersion,
      libraryDependencies += "ch.qos.logback"        % "logback-classic" % LogbackVersion,
      resolvers += "sandec" at "https://sandec.bintray.com/repo",
      libraryDependencies += "com.sandec"            % "mdfx"          % "0.1.6",
      libraryDependencies += "com.jfoenix"           % "jfoenix"       % "9.0.9",
      libraryDependencies += "org.fxmisc.richtext"   % "richtextfx"    % "0.10.3",
      libraryDependencies += "com.github.pathikrit" %% "better-files"  % "3.9.1",
      libraryDependencies += "net.sf.jasperreports"  % "jasperreports" % "6.15.0",
      libraryDependencies += "xerces"                % "xercesImpl"    % "2.12.0",
      mainClass := Some("com.sfxcode.sapphire.extension.showcase.Application")
    )
    .dependsOn(sapphire_extension_root)

lazy val sapphire_extension_scenebuilder = Project(
  id = "sapphire-extension-scenebuilder",
  base = file("scenebuilder")
).settings(
  name := "sapphire-extension-scenebuilder",
  description := "Sapphire Extension Scenebuilder",
  crossPaths := false,
  libraryDependencies ++= Seq(
        "base",
        "controls",
        "fxml",
        "graphics",
        "media",
        "swing",
        "web"
      ).map(m => "org.openjfx" % s"javafx-$m" % JavaFXVersion classifier osName)
)

addCommandAlias("run-showcase", "sapphire-extension-showcase/run")

// resolvers

resolvers += Resolver.jcenterRepo

resolvers += "sfxcode-maven" at "https://bintray.com/sfxcode/maven/"

// Test

libraryDependencies += "org.specs2" %% "specs2-core" % "4.10.5" % Test

libraryDependencies += "org.json4s" %% "json4s-native" % Json4sVersion % Test

// Provided

libraryDependencies ++= Seq(
  "base",
  "controls",
  "fxml",
  "graphics",
  "media",
  "swing",
  "web"
).map(m => "org.openjfx" % s"javafx-$m" % JavaFXVersion % Provided classifier osName)

libraryDependencies += "ch.qos.logback" % "logback-classic" % LogbackVersion % Provided

libraryDependencies += "com.sfxcode.sapphire" %% "sapphire-core" % SapphireCoreVersion % Provided

libraryDependencies += "com.github.pathikrit" %% "better-files" % "3.9.1" % Provided

// concurrent / akka

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.6.10" % Provided

// report

resolvers += "jasperreports-repo" at "https://jaspersoft.jfrog.io/jaspersoft/third-party-ce-artifacts"

libraryDependencies += "net.sf.jasperreports" % "jasperreports" % "6.15.0" % Provided

// Compile

libraryDependencies += "org.controlsfx" % "controlsfx" % "11.0.2" intransitive ()

libraryDependencies += "org.kordamp.ikonli" % "ikonli-javafx" % IkonliVersion

libraryDependencies += "org.kordamp.ikonli" % "ikonli-fontawesome-pack" % IkonliVersion

licenses += ("Apache-2.0", url(
  "https://www.apache.org/licenses/LICENSE-2.0.html"
))

enablePlugins(BuildInfoPlugin)

buildInfoPackage := "com.sfxcode.sapphire.extension"

buildInfoOptions += BuildInfoOption.BuildTime

// publish

// Use `pomPostProcess` to remove dependencies marked as "provided" from publishing in POM
// This is to avoid dependency on wrong OS version JavaFX libraries [Issue #289]
// See also [https://stackoverflow.com/questions/27835740/sbt-exclude-certain-dependency-only-during-publish]

pomPostProcess := { node: XmlNode =>
  new RuleTransformer(new RewriteRule {
    override def transform(node: XmlNode): XmlNodeSeq =
      node match {
        case e: Elem
            if e.label == "dependency" && e.child
                .exists(c => c.label == "scope" && c.text == "provided")
              && e.child
                .exists(c => c.label == "groupId" && c.text == "org.openjfx") =>
          val organization =
            e.child.filter(_.label == "groupId").flatMap(_.text).mkString
          val artifact =
            e.child.filter(_.label == "artifactId").flatMap(_.text).mkString
          val version =
            e.child.filter(_.label == "version").flatMap(_.text).mkString
          Comment(
            s"provided dependency $organization#$artifact;$version has been omitted"
          )
        case _ => node
      }
  }).transform(node).head
}

scalafmtOnCompile := false

releaseCrossBuild := true

bintrayReleaseOnPublish in ThisBuild := true

publishMavenStyle := true

homepage := Some(url("https://github.com/sfxcode/sapphire-extension"))

scmInfo := Some(
  ScmInfo(
    url("https://github.com/sfxcode/sapphire-core"),
    "scm:https://github.com/sfxcode/sapphire-core.git"
  )
)

developers := List(
  Developer(
    id = "sfxcode",
    name = "Tom Lamers",
    email = "tom@sfxcode.com",
    url = url("https://github.com/sfxcode")
  )
)

lazy val docs = (project in file("docs"))
  .enablePlugins(ParadoxSitePlugin)
  .enablePlugins(ParadoxMaterialThemePlugin)
  .enablePlugins(GhpagesPlugin)
  .settings(
    scalaVersion := "2.13.2",
    name := "sapphire extension docs",
    publish / skip := true,
    ghpagesNoJekyll := true,
    git.remoteRepo := "git@github.com:sfxcode/sapphire-extension.git",
    Compile / paradoxMaterialTheme ~= {
      _.withRepository(uri("https://github.com/sfxcode/sapphire-extension"))

    },
    (Compile / paradoxMarkdownToHtml / excludeFilter) := (Compile / paradoxMarkdownToHtml / excludeFilter).value ||
          ParadoxPlugin.InDirectoryFilter(
            (Compile / paradox / sourceDirectory).value / "includes"
          )
  )
