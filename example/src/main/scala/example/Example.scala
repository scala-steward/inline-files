package example

import frawa.inlinefiles.InlineFiles.inlineTextFile

object Example {
  val inlinedText = inlineTextFile("./test-files/inlined.txt")
}
