package frawa.inlinefiles

import scala.quoted.*
import frawa.inlinefiles.compiletime.FileContents

case class Word(word: String)

object Words {
  def parseWords(text: String): Seq[Word] = text.split("\\s").map(Word(_))

  inline def inlineWords(inline path: String): Map[String, Seq[Word]] = ${
    inlineWords_impl('path)
  }

  private def inlineWords_impl(path: Expr[String])(using Quotes): Expr[Map[String, Seq[Word]]] =
    given ToExpr[Word] with
      def apply(v: Word)(using Quotes): Expr[Word] =
        val vv = Expr(v.word)
        '{ Word($vv) }
    Expr(FileContents.parseDeepTextContentsIn(path.valueOrAbort, ".txt")(parseWords))

}
