import sbt._
import Keys._

object BuildSettings {
    val buildOrganization = "org.analogweb"
    val buildVersion      = "0.9.1-SNAPSHOT"
    val buildScalaVersion = "2.10.4"

    val buildSettings = Defaults.defaultSettings ++ Seq (
      organization := buildOrganization,
      version      := buildVersion,
      scalaVersion := buildScalaVersion
    )
}
object Dependencies {
  val scalaplugin = "org.analogweb" %% "analogweb-scala" % "0.9.1-SNAPSHOT"
  val nettyplugin = "org.analogweb" % "analogweb-netty" % "0.9.1-SNAPSHOT"
  val slf4jplugin = "org.analogweb" % "analogweb-slf4j" % "0.9.1-SNAPSHOT"
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
    id = "helloscala",
    base = file("."),
    settings = buildSettings ++ Seq (
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
