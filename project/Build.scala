import sbt._
import Keys._
import sbtassembly._
import sbtassembly.AssemblyPlugin._
import sbtassembly.AssemblyKeys._

object BuildSettings {
    val buildOrganization = "org.analogweb"
    val buildVersion      = "1"
    val buildScalaVersion = "2.11.6"

    val buildSettings = Defaults.defaultSettings ++ Seq (
      organization := buildOrganization,
      version      := buildVersion,
      scalaVersion := buildScalaVersion
    )
    val asmSettings = assemblySettings ++ Seq (
      assemblyJarName := "analogweb-hello-scala-" + buildVersion + ".jar",
      assemblyMergeStrategy in assembly := {
        case PathList(ps @ _*) if ps.last endsWith ".properties" => MergeStrategy.first 
        case x =>
          val oldStrategy = (assemblyMergeStrategy in assembly).value
          oldStrategy(x)
      }
    )
}

object Dependencies {
  val analogwebVersion = "0.9.10"
  val scalaplugin = "org.analogweb" %% "analogweb-scala" % analogwebVersion 
  val nettyplugin = "org.analogweb" % "analogweb-netty" % analogwebVersion 
  val slf4jplugin = "org.analogweb" % "analogweb-slf4j" % analogwebVersion 
  val guiceplugin = "org.analogweb" % "analogweb-guice" % analogwebVersion
  val scalaguice  = "net.codingwell" %% "scala-guice" % "4.0.0"
  val logback = "ch.qos.logback" % "logback-classic" % "1.1.2"
  val all = Seq (
    scalaplugin,
    nettyplugin,
    slf4jplugin,
    guiceplugin,
    scalaguice,
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
