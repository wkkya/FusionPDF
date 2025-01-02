package com.github.wkkya.fusionpdf.ui.read;

import com.github.wkkya.fusionpdf.module.ReadUIState;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.ui.Messages;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReadUI  {
    private JPanel mainPanel;
    private JLabel pdfViewer;
    private JScrollPane pdfScrollPane;
    private JPanel controlPanel;
    private JButton prevButton;
    private JButton nextButton;
    private JTextField pageField;


    private JLabel zoomInLabel;
    private JLabel zoomOutLabel;

    private PDDocument pdfDocument;
    private PDFRenderer pdfRenderer;
    private int currentPage = 0;
    private float scale = 1.0f; // 初始缩放比例
    private Color backgroundColor = Color.DARK_GRAY; // 默认背景色
    private Color textColor = Color.WHITE; // 默认文字颜色

    private final ExecutorService renderExecutor = Executors.newSingleThreadExecutor(); // 渲染线程池

    // 缓存已渲染的页面：避免页面重复渲染
    public final Map<Integer, BufferedImage> pageCache = new HashMap<>();

    public ReadUI() {
        setupUI();
        setupListeners();

        // 读取上次保存的页面位置
        ReadUIState state = ServiceManager.getService(ReadUIState.class);
        if (state != null) {
            currentPage = state.getLastPage();
        }
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

        renderExecutor.submit(() -> {
            if (pdfDocument == null || pdfRenderer == null) {
                return;
            }
            try {
                //从缓存中获取已渲染对象，未命中则重新渲染
                BufferedImage cachedImage = getPageFromCache(currentPage, scale);

                // 对渲染图像进行抗锯齿和文字颜色优化
                BufferedImage processedImage = processPDFImage(cachedImage);

                // 缩放至窗口大小
                BufferedImage scaledImage = quickScaleImage(processedImage, scale);

                // 显示优化后的图像
                pdfViewer.setIcon(new ImageIcon(scaledImage));
                pageField.setText((currentPage + 1) + " / " + pdfDocument.getNumberOfPages());

                //renderPageAsync(currentPage);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(mainPanel, "渲染 PDF 页面失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        });


    }

    /**
     * 渲染可见区域
     * @param fullImage
     * @param visibleWidth
     * @param visibleHeight
     * @return
     */
    private BufferedImage renderVisiblePart(BufferedImage fullImage, int visibleWidth, int visibleHeight) {
        return fullImage.getSubimage(0, 0, visibleWidth, visibleHeight);
    }


    /**
     * 搞笑缩放
     * @param original
     * @param scale
     * @return
     */
    private BufferedImage quickScaleImage(BufferedImage original, float scale) {
        int newWidth = (int) (original.getWidth() * scale);
        int newHeight = (int) (original.getHeight() * scale);
        Image scaled = original.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        BufferedImage result = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = result.createGraphics();
        g2d.drawImage(scaled, 0, 0, null);
        g2d.dispose();
        return result;
    }

    /**
     * 缓存中获取已渲染对象
     * @param pageIndex
     * @param scale
     * @return
     * @throws IOException
     */
    private BufferedImage getPageFromCache(int pageIndex, float scale) throws IOException {
        int cacheKey = (int) (pageIndex + scale * 100); // 生成唯一缓存键
        if (!pageCache.containsKey(cacheKey)) {
            doCache(cacheKey);
        }
        return pageCache.get(cacheKey);
    }

    private void doCache(int cacheKey) throws IOException {
        // 提高 DPI 渲染 PDF 页面
        int dpi = 300; // 默认 DPI 设置为 300
        BufferedImage originalImage = pdfRenderer.renderImageWithDPI(currentPage, dpi * scale);
        pageCache.put(cacheKey, originalImage);
    }

    private BufferedImage scaleImage(BufferedImage image, int targetWidth, int targetHeight) {
        // 使用高质量缩放方法
        Image scaledInstance = image.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        BufferedImage scaledImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = scaledImage.createGraphics();
        g2d.drawImage(scaledInstance, 0, 0, null);
        g2d.dispose();
        return scaledImage;
    }

    private BufferedImage processPDFImage(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = result.createGraphics();

        // 启用抗锯齿
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        // 绘制原始图像到结果图像
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();

        // 替换背景色和调整文字颜色
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color originalColor = new Color(image.getRGB(x, y), true);
                if (isBackgroundColor(originalColor)) {
                    result.setRGB(x, y, backgroundColor.getRGB());
                } else {
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

        // 创建放大图标
        ImageIcon zoomInIcon = new ImageIcon(getClass().getResource("/icons/zoom_in.png"));
        Image zoomInImage = zoomInIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH); // 调整图标大小
        zoomInIcon = new ImageIcon(zoomInImage);
        zoomInLabel = new JLabel(zoomInIcon);
        zoomInLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // 鼠标指针变为手型

        // 创建缩小图标
        ImageIcon zoomOutIcon = new ImageIcon(getClass().getResource("/icons/zoom_out.png"));
        Image zoomOutImage = zoomOutIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH); // 调整图标大小
        zoomOutIcon = new ImageIcon(zoomOutImage);
        zoomOutLabel = new JLabel(zoomOutIcon);
        zoomOutLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // 鼠标指针变为手型

        pageField = new JTextField(5);
        pageField.setEditable(false);

        // 获取纵向滚动条并设置每次滚动的步长
        JScrollBar verticalScrollBar = pdfScrollPane.getVerticalScrollBar();
        verticalScrollBar.setUnitIncrement(20); // 设置为 20，增加滚动速度

        controlPanel.add(prevButton);
        controlPanel.add(pageField);
        controlPanel.add(nextButton);
        controlPanel.add(zoomInLabel);
        controlPanel.add(zoomOutLabel);

        mainPanel.add(pdfScrollPane, BorderLayout.CENTER);
        mainPanel.add(controlPanel, BorderLayout.SOUTH);

        pdfScrollPane.getViewport().setBackground(backgroundColor); // 设置滚动区域背景色
    }

    private void setupListeners() {
        prevButton.addActionListener(e -> navigatePage(-1));
        nextButton.addActionListener(e -> navigatePage(1));
        // 为图标添加点击事件
        zoomInLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                zoomIn();
            }
        });

        zoomOutLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                zoomOut();
            }
        });


    }

    // 放大功能
    private void zoomIn() {
        scale += 0.1f; // 增加缩放比例
        renderPage();
    }

    // 缩小功能
    private void zoomOut() {
        if (scale > 0.2f) { // 限制最小缩放比例
            scale -= 0.1f;
        }
        renderPage();
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

    /**
     * 存储当前页面
     */
    private void saveCurrentPage() {
        ReadUIState state = ServiceManager.getService(ReadUIState.class);
        if (state != null) {
            state.setLastPage(currentPage);
        }
    }

    public void closePDF() {
        try {
            if (pdfDocument != null) {
                pdfDocument.close();
                pdfDocument = null;
                pdfRenderer = null;
                saveCurrentPage(); // 保存当前页面位置
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(mainPanel, "关闭 PDF 文件失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

}

