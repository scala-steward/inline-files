package frawa.inlinefiles

import scala.collection.immutable.Seq
import scala.quoted.*
import frawa.inlinefiles.compiletime.FileContents
import scala.annotation.experimental

@experimental
object WordsWithHome {
  def parseWords(text: String): Seq[Word] =
    text.split("\\s").toSeq.map(Word(_))

  inline def inlineWords(inline path: String)(homeSetting: String): Map[String, Seq[Word]] = ${
    inlineWords_impl('path, 'homeSetting)
  }

  @experimental
  private def inlineWords_impl(path: Expr[String], homeSetting: Expr[String])(using
      Quotes
  ): Expr[Map[String, Seq[Word]]] =
    given ToExpr[Word] with
      def apply(v: Word)(using Quotes): Expr[Word] =
        val vv = Expr(v.word)
        '{ Word($vv) }
    val home = InlineFilesWithHome.resolveHome(homeSetting.valueOrAbort)
    Expr(FileContents.parseTextContentsIn(path.valueOrAbort, ".txt", true, Some(home))(parseWords))

}
