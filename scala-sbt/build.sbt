import scala.collection.generic.{GenericCompanion => assembly}
import scala.collection.generic.{GenericCompanion => Compile}
import scala.collection.generic.{GenericCompanion => run}
import scala.collection.generic.{GenericCompanion => javaOptions}


version := "0.1"

scalaVersion := "2.12.20"

lazy val root = (project in file("."))
  .settings(
    name := "scala2_12_20"
  )

val sparkVersion = "3.5.0"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion,
  "com.oracle.oci.sdk" % "oci-java-sdk-common" % "3.42.0",
  "org.apache.logging.log4j" % "log4j-api" % "2.17.2",
  "org.apache.logging.log4j" % "log4j-core" % "2.17.2"
)

dependencyOverrides ++= Seq(
  // Scala module 2.12.3 requires Jackson Databind version >= 2.12.0 and < 2.13.0 오류로 추가
  "com.fasterxml.jackson.core" % "jackson-core" % "2.12.7",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.12.7",
  "com.fasterxml.jackson.core" % "jackson-annotations" % "2.12.7",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.12.7"
)

lazy val buildSetting = Seq(
  assembly / assemblyJarName := s"${name}-v-${version}.jar"
)

assembly / assemblyMergeStrategy := {
  case PathList("META-INF", _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}

Compile / run / fork := true
Compile / run / javaOptions ++= Seq(
  "-Dspark.master=local[*]",
  "-Dlog4j.configurationFile=log4j2.properties",
  "--add-exports=java.base/sun.nio.ch=ALL-UNNAMED"
)