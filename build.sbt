addCommandAlias("lint", "headerCheckAll;fmtCheck;fixCheck")
addCommandAlias("lintFix", "headerCreateAll;fixFix;fmtFix")
addCommandAlias("fmtCheck", "all scalafmtCheck scalafmtSbtCheck")
addCommandAlias("fmtFix", "all scalafmt scalafmtSbt")
addCommandAlias("fixCheck", "scalafixAll --check")
addCommandAlias("fixFix", "scalafixAll")
addCommandAlias("testAll", "test;+ test")

lazy val scalaVersion3 = "3.3.3"

import xerial.sbt.Sonatype._

lazy val sharedSettings = Seq(
  scalaVersion     := scalaVersion3,
  organization     := "io.github.frawa",
  organizationName := "Frank Wagner",
  startYear        := Some(2022),
  licenses += ("Apache-2.0", new URL("https://www.apache.org/licenses/LICENSE-2.0.txt")),
  description := "A macro library to inline file contents.",
  sonatypeProjectHosting := Some(
    GitHubHosting("frawa", "inline-files", "agilecoderfrank@gmail.com")
  ),
  sonatypeCredentialHost := "s01.oss.sonatype.org",
  sonatypeRepository     := "https://s01.oss.sonatype.org/service/local",
  versionScheme          := Some("semver-spec"),
  crossScalaVersions     := Nil
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
  // ThisBuild / semanticdbEnabled := true
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

lazy val inlineFiles = crossProject(JVMPlatform, JSPlatform)
  .crossType(CrossType.Pure)
  .withoutSuffixFor(JVMPlatform)
  .in(file("inline-files"))
  .settings(
    name := "inline-files"
  )
  .settings(sharedSettings)
  .settings(sharedScalacSettings)
  .settings(sharedTestSettings)
  .settings(scalacOptions ++= {
    Seq(
      s"-Xmacro-settings:MY_INLINE_HOME=${rootFolder.getAbsolutePath()}"
    )
  })

lazy val example = crossProject(JVMPlatform, JSPlatform)
  .crossType(CrossType.Pure)
  .withoutSuffixFor(JVMPlatform)
  .in(file("example"))
  .settings(
    scalaVersion   := scalaVersion3,
    name           := "example",
    publish / skip := true
  )
  .settings(sharedTestSettings)
  .settings(
    crossScalaVersions := Seq(scalaVersion3, "2.13.12")
    // scalacOptions ++= {
    //   CrossVersion.partialVersion(scalaVersion.value) match {
    //     case Some((2, 13)) => Seq("-Ytasty-reader")
    //     case _             => Seq.empty
    //   }
    // }
  )
  .dependsOn(inlineFiles)
