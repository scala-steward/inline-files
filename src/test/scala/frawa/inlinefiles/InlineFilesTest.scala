package frawa.inlinefiles

import InlineFiles.*

import munit.FunSuite

class InlineFilesTest extends FunSuite:

  test("inline a file") {
    val inlined = inlineTextFile("./test-files/inlined.txt")
    assertEquals(inlined, "This file content will be inlined.")
  }
