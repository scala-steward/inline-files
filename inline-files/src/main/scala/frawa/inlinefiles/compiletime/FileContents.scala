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

import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import scala.collection.immutable.Seq
import scala.io.Source
import scala.jdk.CollectionConverters._
import scala.util.Using

object FileContents:
  def readTextContentOf(path: String, home: Option[Path] = None): String =
    val root = resolve(path, home)
    readTextContent(root.toFile)

  def readTextContentsIn(
      path: String,
      ext: String,
      home: Option[Path] = None
  ): Map[String, String] =
    val root = resolve(path, home)
    parseTextContentsIn(root, folderItems(root), ext)(identity)

  def readDeepTextContentsIn(
      path: String,
      ext: String,
      home: Option[Path] = None
  ): Map[String, String] =
    val root = resolve(path, home)
    parseTextContentsIn(root, root, ext, true)(identity)

  def parseTextContentsIn[T](
      path: String,
      ext: String,
      recurse: Boolean,
      home: Option[Path] = None
  )(
      f: String => T
  ): Map[String, T] =
    val root = resolve(path, home)
    parseTextContentsIn(root, root, ext, recurse)(f)

  private def resolve(path: String, home: Option[Path] = None): Path =
    home.map(_.resolve(path)).getOrElse(Paths.get(path))

  private def readTextContent(file: File): String =
    Using.resource(Source.fromFile(file))(_.getLines().mkString("\n"))

  private def parseTextContentsIn[T](root: Path, path: Path, ext: String, recurse: Boolean)(
      f: String => T
  ): Map[String, T] =
    val items   = folderItems(path)
    val files   = parseTextContentsIn(root, items, ext)(f)
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
      .map(path => (root.relativize(path).toString, f(readTextContent(path.toFile))))
      .toMap

  private def folderItems(path: Path): Seq[Path] =
    Files
      .list(path)
      .iterator
      .asScala
      .toSeq
