package com.github.wkkya.fusionpdf.ui.setting;

import com.github.wkkya.fusionpdf.config.Config;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.prefs.Preferences;

public class SettingUI {
    private JPanel mainPanel;
    private JTextField urlTextField;
    private JButton urlBtn;
    private JColorChooser bgColorChooser;
    private JColorChooser fontColorChooser;
    private JPanel previewPanel;
    private Color selectedBackgroundColor;
    private Color selectedFontColor;
    private Preferences prefs;


    public SettingUI() {
        // 初始化默认颜色，与 IDE 主题颜色一致（示例中默认深色背景）
        selectedBackgroundColor = UIManager.getColor("Panel.background");
        selectedFontColor = getContrastingColor(selectedBackgroundColor);

        // 初始化 Preferences
        prefs = Preferences.userNodeForPackage(SettingUI.class);

        mainPanel = new JPanel(new BorderLayout());
        JPanel controlPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.fill = GridBagConstraints.BOTH;
        // 文件选择区域
        urlBtn = new JButton("请选择PDF文件");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 0.1;  // 设置较小的权重以避免按钮过度拉伸
        gbc.insets = new Insets(5, 5, 5, 5);  // 添加一些内边距
        controlPanel.add(urlBtn, gbc.clone());  // 使用 clone() 避免重复使用同一个约束对象
        controlPanel.add(urlBtn, gbc);

        urlTextField = new JTextField(30);

        // 读取上次保存的文件路径
        String lastFilePath = prefs.get("lastFilePath", "");
        urlTextField = new JTextField(lastFilePath, 30);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;  // 设置较大的权重以允许文本字段扩展
        gbc.insets = new Insets(5, 5, 5, 5);  // 确保内边距一致
        controlPanel.add(urlTextField, gbc.clone());  // 使用 clone() 避免重复使用同一个约束对
        controlPanel.add(urlTextField, gbc);

        // 颜色选择区域
        bgColorChooser = new JColorChooser(selectedBackgroundColor);
        fontColorChooser = new JColorChooser(selectedFontColor);

        bgColorChooser.getSelectionModel().addChangeListener(e -> {
            selectedBackgroundColor = bgColorChooser.getColor();
            updatePreview();
        });

        fontColorChooser.getSelectionModel().addChangeListener(e -> {
            selectedFontColor = fontColorChooser.getColor();
            updatePreview();
        });

        // 添加到控制面板
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        controlPanel.add(new JLabel("背景色选择："), gbc);

        gbc.gridy = 2;
        controlPanel.add(bgColorChooser, gbc);

        gbc.gridy = 3;
        controlPanel.add(new JLabel("字体颜色选择："), gbc);

        gbc.gridy = 4;
        controlPanel.add(fontColorChooser, gbc);

        // 预览区域
        previewPanel = new JPanel();
        previewPanel.setPreferredSize(new Dimension(400, 200));
        previewPanel.setBackground(selectedBackgroundColor);

        JLabel previewLabel = new JLabel("<html>预览区域 <br> 以下是正文展示 <br> 当前文字就是正文 <br> 当前文字就是正文</html>");

        // 设置字体大小
        Font font = new Font("Serif", Font.PLAIN, 16);  // 字体名称、样式、大小
        previewLabel.setFont(font);

        previewLabel.setForeground(selectedFontColor);
        previewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        previewPanel.add(previewLabel);

        // 主面板布局
        mainPanel.add(controlPanel, BorderLayout.WEST);
        mainPanel.add(previewPanel, BorderLayout.CENTER);

        // 文件选择按钮事件
        urlBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("PDF 文件 (*.pdf)", "pdf"));
            int result = fileChooser.showOpenDialog(mainPanel);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                urlTextField.setText(selectedFile.getAbsolutePath());

                // 保存文件路径到 Preferences
                prefs.put("lastFilePath", selectedFile.getAbsolutePath());
                //清空预览页面的缓存内容
                Config.readUI.pageCache.clear();
            }
        });

        // 更新预览
        updatePreview();
    }

    private void updatePreview() {
        previewPanel.setBackground(selectedBackgroundColor);
        for (Component component : previewPanel.getComponents()) {
            if (component instanceof JLabel) {
                component.setForeground(selectedFontColor);
            }
        }
    }

    private Color getContrastingColor(Color color) {
        int d = 0;
        double luminance = (0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue()) / 255;
        d = luminance > 0.5 ? 0 : 255; // 高对比度颜色
        return new Color(d, d, d);
    }

    public boolean isModified() {
        return true;
    }

    public JComponent getComponent() {
        return mainPanel;
    }

    public String getSelectedFilePath() {
        return urlTextField.getText();
    }

    public Color getSelectedBackgroundColor() {
        return selectedBackgroundColor;
    }

    public Color getSelectedFontColor() {
        return selectedFontColor;
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            JFrame frame = new JFrame("设置界面");
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            SettingUI settingUI = new SettingUI();
//            frame.setContentPane(settingUI.getComponent());
//            frame.pack();
//            frame.setVisible(true);
//        });
//    }
}

