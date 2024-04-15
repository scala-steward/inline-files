/*
 * Copyright 2022 Frank Wagner
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package example

import munit.FunSuite

class ExampleTest extends FunSuite {

  test("inlined text") {
    assertEquals(Example.inlinedText, "This file content will be inlined.")
  }

  test("inlined large file") {
    val lines = Example.largeText.split('\n').size
    assertEquals(lines, 11313)
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

  test("inline large file in folder") {
    val lines = Example.largeInFolder.view.mapValues(_.split('\n').size).toMap
    assertEquals(
      lines,
      Map(
        "large.txt" -> 11313
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
