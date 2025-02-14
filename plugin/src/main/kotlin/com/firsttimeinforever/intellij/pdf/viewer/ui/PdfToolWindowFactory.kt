package com.firsttimeinforever.intellij.pdf.viewer.ui

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.JBColor
import com.intellij.ui.content.Content
import com.intellij.ui.content.ContentFactory
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.UIUtil
import javax.swing.JLabel
import javax.swing.JPanel
import java.awt.BorderLayout
import java.awt.Font
import javax.swing.SwingConstants

class PdfToolWindowFactory : ToolWindowFactory {
  override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
    val usagePanel = createUsagePanel()
    val content = ContentFactory.getInstance().createContent(usagePanel, "使用说明", false)
    toolWindow.contentManager.addContent(content)
  }

  private fun createUsagePanel(): JPanel {
    val panel = JPanel(BorderLayout())
    panel.border = JBUI.Borders.empty(20)

    val textColor = UIUtil.getLabelForeground() // 自动适配 IDEA 主题颜色
    val highlightColor = JBColor.BLUE // 关键字高亮颜色

    val text = """
            <html>
                <div style='text-align:center;'>
                    <div style='text-align:left; font-size:14px; font-weight:bold; color:#${Integer.toHexString(textColor.rgb).substring(2)};'>
                        <p>请选择一个 PDF 文件 拖入 进行阅读</p>
                        <p>放大/缩小 <span style='color:#${Integer.toHexString(highlightColor.rgb).substring(2)};'>Ctrl + 或 Ctrl - 或 Ctrl 鼠标滚轮</span> 缩放</p>
                        <p>向右/向左 <span style='color:#${Integer.toHexString(highlightColor.rgb).substring(2)};'> 左箭头 或 右箭头 </span> 翻页</p>
                        <p style='text-align:left; font-size:12px; font-weight:normal; color:#${Integer.toHexString(textColor.rgb).substring(2)};'>
                        ps : 2020.2 以上版本 IDE 默认捆绑 JCEF，插件应该可以正常工作。
                        （如果不起作用，可以手动更改IDE JBR或将ide.browser.jcef.enabled注册表标志设置为true）。
                        </p>
                    </div>
                </div>
            </html>
        """.trimIndent()

    val label = JLabel(text, SwingConstants.CENTER)
    label.font = Font("Microsoft YaHei", Font.BOLD, 14) // 加粗字体

    panel.add(label, BorderLayout.CENTER)
    return panel
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
