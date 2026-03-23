package com.github.wkkya.fusionpdf.settings;

import com.github.wkkya.fusionpdf.enums.SidebarViewMode;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.util.registry.Registry;
import com.intellij.util.messages.Topic;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

@State(name = "FusionPDFSettings", storages = {@Storage("fusion_pdf.xml")})
public class FusionPDFSettings implements PersistentStateComponent<FusionPDFSettings> {

    public static final Topic<FusionPDFSettingsListener> TOPIC =
            new Topic<>("PdfViewerSettingsChanged", FusionPDFSettingsListener.class);

    private boolean useCustomColors = false;
    private int customBackgroundColor = defaultBackgroundColor().getRGB();
    private int customForegroundColor = defaultForegroundColor().getRGB();
    private int customIconColor = defaultIconColor().getRGB();
    private boolean enableDocumentAutoReload = true;
    private int documentColorsInvertIntensity = defaultDocumentColorsInvertIntensity;
    private boolean invertDocumentColors = false;
    private boolean invertColorsWithTheme = false;
    private SidebarViewMode defaultSidebarViewMode = SidebarViewMode.ATTACHMENTS;

    @Nullable
    @Override
    public FusionPDFSettings getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull FusionPDFSettings state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    public void notifyListeners() {
        ApplicationManager.getApplication().getMessageBus().syncPublisher(TOPIC).settingsChanged(this);
    }

    public static @NotNull FusionPDFSettings getInstance() {
        return ApplicationManager.getApplication().getService(FusionPDFSettings.class);
    }

    public static Color defaultBackgroundColor() {
        return EditorColorsManager.getInstance().getGlobalScheme().getDefaultBackground();
    }

    public static Color defaultForegroundColor() {
        return EditorColorsManager.getInstance().getGlobalScheme().getDefaultForeground();
    }

    public static Color defaultIconColor() {
        return defaultForegroundColor();
    }

    public static final int defaultDocumentColorsInvertIntensity = 85;

    public static boolean isEnableExperimentalFeatures() {
        return Registry.is("fusion.pdf.enableExperimentalFeatures");
    }

    public static boolean isDebugMode() {
        return Registry.is("fusion.pdf.debug", false);
    }

    // Getters and Setters

    public boolean isUseCustomColors() {
        return useCustomColors;
    }

    public void setUseCustomColors(boolean useCustomColors) {
        this.useCustomColors = useCustomColors;
    }

    public int getCustomBackgroundColor() {
        return customBackgroundColor;
    }

    public void setCustomBackgroundColor(int customBackgroundColor) {
        this.customBackgroundColor = customBackgroundColor;
    }

    public int getCustomForegroundColor() {
        return customForegroundColor;
    }

    public void setCustomForegroundColor(int customForegroundColor) {
        this.customForegroundColor = customForegroundColor;
    }

    public int getCustomIconColor() {
        return customIconColor;
    }

    public void setCustomIconColor(int customIconColor) {
        this.customIconColor = customIconColor;
    }

    public boolean isEnableDocumentAutoReload() {
        return enableDocumentAutoReload;
    }

    public void setEnableDocumentAutoReload(boolean enableDocumentAutoReload) {
        this.enableDocumentAutoReload = enableDocumentAutoReload;
    }

    public int getDocumentColorsInvertIntensity() {
        return documentColorsInvertIntensity;
    }

    public void setDocumentColorsInvertIntensity(int documentColorsInvertIntensity) {
        this.documentColorsInvertIntensity = documentColorsInvertIntensity;
    }

    public boolean isInvertDocumentColors() {
        return invertDocumentColors;
    }

    public void setInvertDocumentColors(boolean invertDocumentColors) {
        this.invertDocumentColors = invertDocumentColors;
    }

    public boolean isInvertColorsWithTheme() {
        return invertColorsWithTheme;
    }

    public void setInvertColorsWithTheme(boolean invertColorsWithTheme) {
        this.invertColorsWithTheme = invertColorsWithTheme;
    }

    public SidebarViewMode getDefaultSidebarViewMode() {
        return defaultSidebarViewMode;
    }

    public void setDefaultSidebarViewMode(SidebarViewMode defaultSidebarViewMode) {
        this.defaultSidebarViewMode = defaultSidebarViewMode;
    }
}



