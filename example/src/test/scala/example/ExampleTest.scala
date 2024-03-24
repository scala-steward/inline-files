package example

import munit.FunSuite

class ExampleTest extends FunSuite {

  test("inlined text") {
    assertEquals(Example.inlinedText, "This file content will be inlined.")
  }

  test("inline files in a folder") {
    assertEquals(
      Example.inlinedTextFiles,
      Map(
        "inlined1.txt" -> "First",
        "inlined2.txt" -> "Second"
      )
    )
  }

  test("inline files in nested folders") {
    assertEquals(
      Example.inlinedDeepTextFiles,
      Map(
        "inlined1.txt"      -> "First",
        "inlined2.txt"      -> "Second",
        "deep/inlined3.txt" -> "Third\nand more"
      )
    )
  }

}
