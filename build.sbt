ThisBuild / resolvers ++= Seq(
  "Apache Development Snapshot Repository" at "https://repository.apache.org/content/repositories/snapshots/",
  Resolver.mavenLocal
)

name := "flink-kafka-druid"

version := "0.1-SNAPSHOT"

organization := "org.example"

ThisBuild / scalaVersion := "2.11.12"

val flinkVersion = "1.11.2"
val jacksonVersion = "2.9.9"

val flinkDependencies = Seq(
  "org.apache.flink" %% "flink-scala" % flinkVersion % "provided",
  "org.apache.flink" %% "flink-streaming-scala" % flinkVersion % "provided",
  "org.apache.flink" %% "flink-connector-kafka" % flinkVersion
)

val jacksonDependencies = Seq(
  "com.fasterxml.jackson.core" % "jackson-core" % jacksonVersion force (),
  "com.fasterxml.jackson.core" % "jackson-databind" % jacksonVersion force (),
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % jacksonVersion force ()
)

lazy val root = (project in file(".")).settings(
  libraryDependencies ++= flinkDependencies ++ jacksonDependencies
)

assembly / mainClass := Some("org.example.WordCount")

// make run command include the provided dependencies
Compile / run  := Defaults.runTask(Compile / fullClasspath,
  Compile / run / mainClass,
  Compile / run / runner
).evaluated

// stays inside the sbt console when we press "ctrl-c" while a Flink programme executes with "run" or "runMain"
Compile / run / fork := true
Global / cancelable := true

// exclude Scala library from assembly
assembly / assemblyOption  := (assembly / assemblyOption).value.copy(includeScala = false)
