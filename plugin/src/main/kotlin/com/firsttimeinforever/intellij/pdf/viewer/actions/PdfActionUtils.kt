package com.firsttimeinforever.intellij.pdf.viewer.actions

import com.intellij.ide.DataManager
import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.actionSystem.ex.ActionUtil
import java.awt.Component
import java.awt.event.KeyEvent
import javax.swing.JComponent

internal object PdfActionUtils {
  fun createActionToolbar(groupId: String, place: String, targetComponent: JComponent, horizontal: Boolean = true): ActionToolbar {
    val group = ActionManager.getInstance().getAction(groupId)
    checkNotNull(group)
    check(group is ActionGroup)
    return createActionToolbar(group, place, targetComponent, horizontal)
  }

  fun createActionToolbar(group: ActionGroup, place: String, targetComponent: JComponent, horizontal: Boolean = true): ActionToolbar {
    val toolbar = ActionManager.getInstance().createActionToolbar(place, group, horizontal)
    toolbar.targetComponent = targetComponent
    return toolbar
  }

//  fun performAction(action: AnAction, component: Component) {
//    val context = DataManager.getInstance().getDataContext(component)
//    action.actionPerformed(
//      AnActionEvent(
//        null,
//        context,
//        ActionPlaces.UNKNOWN,
//        Presentation(),
//        ActionManager.getInstance(),
//        0
//      )
//    )
//  }

  fun performAction(action: AnAction, component: Component) {
    // 方式一：创建带InputEvent的合法事件
    val inputEvent = KeyEvent(component, KeyEvent.KEY_PRESSED,
      System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, '\n')
    val event = AnActionEvent.createFromInputEvent(
      inputEvent,
      ActionPlaces.UNKNOWN,
      Presentation(),
      DataManager.getInstance().getDataContext(component)
    )

    // 合法调用方式（需API 2021.3+）
    ActionManager.getInstance().tryToExecute(action, inputEvent, component, "", true)
  }












//  fun performAction(actionId: String, component: Component) {
//    val manager = ActionManager.getInstance()
//    val action = manager.getAction(actionId)
//    checkNotNull(action)
////    performAction(action, component)
//
//    val inputEvent = KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, '\n')
//    val event = AnActionEvent.createFromInputEvent(inputEvent, action.templatePresentation)
//    ActionManager.getInstance().tryToExecute(action, event.inputEvent, null, null, true)
//  }
}
