package com.hidefile.secure.folder.vault.edptrs;


import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class PrintRotation {
    public static final int ROTATION_90 = 90;
    public static final int ROTATION_180 = 180;
    public static final int ROTATION_270 = 270;
    public static final int ROTATION_0 = 0;
    private PrintRotation() {
        throw new RuntimeException("Cannot initialize this class.");
    }
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ROTATION_0, ROTATION_90, ROTATION_180, ROTATION_270})
    public @interface SupportedRotation {
    }
}
