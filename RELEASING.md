# Releasing

Releasing is done via Github:
Just finish the release draft, prefilled from GitHub Actions.

## OLD
For now, releasing is done manually.
(See https://docs.scala-lang.org/overviews/contributors/index.html.)

Choose version:

```bash
git describe
VERSION=0.5.0
git tag -am releasing v$VERSION
```

And publish:

```bash
sbt publishSigned
sbt sonatypeOpen
sbt sonatypeBundleRelease
git push origin v$VERSION
```
