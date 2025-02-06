package com.firsttimeinforever.intellij.pdf.viewer.ui.editor

import com.firsttimeinforever.intellij.pdf.viewer.lang.PdfFileType
import com.firsttimeinforever.intellij.pdf.viewer.ui.PdfViewerPanel
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.fileEditor.*
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.wm.ToolWindowManager
import java.beans.PropertyChangeListener
import javax.swing.JComponent
import javax.swing.JPanel


class PdfFileEditorProvider : FileEditorProvider, DumbAware {
  override fun getEditorTypeId() = "FusionPDF"

  // IDEA 检测文件时，直接拦截 PDF，不让其进入编辑器
  override fun accept(project: Project, file: VirtualFile): Boolean {
    logger.debug("Intercepting file opening: $file")
    if (file.extension.equals("pdf", ignoreCase = true)) {
      openPdfInToolWindow(project, file) // 直接在 ToolWindow 打开
      return false  // **阻止 IDEA 认为这个文件需要 FileEditor**
    }
    return false
  }

//  override fun createEditor(project: Project, file: VirtualFile): FileEditor {
//    openPdfInToolWindow(project, file)
//    return DummyPdfEditor(file)
////    return NonEditor(file);
//  }

  override fun createEditor(project: Project, file: VirtualFile): FileEditor {
    throw UnsupportedOperationException("PDF files should not open in editor.")
  }

//  override fun getPolicy(): FileEditorPolicy = FileEditorPolicy.HIDE_DEFAULT_EDITOR

  override fun getPolicy(): FileEditorPolicy = FileEditorPolicy.NONE

//  override fun createEditorAsync(project: Project, file: VirtualFile): AsyncFileEditorProvider.Builder {
//    return object : AsyncFileEditorProvider.Builder() {
//      override fun build(): FileEditor {
//        openPdfInToolWindow(project, file)
//        return DummyPdfEditor(file)
////        return NonEditor(file);
//      }
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


  companion object {
    private val logger = logger<PdfFileEditorProvider>()
  }

  class NonEditor(private val file: VirtualFile?) : FileEditor {
    private val userDataMap = mutableMapOf<Key<*>, Any?>() // 存储用户数据

    override fun getComponent(): JComponent = JPanel()
    override fun getPreferredFocusedComponent(): JComponent? = null
    override fun getName(): String = "NonEditor"
    override fun setState(state: FileEditorState) {}
    override fun isModified(): Boolean = false
    override fun isValid(): Boolean = true
    override fun addPropertyChangeListener(listener: PropertyChangeListener) {}
    override fun removePropertyChangeListener(listener: PropertyChangeListener) {}
    override fun getFile(): VirtualFile? = file
    override fun <T : Any?> getUserData(key: Key<T>): T? {
      @Suppress("UNCHECKED_CAST")
      return userDataMap[key] as T?
    }

    override fun <T : Any?> putUserData(key: Key<T>, value: T?) {
      userDataMap[key] = value
    }

    override fun dispose() {}
  }



}
