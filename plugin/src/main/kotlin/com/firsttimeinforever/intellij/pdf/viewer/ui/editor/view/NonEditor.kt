package com.firsttimeinforever.intellij.pdf.viewer.ui.editor.view

import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.util.Key
import java.beans.PropertyChangeListener
import javax.swing.JComponent
import javax.swing.JPanel

/**
 * 这个 `NonEditor` 类是一个空的 `FileEditor`，防止 IDEA 报错
 */
public final class NonEditor : FileEditor {
  override fun getComponent(): JComponent = JPanel()
  override fun getPreferredFocusedComponent(): JComponent? = null
  override fun getName(): String = "FusionPDF"
  override fun setState(state: FileEditorState) {}
  override fun isModified(): Boolean = false
  override fun isValid(): Boolean = true
  override fun addPropertyChangeListener(listener: PropertyChangeListener) {}
  override fun removePropertyChangeListener(listener: PropertyChangeListener) {}
  override fun <T : Any?> getUserData(p0: Key<T>): T? {
    TODO("Not yet implemented")
  }

  override fun <T : Any?> putUserData(p0: Key<T>, p1: T?) {
    TODO("Not yet implemented")
  }

  override fun dispose() {}
}
