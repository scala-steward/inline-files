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

package frawa.inlinefiles

import munit.FunSuite

import scala.annotation.experimental
import scala.collection.immutable.Seq

@experimental
class InlineFilesWithHomeTest extends FunSuite:
  import InlineFilesWithHome.*

  test("inline a file") {
    val inlined = inlineTextFile("./test-files/inlined.txt")("MY_INLINE_HOME")
    assertEquals(inlined, "This file content will be inlined.")
  }

  test("inline files in a folder") {
    val inlined = inlineTextFiles("./test-files/folder", ".txt")("MY_INLINE_HOME")
    assertEquals(
      inlined,
      Map(
        "inlined1.txt" -> "First",
        "inlined2.txt" -> "Second"
      )
    )
  }

  test("inline files in nested folders") {
    val inlined = inlineDeepTextFiles("./test-files/folder", ".txt")("MY_INLINE_HOME")
    assertEquals(
      inlined,
      Map(
        "inlined1.txt"      -> "First",
        "inlined2.txt"      -> "Second",
        "deep/inlined3.txt" -> "Third\nand more"
      )
    )
  }

  test("filter inline files in nested folders") {
    val inlined =
      inlineDeepTextFiles("./test-files/folder", ".txt")("MY_INLINE_HOME").folder("deep")
    assertEquals(
      inlined,
      Map(
        "inlined3.txt" -> "Third\nand more"
      )
    )
  }

  test("filter inline files without nested folders") {
    val inlined = inlineDeepTextFiles("./test-files/folder", ".txt")("MY_INLINE_HOME").files()
    assertEquals(
      inlined,
      Map(
        "inlined1.txt" -> "First",
        "inlined2.txt" -> "Second"
      )
    )
  }

  test("compile-time mapped inline files in nested folders") {
    val inlined: Map[String, Seq[Word]] =
      WordsWithHome.inlineWords("./test-files/folder")("MY_INLINE_HOME")
    assertEquals(
      inlined,
      Map(
        "inlined1.txt"      -> Seq(Word("First")),
        "inlined2.txt"      -> Seq(Word("Second")),
        "deep/inlined3.txt" -> Seq(Word("Third"), Word("and"), Word("more"))
      )
    )
  }
