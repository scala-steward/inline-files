package frawa.inlinefiles.internal

import java.nio.file.{Files, Path, Paths}

import scala.io.Source
import scala.util.Using
import scala.quoted.*

object FileContents:

  def readTextContentOf(path: String): String =
    Using.resource(Source.fromFile(path))(_.getLines().mkString("\n"))

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
