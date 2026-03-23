package com.github.wkkya.fusionpdf.settings;

@FunctionalInterface
public interface FusionPDFSettingsListener {
    void settingsChanged(FusionPDFSettings settings);
}
