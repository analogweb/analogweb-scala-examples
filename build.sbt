import sbtassembly.AssemblyPlugin._
import com.typesafe.sbt.packager.archetypes._

val buildOrganization = "org.analogweb"
val buildVersion      = "1"
val buildScalaVersion = "2.12.4"

val buildSettings = Seq(
  organization := buildOrganization,
  version := buildVersion,
  scalaVersion := buildScalaVersion
)

val asmSettings = assemblySettings ++ Seq(
  assemblyJarName := "analogweb-hello-scala-" + buildVersion + ".jar",
  assemblyMergeStrategy in assembly := {
    case PathList(ps @ _*) if ps.last endsWith ".properties" => MergeStrategy.first
    case x =>
      val oldStrategy = (assemblyMergeStrategy in assembly).value
      oldStrategy(x)
  }
)

val analogwebVersion = "0.10.1"
val allDependency = Seq(
  "org.analogweb"  %% "analogweb-scala" % analogwebVersion,
  "org.analogweb"  %% "analogweb-circe" % analogwebVersion,
  "org.analogweb"  % "analogweb-netty"  % analogwebVersion,
  "org.analogweb"  % "analogweb-slf4j"  % analogwebVersion,
  "ch.qos.logback" % "logback-classic"  % "1.2.3"
)

val m2local  = Resolver.mavenLocal
val sonatype = Resolver.sonatypeRepo("snapshots")
val allResolver = Seq(
  m2local,
  sonatype
)

fork in run := true

lazy val root =
  (project in file("."))
    .settings(buildSettings: _*)
    .settings(asmSettings)
    .settings(
      name := "analogweb-scala-example",
      resolvers ++= allResolver,
      libraryDependencies ++= allDependency,
      artifactName := { (sv: ScalaVersion, module: ModuleID, artifact: Artifact) =>
        artifact.name + "-" + module.revision + "." + artifact.extension
      }
    )
    .enablePlugins(JavaAppPackaging)
