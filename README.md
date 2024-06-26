# inline-files

![ci](https://github.com/frawa/inline-files/actions/workflows/ci.yml/badge.svg)

[![inline-files Scala version support](https://index.scala-lang.org/frawa/inline-files/inline-files/latest.svg)](https://index.scala-lang.org/frawa/inline-files/inline-files)
[![inline-files Scala version support](https://index.scala-lang.org/frawa/inline-files/inline-files/latest-by-scala-version.svg?platform=sjs1)](https://index.scala-lang.org/frawa/inline-files/inline-files)[![inline-files Scala version support](https://index.scala-lang.org/frawa/inline-files/inline-files/latest-by-scala-version.svg?platform=jvm)](https://index.scala-lang.org/frawa/inline-files/inline-files)

[![Scala Steward badge](https://img.shields.io/badge/Scala_Steward-helping-blue.svg?style=flat&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAQCAMAAAARSr4IAAAAVFBMVEUAAACHjojlOy5NWlrKzcYRKjGFjIbp293YycuLa3pYY2LSqql4f3pCUFTgSjNodYRmcXUsPD/NTTbjRS+2jomhgnzNc223cGvZS0HaSD0XLjbaSjElhIr+AAAAAXRSTlMAQObYZgAAAHlJREFUCNdNyosOwyAIhWHAQS1Vt7a77/3fcxxdmv0xwmckutAR1nkm4ggbyEcg/wWmlGLDAA3oL50xi6fk5ffZ3E2E3QfZDCcCN2YtbEWZt+Drc6u6rlqv7Uk0LdKqqr5rk2UCRXOk0vmQKGfc94nOJyQjouF9H/wCc9gECEYfONoAAAAASUVORK5CYII=)](https://scala-steward.org)

This is a macro library for Scala 3.

Sometimes is is useful to access file contents without going to the file system.
It comes in handly for ScalaJS projects, as files are read during compile time only.

## Example

Inline files with extension `.txt` in sub folder `folder1/folder2` under `./inline-files-root/`:
```
   val inlined = inlineDeepTextFiles("./inline-files-root/", ".txt")
        .folder("folder1/folder2")
        .files()
```

See all examples in [tests](inline-files/src/test/scala/frawa/inlinefiles/InlineFilesTest.scala).


## References

- https://docs.scala-lang.org/scala3/guides/migration/tutorial-macro-mixing.html
- https://www.scala-sbt.org/1.x/docs/Cross-Build.html

