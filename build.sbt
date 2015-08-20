name := "dungeon"

version := "1.0"

scalaVersion := "2.11.7"

javaOptions in run += "-Djava.library.path=lib/"

mainClass in (Compile, run) := Some("me.golubev.dungeon.Runner")

fork := true