package com.github.wkkya.fusionpdf.factory;

import com.github.wkkya.fusionpdf.config.Config;
import com.github.wkkya.fusionpdf.ui.setting.SettingUI;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import groovy.util.logging.Slf4j;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.File;

@Slf4j
public class SettingFactory implements SearchableConfigurable {

    private static final Logger log = LoggerFactory.getLogger(SettingFactory.class);
    private SettingUI settingUI = new SettingUI();

    @Override
    public @NotNull @NonNls String getId() {
        return "fusionpdf";
    }

    @Override
    public @Nls(capitalization = Nls.Capitalization.Title) String getDisplayName() {
        return "FusionPDF";
    }

    @Override
    public @Nullable JComponent createComponent() {
        return settingUI.getComponent();
    }

    @Override
    public boolean isModified() {
        return true;
    }

    @Override
    public void apply() throws ConfigurationException {

        String url = settingUI.getSelectedFilePath(); // 使用新的方法获取文件路径

        if (url == null || url.isEmpty()) {
            throw new ConfigurationException("文件路径不能为空！");
        }

        File file = new File(url);
        if (!file.exists()) {
            throw new ConfigurationException("文件不存在，请检查路径！");
        }

        try {
            // 检测是否为 PDF 文件
            if (url.toLowerCase().endsWith(".pdf")) {
                Config.readUI.loadPDF(url); // 加载 PDF 文件
            } else {
                // 加载普通文本文件
                throw new ConfigurationException("请选择pdf文件");
            }

            // 应用背景色和字体色
            Color bgColor = settingUI.getSelectedBackgroundColor(); // 从 SettingUI 获取
            Color fontColor = settingUI.getSelectedFontColor();

            Config.readUI.setBackgroundColor(bgColor);
            Config.readUI.setTextColor(fontColor);

            JOptionPane.showMessageDialog(null, "设置成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            log.error("文件加载失败", e);
            JOptionPane.showMessageDialog(null, "设置成功！", "提示", JOptionPane.ERROR_MESSAGE);
//            throw new ConfigurationException("文件加载失败，请检查文件内容或格式是否为PDF！");
        }



    }
}
