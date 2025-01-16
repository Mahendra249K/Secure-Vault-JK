package com.hidefile.secure.folder.vault.edptrs;


import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class PrintResolution {

    public static final int HIGH_RESOLUTION = 2006;

    public static final int MEDIUM_RESOLUTION = 7895;

    public static final int LOW_RESOLUTION = 7821;

    private PrintResolution() {
        throw new RuntimeException("Cannot initiate PrintResolution.");
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({HIGH_RESOLUTION, MEDIUM_RESOLUTION, LOW_RESOLUTION})
    public @interface SupportedResolution {
    }
}
