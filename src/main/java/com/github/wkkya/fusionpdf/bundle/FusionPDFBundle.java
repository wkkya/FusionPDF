package com.github.wkkya.fusionpdf.bundle;

import com.intellij.AbstractBundle;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

import java.util.ResourceBundle;

public final class FusionPDFBundle {

    @NonNls
    private static final String BUNDLE_NAME = "messages.FusionPDFBundle";

    private static final ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME);

    private FusionPDFBundle() {
    }

    @NotNull
    public static String message(@PropertyKey(resourceBundle = BUNDLE_NAME) @NotNull String key, Object... params) {
        return AbstractBundle.message(bundle, key, params);
    }
}
