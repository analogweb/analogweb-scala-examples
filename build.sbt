import sbtassembly.AssemblyPlugin._
import com.typesafe.sbt.packager.archetypes._

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

val analogwebVersion = "0.9.11"
val scalaplugin = "org.analogweb" %% "analogweb-scala" % analogwebVersion 
val nettyplugin = "org.analogweb" % "analogweb-netty" % analogwebVersion 
val slf4jplugin = "org.analogweb" % "analogweb-slf4j" % analogwebVersion 
val guiceplugin = "org.analogweb" % "analogweb-guice" % analogwebVersion
val scalaguice  = "net.codingwell" %% "scala-guice" % "4.0.0"
val logback = "ch.qos.logback" % "logback-classic" % "1.1.2"
val allDependency = Seq (
    scalaplugin,
//  nettyplugin,
    slf4jplugin,
    guiceplugin,
    scalaguice,
    logback
)

val m2local = Resolver.mavenLocal
val sonatype = Resolver.sonatypeRepo("snapshots")
val allResolver = Seq(
  m2local,
  sonatype
)

lazy val root = Project (
  id = "analogweb-hello-scala",
  base = file("."),
  settings = buildSettings ++ asmSettings ++ Seq (
    resolvers ++= allResolver,
    libraryDependencies ++= allDependency,
    artifactName := { (sv: ScalaVersion, module: ModuleID, artifact: Artifact) =>
          artifact.name + "-" + module.revision + "." + artifact.extension
    }
  )
).enablePlugins(JavaAppPackaging)
