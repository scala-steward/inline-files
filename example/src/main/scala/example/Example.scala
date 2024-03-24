/*
 * Copyright 2022 example
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

import frawa.inlinefiles.InlineFiles._

object Example {
  val inlinedText          = inlineTextFile("./test-files/inlined.txt")
  val inlinedTextFiles     = inlineTextFiles("./test-files/folder", ".txt")
  val inlinedDeepTextFiles = inlineDeepTextFiles("./test-files/folder", ".txt")
}
