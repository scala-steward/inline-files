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

import java.nio.file.Path
import java.nio.file.Paths
import scala.annotation.experimental
import scala.quoted._

@experimental
object InlineFilesWithHome:
  import InlineFiles.{inlineText, given}

  inline def inlineTextFile(inline path: String)(inline homeSetting: String): String = ${
    inlineTextFile_impl('path, 'homeSetting)
  }

  inline def inlineTextFiles(inline path: String, inline ext: String)(
      inline homeSetting: String
  ): Map[String, String] = ${
    inlineTextFiles_impl('path, 'ext, 'homeSetting)
  }

  inline def inlineDeepTextFiles(inline path: String, inline ext: String)(
      inline homeSetting: String
  ): Map[String, String] = ${
    inlineDeepTextFiles_impl('path, 'ext, 'homeSetting)
  }

  extension [T](inlined: Map[String, T])
    def folder(path: String): Map[String, T] =
      val prefix       = if path.endsWith("/") then path else path + "/"
      val prefixLength = prefix.length()
      inlined.view
        .filterKeys(_.startsWith(prefix))
        .map((p, v) => (p.substring(prefixLength), v))
        .toMap

    def files(): Map[String, T] =
      inlined.view
        .filterKeys(!_.contains("/"))
        .toMap

  import compiletime.FileContents.*

  def inlineTextFile_impl(path: Expr[String], homeSetting: Expr[String])(using
      Quotes
  ): Expr[String] =
    inlineText(
      readTextContentOf(path.valueOrAbort, Some(resolveHome(homeSetting.valueOrAbort)))
    )

  def inlineTextFiles_impl(path: Expr[String], ext: Expr[String], homeSetting: Expr[String])(using
      Quotes
  ): Expr[Map[String, String]] =
    Expr(
      readTextContentsIn(
        path.valueOrAbort,
        ext.valueOrAbort,
        Some(resolveHome(homeSetting.valueOrAbort))
      )
    )

  def inlineDeepTextFiles_impl(path: Expr[String], ext: Expr[String], homeSetting: Expr[String])(
      using Quotes
  ): Expr[Map[String, String]] =
    Expr(
      readDeepTextContentsIn(
        path.valueOrAbort,
        ext.valueOrAbort,
        Some(resolveHome(homeSetting.valueOrAbort))
      )
    )

  @experimental
  def resolveHome(setting: String)(using
      Quotes
  ): Path =
    import quotes.reflect.*
    val prefix = s"${setting}="
    val home = CompilationInfo.XmacroSettings
      .find(_.startsWith(prefix))
      .map(_.substring(prefix.length()))
      .getOrElse {
        throw new IllegalArgumentException(s"missing -Xmacro-settings:${setting}=...,")
      }
    Paths.get(home)
