import sbt._
import Keys._
import sbtassembly.AssemblyPlugin._
import sbtassembly.AssemblyKeys._

object BuildSettings {
    val buildOrganization = "org.analogweb"
    val buildVersion      = "0.9.7-SNAPSHOT"
    val buildScalaVersion = "2.11.6"

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
  val analogwebVersion = "0.9.6"
  val scalaplugin = "org.analogweb" %% "analogweb-scala" % analogwebVersion 
  val nettyplugin = "org.analogweb" % "analogweb-netty" % analogwebVersion 
  val slf4jplugin = "org.analogweb" % "analogweb-slf4j" % analogwebVersion 
  val logback = "ch.qos.logback" % "logback-classic" % "1.1.2"
  val all = Seq (
    scalaplugin,
    nettyplugin,
    slf4jplugin,
    logback
  )
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

  lazy val root = Project (
    id = "analogweb-hello-scala",
    base = file("."),
    settings = buildSettings ++ asmSettings ++ Seq (
      resolvers ++= Resolvers.all,
      libraryDependencies ++= Dependencies.all,
      artifactName := { (sv: ScalaVersion, module: ModuleID, artifact: Artifact) =>
            artifact.name + "-" + module.revision + "." + artifact.extension
      }
    )
  )
}
