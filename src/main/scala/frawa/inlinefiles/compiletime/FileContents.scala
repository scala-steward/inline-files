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

package frawa.inlinefiles.compiletime

import java.nio.file.{Files, Path, Paths}
import scala.jdk.CollectionConverters.*

import scala.io.Source
import scala.util.Using
import scala.quoted.*

object FileContents:

  def readTextContentOf(path: String): String =
    Using.resource(Source.fromFile(path))(_.getLines().mkString("\n"))

  def readTextContentsIn(path: String, ext: String): Map[String, String] =
    val root = Paths.get(path)
    parseTextContentsIn(root, folderItems(root), ext)(identity)

  def readDeepTextContentsIn(path: String, ext: String): Map[String, String] =
    val root = Paths.get(path)
    parseTextContentsIn(root, root, ext, true)(identity)

  def parseTextContentsIn[T](path: String, ext: String, recurse: Boolean)(
      f: String => T
  ): Map[String, T] =
    val root = Paths.get(path)
    parseTextContentsIn(root, root, ext, recurse)(f)

  private def parseTextContentsIn[T](root: Path, path: Path, ext: String, recurse: Boolean)(
      f: String => T
  ): Map[String, T] =
    val items = folderItems(path)
    val files = parseTextContentsIn(root, items, ext)(f)
    val folders = items
      .filter(_.toFile.isDirectory)
      .flatMap(path => parseTextContentsIn(root, path, ext, recurse)(f))
      .toMap
    files ++ folders

  private def parseTextContentsIn[T](root: Path, folderItems: Seq[Path], ext: String)(
      f: String => T
  ): Map[String, T] =
    folderItems
      .filterNot(_.toFile.isDirectory)
      .filter(_.getFileName.toString.endsWith(ext))
      .sortBy(_.getFileName.toString)
      .map(path => (root.relativize(path).toString, f(readTextContentOf(path.toString))))
      .toMap

  private def folderItems(path: Path): Seq[Path] =
    Files
      .list(path)
      .iterator
      .asScala
      .toSeq
