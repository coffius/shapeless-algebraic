name := "shapeless-algebraic"

version := "1.0"

scalaVersion := "2.12.5"

scalacOptions += "-Ypartial-unification"

libraryDependencies += "com.chuusai"                %% "shapeless"            % "2.3.3"
libraryDependencies += "org.scalatest"              %% "scalatest"            % "3.0.4"     % Test
