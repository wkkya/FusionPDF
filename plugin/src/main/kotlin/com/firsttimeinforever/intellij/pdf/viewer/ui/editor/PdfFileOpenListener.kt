package com.firsttimeinforever.intellij.pdf.viewer.ui.editor

import com.firsttimeinforever.intellij.pdf.viewer.ui.PdfViewerPanel
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.wm.ToolWindowManager

class PdfFileOpenListener : FileEditorManagerListener {
  override fun fileOpened(manager: FileEditorManager, file: VirtualFile) {
    if (file.extension.equals("pdf", ignoreCase = true)) {
      manager.closeFile(file) // 立即关闭
      openPdfInToolWindow(manager.project, file) // 直接用 ToolWindow 处理
    }
  }

  private fun openPdfInToolWindow(project: Project, file: VirtualFile) {
    val toolWindow = ToolWindowManager.getInstance(project).getToolWindow("FusionPDF")
    toolWindow?.activate {
      val pdfPanel = PdfViewerPanel(project, file)
      val content = toolWindow.contentManager.factory.createContent(pdfPanel, file.name, false)
      toolWindow.contentManager.removeAllContents(true)
      toolWindow.contentManager.addContent(content)
      toolWindow.contentManager.setSelectedContent(content)
    }
  }
}
