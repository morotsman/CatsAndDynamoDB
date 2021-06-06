name := "CatsAndDynamoDB"

version := "0.1"

scalaVersion := "2.13.6"

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
  case x => MergeStrategy.first
}

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

// needed for the tests
libraryDependencies += "org.scanamo" %% "scanamo-testkit" % "1.0.0-M15" % Test
libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.9"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.9" % "test"
resolvers += "DynamoDB Local Release Repository" at "https://s3-us-west-2.amazonaws.com/dynamodb-local/release"
libraryDependencies += "com.amazonaws" % "aws-java-sdk-dynamodb" % "latest.integration"
libraryDependencies += "com.amazonaws" % "DynamoDBLocal" % "latest.integration" % "test"

libraryDependencies += "com.almworks.sqlite4java" % "sqlite4java" % "latest.integration" % "test"
libraryDependencies += "com.almworks.sqlite4java" % "sqlite4java-win32-x86" % "latest.integration" % "test"
libraryDependencies += "com.almworks.sqlite4java" % "sqlite4java-win32-x64" % "latest.integration" % "test"
libraryDependencies += "com.almworks.sqlite4java" % "libsqlite4java-osx" % "latest.integration" % "test"
libraryDependencies += "com.almworks.sqlite4java" % "libsqlite4java-linux-i386" % "latest.integration" % "test"
libraryDependencies += "com.almworks.sqlite4java" % "libsqlite4java-linux-amd64" % "latest.integration" % "test"

lazy val copyJars = taskKey[Unit]("copyJars")
copyJars := {
  import java.nio.file.Files
  import java.io.File
  // For Local Dynamo DB to work, we need to copy SQLLite native libs from
  // our test dependencies into a directory that Java can find ("lib" in this case)
  // Then in our Java/Scala program, we need to set System.setProperty("sqlite4java.library.path", "lib");
  // before attempting to instantiate a DynamoDBEmbedded instance
  val artifactTypes = Set("dylib", "so", "dll")
  val files = Classpaths.managedJars(Test, artifactTypes, update.value).files
  Files.createDirectories(new File(baseDirectory.value, "native-libs").toPath)
  files.foreach { f =>
    val fileToCopy = new File("native-libs", f.name)
    if (!fileToCopy.exists()) {
      Files.copy(f.toPath, fileToCopy.toPath)
    }
  }
}

(compile in Compile) := (compile in Compile).dependsOn(copyJars).value