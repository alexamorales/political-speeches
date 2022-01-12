name := "political-speeches"

version := "0.1"

scalaVersion := "2.13.8"

val http4sVersion    = "0.21.31"
val zioVersion       = "1.0.12"
val zioCatsVersion   = "2.5.1.0"
val kantanVerion     = "0.6.2"
val circeVersion     = "0.14.1"
val scalatestVersion = "3.2.10"

idePackagePrefix := Some("com.political.speeches")

libraryDependencies ++= Seq(
  "dev.zio"       %% "zio"                 % zioVersion,
  "org.http4s"    %% "http4s-dsl"          % http4sVersion,
  "org.http4s"    %% "http4s-blaze-client" % http4sVersion,
  "org.http4s"    %% "http4s-blaze-server" % http4sVersion,
  "org.http4s"    %% "http4s-circe"        % http4sVersion,
  "dev.zio"       %% "zio-interop-cats"    % zioCatsVersion,
  "com.nrinaudo"  %% "kantan.csv-generic"  % kantanVerion,
//  "com.nrinaudo"  %% "kantan.csv-java8"    % kantanVerion,
  "io.circe"      %% "circe-generic"       % circeVersion,
  "io.circe"      %% "circe-parser"        % circeVersion,
  "io.circe"      %% "circe-scodec"        % circeVersion,
  "org.scalatest" %% "scalatest"           % scalatestVersion % "test"
)

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding",
  "UTF-8",
  "-language:higherKinds",
  "-language:postfixOps",
  "-feature",
  "-Xfatal-warnings"
)
