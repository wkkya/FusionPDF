package com.github.wkkya.fusionpdf.ui.read;

import javax.swing.*;
import java.awt.*;

public class ReadUI {
    private JPanel mainPanel;
    private JTextPane textContent;
    private JScrollPane scrollPane;

    public ReadUI() {
        // 初始化 JTextPane
        textContent = new JTextPane();

        // 创建 JScrollPane 并设置滚动策略
        new JScrollPane();
        scrollPane = new JScrollPane();
        scrollPane.setViewportView(textContent); // 将 JTextPane 设置为滚动视图
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // 初始化主面板并将 JScrollPane 添加到其中
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(scrollPane, BorderLayout.CENTER);
    }

    // 获取主组件
    public JComponent getComponent() {
        return mainPanel;
    }

    // 获取文本内容组件
    public JTextPane getTextContent() {
        return textContent;
    }
}
