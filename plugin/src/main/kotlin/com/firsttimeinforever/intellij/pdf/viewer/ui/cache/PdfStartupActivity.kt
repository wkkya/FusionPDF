package com.firsttimeinforever.intellij.pdf.viewer.ui.cache

import com.firsttimeinforever.intellij.pdf.viewer.ui.PdfPreviewStateService
import com.firsttimeinforever.intellij.pdf.viewer.ui.PdfViewerPanel
import com.firsttimeinforever.intellij.pdf.viewer.ui.editor.PdfFileEditorProvider
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.intellij.openapi.startup.StartupActivity

class PdfStartupActivity : ProjectActivity {

  override suspend fun execute(project: Project) {
    val stateService = PdfPreviewStateService.instance
    val lastPdf = stateService.state.lastOpenedPdf
    val lastScrollPosition = stateService.state.lastScrollPosition

    if (!lastPdf.isNullOrEmpty()) {
      PdfViewerPanel.openPdf(project, lastPdf) // 需要在 PdfViewerPanel 添加静态方法
//      PdfViewerPanel.restoreScrollPosition(lastScrollPosition)  // 需要在 PdfViewerPanel 添加静态方法

    }
  }
}

