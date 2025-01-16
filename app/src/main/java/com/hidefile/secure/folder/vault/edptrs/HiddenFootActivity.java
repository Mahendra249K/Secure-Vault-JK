package com.hidefile.secure.folder.vault.edptrs;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.core.app.ActivityCompat;

import com.hidefile.secure.folder.vault.dashex.FoundationActivity;

public abstract class HiddenFootActivity extends FoundationActivity implements FootCallbacks {

    private FootPreview mFootPreview;
    private PrintConfig mCachedPrintConfig;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFootPreview = addPreView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopCamera();
    }

    @RequiresPermission(Manifest.permission.CAMERA)
    protected void startCamera(PrintConfig printConfig) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) { //check if the camera permission is available
            onCameraError(FootError.ERROR_CAMERA_PERMISSION_NOT_AVAILABLE);
        } else if (printConfig.getFacing() == PrintFacing.FRONT_FACING_CAMERA && !HiddenCameraUtils.isFrontCameraAvailable(this)) {   //Check if for the front camera
            onCameraError(FootError.ERROR_DOES_NOT_HAVE_FRONT_CAMERA);
        } else {
            mCachedPrintConfig = printConfig;
            mFootPreview.startCameraInternal(printConfig);
        }
    }
    protected void takePicture() {
        AudioManager mgr = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        mgr.setStreamMute(AudioManager.STREAM_SYSTEM, true);
        if (mFootPreview != null) {
            if (mFootPreview.isSafeToTakePictureInternal()) {
                mFootPreview.takePictureInternal();
            }
        } else {
            mFootPreview = addPreView();
            throw new RuntimeException("Background camera not initialized. Call startCamera() to initialize the camera.");
        }
    }

    protected void stopCamera() {
        mCachedPrintConfig = null;
        if (mFootPreview != null) mFootPreview.stopPreviewAndFreeCamera();
    }

    private FootPreview addPreView() {
        FootPreview cameraSourceFootPreview = new FootPreview(this, this);
        cameraSourceFootPreview.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        View view = ((ViewGroup) getWindow().getDecorView().getRootView()).getChildAt(0);

        if (view instanceof LinearLayout) {
            LinearLayout linearLayout = (LinearLayout) view;

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(1, 1);
            linearLayout.addView(cameraSourceFootPreview, params);
        } else if (view instanceof RelativeLayout) {
            RelativeLayout relativeLayout = (RelativeLayout) view;

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(1, 1);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            relativeLayout.addView(cameraSourceFootPreview, params);
        } else if (view instanceof FrameLayout) {
            FrameLayout frameLayout = (FrameLayout) view;

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(1, 1);
            frameLayout.addView(cameraSourceFootPreview, params);
        } else {
            throw new RuntimeException("Root view of the activity/fragment cannot be other than Linear/Relative/Frame layout");
        }

        return cameraSourceFootPreview;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mCachedPrintConfig != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startCamera(mCachedPrintConfig);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mFootPreview != null) mFootPreview.stopPreviewAndFreeCamera();
    }
}