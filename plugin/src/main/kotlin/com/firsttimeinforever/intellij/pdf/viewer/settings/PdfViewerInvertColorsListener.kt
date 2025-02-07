package com.firsttimeinforever.intellij.pdf.viewer.settings

import com.intellij.openapi.editor.colors.EditorColorsListener
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.editor.colors.EditorColorsScheme
import com.intellij.ui.ColorUtil

class PdfViewerInvertColorsListener : EditorColorsListener {
  override fun globalSchemeChange(scheme: EditorColorsScheme?) {
    val settings = PdfViewerSettings.instance
    if (settings.invertColorsWithTheme) {
      val backGround = scheme?.defaultBackground ?: return
      settings.customBackgroundColor = EditorColorsManager.getInstance().globalScheme.defaultBackground.rgb
      PdfViewerSettings.instance.invertDocumentColors = ColorUtil.isDark(backGround)
      PdfViewerSettings.instance.notifyListeners()
    }
  }
}
