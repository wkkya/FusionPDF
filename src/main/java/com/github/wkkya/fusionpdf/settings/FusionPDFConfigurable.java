package com.github.wkkya.fusionpdf.settings;

import com.github.wkkya.fusionpdf.bundle.FusionPDFBundle;
import com.intellij.openapi.options.Configurable;
import javax.swing.JComponent;

public class FusionPDFConfigurable implements Configurable {
    private FusionPDFSettingsForm settingsForm = null;
    private final FusionPDFSettings settings = FusionPDFSettings.getInstance();

    @Override
    public boolean isModified() {
        if (settingsForm == null) {
            return false;
        }


        return true;

//        return settings.isEnableDocumentAutoReload() != settingsForm.enableDocumentAutoReloadCheckbox.getse
//                || settings.getDefaultSidebarViewMode() != settingsForm.getDefaultSidebarViewMode().get()
//                || settings.isInvertColorsWithTheme() != settingsForm.getInvertDocumentColorsWithTheme().get()
//                || settings.isInvertDocumentColors() != settingsForm.getInvertDocumentColors().get()
//                || settings.getDocumentColorsInvertIntensity() != settingsForm.getDocumentColorsInvertIntensity().get()
//                || settings.isUseCustomColors() != settingsForm.getUseCustomColors().get()
//                || !settings.getCustomForegroundColor().equals(settingsForm.getCustomForegroundColor().get())
//                || !settings.getCustomBackgroundColor().equals(settingsForm.getCustomBackgroundColor().get())
//                || !settings.getCustomIconColor().equals(settingsForm.getCustomIconColor().get());
    }

    @Override
    public String getDisplayName() {
        return FusionPDFBundle.message("pdf.viewer.settings.display.name");
    }

    @Override
    public void apply() {
        boolean wasModified = isModified();
        if (settingsForm != null) {
//            settings.setEnableDocumentAutoReload(settingsForm.getEnableDocumentAutoReload().get());
//            settings.setDefaultSidebarViewMode(settingsForm.getDefaultSidebarViewMode().get());
//            settings.setInvertColorsWithTheme(settingsForm.getInvertDocumentColorsWithTheme().get());
//            settings.setInvertDocumentColors(settingsForm.getInvertDocumentColors().get());
//            settings.setDocumentColorsInvertIntensity(settingsForm.getDocumentColorsInvertIntensity().get());
//            settings.setUseCustomColors(settingsForm.getUseCustomColors().get());
//            settings.setCustomForegroundColor(settingsForm.getCustomForegroundColor().get());
//            settings.setCustomBackgroundColor(settingsForm.getCustomBackgroundColor().get());
//            settings.setCustomIconColor(settingsForm.getCustomIconColor().get());
        }
        if (wasModified) {
            settings.notifyListeners();
        }
    }

    @Override
    public void reset() {
        if (settingsForm != null) {
            settingsForm.reset();
        }
    }

    @Override
    public JComponent createComponent() {
        if (settingsForm == null) {
            settingsForm = new FusionPDFSettingsForm();
        }
        return settingsForm;
    }

    @Override
    public void disposeUIResources() {
        settingsForm = null;
    }
}

