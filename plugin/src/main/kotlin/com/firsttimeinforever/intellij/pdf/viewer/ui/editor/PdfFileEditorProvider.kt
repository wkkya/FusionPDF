package com.firsttimeinforever.intellij.pdf.viewer.ui.editor

import com.firsttimeinforever.intellij.pdf.viewer.lang.PdfFileType
import com.firsttimeinforever.intellij.pdf.viewer.ui.PdfViewerPanel
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.fileEditor.*
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.wm.ToolWindowManager


class PdfFileEditorProvider : AsyncFileEditorProvider, DumbAware {
  override fun getEditorTypeId() = "FusionPDF"

  override fun accept(project: Project, file: VirtualFile): Boolean {
    logger.debug("check accept, file: $file")
    return file.fileType == PdfFileType
//    return false;
  }

  override fun createEditor(project: Project, file: VirtualFile): FileEditor {
    openPdfInToolWindow(project, file)
    return DummyPdfEditor(file)
//    return NonEditor();
  }

  override fun getPolicy(): FileEditorPolicy = FileEditorPolicy.HIDE_DEFAULT_EDITOR

//  override fun getPolicy(): FileEditorPolicy = FileEditorPolicy.NONE

  override fun createEditorAsync(project: Project, file: VirtualFile): AsyncFileEditorProvider.Builder {
    return object : AsyncFileEditorProvider.Builder() {
      override fun build(): FileEditor {
        openPdfInToolWindow(project, file)
        return DummyPdfEditor(file)
      }
    }
  }

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

  companion object {
    private val logger = logger<PdfFileEditorProvider>()
  }



}
