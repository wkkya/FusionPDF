package com.github.wkkya.fusionpdf.ui.setting;

import javax.swing.*;

import java.awt.*;

public class SettingUI {
    private JPanel mainPanel;
    private JPanel settingPanel;
    private JLabel urlLabel;
    private JTextField urlTextField;
    private JButton urlBtn;
    private JButton bgColorBtn;
    private JButton fontColorBtn;

    private Color selectedBackgroundColor = Color.WHITE;
    private Color selectedFontColor = Color.BLACK;

    public SettingUI() {

//        mainPanel = new JPanel(new BorderLayout());
//        urlLabel = new JLabel("选择文件");
//        urlTextField = new JTextField();
//        urlBtn = new JButton("选择");
//        bgColorBtn = new JButton("背景色");
//        fontColorBtn = new JButton("字体色");
//        settingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        settingPanel.add(urlLabel);
//        settingPanel.add(urlTextField);
//        settingPanel.add(urlBtn);
//        settingPanel.add(bgColorBtn);
//        settingPanel.add(fontColorBtn);
//        mainPanel.add(settingPanel, BorderLayout.NORTH);



        urlBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int result = fileChooser.showOpenDialog(mainPanel);
            if (result == JFileChooser.APPROVE_OPTION) {
                urlTextField.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });

        bgColorBtn.addActionListener(e -> {
            Color color = JColorChooser.showDialog(mainPanel, "选择背景色", selectedBackgroundColor);
            if (color != null) selectedBackgroundColor = color;
        });

        fontColorBtn.addActionListener(e -> {
            Color color = JColorChooser.showDialog(mainPanel, "选择字体颜色", selectedFontColor);
            if (color != null) selectedFontColor = color;
        });
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
}

