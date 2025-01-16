package com.hidefile.secure.folder.vault.cluecanva;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;

import com.hidefile.secure.folder.vault.dashex.Pswd;

public class HandleLyf implements Application.ActivityLifecycleCallbacks {

    private int resumed;
    private int paused;
    private int started;
    private int stopped;
    private boolean isFromBackground = false;



    public void startHome(Activity activity) {

        Intent intent = new Intent(activity, Pswd.class);
        intent.putExtra("isFromBackground", true);
        ActivityCompat.startActivity(activity, intent, null);
        activity.startActivity(intent);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
        ++resumed;

        if (!activity.getLocalClassName().equalsIgnoreCase("activity.ResetPinByEmail") || !activity.getLocalClassName().equalsIgnoreCase("activity.PinActivity")) {
            isFromBackground = false;
        }
        if (isFromBackground) {
            isFromBackground = false;
            startHome(activity);
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        ++paused;
        if (resumed < paused) {
            isFromBackground = true;
        } else {
            isFromBackground = false;
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
        ++started;
    }

    @Override
    public void onActivityStopped(Activity activity) {
        ++stopped;
        if (started == stopped) {
            isFromBackground = true;
        } else {
            isFromBackground = false;
        }
    }

}