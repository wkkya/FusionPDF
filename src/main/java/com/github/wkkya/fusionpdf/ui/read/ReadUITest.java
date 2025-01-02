package com.github.wkkya.fusionpdf.ui.read;

import com.intellij.openapi.ui.Messages;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReadUITest {
    private JPanel controlPanel;
    private JPanel mainPanel;
    private JLabel pdfViewer;
    private JScrollPane pdfScrollPane;
    private JButton prevButton;
    private JButton nextButton;
    private JButton enlargeButton;
    private JButton zoomOutButton;
    private JTextField pageField;
    private PDDocument pdfDocument;
    private PDFRenderer pdfRenderer;
    private int currentPage = 0;
    private float scale = 1.0f; // 缩放比例
    private Color backgroundColor = new Color(43, 43, 43); // IDEA 默认背景色
    private Color fontColor = Color.WHITE;
    private final ExecutorService renderExecutor = Executors.newSingleThreadExecutor(); // 渲染线程池

    public ReadUITest() {

        setupUI();
        // 初始化组件
        pdfScrollPane.getViewport().setBackground(backgroundColor);
        pdfViewer.setHorizontalAlignment(SwingConstants.CENTER);

        // 按钮事件
        prevButton.addActionListener(e -> navigatePage(-1));
        nextButton.addActionListener(e -> navigatePage(1));
        enlargeButton.addActionListener(e -> changeScale(0.1f));
        zoomOutButton.addActionListener(e -> changeScale(-0.1f));
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

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void loadPDF(String filePath) {
        try {
            if (pdfDocument != null) {
                pdfDocument.close();
            }
            pdfDocument = PDDocument.load(new File(filePath));
            pdfRenderer = new PDFRenderer(pdfDocument);
            renderPage(currentPage);
        } catch (IOException e) {
            Messages.showErrorDialog("加载 PDF 文件失败：" + e.getMessage(), "错误");
        }
    }

    private void renderPage(int pageIndex) {
        renderExecutor.submit(() -> {
            try {
                if (pdfDocument == null || pageIndex < 0 || pageIndex >= pdfDocument.getNumberOfPages()) {
                    return;
                }
                BufferedImage image = pdfRenderer.renderImageWithDPI(pageIndex, 300 * scale); // 高 DPI 渲染
                image = applyBackground(image, backgroundColor);
                BufferedImage finalImage = image;
                SwingUtilities.invokeLater(() -> {
                    pdfViewer.setIcon(new ImageIcon(finalImage));
                    pageField.setText((currentPage + 1) + " / " + pdfDocument.getNumberOfPages());
                });
            } catch (IOException e) {
                SwingUtilities.invokeLater(() -> Messages.showErrorDialog("渲染 PDF 页面失败：" + e.getMessage(), "错误"));
            }
        });
    }

    private BufferedImage applyBackground(BufferedImage image, Color backgroundColor) {
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = result.createGraphics();
        g.setColor(backgroundColor);
        g.fillRect(0, 0, result.getWidth(), result.getHeight());
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return result;
    }

    private void navigatePage(int delta) {
        if (pdfDocument == null) return;
        currentPage = Math.max(0, Math.min(currentPage + delta, pdfDocument.getNumberOfPages() - 1));
        renderPage(currentPage);
    }

    private void changeScale(float delta) {
        scale = Math.max(0.1f, scale + delta);
        renderPage(currentPage);
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        pdfScrollPane.getViewport().setBackground(backgroundColor);
        renderPage(currentPage);
    }

    public void setFontColor(Color fontColor) {
        this.fontColor = fontColor;
        // 实现设置字体颜色的功能（需要进一步扩展）
        renderPage(currentPage); // 重新渲染当前页面
    }
}
