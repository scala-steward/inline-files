package example

import munit.FunSuite

class ExampleTest extends FunSuite {

  test("inlined text") {
    assertEquals("foo", Example.inlinedText)
  }
}
