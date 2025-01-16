package com.hidefile.secure.folder.vault.edptrs;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


public final class FootError {


    public static final int ERROR_CAMERA_OPEN_FAILED = 1122;

    public static final int ERROR_CAMERA_PERMISSION_NOT_AVAILABLE = 5472;

    public static final int ERROR_DOES_NOT_HAVE_OVERDRAW_PERMISSION = 3136;

    public static final int ERROR_DOES_NOT_HAVE_FRONT_CAMERA = 8722;

    public static final int ERROR_IMAGE_WRITE_FAILED = 9854;

    private FootError() {
        throw new RuntimeException("Cannot initiate FootError.");
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ERROR_CAMERA_PERMISSION_NOT_AVAILABLE,
            ERROR_CAMERA_OPEN_FAILED,
            ERROR_DOES_NOT_HAVE_FRONT_CAMERA,
            ERROR_DOES_NOT_HAVE_OVERDRAW_PERMISSION,
            ERROR_IMAGE_WRITE_FAILED})
    public @interface CameraErrorCodes {
    }
}