package com.hidefile.secure.folder.vault.calldorado;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

public class OverlayPermissionManager {
    public static final int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 2803;
    public static final int LISTEN_TIMEOUT = 5 * 20;
    private static final int THREAD_SLEEP_TIME = 200;
    private final Activity activity;
    private OnOverlayPermissionListner listner;
    private Thread thread;
    public static boolean shouldContinueThread = true;

    public OverlayPermissionManager(Activity activity) {
        this.activity = activity;
    }

    public void requestOverlay(OnOverlayPermissionListner listner) {
        shouldContinueThread = true;
        this.listner = listner;
        sendToSettings();
        startGrantedCheckThread();
    }

    private void sendToSettings() {

        listner.onGrantPermission();
    }

//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
//            shouldContinueThread = false;
//        }
//    }

    private void startGrantedCheckThread() {
        thread = new Thread() {
            @Override
            public void run() {
                int counter = 0;
                while (!Settings.canDrawOverlays(activity) && shouldContinueThread
                        && counter < LISTEN_TIMEOUT) {
                    try {
                        counter++;
// CLog.d(TAG, "run: still no permission");
                        sleep(THREAD_SLEEP_TIME);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (shouldContinueThread && counter < LISTEN_TIMEOUT) {
                    Intent intent = new Intent(activity, activity.getClass());
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                        activity.startActivity(intent);
                    } else {
                        activity.startActivityIfNeeded(intent, 0);
                    }

                }
            }
        };
        thread.start();
    }

    public boolean isGranted() {
        return Settings.canDrawOverlays(activity);
    }

    public interface OnOverlayPermissionListner{
        void onGrantPermission();
    }
}