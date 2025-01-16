package com.hidefile.secure.folder.vault.edptrs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Looper;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;


@SuppressLint("ViewConstructor")
class FootPreview extends SurfaceView implements SurfaceHolder.Callback {
    private FootCallbacks mFootCallbacks;

    private SurfaceHolder mHolder;
    private Camera mCamera;

    private PrintConfig mPrintConfig;

    private volatile boolean safeToTakePicture = false;

    FootPreview(@NonNull Context context, FootCallbacks footCallbacks) {
        super(context);
        mFootCallbacks = footCallbacks;
        initSurfaceView();
    }
    private void initSurfaceView() {
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }


    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        if (mCamera == null) {  //Camera is not initialized yet.
            mFootCallbacks.onCameraError(FootError.ERROR_CAMERA_OPEN_FAILED);
            return;
        } else if (surfaceHolder.getSurface() == null) { //Surface preview is not initialized yet
            mFootCallbacks.onCameraError(FootError.ERROR_CAMERA_OPEN_FAILED);
            return;
        }
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
        }
        Camera.Parameters parameters = mCamera.getParameters();
        List<Camera.Size> pictureSizes = mCamera.getParameters().getSupportedPictureSizes();
        Collections.sort(pictureSizes, new PrintSize());
        Camera.Size cameraSize;
        switch (mPrintConfig.getResolution()) {
            case PrintResolution.HIGH_RESOLUTION:
                cameraSize = pictureSizes.get(0);
                break;
            case PrintResolution.MEDIUM_RESOLUTION:
                cameraSize = pictureSizes.get(pictureSizes.size() / 2);
                break;
            case PrintResolution.LOW_RESOLUTION:
                cameraSize = pictureSizes.get(pictureSizes.size() - 1);
                break;
            default:
                throw new RuntimeException("Invalid camera resolution.");
        }
        parameters.setPictureSize(cameraSize.width, cameraSize.height);
        List<String> supportedFocusModes = parameters.getSupportedFocusModes();
        if (supportedFocusModes.contains(mPrintConfig.getFocusMode())) {
            parameters.setFocusMode(mPrintConfig.getFocusMode());
        }

        requestLayout();

        mCamera.setParameters(parameters);

        try {
            mCamera.setDisplayOrientation(90);
            mCamera.setPreviewDisplay(surfaceHolder);
            mCamera.setPreviewDisplay(surfaceHolder);
            mCamera.startPreview();

            safeToTakePicture = true;
        } catch (IOException | NullPointerException e) {
            //Cannot start preview
            mFootCallbacks.onCameraError(FootError.ERROR_CAMERA_OPEN_FAILED);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mCamera != null) mCamera.stopPreview();
    }
    void startCameraInternal(@NonNull PrintConfig printConfig) {
        mPrintConfig = printConfig;

        if (safeCameraOpen(mPrintConfig.getFacing())) {
            if (mCamera != null) {
                requestLayout();

                try {
                    mCamera.setPreviewDisplay(mHolder);
                    mCamera.startPreview();
                } catch (IOException e) {
                    e.printStackTrace();
                    mFootCallbacks.onCameraError(FootError.ERROR_CAMERA_OPEN_FAILED);
                }
            }
        } else {
            mFootCallbacks.onCameraError(FootError.ERROR_CAMERA_OPEN_FAILED);
        }
    }

    private boolean safeCameraOpen(int id) {
        boolean qOpened = false;

        try {
            stopPreviewAndFreeCamera();

            mCamera = Camera.open(id);
            qOpened = (mCamera != null);
        } catch (Exception e) {
            Log.e("FootPreview", "failed to open Camera");
            e.printStackTrace();
        }

        return qOpened;
    }

    boolean isSafeToTakePictureInternal() {
        return safeToTakePicture;
    }

    void takePictureInternal() {
        safeToTakePicture = false;
        try {
            if (mCamera != null) {
                mCamera.takePicture(null, null, new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(final byte[] bytes, Camera camera) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                                Bitmap rotatedBitmap;
                                if (mPrintConfig.getImageRotation() != PrintRotation.ROTATION_0) {
                                    rotatedBitmap = HiddenCameraUtils.rotateBitmap(bitmap, mPrintConfig.getImageRotation());
                                    //avc
                                    bitmap = null;
                                } else {
                                    rotatedBitmap = bitmap;
                                }
                                if (HiddenCameraUtils.saveImageFromFile(rotatedBitmap,
                                        tempPath(),
                                        mPrintConfig.getImageFormat())) {
                                    //Post image file to the main thread
                                    new android.os.Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            mFootCallbacks.onImageCapture(mImageFile);
                                        }
                                    });
                                } else {
                                    //Post error to the main thread
                                    new android.os.Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            mFootCallbacks.onCameraError(FootError.ERROR_IMAGE_WRITE_FAILED);
                                        }
                                    });
                                }

                                if (mCamera != null) {
                                    mCamera.startPreview();
                                    safeToTakePicture = true;
                                }
                            }
                        }).start();
                    }
                });
            } else {
                mFootCallbacks.onCameraError(FootError.ERROR_CAMERA_OPEN_FAILED);
                safeToTakePicture = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private File mImageFile;

    private File tempPath() {
        mImageFile = new File(HiddenCameraUtils.getCacheDir(), "." + System.currentTimeMillis() + ".png");
       return mImageFile ;
    }

    void stopPreviewAndFreeCamera() {
        safeToTakePicture = false;
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }
}