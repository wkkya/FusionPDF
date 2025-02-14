package com.firsttimeinforever.intellij.pdf.viewer.ui.cache

import com.firsttimeinforever.intellij.pdf.viewer.ui.PdfPreviewStateService
import com.firsttimeinforever.intellij.pdf.viewer.ui.PdfViewerPanel
import com.intellij.ide.AppLifecycleListener
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.wm.ToolWindowManager

class PdfProjectCloseListener : AppLifecycleListener {
  override fun appWillBeClosed(isRestart: Boolean) {
    val stateService = PdfPreviewStateService.instance

    // 1获取当前打开的 PdfViewerPanel 实例
    val pdfPanel = findCurrentPdfViewerPanel() ?: return

    // 2读取 PDF 路径和滚动位置
    val currentPdfPath = pdfPanel.getCurrentPdfPath()
//    val scrollPosition = pdfPanel.getScrollPosition()  // 假设你已经实现了这个方法

    // 3持久化状态
    stateService.state.lastOpenedPdf = currentPdfPath
//    stateService.state.lastScrollPosition = scrollPosition

    ApplicationManager.getApplication().saveSettings()
  }

  private fun findCurrentPdfViewerPanel(): PdfViewerPanel? {
    // 这里根据你的插件架构，找到当前打开的 PdfViewerPanel
    val project = ProjectManager.getInstance().openProjects.firstOrNull() ?: return null
    val toolWindow = ToolWindowManager.getInstance(project).getToolWindow("FusionPDF") ?: return null
    val content = toolWindow.contentManager.selectedContent ?: return null
    return content.component as? PdfViewerPanel
  }
}




