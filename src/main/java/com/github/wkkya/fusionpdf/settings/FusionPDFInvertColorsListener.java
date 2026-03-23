package com.github.wkkya.fusionpdf.settings;

import com.intellij.openapi.editor.colors.EditorColorsListener;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.ui.ColorUtil;
import org.jetbrains.annotations.Nullable;

import java.awt.*;


public class FusionPDFInvertColorsListener implements EditorColorsListener {

    @Override
    public void globalSchemeChange(@Nullable EditorColorsScheme scheme) {
        if (scheme != null && FusionPDFSettings.getInstance().isInvertColorsWithTheme()) {
            Color backGround = scheme.getDefaultBackground();
            if (backGround != null) {
                boolean isDark = ColorUtil.isDark(backGround);
                FusionPDFSettings.getInstance().setInvertDocumentColors(isDark);
                FusionPDFSettings.getInstance().notifyListeners();
            }
        }
    }
}
