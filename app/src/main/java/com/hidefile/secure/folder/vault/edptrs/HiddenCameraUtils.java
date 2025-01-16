package com.hidefile.secure.folder.vault.edptrs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;

import com.hidefile.secure.folder.vault.cluecanva.TillsPth;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public final class HiddenCameraUtils {

    @SuppressLint("NewApi")
    public static boolean canOverDrawOtherApps(Context context) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(context);
    }

    public static void openDrawOverPermissionSetting(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return;
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    static File getCacheDir() {
        return new File(TillsPth.worngPwd);
    }

    @SuppressWarnings("deprecation")
    public static boolean isFrontCameraAvailable(@NonNull Context context) {
        int numCameras = Camera.getNumberOfCameras();
        return numCameras > 0 && context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT);
    }

    @WorkerThread
    static Bitmap rotateBitmap(@NonNull Bitmap bitmap, @PrintRotation.SupportedRotation int rotation) {
        Matrix matrix = new Matrix();
        matrix.postRotate(rotation);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    static boolean saveImageFromFile(@NonNull Bitmap bitmap, @NonNull File fileToSave, @PrintFormat.SupportedImageFormat int imageFormat) {
        FileOutputStream out = null;
        boolean isSuccess;
        Bitmap.CompressFormat compressFormat;
        switch (imageFormat) {
            case PrintFormat.FORMAT_JPEG:
                compressFormat = Bitmap.CompressFormat.JPEG;
                break;
            case PrintFormat.FORMAT_WEBP:
                compressFormat = Bitmap.CompressFormat.WEBP;
                break;
            case PrintFormat.FORMAT_PNG:
            default:
                compressFormat = Bitmap.CompressFormat.PNG;
        }

        try {
            if (!fileToSave.exists())
                fileToSave.createNewFile();

            out = new FileOutputStream(fileToSave);
            bitmap.compress(compressFormat, 100, out);
            isSuccess = true;
        } catch (Exception e) {
            e.printStackTrace();
            isSuccess = false;
        } finally {
            try {
                if (out != null) out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return isSuccess;
    }
}
