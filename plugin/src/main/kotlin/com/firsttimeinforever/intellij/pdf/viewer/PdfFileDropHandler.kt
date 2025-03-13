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

import com.intellij.openapi.application.ApplicationManager

class PdfFileDropHandler : FileDropHandler {

  private fun openPdfInToolWindow(project: Project, file: VirtualFile) {
    ApplicationManager.getApplication().invokeLater {
      val toolWindow = ToolWindowManager.getInstance(project).getToolWindow("FusionPDF")
      if (toolWindow != null) {
        toolWindow.activate {
          val contentManager = toolWindow.contentManager
          val existingContent = contentManager.contents.find { content ->
            content.component is PdfViewerPanel && (content.component as PdfViewerPanel).file.path == file.path
          }
          if (existingContent != null) {
            contentManager.setSelectedContent(existingContent)
          } else {
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
  }

  override suspend fun handleDrop(e: FileDropEvent): Boolean {
    val file = e.files.firstOrNull() ?: return false
    val project = e.project ?: return false
    if (file.extension.equals("pdf", ignoreCase = true)) {
      val virtualFile: VirtualFile? = VfsUtil.findFileByIoFile(file, true)
      if (virtualFile != null) {
        openPdfInToolWindow(project, virtualFile)
        return true
      }
    }
    return false
  }

  companion object {
    private val logger = logger<PdfFileDropHandler>()
  }
}
