name := """PlayTutorial"""

version := "0.0.1"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  // database
  "com.typesafe.play" %% "play-slick" % "2.0.2",
  "com.typesafe.slick" %% "slick" % "3.1.1",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.1.1",
  "org.postgresql" % "postgresql" % "9.4.1209.jre7",
  // others
  "org.slf4j" % "slf4j-nop" % "1.7.20",
  "org.apache.commons" % "commons-math3" % "3.6.1",
  // for test
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
