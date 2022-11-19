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

package frawa.inlinefiles.internal

import java.nio.file.{Files, Path, Paths}

import scala.io.Source
import scala.util.Using
import scala.quoted.*

object FileContents:

  def readTextContentOf(path: String): String =
    Using.resource(Source.fromFile(path))(_.getLines().mkString("\n"))

  def readTextContentsIn(path: String, ext: String): Map[String, String] =
    import scala.jdk.CollectionConverters.*
    Files
      .list(Paths.get(path))
      .iterator
      .asScala
      .toSeq
      .filterNot(_.toFile.isDirectory)
      .filter(_.getFileName.toString.endsWith(ext))
      .sortBy(_.getFileName.toString)
      .map(path => (path.toString, readTextContentOf(path.toAbsolutePath.toString)))
      .toMap
      .view
      .toMap

  // given [T: ToExpr: Type]: ToExpr[FolderContents[T]] with
  //   def apply(value: FolderContents[T])(using Quotes): Expr[FolderContents[T]] =
  //     value match {
  //       case FolderContents.File(content) =>
  //         val vv = Expr(content)
  //         '{ FolderContents.File($vv) }
  //       case FolderContents.Folder(items) =>
  //         val vv = Expr(items)
  //         '{ FolderContents.Folder($vv) }
  //     }

  // def readFolderContentsOf[T](path: String, ext: String)(f: String => T): FolderContents[T] =
  //   readFolderContentsOf(Paths.get(path), ext)(f)

  // private def readFolderContentsOf[T](path: Path, ext: String)(f: String => T): FolderContents[T] =
  //   import scala.jdk.CollectionConverters.*
  //   val items = Files
  //     .list(path)
  //     .iterator
  //     .asScala
  //     .toSeq
  //   val files = items
  //     .filterNot(_.toFile.isDirectory)
  //     .filter(_.getFileName.toString.endsWith(ext))
  //     .sortBy(_.getFileName.toString)
  //     .map(path => (path.getFileName.toString, f(readContentOf(path.toAbsolutePath.toString))))
  //     .toMap
  //     .view
  //     .mapValues(FolderContents.File(_))
  //     .toMap
  //   val folders = items
  //     .filter(_.toFile.isDirectory)
  //     .map(path => (path.getFileName.toString, readFolderContentsOf(path, ext)(f)))
  //     .toMap
  //   FolderContents.Folder(files ++ folders)
