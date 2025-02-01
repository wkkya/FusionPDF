package com.firsttimeinforever.intellij.pdf.viewer.ui

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.Content
import com.intellij.ui.content.ContentFactory
import javax.swing.JPanel

class PdfToolWindowFactory : ToolWindowFactory, DumbAware {
  override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
    val emptyPanel = JPanel() // 初始时是空白面板，文件打开时才动态添加 PDF 视图
    val content = ContentFactory.getInstance().createContent(emptyPanel, "FusionPDF", false)
    toolWindow.contentManager.addContent(content)
  }

  companion object {
    fun addPdfViewerContent(project: Project, toolWindow: ToolWindow, pdfPanel: PdfViewerPanel) {
      val contentManager = toolWindow.contentManager
      val contentFactory = ContentFactory.getInstance()

      // 先清除旧的 PDF 视图，避免重复添加
      contentManager.removeAllContents(true)

      val newContent: Content = contentFactory.createContent(pdfPanel, "FusionPDF", false)
      contentManager.addContent(newContent)
      contentManager.setSelectedContent(newContent)
    }
  }
}
