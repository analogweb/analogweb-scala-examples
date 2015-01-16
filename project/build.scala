import sbt._
import Keys._
import sbtassembly.AssemblyPlugin._
import sbtassembly.AssemblyKeys._

object BuildSettings {
    val buildOrganization = "org.analogweb"
    val buildVersion      = "0.9.2-SNAPSHOT"
    val buildScalaVersion = "2.10.4"

    val buildSettings = Defaults.defaultSettings ++ Seq (
      organization := buildOrganization,
      version      := buildVersion,
      scalaVersion := buildScalaVersion
    )
    val asmSettings = assemblySettings ++ Seq (
      assemblyJarName := "analogweb-hello-scala-" + buildVersion + ".jar"
    )
}

object Dependencies {
  val scalaplugin = "org.analogweb" %% "analogweb-scala" % "0.9.3-SNAPSHOT"
  val nettyplugin = "org.analogweb" % "analogweb-netty" % "0.9.2"
  val slf4jplugin = "org.analogweb" % "analogweb-slf4j" % "0.9.2"
  val logback = "ch.qos.logback" % "logback-classic" % "1.1.2"
}

object Resolvers {
  val m2local = Resolver.mavenLocal
  val sonatype = Resolver.sonatypeRepo("snapshots")
  val all = Seq (
    m2local,
    sonatype
  )
}

object AnalogwebScala extends Build {
  import BuildSettings._
  import Dependencies._

  lazy val root = Project (
    id = "analogweb-hello-scala",
    base = file("."),
    settings = buildSettings ++ asmSettings ++ Seq (
      resolvers ++= Resolvers.all,
      libraryDependencies ++= Seq(
          scalaplugin,
          nettyplugin,
          slf4jplugin,
          logback
      ),
      artifactName := { (sv: ScalaVersion, module: ModuleID, artifact: Artifact) =>
            artifact.name + "-" + module.revision + "." + artifact.extension
      }
    )
  )
}
