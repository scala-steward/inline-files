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

  // inline def folderContents(inline path: String, ext: String): FolderContents[String] = ${
  //   folderContents_impl('path, 'ext)
  // }

  // private def folderContents_impl(path: Expr[String], ext: Expr[String])(using
  //     Quotes
  // ): Expr[FolderContents[String]] =
  //   Expr(readFolderContentsOf(path.valueOrAbort, ext.valueOrAbort)(identity))
