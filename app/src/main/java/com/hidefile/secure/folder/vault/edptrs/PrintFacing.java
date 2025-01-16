package com.hidefile.secure.folder.vault.edptrs;


import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class PrintFacing {

    public static final int REAR_FACING_CAMERA = 0;

    public static final int FRONT_FACING_CAMERA = 1;

    private PrintFacing() {
        throw new RuntimeException("Cannot initialize this class.");
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({REAR_FACING_CAMERA, FRONT_FACING_CAMERA})
    public @interface SupportedCameraFacing {
    }
}
