name := "algebraic-streams"

version := "1.0"

scalaVersion := "2.12.5"

scalacOptions += "-Xfatal-warnings"

scalacOptions += "-feature"

scalacOptions += "-language:higherKinds"

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.6")

libraryDependencies += "com.chuusai"    %% "shapeless"      % "2.3.3"
libraryDependencies += "org.typelevel"  %% "cats-free"      % "1.1.0"
libraryDependencies += "co.fs2"         %% "fs2-core"       % "0.10.3"
libraryDependencies += "org.scalatest"  %% "scalatest"      % "3.0.4"     % Test
libraryDependencies += "org.scalamock"  %% "scalamock"      % "4.1.0" % Test
