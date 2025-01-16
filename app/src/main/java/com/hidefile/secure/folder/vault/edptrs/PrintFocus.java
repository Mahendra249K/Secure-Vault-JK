package com.hidefile.secure.folder.vault.edptrs;


import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class PrintFocus {

    public static final int AUTO = 0;

    public static final int CONTINUOUS_PICTURE = 1;

    public static final int NO_FOCUS = 2;

    private PrintFocus() {
        throw new RuntimeException("Cannot initialize this class.");
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({AUTO, CONTINUOUS_PICTURE, NO_FOCUS})
    public @interface SupportedCameraFocus {
    }
}
