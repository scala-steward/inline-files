addCommandAlias("lint", "headerCheckAll;fmtCheck;fixCheck;npmAll")
addCommandAlias("lintFix", "headerCreateAll;fixFix;fmtFix")
addCommandAlias("fmtCheck", "all scalafmtCheck scalafmtSbtCheck")
addCommandAlias("fmtFix", "all scalafmt scalafmtSbt")
addCommandAlias("fixCheck", "scalafixAll --check")
addCommandAlias("fixFix", "scalafixAll")

lazy val scalaVersion3 = "3.3.0"

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
  versionScheme          := Some("semver-spec")
)

lazy val sharedPlatformSettings = Seq(
  scalaVersion3
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
  },
  ThisBuild / semanticdbEnabled := true
)

lazy val sharedTestSettings = Seq(
  libraryDependencies += "org.scalameta" %%% "munit" % "1.0.0-M8" % Test,
  Test / testOptions += Tests.Argument("-q", "--summary=0")
)

lazy val root = project
  .in(file("."))
  .settings(
    name           := "inline-files-root",
    publish / skip := true
  )
  .aggregate(inlineFiles.jvm, inlineFiles.js)
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
