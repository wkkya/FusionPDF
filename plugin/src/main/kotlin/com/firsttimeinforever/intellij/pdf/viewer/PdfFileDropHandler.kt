package com.firsttimeinforever.intellij.pdf.viewer

import com.firsttimeinforever.intellij.pdf.viewer.ui.PdfViewerPanel
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.editor.FileDropEvent
import com.intellij.openapi.editor.FileDropHandler
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.wm.ToolWindowManager
//import com.intellij.openapi.fileEditor.impl.FileDropHandler

class PdfFileDropHandler : FileDropHandler {
//  override fun handleDrop(project: Project, file: VirtualFile): Boolean {
//    if (file.extension.equals("pdf", ignoreCase = true)) {
//      openPdfInToolWindow(project, file)
//      return true // 阻止默认打开行为
//    }
//    return false
//  }

//  private fun openPdfInToolWindow(project: Project, file: VirtualFile) {
//    val toolWindow = ToolWindowManager.getInstance(project).getToolWindow("FusionPDF")
//    toolWindow?.activate {
//      val pdfPanel = PdfViewerPanel(project, file)
//      val content = toolWindow.contentManager.factory.createContent(pdfPanel, file.name, false)
//      toolWindow.contentManager.removeAllContents(true)
//      toolWindow.contentManager.addContent(content)
//      toolWindow.contentManager.setSelectedContent(content)
//    }
//  }


  private fun openPdfInToolWindow(project: Project, file: VirtualFile) {
    val toolWindow = ToolWindowManager.getInstance(project).getToolWindow("FusionPDF")
    if (toolWindow != null) {
      toolWindow.activate {
        val contentManager = toolWindow.contentManager
        // 检查是否已经存在相同文件的 PdfViewerPanel
        val existingContent = contentManager.contents.find { content ->
          content.component is PdfViewerPanel && (content.component as PdfViewerPanel).file.path == file.path
        }
        if (existingContent != null) {
          contentManager.setSelectedContent(existingContent)
        } else {
          // 关闭其他已经打开的文件
          contentManager.contents.forEach { content ->
            contentManager.removeContent(content, true)
          }

          val pdfPanel = PdfViewerPanel(project, file)
          val content = contentManager.factory.createContent(pdfPanel, file.name, false)
          contentManager.addContent(content)
          contentManager.setSelectedContent(content)
        }
      }
    } else {
      logger.warn("FusionPDF ToolWindow not found!")
    }
  }

  override suspend fun handleDrop(e: FileDropEvent): Boolean {
    val file = e.files.firstOrNull() ?: return false
    val project = e.project ?: return false
    if (file.extension.equals("pdf", ignoreCase = true)) {
      val virtualFile: VirtualFile? = VfsUtil.findFileByIoFile(file, true)
      if (virtualFile != null) {
        openPdfInToolWindow(project, virtualFile)
        return true // 阻止默认打开行为
      }
    }
    return false
  }

  companion object {
    private val logger = logger<PdfFileDropHandler>()
  }
}
