name := "2022-PFSD-CaseStudy-01-Functional"

version := "0.1"

scalaVersion := "2.13.6"

idePackagePrefix := Some("eci.edu.co")

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.4"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.8" % Test
libraryDependencies += "com.github.tototoshi" %% "scala-csv" % "1.3.8"
libraryDependencies += "com.dimafeng" %% "testcontainers-scala-scalatest" % "0.39.8" % "test"
libraryDependencies += "com.dimafeng" %% "testcontainers-scala-postgresql" % "0.39.8" % "test"
libraryDependencies += "org.postgresql" % "postgresql" % "42.2.23"

Test / fork := true
