import sbt.Keys._

lazy val commonSettings = Seq(
  scalaVersion := "2.12.1",
  version := "0.1.0",
  organization := "com.github.baccata"
)

lazy val GOPATH = "/Users/oliviermelois/go"

lazy val googleApi = project
  .in(file("third_party"))
  .settings(commonSettings)
  .settings(
    PB.targets in Compile := Seq(
      scalapb.gen() -> (sourceManaged in Compile).value
    ),
    PB.protoSources in Compile ++= Seq(file(
      s"$GOPATH/src/github.com/grpc-ecosystem/grpc-gateway/third_party/googleapis/")),
    libraryDependencies += "com.trueaccord.scalapb" %% "scalapb-runtime" % com.trueaccord.scalapb.compiler.Version.scalapbVersion % "protobuf"
  )

lazy val grpcDemo = project
  .in(file("."))
  .settings(commonSettings)
  .dependsOn(googleApi)
  .aggregate(googleApi)
  .settings(
    PB.targets in Compile := Seq(
      scalapb.gen() -> (sourceManaged in Compile).value
    ),
    PB.protocOptions in Compile ++= Seq(
      "-I/usr/local/include",
      s"-I$GOPATH/src",
      s"-I$GOPATH/src/github.com/grpc-ecosystem/grpc-gateway/third_party/googleapis",
       "--go_out=Mgoogle/api/annotations.proto=github.com/grpc-ecosystem/grpc-gateway/third_party/googleapis/google/api,plugins=grpc:./gateway",
       "--grpc-gateway_out=logtostderr=true:./gateway",
      "--swagger_out=logtostderr=true:./gateway"
    ),
    libraryDependencies ++= Seq(
      "io.grpc" % "grpc-all" % "1.1.2",
      "com.trueaccord.scalapb" %% "scalapb-runtime" % com.trueaccord.scalapb.compiler.Version.scalapbVersion % "protobuf",
      "com.trueaccord.scalapb" %% "scalapb-runtime-grpc" % com.trueaccord.scalapb.compiler.Version.scalapbVersion
    )
  )
