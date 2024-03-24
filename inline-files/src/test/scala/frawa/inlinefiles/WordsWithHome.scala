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
