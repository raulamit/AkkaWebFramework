name := "WebServer"

version := "1.0"

scalaVersion := "2.12.1"

scalaVersion := "2.12.2"

lazy val akkaVersion = "2.5.3"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
"com.typesafe.akka" %% "akka-cluster" % akkaVersion
)
libraryDependencies += "org.apache.logging.log4j" % "log4j-api" % "2.10.0"
libraryDependencies += "org.apache.logging.log4j" % "log4j-core" % "2.10.0"

// https://mvnrepository.com/artifact/io.netty/netty-all
libraryDependencies += "io.netty" % "netty-all" % "4.1.17.Final"