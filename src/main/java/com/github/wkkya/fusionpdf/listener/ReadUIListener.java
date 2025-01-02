package com.github.wkkya.fusionpdf.listener;

import com.github.wkkya.fusionpdf.ui.read.ReadUI;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.VetoableProjectManagerListener;
import org.jetbrains.annotations.NotNull;

public class ReadUIListener implements VetoableProjectManagerListener {

    private final ReadUI readUI;

    private final Project project;

    public ReadUIListener(Project project, ReadUI readUI) {
        this.project = project;
        this.readUI = readUI;
    }

    @Override
    public void projectClosed(@NotNull Project project) {
        readUI.closePDF();
    }

    @Override
    public boolean canClose(@NotNull Project project) {
        if (project.equals(this.project)) {
            return true;
        }
        return false;
    }
}
