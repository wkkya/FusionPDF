package com.firsttimeinforever.intellij.pdf.viewer.ui.editor

import com.firsttimeinforever.intellij.pdf.viewer.lang.PdfFileType
import com.firsttimeinforever.intellij.pdf.viewer.ui.PdfViewerPanel
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
//import com.jetbrains.rd.ide.model.FileEditorModel
import com.intellij.openapi.wm.ToolWindowManager

class PdfFrontendFileEditorModelHandler {
  fun accept(project: Project, file: VirtualFile): Boolean {
    logger.debug("check accept, file: $file")
    return file.fileType == PdfFileType
  }

  fun openPdfInToolWindow(project: Project, file: VirtualFile) {
    logger.info("Opening PDF in ToolWindow: ${file.path}")
    val toolWindowManager = ToolWindowManager.getInstance(project)
    val toolWindow = toolWindowManager.getToolWindow("FusionPDF")
    if (toolWindow != null) {
      toolWindow.activate {
        val contentManager = toolWindow.contentManager
        val pdfPanel = PdfViewerPanel(project, file)
        val content = contentManager.factory.createContent(pdfPanel, file.name, false)
        contentManager.addContent(content)
        contentManager.setSelectedContent(content)
      }
    } else {
      logger.warn("FusionPDF ToolWindow not found!")
    }
  }

  companion object {
    private val logger = logger<PdfFrontendFileEditorModelHandler>()
  }
}
