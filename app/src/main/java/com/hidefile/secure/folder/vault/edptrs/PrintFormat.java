package com.hidefile.secure.folder.vault.edptrs;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class PrintFormat {

    public static final int FORMAT_JPEG = 849;

    public static final int FORMAT_PNG = 545;

    public static final int FORMAT_WEBP = 563;

    private PrintFormat() {
        throw new RuntimeException("Cannot initialize PrintFormat.");
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({FORMAT_JPEG, FORMAT_PNG, FORMAT_WEBP})
    public @interface SupportedImageFormat {
    }
}
