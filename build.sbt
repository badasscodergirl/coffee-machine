import Dependencies._

ThisBuild / scalaVersion     := "2.13.2"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.dunzo"
ThisBuild / organizationName := "dunzo"

resolvers += "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/"

lazy val root = (project in file("."))
  .settings(
    name := "coffeemachine",
    libraryDependencies += scalaTest % Test,
    libraryDependencies ++= Seq("com.typesafe.play" % "play-json_2.13" % "2.7.4")
  )

