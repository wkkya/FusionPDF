package com.firsttimeinforever.intellij.pdf.viewer.ui

import com.firsttimeinforever.intellij.pdf.viewer.settings.PdfViewerSettings
import com.firsttimeinforever.intellij.pdf.viewer.settings.PdfViewerSettingsListener
import com.firsttimeinforever.intellij.pdf.viewer.ui.editor.view.PdfEditorViewComponent
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import java.awt.BorderLayout
import javax.swing.JPanel

class PdfViewerPanel(private val project: Project, private val virtualFile: VirtualFile) : JPanel(BorderLayout()) {
  private val viewComponent = PdfEditorViewComponent(project, virtualFile)
  private val messageBusConnection = project.messageBus.connect()
  private val fileChangedListener = FileChangedListener(PdfViewerSettings.instance.enableDocumentAutoReload)

  init {
    layout = BorderLayout()
    add(viewComponent, BorderLayout.CENTER)

    Disposer.register(project, viewComponent)
    Disposer.register(project, messageBusConnection)

    messageBusConnection.subscribe(VirtualFileManager.VFS_CHANGES, fileChangedListener)
    messageBusConnection.subscribe(PdfViewerSettings.TOPIC, PdfViewerSettingsListener {
      fileChangedListener.isEnabled = it.enableDocumentAutoReload
    })
  }

  private inner class FileChangedListener(var isEnabled: Boolean = true) : BulkFileListener {
    override fun after(events: MutableList<out VFileEvent>) {
      if (!isEnabled) {
        return
      }
      if (viewComponent.controller == null) {
        logger.warn("FileChangedListener was called for view with controller == null!")
      } else if (events.any { it.file == virtualFile }) {
        logger.debug("Target file ${virtualFile.path} changed. Reloading current view.")
        viewComponent.controller.reload(tryToPreserveState = true)
      }
    }
  }

  companion object {
    private val logger = logger<PdfViewerPanel>()
  }
}
