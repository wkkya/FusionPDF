package com.github.wkkya.fusionpdf.viewer;

import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.jcef.*;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;

public class FusionPDFViewerPanel extends SimpleToolWindowPanel {

    private final JBCefBrowserBase browser;
    private final JBCefJSQuery jsQuery;

    public FusionPDFViewerPanel() {
        super(true, true);

        // Initialize JCEF browser
        new JBCefClient.Properties();

        JBCefClient client = JBCefApp.getInstance().createClient() ;
        browser = new JBCefBrowser("about:blank");
        // 确保 browser 不为 null 再创建 jsQuery
        if (browser != null) {
            jsQuery = JBCefJSQuery.create(browser);
        } else {
            throw new IllegalStateException("Failed to initialize browser");
        }

        // Configure the viewer panel layout
        setLayout(new BorderLayout());
        setContent(createViewerPanel());

        // Load PDF.js viewer
        loadPDFJS();
    }

    private JPanel createViewerPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JBScrollPane(browser.getComponent());
        scrollPane.setBorder(null);

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private void loadPDFJS() {
        String pdfJSPath = getClass().getResource("/pdfjs/web/viewer.html").toExternalForm();
        browser.loadURL(pdfJSPath);
    }

    public void openPDF(Path pdfPath) {
        String jsCode = "loadPDF('" + pdfPath.toUri().toString() + "')";
        browser.getCefBrowser().executeJavaScript(jsCode, browser.getCefBrowser().getURL(), 0);
    }

    public void setViewerColors(Color backgroundColor, Color foregroundColor) {
        String bgHex = String.format("#%02x%02x%02x", backgroundColor.getRed(), backgroundColor.getGreen(), backgroundColor.getBlue());
        String fgHex = String.format("#%02x%02x%02x", foregroundColor.getRed(), foregroundColor.getGreen(), foregroundColor.getBlue());

        String jsCode = "setViewerColors('" + bgHex + "', '" + fgHex + "')";
        browser.getCefBrowser().executeJavaScript(jsCode, browser.getCefBrowser().getURL(), 0);
    }

    public void dispose() {
        jsQuery.dispose();
        browser.dispose();
    }

    public Component getMainComponent() {

        return this;
    }
}

