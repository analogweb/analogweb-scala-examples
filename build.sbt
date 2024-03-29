import sbtassembly.AssemblyPlugin._
import com.typesafe.sbt.packager.archetypes._

val buildOrganization = "org.analogweb"
val buildVersion      = "1"
val buildScalaVersion = "3.1.2"

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

val analogwebScalaVersion = "0.13.0"
val allDependency = Seq(
  "org.analogweb"  %% "analogweb-scala" % analogwebScalaVersion,
  "org.analogweb"  %% "analogweb-circe" % analogwebScalaVersion,
  "org.analogweb"  % "analogweb-netty"  % "0.11.6",
  "org.analogweb"  % "analogweb-slf4j"  % "0.11.1",
  "ch.qos.logback" % "logback-classic"  % "1.2.11"
)

val m2local  = Resolver.mavenLocal
val sonatype = Resolver.sonatypeRepo("snapshots")
val allResolver = Seq(
  m2local,
  sonatype
)

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
