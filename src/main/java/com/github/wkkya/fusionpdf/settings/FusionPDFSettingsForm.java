package com.github.wkkya.fusionpdf.settings;

import com.github.wkkya.fusionpdf.bundle.FusionPDFBundle;
import com.github.wkkya.fusionpdf.enums.SidebarViewMode;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.ColorPanel;
import com.intellij.ui.JBColor;
import com.intellij.ui.SimpleListCellRenderer;
import com.intellij.ui.components.ActionLink;
import com.intellij.ui.components.JBCheckBox;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class FusionPDFSettingsForm extends JPanel {

    private final FusionPDFSettings settings = FusionPDFSettings.getInstance();

    public final JBCheckBox enableDocumentAutoReloadCheckbox;
    public final ComboBox<SidebarViewMode> defaultSidebarViewModeComboBox;

    public final JBCheckBox invertDocumentColorsWithThemeCheckbox;
    public final JBCheckBox invertDocumentColorsCheckbox;
    public final JSpinner documentColorsInvertIntensitySpinner;

    public final JBCheckBox useCustomColorsCheckbox;
    public final ColorPanel customBackgroundColorPanel;
    public final ColorPanel customForegroundColorPanel;
    public final ColorPanel customIconColorPanel;

    public final ActionLink jblink;

    public FusionPDFSettingsForm() {
        setLayout(new BorderLayout());

        // 初始化组件
        enableDocumentAutoReloadCheckbox = new JBCheckBox(FusionPDFBundle.message("pdf.viewer.settings.reload.document"));
        enableDocumentAutoReloadCheckbox.setSelected(settings.isEnableDocumentAutoReload());

        defaultSidebarViewModeComboBox = new ComboBox<>(SidebarViewMode.values());
        defaultSidebarViewModeComboBox.setRenderer(SimpleListCellRenderer.create((label, value, index) -> {
            if (value != null) {
                switch (value) {
                    case NONE -> label.setText("Closed");
                    case THUMBNAILS -> label.setText("Thumbnails");
                    case ATTACHMENTS -> label.setText("Attachments");
                    default -> label.setText("Outline (document structure)");
                }
            }
        }));
        defaultSidebarViewModeComboBox.setSelectedItem(settings.getDefaultSidebarViewMode());

        invertDocumentColorsWithThemeCheckbox = new JBCheckBox(FusionPDFBundle.message("pdf.viewer.settings.colors.document.with.theme"));
        invertDocumentColorsWithThemeCheckbox.setSelected(settings.isInvertColorsWithTheme());
        invertDocumentColorsCheckbox = new JBCheckBox(FusionPDFBundle.message("pdf.viewer.settings.colors.document.invert"));
        invertDocumentColorsCheckbox.setEnabled(!settings.isInvertColorsWithTheme());
        invertDocumentColorsCheckbox.setSelected(settings.isInvertDocumentColors());

        documentColorsInvertIntensitySpinner = new JSpinner(new SpinnerNumberModel(settings.getDocumentColorsInvertIntensity(), 1, 100, 1));

        useCustomColorsCheckbox = new JBCheckBox(FusionPDFBundle.message("pdf.viewer.settings.viewer.colors"));
        useCustomColorsCheckbox.setSelected(settings.isUseCustomColors());

        customBackgroundColorPanel = new ColorPanel();
        customBackgroundColorPanel.setSelectedColor(new Color(settings.getCustomBackgroundColor()));
        customBackgroundColorPanel.addActionListener(e -> settings.setCustomBackgroundColor(customBackgroundColorPanel.getSelectedColor().getRGB()));

        customForegroundColorPanel = new ColorPanel();
        customForegroundColorPanel.setSelectedColor(new Color(settings.getCustomForegroundColor()));
        customForegroundColorPanel.addActionListener(e -> settings.setCustomForegroundColor(customForegroundColorPanel.getSelectedColor().getRGB()));

        customIconColorPanel = new ColorPanel();
        customIconColorPanel.setSelectedColor(new Color(settings.getCustomIconColor()));
        customIconColorPanel.addActionListener(e -> settings.setCustomIconColor(customIconColorPanel.getSelectedColor().getRGB()));

        jblink = new ActionLink(FusionPDFBundle.message("pdf.viewer.settings.set.current.theme"), (ActionListener) e -> resetViewerColorsToTheme());

        // 创建主面板
        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel builderPanel = createBuilderPanel();
        mainPanel.add(builderPanel, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createBuilderPanel() {
        DefaultFormBuilder builder = new DefaultFormBuilder(new FormLayout("left:pref, 4dlu, left:pref"));

        // General Settings Group
        builder.appendSeparator(FusionPDFBundle.message("pdf.viewer.settings.group.general"));
        builder.append( enableDocumentAutoReloadCheckbox);
        builder.nextLine();
        builder.append("Default Sidebar View Mode", defaultSidebarViewModeComboBox);

        // Document Colors Group
        builder.appendSeparator(FusionPDFBundle.message("pdf.viewer.settings.group.colors.document"));
        builder.append(invertDocumentColorsWithThemeCheckbox);
        builder.append(invertDocumentColorsCheckbox);
        builder.append("Invert Intensity", documentColorsInvertIntensitySpinner);

        // Viewer Colors Group
        builder.appendSeparator(FusionPDFBundle.message("pdf.viewer.settings.group.colors.viewer"));
        builder.append(useCustomColorsCheckbox);

        builder.nextLine();
        builder.append("Background", customBackgroundColorPanel);
        builder.append("Foreground", customForegroundColorPanel);
        builder.append("Icons", customIconColorPanel);
//        builder.append(resetViewerColorsToThemeButton);
        builder.nextLine();
        builder.append(jblink);
        addComment(builder, "These colors will override the default viewer colors.");
        builder.nextLine();

        return builder.getPanel();
    }

    // 辅助方法：添加说明标签
    private void addComment(DefaultFormBuilder builder, String comment) {
        if (comment != null && !comment.isEmpty()) {
            JLabel commentLabel = new JLabel(comment);
            commentLabel.setFont(commentLabel.getFont().deriveFont(Font.ITALIC));
            commentLabel.setForeground(JBColor.GRAY);
            builder.nextLine();
            builder.append(commentLabel);
        }
    }

    private void resetViewerColorsToTheme() {
        Color defaultBackgroundColor = FusionPDFSettings.defaultBackgroundColor();
        Color defaultForegroundColor = FusionPDFSettings.defaultForegroundColor();
        Color defaultIconColor = FusionPDFSettings.defaultIconColor();

        customBackgroundColorPanel.setSelectedColor(defaultBackgroundColor);
        settings.setCustomBackgroundColor(defaultBackgroundColor.getRGB());

        customForegroundColorPanel.setSelectedColor(defaultForegroundColor);
        settings.setCustomForegroundColor(defaultForegroundColor.getRGB());

        customIconColorPanel.setSelectedColor(defaultIconColor);
        settings.setCustomIconColor(defaultIconColor.getRGB());
    }

    public void reset() {
        enableDocumentAutoReloadCheckbox.setSelected(settings.isEnableDocumentAutoReload());
        defaultSidebarViewModeComboBox.setSelectedItem(settings.getDefaultSidebarViewMode());
        invertDocumentColorsWithThemeCheckbox.setSelected(settings.isInvertColorsWithTheme());
        invertDocumentColorsCheckbox.setSelected(settings.isInvertDocumentColors());
        documentColorsInvertIntensitySpinner.setValue(settings.getDocumentColorsInvertIntensity());
        useCustomColorsCheckbox.setSelected(settings.isUseCustomColors());
        customBackgroundColorPanel.setSelectedColor(new Color(settings.getCustomBackgroundColor()));
        customForegroundColorPanel.setSelectedColor(new Color(settings.getCustomForegroundColor()));
        customIconColorPanel.setSelectedColor(new Color(settings.getCustomIconColor()));
    }
}
