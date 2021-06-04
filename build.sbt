name := "CatsAndDynamoDB"

version := "0.1"

scalaVersion := "2.13.6"

libraryDependencies ++= Seq(
  // cats
  "org.typelevel" %% "cats-core" % "2.1.1",
  "org.typelevel" %% "cats-effect" % "2.1.1",

  // https://mvnrepository.com/artifact/org.scanamo/scanamo-cats-effect
  "org.scanamo" %% "scanamo-cats-effect" % "1.0.0-M15",

  // aws
  // "com.amazonaws" % "aws-java-sdk-dynamodb" % "1.11.1032",
  "software.amazon.awssdk" % "dynamodb" % "2.16.76"
)
