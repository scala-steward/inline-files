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

import scala.quoted.*

object InlineFiles:
  import internal.FileContents.{given, *}

  inline def inlineTextFile(inline path: String): String = ${
    inlineTextFile_impl('path)
  }

  private def inlineTextFile_impl(path: Expr[String])(using
      Quotes
  ): Expr[String] =
    Expr(readTextContentOf(path.valueOrAbort))

  inline def inlineTextFiles(inline path: String, inline ext: String): Map[String, String] = ${
    inlineTextFiles_impl('path, 'ext)
  }

  private def inlineTextFiles_impl(path: Expr[String], ext: Expr[String])(using
      Quotes
  ): Expr[Map[String, String]] =
    Expr(readTextContentsIn(path.valueOrAbort, ext.valueOrAbort))

  // inline def folderContents(inline path: String, ext: String): FolderContents[String] = ${
  //   folderContents_impl('path, 'ext)
  // }

  // private def folderContents_impl(path: Expr[String], ext: Expr[String])(using
  //     Quotes
  // ): Expr[FolderContents[String]] =
  //   Expr(readFolderContentsOf(path.valueOrAbort, ext.valueOrAbort)(identity))
