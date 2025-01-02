package com.github.wkkya.fusionpdf.factory;

import com.github.wkkya.fusionpdf.config.Config;
import com.github.wkkya.fusionpdf.listener.ReadUIListener;
import com.github.wkkya.fusionpdf.ui.read.ReadUI;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.project.ProjectManagerListener;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;


public class ReadFactory  implements ToolWindowFactory {

    private ReadUI readUI = new ReadUI();

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        // 获取内容工厂的实例
        ContentFactory contentFactory = ContentFactory.getInstance();
        // 获取 ToolWindow 显示的内容
        Content content = contentFactory.createContent(readUI.getComponent(), "", false);
        // 设置 ToolWindow 显示的内容
        toolWindow.getContentManager().addContent(content);
        // 全局使用
        Config.readUI = readUI;
        //注册监听器
//        registerProjectCloseListener(project, readUI);
    }


    private void registerProjectCloseListener(Project project, ReadUI readUI) {
        ProjectManager projectManager = ProjectManager.getInstance();
        projectManager.addProjectManagerListener(new ReadUIListener(project, readUI));
    }
}
