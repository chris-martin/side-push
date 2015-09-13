name := "side-push"

organization in ThisBuild := "side-push"

version in ThisBuild := "0.0"

scalaVersion in ThisBuild := "2.11.5"

scalacOptions in ThisBuild ++= Seq("-deprecation", "-feature")

lazy val core = project in file("core")

lazy val `atmosphere-lib` = (
  project in file("atmosphere-lib")
  dependsOn core
)

lazy val `atmosphere-socketio-lib` = (
  project in file("atmosphere-socketio-lib")
  dependsOn `atmosphere-lib`
)

lazy val `atmosphere-sockjs-lib` = (
  project in file("atmosphere-sockjs-lib")
  dependsOn `atmosphere-lib`
)

lazy val `nettosphere-lib` = (
  project in file("nettosphere-lib")
  dependsOn (`atmosphere-lib`, `atmosphere-socketio-lib`, `atmosphere-sockjs-lib`)
)

lazy val `nettosphere-server` = (
  project in file("nettosphere-server")
  dependsOn `nettosphere-lib`
)
