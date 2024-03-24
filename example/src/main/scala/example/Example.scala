package example

import frawa.inlinefiles.InlineFiles._

object Example {
  val inlinedText          = inlineTextFile("./test-files/inlined.txt")
  val inlinedTextFiles     = inlineTextFiles("./test-files/folder", ".txt")
  val inlinedDeepTextFiles = inlineDeepTextFiles("./test-files/folder", ".txt")
}
