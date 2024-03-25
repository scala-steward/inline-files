addCommandAlias("lint", "headerCheckAll;fmtCheck;fixCheck")
addCommandAlias("lintFix", "headerCreateAll;fixFix;fmtFix")
addCommandAlias("fmtCheck", "all scalafmtCheck scalafmtSbtCheck")
addCommandAlias("fmtFix", "all scalafmt scalafmtSbt")
addCommandAlias("fixCheck", "scalafixAll --check")
addCommandAlias("fixFix", "scalafixAll")
addCommandAlias("testAll", "test;+ test")

lazy val scalaVersion3   = "3.3.3"
lazy val scalaVersion213 = "2.13.13"

import xerial.sbt.Sonatype._

lazy val sharedSettings = Seq(
  scalaVersion     := scalaVersion3,
  organization     := "io.github.frawa",
  organizationName := "Frank Wagner",
  description      := "A macro library to inline file contents.",
  sonatypeProjectHosting := Some(
    GitHubHosting("frawa", "inline-files", "agilecoderfrank@gmail.com")
  ),
  sonatypeCredentialHost := "s01.oss.sonatype.org",
  sonatypeRepository     := "https://s01.oss.sonatype.org/service/local",
  versionScheme          := Some("semver-spec"),
  crossScalaVersions     := Nil,
  publishTo              := sonatypePublishToBundle.value
)

lazy val sharedLintSettings = Seq(
  startYear := Some(2022),
  licenses += ("Apache-2.0", new URL("https://www.apache.org/licenses/LICENSE-2.0.txt")),
  semanticdbEnabled := true,
  semanticdbVersion := scalafixSemanticdb.revision
)

lazy val sharedScalacSettings = Seq(
  scalacOptions ++= {
    Seq(
      "-Xcheck-macros",
      "-deprecation",
      "-feature",
      "-unchecked",
      "-Xmigration",
      "-new-syntax",
      "-indent"
    )
  }
)

lazy val sharedTestSettings = Seq(
  libraryDependencies += "org.scalameta" %%% "munit" % "1.0.0-M11" % Test,
  Test / testOptions += Tests.Argument("-q", "--summary=0")
)

lazy val rootFolder = file(".")
lazy val root = project
  .in(rootFolder)
  .settings(
    name           := "inline-files-root",
    publish / skip := true
  )
  .aggregate(inlineFiles.jvm, inlineFiles.js)
  .aggregate(example.jvm, example.js)
  .settings(sharedSettings)
  .settings(sharedLintSettings)

lazy val inlineFiles = crossProject(JVMPlatform, JSPlatform)
  .crossType(CrossType.Pure)
  .withoutSuffixFor(JVMPlatform)
  .in(file("inline-files"))
  .settings(
    name := "inline-files"
  )
  .settings(sharedSettings)
  .settings(sharedLintSettings)
  .settings(sharedScalacSettings)
  .settings(sharedTestSettings)
  .settings(scalacOptions ++= {
    Seq(
      s"-Xmacro-settings:MY_INLINE_HOME=${rootFolder.getAbsolutePath()}"
    )
  })
  .settings(
    libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion213
  )

lazy val example = crossProject(JVMPlatform, JSPlatform)
  .crossType(CrossType.Pure)
  .withoutSuffixFor(JVMPlatform)
  .in(file("example"))
  .settings(
    scalaVersion   := scalaVersion3,
    name           := "example",
    publish / skip := true
  )
  .settings(sharedLintSettings)
  .settings(sharedTestSettings)
  .settings(
    crossScalaVersions := Seq(scalaVersion3, scalaVersion213),
    scalacOptions ++= {
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((2, 13)) => Seq("-Ytasty-reader")
        case _             => Seq.empty
      }
    }
  )
  .dependsOn(inlineFiles)
