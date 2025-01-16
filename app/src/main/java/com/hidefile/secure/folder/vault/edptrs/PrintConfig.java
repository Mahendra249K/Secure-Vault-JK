package com.hidefile.secure.folder.vault.edptrs;

import android.content.Context;
import android.hardware.Camera;

import androidx.annotation.Nullable;

import java.io.File;

public final class PrintConfig {
    private Context mContext;

    @PrintResolution.SupportedResolution
    private int mResolution = PrintResolution.MEDIUM_RESOLUTION;

    @PrintFacing.SupportedCameraFacing
    private int mFacing = PrintFacing.REAR_FACING_CAMERA;

    @PrintFormat.SupportedImageFormat
    private int mImageFormat = PrintFormat.FORMAT_JPEG;

    @PrintRotation.SupportedRotation
    private int mImageRotation = PrintRotation.ROTATION_0;

    @PrintFocus.SupportedCameraFocus
    private int mCameraFocus = PrintFocus.AUTO;



    public PrintConfig() {
    }

    public Builder getBuilder(Context context) {
        mContext = context;
        return new Builder();
    }

    @PrintResolution.SupportedResolution
    int getResolution() {
        return mResolution;
    }

    @Nullable
    String getFocusMode() {
        switch (mCameraFocus) {
            case PrintFocus.AUTO:
                return Camera.Parameters.FOCUS_MODE_AUTO;
            case PrintFocus.CONTINUOUS_PICTURE:
                return Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE;
            case PrintFocus.NO_FOCUS:
                return null;
            default:
                throw new RuntimeException("Invalid camera focus mode.");
        }
    }

    @PrintFacing.SupportedCameraFacing
    int getFacing() {
        return mFacing;
    }

    @PrintFormat.SupportedImageFormat
    int getImageFormat() {
        return mImageFormat;
    }

//    File getImageFile() {
//        return mImageFile;
//    }

    @PrintRotation.SupportedRotation
    int getImageRotation() {
        return mImageRotation;
    }

    public class Builder {
        public Builder setCameraResolution(@PrintResolution.SupportedResolution int resolution) {
            if (resolution != PrintResolution.HIGH_RESOLUTION &&
                    resolution != PrintResolution.MEDIUM_RESOLUTION &&
                    resolution != PrintResolution.LOW_RESOLUTION) {
                throw new RuntimeException("Invalid camera resolution.");
            }

            mResolution = resolution;
            return this;
        }
        public Builder setCameraFacing(@PrintFacing.SupportedCameraFacing int cameraFacing) {
            if (cameraFacing != PrintFacing.REAR_FACING_CAMERA &&
                    cameraFacing != PrintFacing.FRONT_FACING_CAMERA) {
                throw new RuntimeException("Invalid camera facing value.");
            }

            mFacing = cameraFacing;
            return this;
        }
        public Builder setCameraFocus(@PrintFocus.SupportedCameraFocus int focusMode) {
            if (focusMode != PrintFocus.AUTO &&
                    focusMode != PrintFocus.CONTINUOUS_PICTURE &&
                    focusMode != PrintFocus.NO_FOCUS) {
                throw new RuntimeException("Invalid camera focus mode.");
            }

            mCameraFocus = focusMode;
            return this;
        }
        public Builder setImageFormat(@PrintFormat.SupportedImageFormat int imageFormat) {
            //Validate input
            if (imageFormat != PrintFormat.FORMAT_JPEG &&
                    imageFormat != PrintFormat.FORMAT_PNG) {
                throw new RuntimeException("Invalid output image format.");
            }

            mImageFormat = imageFormat;
            return this;
        }
        public Builder setImageRotation(@PrintRotation.SupportedRotation int rotation) {
            //Validate input
            if (rotation != PrintRotation.ROTATION_0
                    && rotation != PrintRotation.ROTATION_90
                    && rotation != PrintRotation.ROTATION_180
                    && rotation != PrintRotation.ROTATION_270) {
                throw new RuntimeException("Invalid image rotation.");
            }

            mImageRotation = rotation;
            return this;
        }
//        public Builder setImageFile(File imageFile) {
//            mImageFile = imageFile;
//            return this;
//        }
        public PrintConfig build() {
//            if (mImageFile == null) mImageFile = getDefaultStorageFile();
            return PrintConfig.this;
        }
        public File getDefaultStorageFile() {
            return new File(HiddenCameraUtils.getCacheDir(), "." + System.currentTimeMillis() + (mImageFormat == PrintFormat.FORMAT_JPEG ? ".jpeg" : ".png"));
        }
    }
}
