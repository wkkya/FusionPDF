package com.github.wkkya.fusionpdf.ui.read;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ReadUI {
    private JPanel mainPanel;
    private JLabel pdfViewer;
    private JScrollPane pdfScrollPane;
    private JPanel controlPanel;
    private JButton prevButton;
    private JButton nextButton;
    private JTextField pageField;
    private JButton enlargeButton;
    private JButton zoomOutButton;

    private PDDocument pdfDocument;
    private PDFRenderer pdfRenderer;
    private int currentPage = 0;
    private float scale = 1.0f; // 初始缩放比例
    private Color backgroundColor = Color.DARK_GRAY; // 默认背景色
    private Color textColor = Color.WHITE; // 默认文字颜色

    public ReadUI() {
        setupUI();
        setupListeners();
    }

    public JComponent getComponent() {
        return mainPanel;
    }

    public void loadPDF(String filePath) {
        try {
            if (pdfDocument != null) {
                pdfDocument.close();
            }
            pdfDocument = PDDocument.load(new File(filePath));
            pdfRenderer = new PDFRenderer(pdfDocument);
            currentPage = 0;
            renderPage(); // 渲染第一页
        } catch (IOException e) {
            JOptionPane.showMessageDialog(mainPanel, "加载 PDF 文件失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void renderPage() {
        if (pdfDocument == null || pdfRenderer == null) {
            return;
        }
        try {
            BufferedImage image = pdfRenderer.renderImageWithDPI(currentPage, 72 * scale);
            image = processPDFImage(image); // 处理背景和文字颜色
            pdfViewer.setIcon(new ImageIcon(image));
            pageField.setText((currentPage + 1) + " / " + pdfDocument.getNumberOfPages());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(mainPanel, "渲染 PDF 页面失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private BufferedImage processPDFImage(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color originalColor = new Color(image.getRGB(x, y), true);
                if (isBackgroundColor(originalColor)) {
                    // 替换背景色为自定义背景
                    result.setRGB(x, y, backgroundColor.getRGB());
                } else {
                    // 修改文字颜色
                    Color modifiedColor = adjustTextColor(originalColor);
                    result.setRGB(x, y, modifiedColor.getRGB());
                }
            }
        }
        return result;
    }

    private boolean isBackgroundColor(Color color) {
        // 判断是否为背景颜色（亮度阈值可根据需要调整）
        return color.getRed() > 230 && color.getGreen() > 230 && color.getBlue() > 230;
    }

    private Color adjustTextColor(Color originalColor) {
        // 基于亮度调整文字颜色
        int avg = (originalColor.getRed() + originalColor.getGreen() + originalColor.getBlue()) / 3;
        return avg < 128 ? textColor : new Color(255, 255, 255, 0); // 透明或设置为指定文字颜色
    }

    private void setupUI() {
        mainPanel = new JPanel(new BorderLayout());
        pdfViewer = new JLabel();
        pdfScrollPane = new JScrollPane(pdfViewer);
        controlPanel = new JPanel();

        prevButton = new JButton("上一页");
        nextButton = new JButton("下一页");
        pageField = new JTextField(5);
        pageField.setEditable(false);

        enlargeButton = new JButton("放大");
        zoomOutButton = new JButton("缩小");

        controlPanel.add(prevButton);
        controlPanel.add(pageField);
        controlPanel.add(nextButton);
        controlPanel.add(enlargeButton);
        controlPanel.add(zoomOutButton);

        mainPanel.add(pdfScrollPane, BorderLayout.CENTER);
        mainPanel.add(controlPanel, BorderLayout.SOUTH);

        pdfScrollPane.getViewport().setBackground(backgroundColor); // 设置滚动区域背景色
    }

    private void setupListeners() {
        prevButton.addActionListener(e -> navigatePage(-1));
        nextButton.addActionListener(e -> navigatePage(1));
        enlargeButton.addActionListener(e -> adjustScale(0.2f));
        zoomOutButton.addActionListener(e -> adjustScale(-0.2f));
    }

    private void navigatePage(int delta) {
        if (pdfDocument == null) return;
        currentPage = Math.max(0, Math.min(currentPage + delta, pdfDocument.getNumberOfPages() - 1));
        renderPage();
    }

    private void adjustScale(float delta) {
        scale = Math.max(0.5f, scale + delta); // 限制最小缩放比例为 0.5
        renderPage();
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        pdfScrollPane.getViewport().setBackground(backgroundColor); // 设置滚动区域背景色
        renderPage(); // 重新渲染当前页面
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
        renderPage(); // 重新渲染当前页面
    }

    public void closePDF() {
        try {
            if (pdfDocument != null) {
                pdfDocument.close();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(mainPanel, "关闭 PDF 文件失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
}

