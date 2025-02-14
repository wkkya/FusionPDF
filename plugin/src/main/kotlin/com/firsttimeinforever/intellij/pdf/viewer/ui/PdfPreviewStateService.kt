package com.firsttimeinforever.intellij.pdf.viewer.ui

import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project

@State(
  name = "FusionPDFPreviewState",
  storages = [Storage("FusionPDFPreviewState.xml")]
)
class PdfPreviewStateService : PersistentStateComponent<PdfPreviewStateService.State> {

  data class State(var lastOpenedPdf: String? = null, var lastScrollPosition: Float = 0.0f)

  private var state: State = State()

//  override fun getState(): State = state

  override fun loadState(state: State) {
    println("Loading state: lastOpenedPdf = ${state.lastOpenedPdf}, lastScrollPosition = ${state.lastScrollPosition}")
    this.state = state
  }

  override fun getState(): State {
    println("Saving state: lastOpenedPdf = ${state.lastOpenedPdf}, lastScrollPosition = ${state.lastScrollPosition}")
    return state
  }

  companion object {
    val instance: PdfPreviewStateService
      get() = service()
  }
}

