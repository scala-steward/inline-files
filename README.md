# inline-files

![ci](https://github.com/frawa/inline-files/actions/workflows/ci.yml/badge.svg)

_TODO_ Scaladex badge

_TODO_ Scala steward badge

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
