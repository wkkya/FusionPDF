package com.firsttimeinforever.intellij.pdf.viewer

import com.firsttimeinforever.intellij.pdf.viewer.ui.PdfViewerPanel
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.FileDropEvent
import com.intellij.openapi.editor.FileDropHandler
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.openapi.vfs.VirtualFileManager
import java.awt.datatransfer.DataFlavor
import java.io.File

class PdfFileDropHandler : FileDropHandler {
  private val logger = Logger.getInstance(PdfFileDropHandler::class.java)

//  override fun canHandleDrop(flavors: Array<out DataFlavor>): Boolean {
//    return flavors.any { it == DataFlavor.javaFileListFlavor }
//  }
//
//  override fun handleDrop(transferable: Transferable, project: Project?, editorWindow: EditorWindow?) {
//    if (project == null) return
//
//    try {
//      val files = transferable.getTransferData(DataFlavor.javaFileListFlavor) as? List<*>
//      files?.filterIsInstance<File>()?.forEach { file ->
//        if (file.extension.equals("pdf", ignoreCase = true)) {
//          val virtualFile = VirtualFileManager.getInstance().findFileByUrl("file://${file.absolutePath}")
//          if (virtualFile != null) {
//            openPdfInToolWindow(project, virtualFile)
//          }
//        }
//      }
//    } catch (e: Exception) {
//      logger.error("Error handling PDF drop", e)
//    }
//  }

  private fun openPdfInToolWindow(project: Project, file: com.intellij.openapi.vfs.VirtualFile) {
    val toolWindow = ToolWindowManager.getInstance(project).getToolWindow("PdfViewer") ?: return
    toolWindow.show {
      val pdfViewerPanel = PdfViewerPanel(project, file)
      toolWindow.contentManager.removeAllContents(true) // 清除旧内容
      val content = toolWindow.contentManager.factory.createContent(pdfViewerPanel, file.name, false)
      toolWindow.contentManager.addContent(content)
    }
  }

  override suspend fun handleDrop(e: FileDropEvent): Boolean {
    val project = e.project
    val transferable = e.transferable

    if (project == null) return false

    try {
      val files = transferable.getTransferData(DataFlavor.javaFileListFlavor) as? List<*>
      files?.filterIsInstance<File>()?.forEach { file ->
        if (file.extension.equals("pdf", ignoreCase = true)) {
          val virtualFile = VirtualFileManager.getInstance().findFileByUrl("file://${file.absolutePath}")
          if (virtualFile != null) {
            openPdfInToolWindow(project, virtualFile)
          }
        }
      }
    } catch (e: Exception) {
      logger.error("Error handling PDF drop", e)
      return false
    }
    return true
  }
}
