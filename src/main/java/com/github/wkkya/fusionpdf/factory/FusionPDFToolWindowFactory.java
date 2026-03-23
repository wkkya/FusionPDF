package com.github.wkkya.fusionpdf.factory;


import com.github.wkkya.fusionpdf.viewer.FusionPDFViewerPanel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class FusionPDFToolWindowFactory implements ToolWindowFactory{
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        // 创建主面板
        FusionPDFViewerPanel viewerPanel = new FusionPDFViewerPanel();
        toolWindow.getComponent().add(viewerPanel.getMainComponent());
    }
}

