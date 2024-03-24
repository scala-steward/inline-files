package example

import munit.FunSuite

class ExampleTest extends FunSuite {

  test("inlined text") {
    assertEquals("This file content will be inlined.", Example.inlinedText)
  }
}
