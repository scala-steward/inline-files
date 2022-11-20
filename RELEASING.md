# Realising

For now, relasing is done manually.
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
sbt sonatypeRelease
git push origin v$VERSION
```
