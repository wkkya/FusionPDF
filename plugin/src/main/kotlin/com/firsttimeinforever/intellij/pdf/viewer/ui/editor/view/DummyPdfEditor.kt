package com.firsttimeinforever.intellij.pdf.viewer.ui.editor

import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.util.Key
import com.intellij.openapi.vfs.VirtualFile
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel

class DummyPdfEditor(private val file: VirtualFile) : FileEditor {
  override fun getComponent(): JComponent = JPanel().apply {
    add(JLabel("PDF 文件 ${file.name} 已在 FusionPDF ToolWindow 中打开"))
  }

  override fun getName(): String = "FusionPDF Editor"

  override fun dispose() {}

  override fun isModified(): Boolean = false

  override fun isValid(): Boolean = true

  override fun getFile(): VirtualFile = file

  override fun getPreferredFocusedComponent(): JComponent? = null

  override fun setState(state: FileEditorState) {}

  override fun addPropertyChangeListener(listener: java.beans.PropertyChangeListener) {}

  override fun removePropertyChangeListener(listener: java.beans.PropertyChangeListener) {}

  override fun <T : Any?> getUserData(key: Key<T>): T? = null

  override fun <T : Any?> putUserData(key: Key<T>, value: T?) {}
}
