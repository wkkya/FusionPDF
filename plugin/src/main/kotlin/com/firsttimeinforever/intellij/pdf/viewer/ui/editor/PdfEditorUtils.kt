package com.firsttimeinforever.intellij.pdf.viewer.ui.editor

import com.firsttimeinforever.intellij.pdf.viewer.ui.editor.view.PdfJcefPreviewController
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

object PdfEditorUtils {
//  fun findPdfEditor(project: Project, file: VirtualFile): PdfFileEditor? {
//    val editorManager = FileEditorManagerEx.getInstanceEx(project)
//    val selectedEditor = editorManager.getSelectedEditor(file) as? PdfFileEditor
//    if (selectedEditor != null) {
//      return selectedEditor
//    }
//    return editorManager.getEditors(file).find { it is PdfFileEditor } as? PdfFileEditor
//  }

  fun findPdfEditor(project: Project, file: VirtualFile): PdfFileEditor? {
    val editorManager = FileEditorManagerEx.getInstanceEx(project)

    // 尝试从选中的编辑器中获取 PdfFileEditor
    val selectedEditor = editorManager.getSelectedEditor(file)
    if (selectedEditor is PdfFileEditor) {
      return selectedEditor
    }

    // 如果未找到，则尝试从所有编辑器中查找
    return editorManager.getEditors(file)
      .filterIsInstance<PdfFileEditor>() // 使用 filterIsInstance 简化类型检查
      .firstOrNull() // 返回第一个找到的 PdfFileEditor，如果没有找到则返回 null
  }

  fun findPdfViewController(project: Project, file: VirtualFile): PdfJcefPreviewController? {
    return findPdfEditor(project, file)?.viewComponent?.controller
  }

  fun findPdfViewController(editor: FileEditor): PdfJcefPreviewController? {
    return (editor as? PdfFileEditor)?.viewComponent?.controller
  }
}
