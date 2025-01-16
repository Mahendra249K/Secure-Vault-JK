package com.hidefile.secure.folder.vault.AdActivity;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;


import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.hidefile.secure.folder.vault.dashex.Str11;


public class Open_App_Manager implements LifecycleObserver, Application.ActivityLifecycleCallbacks {
    private static final String TAG = "Open_App_Manager";
    public static boolean doNotDisplayAds = false;
    /* access modifiers changed from: private */
    public static boolean isShowingAd = false;
    public Preference_Ads adsPreference;
    /* access modifiers changed from: private */
    public AppOpenAd appOpenAd = null;

    private static volatile Open_App_Manager INSTANCE;
    private Activity currentActivity;
    private AppOpenAd.AppOpenAdLoadCallback loadCallback;
    private final MyApplication myApplication;

    public void onActivityCreated(Activity activity, Bundle bundle) {
    }

    public void onActivityPaused(Activity activity) {
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    public void onActivityStopped(Activity activity) {
    }

    public Open_App_Manager(MyApplication myApplication) {
        this.myApplication = myApplication;
        myApplication.registerActivityLifecycleCallbacks(this);
        this.adsPreference = new Preference_Ads(myApplication);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    public static synchronized Open_App_Manager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Open_App_Manager(new MyApplication());
        }

        return INSTANCE;
    }

    public void fetchAd() {
        if (!isAdAvailable()) {
            Log.d(TAG, "fetchAd: ");
            this.loadCallback = new AppOpenAd.AppOpenAdLoadCallback() {
                public void onAdLoaded(AppOpenAd appOpenAd) {
                    Log.d(Open_App_Manager.TAG, "onAdLoaded: ");
                    AppOpenAd unused = Open_App_Manager.this.appOpenAd = appOpenAd;
                }

                public void onAdFailedToLoad(LoadAdError loadAdError) {
                    Log.d(Open_App_Manager.TAG, "onAdFailedToLoad: " + loadAdError.getMessage());
                }
            };
            AppOpenAd.load((Context) this.myApplication, this.adsPreference.getAdmAppOpenID(), getAdRequest(), 1, this.loadCallback);
        }
    }

    private AdRequest getAdRequest() {
        return new AdRequest.Builder().build();
    }

    public boolean isAdAvailable() {
        return this.appOpenAd != null;
    }

    public void showAdIfAvailable() {

        Log.d(TAG, "showAdIfAvailable: ");
        if (isShowingAd || !isAdAvailable()) {
            Log.d(TAG, "Can not show ad.");
            fetchAd();
            return;
        }
        Log.d(TAG, "Will show ad.");
        this.appOpenAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            public void onAdFailedToShowFullScreenContent(AdError adError) {
            }
            public void onAdDismissedFullScreenContent() {
                AppOpenAd unused = Open_App_Manager.this.appOpenAd = null;
                boolean unused2 = Open_App_Manager.isShowingAd = false;
                Open_App_Manager.this.fetchAd();
            }

            public void onAdShowedFullScreenContent() {
                boolean unused = Open_App_Manager.isShowingAd = true;
            }
        });
        Activity activity = this.currentActivity;
        if (!(activity instanceof Str11)) {
            this.appOpenAd.show(activity);
        }

    }

    public void onActivityStarted(Activity activity) {
        this.currentActivity = activity;
    }

    public void onActivityResumed(Activity activity) {
        this.currentActivity = activity;
    }

    public void onActivityDestroyed(Activity activity) {
        this.currentActivity = null;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        if(!SharedPref.AppOpenShow) {
            if (!doNotDisplayAds) {
                showAdIfAvailable();
            }
            Log.d(TAG, "onStart");
        }

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        if(!SharedPref.AppOpenShow) {
            if (!doNotDisplayAds) {
                showAdIfAvailable();
            }
        }
        Log.d(TAG, "onResume");

    }

    public void appInForeground() {
        if (isShowingAd || !isAdAvailable()) {
            Log.d(TAG, "Can not show ad.");
            fetchAd();
            return;
        }

    }
}
