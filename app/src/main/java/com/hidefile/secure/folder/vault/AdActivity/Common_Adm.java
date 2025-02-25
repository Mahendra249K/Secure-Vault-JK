package com.hidefile.secure.folder.vault.AdActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ProcessLifecycleOwner;


import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.MediaViewListener;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.hidefile.secure.folder.vault.R;

import java.util.ArrayList;
import java.util.List;

public class Common_Adm {
    public static String app_name = null;
    public static Dialog dialog = null;
    public static InterstitialAd fb_interstitial = null;
    public static int app_ad = 1;
    public static int i_back = 1;
    public static int interstitialBackpressGapNumber = 0;
    public static int interstitialGapNumber = 0;
    public static Common_Adm mInstance = null;
    public static int mInterstitialAdBackPressClickCount = 0;
    public static int mInterstitialAdClickCount = 0;
    public Dialog LoadingAds;
    /* access modifiers changed from: private */
    public Call_Back_Ads adsCallingBack;
    public boolean isLoading = false;
    public boolean live = false;
    /* access modifiers changed from: private */
    public com.google.android.gms.ads.interstitial.InterstitialAd mInterstitialAd;
    public NativeAdLayout nativeAdLayout;
    public NativeAdView nativeAdView;
    public MediaView nativeAdMedia;

    public static Common_Adm getInstance() {
        if (mInstance == null) {
            mInstance = new Common_Adm();
        }
        return mInstance;
    }

    private static MediaViewListener getMediaViewListener() {
        return new MediaViewListener() {
            public void onComplete(MediaView mediaView) {
            }

            public void onEnterFullscreen(MediaView mediaView) {
            }

            public void onExitFullscreen(MediaView mediaView) {
            }

            public void onFullscreenBackground(MediaView mediaView) {
            }

            public void onFullscreenForeground(MediaView mediaView) {
            }

            public void onPause(MediaView mediaView) {
            }

            public void onPlay(MediaView mediaView) {
            }

            public void onVolumeChange(MediaView mediaView, float f) {
            }
        };
    }

    public void loadOrShowAdmInterstial(boolean z, Activity activity, Call_Back_Ads call_Back_Ads) {

        if (!new Common_Adm().isOnline(activity)) {
            call_Back_Ads.onAdsFail();
            return;
        }
        getInstance();

        Dialog dialog = new Dialog(activity);
        LoadingAds = dialog;
        dialog.setContentView(R.layout.ad_dialog);

        LoadingAds.getWindow().setFlags(1024, 1024);
        LoadingAds.setCanceledOnTouchOutside(false);
        LoadingAds.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        if (!new Preference_Ads(activity).getinterstitial_enable()
                && ((this.isLoading || !isOnline(activity)) && (call_Back_Ads = this.adsCallingBack) != null)) {
            call_Back_Ads.onLoading();
        }
        interstitialGapNumber = Integer.parseInt(new Preference_Ads(activity).getInterstitialClickGap());
        interstitialBackpressGapNumber = Integer.parseInt(new Preference_Ads(activity).getInterstitialBackPressClickGap());
        adsCallingBack = call_Back_Ads;

        if (new Preference_Ads(activity).get_priority().equalsIgnoreCase("FB")) {
            if (z) {

                getInstance();
                if (mInterstitialAdClickCount == app_ad) {
                    if (call_Back_Ads.toString().contains("SplashActivity")) {
                        app_ad = 1;
                        mInterstitialAdClickCount = 0;
                    } else {
                        app_ad = app_ad + interstitialGapNumber + 1;
                    }
                    if (isLoading || !isOnline(activity)) {
                        call_Back_Ads = adsCallingBack;
                        if (call_Back_Ads != null) {
                            call_Back_Ads.onLoading();
                        }
                    } else if (fb_interstitial != null) {
                    } else {
                        if (new Preference_Ads(activity).getinterstitial_enable()) {
                            FB_Interstitial_Load(activity, call_Back_Ads);
                            return;
                        }
                        isLoading = false;

                    }
                }
                else {
                    call_Back_Ads.onLoading();
                }
            } else if (new Preference_Ads(activity).getInterstitialEnableBackPress()) {
                getInstance();
                int i4 = mInterstitialAdBackPressClickCount;
                int i5 = i_back;
                if (i4 == i5) {
                    i_back = i5 + interstitialBackpressGapNumber + 1;
                    if (isLoading || !isOnline(activity)) {
                        call_Back_Ads = adsCallingBack;
                        if (call_Back_Ads != null) {
                            call_Back_Ads.onLoading();
                        }
                    } else if (fb_interstitial != null) {
                    } else {
                        if (new Preference_Ads(activity).getinterstitial_enable()) {
                            FB_Interstitial_Load(activity, call_Back_Ads);
                            return;
                        }
                        isLoading = false;
                    }
                }
                else {
                    if (!activity.isFinishing() && LoadingAds.isShowing()) {
                        LoadingAds.dismiss();
                    }
                    call_Back_Ads.onLoading();
                }
            } else {
                if (!activity.isFinishing() && LoadingAds.isShowing()) {
                    LoadingAds.dismiss();
                }
                call_Back_Ads.onLoading();
            }
        } else if (new Preference_Ads(activity).get_priority().equalsIgnoreCase("AM")) {
            if (z) {

                getInstance();
                if (mInterstitialAdClickCount == app_ad) {
                    if (call_Back_Ads.toString().contains("SplashActivity")) {
                        app_ad = 1;
                        mInterstitialAdClickCount = 0;
                    } else {
                        app_ad = app_ad + interstitialGapNumber + 1;
                    }
                    if (isLoading || !isOnline(activity)) {
                        call_Back_Ads = adsCallingBack;
                        if (call_Back_Ads != null) {
                            call_Back_Ads.onLoading();
                        }
                    } else if (mInterstitialAd == null) {
                        AM_Interstitial_Load(activity, call_Back_Ads);

                    }
                }
                else {
                    call_Back_Ads.onLoading();
                }
            }
            else
                if (new Preference_Ads(activity).getInterstitialEnableBackPress()) {
                getInstance();
                int i2 = mInterstitialAdBackPressClickCount;
                int i3 = i_back;
                if (i2 == i3) {
                    i_back = i3 + interstitialBackpressGapNumber + 1;
                    if (isLoading || !isOnline(activity)) {
                        call_Back_Ads = adsCallingBack;
                        if (call_Back_Ads != null) {
                            call_Back_Ads.onLoading();
                        }
                    } else if (mInterstitialAd == null) {
                        AM_Interstitial_Load(activity, call_Back_Ads);
                    }
                }
                else {
                    if (!activity.isFinishing() && LoadingAds.isShowing()) {
                        LoadingAds.dismiss();
                    }
                    call_Back_Ads.onLoading();
                }
            } else {
                if (!activity.isFinishing() && LoadingAds.isShowing()) {
                    LoadingAds.dismiss();
                }
                call_Back_Ads.onLoading();
            }
        } else if (!new Preference_Ads(activity).get_priority().equalsIgnoreCase("") && new Preference_Ads(activity).get_priority().equalsIgnoreCase("FB") && new Preference_Ads(activity).get_priority().equalsIgnoreCase("AM")) {
        } else {
            if (z) {
                getInstance();
                if (mInterstitialAdClickCount == app_ad) {
                    if (call_Back_Ads.toString().contains("SplashActivity")) {
                        app_ad = 1;
                        mInterstitialAdClickCount = 0;
                    } else {
                        app_ad = app_ad + interstitialGapNumber + 1;
                    }
                    isLoading = false;
                }
                else {
                    call_Back_Ads.onLoading();
                }
            } else if (new Preference_Ads(activity).getInterstitialEnableBackPress()) {
                getInstance();
                int i6 = mInterstitialAdBackPressClickCount;
                int i7 = i_back;
                if (i6 == i7) {
                    i_back = i7 + interstitialBackpressGapNumber;
                    isLoading = false;
                }
                else {
                    if (!activity.isFinishing() && LoadingAds.isShowing()) {
                        LoadingAds.dismiss();
                    }
                    call_Back_Ads.onLoading();
                }
            } else {
                if (!activity.isFinishing() && LoadingAds.isShowing()) {
                    LoadingAds.dismiss();
                }
                call_Back_Ads.onLoading();
            }
        }
    }

    public void everytimeInterstialAdShow(boolean z, Activity activity, Call_Back_Ads call_Back_Ads) {
        if (!new Common_Adm().isOnline(activity)) {
//            new Common_Adm().NoConnectionDialog(z, activity, call_Back_Ads);
            call_Back_Ads.onAdsFail();
            return;
        }
        getInstance();
        Dialog dialog = new Dialog(activity);
        LoadingAds = dialog;
        dialog.setContentView(R.layout.ad_dialog);
        LoadingAds.getWindow().setFlags(1024, 1024);
        LoadingAds.setCanceledOnTouchOutside(false);
        LoadingAds.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        if (!new Preference_Ads(activity).getinterstitial_enable() && ((this.isLoading || !isOnline(activity)) && (call_Back_Ads = this.adsCallingBack) != null)) {
            call_Back_Ads.onLoading();
        }
        adsCallingBack = call_Back_Ads;
        if (new Preference_Ads(activity).get_priority().equalsIgnoreCase("FB")) {
            if (z) {
                getInstance();

                if (isLoading || !isOnline(activity)) {
                    call_Back_Ads = adsCallingBack;
                    if (call_Back_Ads != null) {
                        call_Back_Ads.onLoading();
                    }
                } else if (fb_interstitial == null) {
                    if (new Preference_Ads(activity).getinterstitial_enable()) {
                        FB_Interstitial_Load(activity, call_Back_Ads);
                        return;
                    }
                    isLoading = false;

                }
                else {
                    call_Back_Ads.onLoading();
                }
            } else {
                if (!activity.isFinishing() && LoadingAds.isShowing()) {
                    LoadingAds.dismiss();
                }
                call_Back_Ads.onLoading();
            }
        } else if (new Preference_Ads(activity).get_priority().equalsIgnoreCase("AM")) {
            if (z) {
                getInstance();
                if (isLoading || !isOnline(activity)) {
                    call_Back_Ads = adsCallingBack;
                    if (call_Back_Ads != null) {
                        call_Back_Ads.onLoading();
                    }
                } else if (mInterstitialAd == null) {
                    AM_Interstitial_Load(activity, call_Back_Ads);

                }
                else {
                    call_Back_Ads.onLoading();
                }
            } else {
                if (!activity.isFinishing() && LoadingAds.isShowing()) {
                    LoadingAds.dismiss();
                }
                call_Back_Ads.onLoading();
            }
        } else if (new Preference_Ads(activity).get_priority().equalsIgnoreCase("")) {

            if (z) {
                getInstance();
                isLoading = false;

                    call_Back_Ads.onLoading();

            } else {
                if (!activity.isFinishing() && LoadingAds.isShowing()) {
                    LoadingAds.dismiss();
                }
                call_Back_Ads.onLoading();
            }

        }

    }

    public void FB_Interstitial_Load(Activity activity, final Call_Back_Ads call_Back_Ads) {
        isLoading = true;
        if (!activity.isFinishing() && !LoadingAds.isShowing()) {
            LoadingAds.show();
        }
        InterstitialAd interstitialAd = new InterstitialAd(activity, new Preference_Ads(activity).getFbInterstitialID());
        fb_interstitial = interstitialAd;

        interstitialAd.loadAd(interstitialAd.buildLoadAdConfig().withAdListener(new InterstitialAdListener() {
            public void onAdClicked(Ad ad) {
            }

            public void onInterstitialDisplayed(Ad ad) {
            }

            public void onLoggingImpression(Ad ad) {
            }

            public void onInterstitialDismissed(Ad ad) {
                Open_App_Manager.doNotDisplayAds = false;
                InterstitialAd unused = fb_interstitial = null;
                if (adsCallingBack != null) {
                    adsCallingBack.onAdsClose();
                }
            }

            public void onError(Ad ad, AdError adError) {
                AM_Interstitial_LoadAgain(activity, call_Back_Ads);
                InterstitialAd unused = fb_interstitial = null;
                isLoading = false;


            }

            public void onAdLoaded(Ad ad) {
                if (ProcessLifecycleOwner.get().getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                    Open_App_Manager.doNotDisplayAds = true;
                    if (!activity.isFinishing() && LoadingAds.isShowing()) {
                        LoadingAds.dismiss();
                    }
                    fb_interstitial.show();
                }
                isLoading = false;
            }
        }).build());
    }

    public void FB_Interstitial_LoadAgain(Activity activity, final Call_Back_Ads call_Back_Ads) {
        isLoading = true;
        if (!activity.isFinishing() && !LoadingAds.isShowing()) {
            LoadingAds.show();
        }
        InterstitialAd interstitialAd = new InterstitialAd(activity, new Preference_Ads(activity).getFbInterstitialID());
        fb_interstitial = interstitialAd;
        interstitialAd.loadAd(interstitialAd.buildLoadAdConfig().withAdListener(new InterstitialAdListener() {
            public void onAdClicked(Ad ad) {
            }

            public void onInterstitialDisplayed(Ad ad) {
            }

            public void onLoggingImpression(Ad ad) {
            }

            public void onInterstitialDismissed(Ad ad) {
                Open_App_Manager.doNotDisplayAds = false;
                InterstitialAd unused = fb_interstitial = null;
                if (adsCallingBack != null) {
                    adsCallingBack.onAdsClose();
                }
            }

            public void onError(Ad ad, AdError adError) {
                if (!activity.isFinishing() && LoadingAds.isShowing()) {
                    LoadingAds.dismiss();
                }
                if (adsCallingBack != null) {
                    adsCallingBack.onAdsFail();
                }
                InterstitialAd unused = fb_interstitial = null;
                isLoading = false;
            }

            public void onAdLoaded(Ad ad) {
                if (ProcessLifecycleOwner.get().getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                    Open_App_Manager.doNotDisplayAds = true;
                    if (!activity.isFinishing() && LoadingAds.isShowing()) {
                        LoadingAds.dismiss();
                    }
                    fb_interstitial.show();
                }
                isLoading = false;
            }
        }).build());
    }


    public void AM_Interstitial_Load(final Activity activity, final Call_Back_Ads call_Back_Ads) {
        if (new Preference_Ads(activity).getinterstitial_enable()) {
            if (!activity.isFinishing() && !LoadingAds.isShowing()) {
                LoadingAds.show();
            }
            isLoading = true;
            com.google.android.gms.ads.interstitial.InterstitialAd.load(activity, new Preference_Ads(activity).getAdmInterstitialID(), new AdRequest.Builder().build(), new InterstitialAdLoadCallback() {
                public void onAdLoaded(com.google.android.gms.ads.interstitial.InterstitialAd interstitialAd) {
                    if (!activity.isFinishing() && !LoadingAds.isShowing()) {
                        LoadingAds.show();
                    }
                    isLoading = false;
                    com.google.android.gms.ads.interstitial.InterstitialAd unused = mInterstitialAd = interstitialAd;
                    mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {

                        public void onAdFailedToShowFullScreenContent(com.google.android.gms.ads.AdError adError) {
                            super.onAdFailedToShowFullScreenContent(adError);
                            Open_App_Manager.doNotDisplayAds = false;
                        }

                        public void onAdDismissedFullScreenContent() {
                            super.onAdDismissedFullScreenContent();
                            Open_App_Manager.doNotDisplayAds = false;
                            com.google.android.gms.ads.interstitial.InterstitialAd unused = mInterstitialAd = null;
                            if (adsCallingBack != null) {
                                adsCallingBack.onAdsClose();
                            }

                        }
                    });
                    if (ProcessLifecycleOwner.get().getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                        Open_App_Manager.doNotDisplayAds = true;
                        if (!activity.isFinishing() && LoadingAds.isShowing()) {
                            LoadingAds.dismiss();
                        }
                        mInterstitialAd.show(activity);
                        mInterstitialAd = null;
                    }
                }

                public void onAdFailedToLoad(LoadAdError loadAdError) {
                    FB_Interstitial_LoadAgain(activity,call_Back_Ads);
                    isLoading = false;
                    com.google.android.gms.ads.interstitial.InterstitialAd unused = mInterstitialAd = null;

                }
            });
            return;
        }
        isLoading = false;

    }

    public void AM_Interstitial_LoadAgain(final Activity activity, final Call_Back_Ads call_Back_Ads) {
        if (new Preference_Ads(activity).getinterstitial_enable()) {
            if (!activity.isFinishing() && !LoadingAds.isShowing()) {
                LoadingAds.show();
            }
            isLoading = true;
            com.google.android.gms.ads.interstitial.InterstitialAd.load(activity, new Preference_Ads(activity).getAdmInterstitialID(), new AdRequest.Builder().build(), new InterstitialAdLoadCallback() {
                public void onAdLoaded(com.google.android.gms.ads.interstitial.InterstitialAd interstitialAd) {
                    if (!activity.isFinishing() && !LoadingAds.isShowing()) {
                        LoadingAds.show();
                    }
                    isLoading = false;
                    com.google.android.gms.ads.interstitial.InterstitialAd unused = mInterstitialAd = interstitialAd;
                    mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {

                        public void onAdFailedToShowFullScreenContent(com.google.android.gms.ads.AdError adError) {
                            super.onAdFailedToShowFullScreenContent(adError);
                            Open_App_Manager.doNotDisplayAds = false;
                        }

                        public void onAdDismissedFullScreenContent() {
                            super.onAdDismissedFullScreenContent();
                            Open_App_Manager.doNotDisplayAds = false;
                            com.google.android.gms.ads.interstitial.InterstitialAd unused = mInterstitialAd = null;
                            if (adsCallingBack != null) {
                                adsCallingBack.onAdsClose();
                            }

                        }
                    });
                    if (ProcessLifecycleOwner.get().getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                        Open_App_Manager.doNotDisplayAds = true;
                        if (!activity.isFinishing() && LoadingAds.isShowing()) {
                            LoadingAds.dismiss();
                        }
                        mInterstitialAd.show(activity);
                        mInterstitialAd = null;
                    }
                }

                public void onAdFailedToLoad(LoadAdError loadAdError) {
                    if (!activity.isFinishing() && LoadingAds.isShowing()) {
                        LoadingAds.dismiss();
                    }
                    if (adsCallingBack != null) {
                        adsCallingBack.onAdsFail();
                    }

                    isLoading = false;
                    com.google.android.gms.ads.interstitial.InterstitialAd unused = mInterstitialAd = null;

                }
            });
            return;
        }
        isLoading = false;

    }

    public void AmNativeLoadList(final Activity activity, final ViewGroup viewGroup, final boolean z) {
        if (isOnline(activity)) {
            if (new Preference_Ads(activity).getnative_ads_enable()) {
               if (new Preference_Ads(activity).get_priority().equalsIgnoreCase("FB")) {

                    try {
                        viewGroup.findViewById(R.id.rlLoading).setVisibility(View.VISIBLE);
                        com.facebook.ads.NativeAd nativeAd;
                        if(!z){
                            nativeAd = new com.facebook.ads.NativeAd(activity, new Preference_Ads(activity).getFbNativeBannerID());
                        }else{
                            nativeAd = new com.facebook.ads.NativeAd(activity, new Preference_Ads(activity).getFbNativeID());
                        }
                        NativeAdListener nativeAdListener = new NativeAdListener() {
                            @Override
                            public void onMediaDownloaded(Ad ad) {

                            }

                            @Override
                            public void onError(Ad ad, AdError adError) {
                                viewGroup.findViewById(R.id.rlLoading).setVisibility(View.GONE);
                                fbfailAfterLoadAdmobList(viewGroup, activity, z);
//                                AdMobFailAfterLoadFbList(activity, viewGroup, z);
                            }

                            @Override
                            public void onAdLoaded(Ad ad) {
                                if (nativeAd == null || nativeAd != ad) {
                                    return;
                                }
                                    viewGroup.findViewById(R.id.rlLoading).setVisibility(View.GONE);
                                    nativeAdLayout = (NativeAdLayout) activity.getLayoutInflater().inflate(R.layout.ad_fb_big_native_list, null);
                                    inflateAd1(nativeAd, nativeAdLayout, z);
                                    viewGroup.removeAllViews();
                                    viewGroup.addView(nativeAdLayout);

                            }

                            @Override
                            public void onAdClicked(Ad ad) {
                            }

                            @Override
                            public void onLoggingImpression(Ad ad) {

                            }
                        };

                        nativeAd.loadAd(nativeAd.buildLoadAdConfig().withAdListener(nativeAdListener).build());
                        return;

                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                }
               else if (new Preference_Ads(activity).get_priority().equalsIgnoreCase("AM")) {
                    viewGroup.findViewById(R.id.rlLoading).setVisibility(View.VISIBLE);
                   AdLoader.Builder builder = new AdLoader.Builder(activity, new Preference_Ads(activity).getAdmNativeID());

                    builder.forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                        public void onNativeAdLoaded(NativeAd nativeAd) {
                            viewGroup.findViewById(R.id.rlLoading).setVisibility(View.GONE);
//                            populateUnifiedNativeAdView(nativeAd, viewGroup.findViewById(R.id.nativeAdView), z);
                            FrameLayout frameLayout =viewGroup.findViewById(R.id.llNativeSmall);
                            nativeAdView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.ad_view_small_list, null);
                            populateUnifiedNativeAdView(nativeAd, nativeAdView, z);
                            frameLayout.removeAllViews();
                            frameLayout.addView(nativeAdView);
                        }
                    });
                    builder.withAdListener(new AdListener() {
                        public void onAdFailedToLoad(LoadAdError loadAdError) {
                            viewGroup.findViewById(R.id.rlLoading).setVisibility(View.GONE);
                            AdMobFailAfterLoadFbList(activity, viewGroup, z);
                            viewGroup.findViewById(R.id.nativeAdView).setVisibility(View.GONE);

                        }
                    }).build().loadAd(new AdRequest.Builder().build());
                    return;
                }

            }

        }
        viewGroup.findViewById(R.id.rlLoading).setVisibility(View.GONE);

    }



    public void AdMobFailAfterLoadFbList(Activity activity, ViewGroup viewGroup, boolean z) {

//        viewGroup.findViewById(R.id.rlLoading).setVisibility(View.VISIBLE);
        try {
            viewGroup.findViewById(R.id.rlLoading).setVisibility(View.VISIBLE);
            com.facebook.ads.NativeAd nativeAd;
            if(!z){
                nativeAd = new com.facebook.ads.NativeAd(activity, new Preference_Ads(activity).getFbNativeBannerID());
            }else{
                nativeAd = new com.facebook.ads.NativeAd(activity, new Preference_Ads(activity).getFbNativeID());
            }
            NativeAdListener nativeAdListener = new NativeAdListener() {
                @Override
                public void onMediaDownloaded(Ad ad) {
                }
                @Override
                public void onError(Ad ad, AdError adError) {
                    viewGroup.findViewById(R.id.rlLoading).setVisibility(View.GONE);
                }
                @Override
                public void onAdLoaded(Ad ad) {
                    if (nativeAd == null || nativeAd != ad) {
                        return;
                    }
                    viewGroup.findViewById(R.id.rlLoading).setVisibility(View.GONE);
                    nativeAdLayout = (NativeAdLayout) activity.getLayoutInflater().inflate(R.layout.ad_fb_big_native_list, null);
                    inflateAd1(nativeAd, nativeAdLayout, z);
                    viewGroup.removeAllViews();
                    viewGroup.addView(nativeAdLayout);
                }
                @Override
                public void onAdClicked(Ad ad) {
                }
                @Override
                public void onLoggingImpression(Ad ad) {
                }
            };
            nativeAd.loadAd(nativeAd.buildLoadAdConfig().withAdListener(nativeAdListener).build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public void loadBanner(Activity activity, final ViewGroup viewGroup, final ViewGroup viewGroup1) {
        if (isOnline(activity) && new Preference_Ads(activity).getbanner_enable()) {
            viewGroup1.findViewById(R.id.adSimmer1).setVisibility(View.VISIBLE);
            if (new Preference_Ads(activity).get_priority().equalsIgnoreCase("FB")) {

                final com.facebook.ads.AdView adView = new com.facebook.ads.AdView(activity,
                        new Preference_Ads(activity).getFbBannerID(), com.facebook.ads.AdSize.BANNER_HEIGHT_50);
                com.facebook.ads.AdListener adListener = new com.facebook.ads.AdListener() {
                    @Override
                    public void onError(Ad ad, AdError adError) {
                        FbBannerFailToLoadAdMob(activity,viewGroup,viewGroup1);
                        viewGroup1.findViewById(R.id.adSimmer1).setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAdLoaded(Ad ad) {
                        viewGroup1.findViewById(R.id.adSimmer1).setVisibility(View.GONE);
                        viewGroup.removeAllViews();
                        viewGroup.addView(adView);
                    }
                    @Override
                    public void onAdClicked(Ad ad) {

                    }
                    @Override
                    public void onLoggingImpression(Ad ad) {
                    }
                };

                adView.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build());

            } else if (new Preference_Ads(activity).get_priority().equalsIgnoreCase("AM")) {
                String admBannerID = new Preference_Ads(activity).getAdmBannerID();
                AdView adView = new AdView(activity);
                adView.setAdSize(AdSize.BANNER);
                adView.setAdUnitId(admBannerID);
                adView.setAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        viewGroup1.findViewById(R.id.adSimmer1).setVisibility(View.VISIBLE);
                        AdMobBannerFailToLoadFB(activity, viewGroup, viewGroup1);
                    }
                    @Override
                    public void onAdImpression() {
                        super.onAdImpression();
                    }

                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                        viewGroup1.findViewById(R.id.adSimmer1).setVisibility(View.GONE);
                    }
                });
                adView.loadAd(new AdRequest.Builder().build());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -1);
                viewGroup.removeAllViews();
                viewGroup.addView(adView, layoutParams);
            }
        }
    }

    private void AdMobBannerFailToLoadFB(Activity activity, ViewGroup viewGroup, ViewGroup viewGroup1) {

        final com.facebook.ads.AdView adView = new com.facebook.ads.AdView(activity, new Preference_Ads(activity).getFbBannerID(), com.facebook.ads.AdSize.BANNER_HEIGHT_50);
        com.facebook.ads.AdListener adListener = new com.facebook.ads.AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                viewGroup1.findViewById(R.id.adSimmer1).setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdLoaded(Ad ad) {
                viewGroup1.findViewById(R.id.adSimmer1).setVisibility(View.GONE);
                viewGroup.removeAllViews();
                viewGroup.addView(adView);
            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        };

        adView.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build());

    }

    private void FbBannerFailToLoadAdMob(Activity activity, ViewGroup viewGroup, ViewGroup viewGroup1) {
        String admBannerID = new Preference_Ads(activity).getAdmBannerID();
        AdView adView = new AdView(activity);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(admBannerID);

        adView.loadAd(new AdRequest.Builder().build());

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                viewGroup1.findViewById(R.id.adSimmer1).setVisibility(View.VISIBLE);
//                AdMobBannerFailToLoadFB(activity, viewGroup, viewGroup1);
            }
            @Override
            public void onAdImpression() {
                super.onAdImpression();
            }
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                viewGroup1.findViewById(R.id.adSimmer1).setVisibility(View.GONE);

            }
        });

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -1);
        viewGroup.removeAllViews();
        viewGroup.addView(adView, layoutParams);
    }

    public void fbfailAfterLoadAdmobList(final ViewGroup viewGroup, final Activity activity, final boolean z) {
        viewGroup.findViewById(R.id.rlLoading).setVisibility(View.VISIBLE);

        AdLoader.Builder builder = new AdLoader.Builder(activity, new Preference_Ads(activity).getAdmNativeID());

        builder.forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
            public void onNativeAdLoaded(NativeAd nativeAd) {
                viewGroup.findViewById(R.id.rlLoading).setVisibility(View.GONE);
//                populateUnifiedNativeAdView(nativeAd, viewGroup.findViewById(R.id.nativeAdView), z);
                FrameLayout frameLayout =viewGroup.findViewById(R.id.llNativeSmall);
                nativeAdView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.ad_view_small_list, null);
                populateUnifiedNativeAdView(nativeAd, nativeAdView, z);
                frameLayout.removeAllViews();
                frameLayout.addView(nativeAdView);
            }
        });
        builder.withAdListener(new AdListener() {
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                viewGroup.findViewById(R.id.rlLoading).setVisibility(View.GONE);
                viewGroup.findViewById(R.id.nativeAdView).setVisibility(View.GONE);
//                AdMobFailAfterLoadFbList(activity, viewGroup, z);

            }
        }).build().loadAd(new AdRequest.Builder().build());
    }

    public void AmNativeLoad(final Activity activity, final ViewGroup viewGroup, final boolean z) {
        if (isOnline(activity)) {
            if (new Preference_Ads(activity).getnative_ads_enable()) {
                if (new Preference_Ads(activity).get_priority().equalsIgnoreCase("FB")) {
                    try {
                        viewGroup.findViewById(R.id.rlLoading).setVisibility(View.VISIBLE);
                        com.facebook.ads.NativeAd nativeAd;
                        if(!z){
                                 nativeAd = new com.facebook.ads.NativeAd(activity, new Preference_Ads(activity).getFbNativeBannerID());
                        }else{
                                 nativeAd = new com.facebook.ads.NativeAd(activity, new Preference_Ads(activity).getFbNativeID());
                        }

                        NativeAdListener nativeAdListener = new NativeAdListener() {
                            @Override
                            public void onMediaDownloaded(Ad ad) {
                            }
                            @Override
                            public void onError(Ad ad, AdError adError) {
                                viewGroup.findViewById(R.id.rlLoading).setVisibility(View.GONE);
                                FbfailAfterLoadNativeAdmob(viewGroup, activity, z);
                            }

                            @Override
                            public void onAdLoaded(Ad ad) {

                                if (nativeAd == null || nativeAd != ad) {
                                    return;
                                }
                                viewGroup.findViewById(R.id.rlLoading).setVisibility(View.GONE);
                                if (!z) {
                                    nativeAdLayout = (NativeAdLayout) activity.getLayoutInflater().inflate(R.layout.ad_fb_medium_native, null);
                                    inflateAd1(nativeAd, nativeAdLayout, z);
                                    viewGroup.removeAllViews();
                                    viewGroup.addView(nativeAdLayout);
                                } else {
                                    nativeAdLayout = (NativeAdLayout) activity.getLayoutInflater().inflate(R.layout.ad_fb_big_native, null);
                                    inflateAd(nativeAd, nativeAdLayout, z);
                                    viewGroup.removeAllViews();
                                    viewGroup.addView(nativeAdLayout);
                                }
                            }

                            @Override
                            public void onAdClicked(Ad ad) {
                            }

                            @Override
                            public void onLoggingImpression(Ad ad) {
                            }
                        };
                        nativeAd.loadAd(nativeAd.buildLoadAdConfig().withAdListener(nativeAdListener).build());
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                }
                else if (new Preference_Ads(activity).get_priority().equalsIgnoreCase("AM")) {
                    viewGroup.findViewById(R.id.rlLoading).setVisibility(View.VISIBLE);
                    AdLoader.Builder builder;
                    if(!z){
                       builder = new AdLoader.Builder(activity, new Preference_Ads(activity).getAdmNativeID());
                    }
                    else{
                        builder = new AdLoader.Builder(activity, new Preference_Ads(activity).getAdmNativeVideoID());
                    }
;
                    builder.forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                        public void onNativeAdLoaded(NativeAd nativeAd) {
                            viewGroup.findViewById(R.id.rlLoading).setVisibility(View.GONE);
//                            populateUnifiedNativeAdView(nativeAd, viewGroup.findViewById(R.id.nativeAdView), z);
                            if (!z) {
                                FrameLayout frameLayout =viewGroup.findViewById(R.id.llNativeSmall);
                                nativeAdView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.ad_view_medium, null);
                                populateUnifiedNativeAdView(nativeAd, nativeAdView, z);
                                frameLayout.removeAllViews();
                                frameLayout.addView(nativeAdView);
                            } else {
                                FrameLayout frameLayout =viewGroup.findViewById(R.id.llNativeLarge);
                                nativeAdView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.ad_view_big, null);
                                populateUnifiedNativeAdView(nativeAd, nativeAdView, z);
                                frameLayout.removeAllViews();
                                frameLayout.addView(nativeAdView);
                            }
                        }
                    });
                    builder.withAdListener(new AdListener() {
                        public void onAdFailedToLoad(LoadAdError loadAdError) {
                            viewGroup.findViewById(R.id.rlLoading).setVisibility(View.GONE);
                            AdMobFailAfterLoadNativeFb(activity, viewGroup, z);
                            viewGroup.findViewById(R.id.nativeAdView).setVisibility(View.GONE);

                        }
                    }).build().loadAd(new AdRequest.Builder().build());
                    return;
                }

            }
        }
        viewGroup.findViewById(R.id.rlLoading).setVisibility(View.GONE);

    }
    public void AdMobFailAfterLoadNativeFb(Activity activity, ViewGroup viewGroup, boolean z) {

        viewGroup.findViewById(R.id.rlLoading).setVisibility(View.VISIBLE);
        try {
            com.facebook.ads.NativeAd nativeAd;
            if(!z){
                nativeAd = new com.facebook.ads.NativeAd(activity, new Preference_Ads(activity).getFbNativeBannerID());
            }else{
                nativeAd = new com.facebook.ads.NativeAd(activity, new Preference_Ads(activity).getFbNativeID());
            }
            NativeAdListener nativeAdListener = new NativeAdListener() {
                @Override
                public void onMediaDownloaded(Ad ad) {

                }

                @Override
                public void onError(Ad ad, AdError adError) {
                    viewGroup.findViewById(R.id.rlLoading).setVisibility(View.GONE);
//                    FbfailAfterLoadNativeAdmob(viewGroup, activity, z);
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    if (nativeAd == null || nativeAd != ad) {
                        return;
                    }
                    viewGroup.findViewById(R.id.rlLoading).setVisibility(View.GONE);
                    if (!z) {
                        nativeAdLayout = (NativeAdLayout) activity.getLayoutInflater().inflate(R.layout.ad_fb_medium_native, null);
                        inflateAd1(nativeAd, nativeAdLayout, z);
                        viewGroup.removeAllViews();
                        viewGroup.addView(nativeAdLayout);
                    } else {
                        nativeAdLayout = (NativeAdLayout) activity.getLayoutInflater().inflate(R.layout.ad_fb_big_native, null);
                        inflateAd(nativeAd, nativeAdLayout, z);
                        viewGroup.removeAllViews();
                        viewGroup.addView(nativeAdLayout);
                    }
                }

                @Override
                public void onAdClicked(Ad ad) {
                }
                @Override
                public void onLoggingImpression(Ad ad) {
                }

            };

            nativeAd.loadAd(nativeAd.buildLoadAdConfig().withAdListener(nativeAdListener).build());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void FbfailAfterLoadNativeAdmob(final ViewGroup viewGroup, final Activity activity, final boolean z) {
        viewGroup.findViewById(R.id.rlLoading).setVisibility(View.VISIBLE);
        AdLoader.Builder builder;
        if(!z){
            builder = new AdLoader.Builder(activity, new Preference_Ads(activity).getAdmNativeID());
        }
        else{
            builder = new AdLoader.Builder(activity, new Preference_Ads(activity).getAdmNativeVideoID());
        }
        builder.forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
            public void onNativeAdLoaded(NativeAd nativeAd) {
                viewGroup.findViewById(R.id.rlLoading).setVisibility(View.GONE);
//                populateUnifiedNativeAdView(nativeAd, viewGroup.findViewById(R.id.nativeAdView), z);
                if (!z) {
                    FrameLayout frameLayout =viewGroup.findViewById(R.id.llNativeSmall);
                    nativeAdView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.ad_view_medium, null);
                    populateUnifiedNativeAdView(nativeAd, nativeAdView, z);
                    frameLayout.removeAllViews();
                    frameLayout.addView(nativeAdView);
                } else {
                    FrameLayout frameLayout =viewGroup.findViewById(R.id.llNativeLarge);
                    nativeAdView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.ad_view_big, null);
                    populateUnifiedNativeAdView(nativeAd, nativeAdView, z);
                    frameLayout.removeAllViews();
                    frameLayout.addView(nativeAdView);
                }
            }
        });
        builder.withAdListener(new AdListener() {
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                viewGroup.findViewById(R.id.rlLoading).setVisibility(View.GONE);
//                AdMobFailAfterLoadNativeFb(activity, viewGroup, z);
                viewGroup.findViewById(R.id.nativeAdView).setVisibility(View.GONE);
            }
        }).build().loadAd(new AdRequest.Builder().build());
    }

    public void populateUnifiedNativeAdView(NativeAd nativeAd, NativeAdView nativeAdView, boolean z) {
        if (nativeAdView != null) {

            if (nativeAdView.findViewById(R.id.ad_media) != null) {
                com.google.android.gms.ads.nativead.MediaView mediaView = nativeAdView.findViewById(R.id.ad_media);
                nativeAdView.setMediaView(mediaView);
                mediaView.setVisibility(View.VISIBLE);
            }

            if(nativeAdView.findViewById(R.id.nativeAdView)!=null){
                nativeAdView.findViewById(R.id.nativeAdView).setVisibility(View.VISIBLE);
            }
            if (nativeAdView.findViewById(R.id.ad_headline) != null) {
                nativeAdView.setHeadlineView(nativeAdView.findViewById(R.id.ad_headline));
                View headlineView = nativeAdView.getHeadlineView();
                if (headlineView != null) {
                    ((TextView) headlineView).setText(nativeAd.getHeadline());
                }
            }
            if (nativeAdView.findViewById(R.id.ad_body) != null) {
                nativeAdView.setBodyView(nativeAdView.findViewById(R.id.ad_body));
                View bodyView = nativeAdView.getBodyView();
                if (nativeAd.getBody() == null) {
                    if (bodyView != null) {
                        bodyView.setVisibility(View.INVISIBLE);
                    }
                } else if (bodyView != null) {
                    bodyView.setVisibility(View.VISIBLE);
                    ((TextView) bodyView).setText(nativeAd.getBody());
                }
            }
            if (nativeAdView.findViewById(R.id.ad_call_to_action) != null) {
                nativeAdView.setCallToActionView(nativeAdView.findViewById(R.id.ad_call_to_action));
                View callToActionView = nativeAdView.getCallToActionView();
                if (nativeAd.getCallToAction() == null) {
                    if (callToActionView != null) {
                        callToActionView.setVisibility(View.INVISIBLE);
                    }
                } else if (callToActionView != null) {
                    callToActionView.setVisibility(View.VISIBLE);
                    ((Button) callToActionView).setText(nativeAd.getCallToAction());
                }
            }

            if(nativeAdView.findViewById(R.id.ad_text)!= null){
                nativeAdView.findViewById(R.id.ad_text).setVisibility(View.VISIBLE);
            }
            if (nativeAdView.findViewById(R.id.ad_app_icon) != null) {
                nativeAdView.setIconView(nativeAdView.findViewById(R.id.ad_app_icon));
                View iconView = nativeAdView.getIconView();
                if (nativeAd.getIcon() == null) {
                    if (iconView != null) {
                        iconView.setVisibility(View.GONE);
                    }
                } else if (iconView != null) {
                    iconView.setVisibility(View.VISIBLE);
                    ((ImageView) iconView).setImageDrawable(nativeAd.getIcon().getDrawable());
                }
            }
            if (nativeAdView.findViewById(R.id.ad_price) != null) {
                nativeAdView.setPriceView(nativeAdView.findViewById(R.id.ad_price));
                View priceView = nativeAdView.getPriceView();
                if (nativeAd.getPrice() == null) {
                    if (priceView != null) {
                        priceView.setVisibility(View.INVISIBLE);
                    }
                } else if (priceView != null) {
                    priceView.setVisibility(View.VISIBLE);
                    ((TextView) priceView).setText(nativeAd.getPrice());
                }
            }
            if (nativeAdView.findViewById(R.id.ad_stars) != null) {
                nativeAdView.setStarRatingView(nativeAdView.findViewById(R.id.ad_stars));
                View starRatingView = nativeAdView.getStarRatingView();
                if (nativeAd.getStarRating() == null) {
                    if (starRatingView != null) {
                        starRatingView.setVisibility(View.INVISIBLE);
                    }
                } else if (starRatingView != null) {
                    starRatingView.setVisibility(View.VISIBLE);
                    ((RatingBar) starRatingView).setRating(Float.parseFloat(String.valueOf(nativeAd.getStarRating().doubleValue())));
                }
            }
            if (nativeAdView.findViewById(R.id.ad_store) != null) {
                nativeAdView.setStoreView(nativeAdView.findViewById(R.id.ad_store));
                View storeView = nativeAdView.getStoreView();
                if (nativeAd.getStore() == null) {
                    if (storeView != null) {
                        storeView.setVisibility(View.INVISIBLE);
                    }
                } else if (storeView != null) {
                    storeView.setVisibility(View.VISIBLE);
                    ((TextView) storeView).setText(nativeAd.getStore());
                }
            }
            if (nativeAdView.findViewById(R.id.ad_advertiser) != null) {
                nativeAdView.setAdvertiserView(nativeAdView.findViewById(R.id.ad_advertiser));
                View advertiserView = nativeAdView.getAdvertiserView();
                if (nativeAd.getAdvertiser() == null) {
                    if (advertiserView != null) {
                        advertiserView.setVisibility(View.GONE);
                    }
                } else if (advertiserView != null) {
                    advertiserView.setVisibility(View.VISIBLE);
                    ((TextView) advertiserView).setText(nativeAd.getAdvertiser());
                }
            }
            if (nativeAdView.getHeadlineView() != null) {
                nativeAdView.setNativeAd(nativeAd);
            }
            nativeAdView.setVisibility(View.VISIBLE);
        }
    }

    public void inflateAd(com.facebook.ads.NativeAd nativeAd, View view, boolean z) {
        nativeAd.unregisterView();
        nativeAdLayout = view.findViewById(R.id.native_ad_container);
        MediaView mediaView = view.findViewById(R.id.native_ad_icon);
        TextView textView1 = (TextView) view.findViewById(R.id.sponsoredText);
        TextView textView3 = (TextView) view.findViewById(R.id.native_ad_sponsored_label);
        TextView textView4 = view.findViewById(R.id.native_ad_social_context);
        TextView textView5 = view.findViewById(R.id.native_ad_body);
        Button button = view.findViewById(R.id.native_ad_call_to_action);
        MediaView mediaView2 = view.findViewById(R.id.native_ad_media);
        nativeAdMedia = mediaView2;
        mediaView2.setListener(getMediaViewListener());
        if (!z) {
            nativeAdMedia.setVisibility(View.GONE);
        }

        LinearLayout adChoicesContainer = view.findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(adChoicesContainer.getContext(), nativeAd, nativeAdLayout);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);

        button.setText(nativeAd.getAdCallToAction());
        button.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        textView3.setText(nativeAd.getAdvertiserName());
        textView1.setText(nativeAd.getSponsoredTranslation());
        textView4.setText(nativeAd.getAdSocialContext());
        textView5.setText(nativeAd.getAdBodyText());
        List<View> arrayList = new ArrayList<>();
        arrayList.add(button);
        arrayList.add(textView3);
        arrayList.add(textView4);
        arrayList.add(textView1);
        arrayList.add(textView5);
        arrayList.add(nativeAdMedia);
        arrayList.add(mediaView);


        nativeAd.registerViewForInteraction(nativeAdLayout, nativeAdMedia, mediaView, arrayList);
    }

    private void inflateAd1(com.facebook.ads.NativeAd nativeAd, View view, boolean z) {
        nativeAd.unregisterView();
        nativeAdLayout = view.findViewById(R.id.native_ad_container);
        MediaView mediaView = view.findViewById(R.id.native_ad_icon);
        TextView textView1 = (TextView) view.findViewById(R.id.sponsoredText);
        TextView textView3 = (TextView) view.findViewById(R.id.native_ad_sponsored_label);
        TextView textView4 = view.findViewById(R.id.native_ad_social_context);
        Button button = view.findViewById(R.id.native_ad_call_to_action);
        nativeAdMedia = mediaView;
        mediaView.setListener(getMediaViewListener());
        LinearLayout adChoicesContainer = view.findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(adChoicesContainer.getContext(), nativeAd, nativeAdLayout);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);
        button.setText(nativeAd.getAdCallToAction());
        button.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        textView3.setText(nativeAd.getAdvertiserName());
        textView1.setText(nativeAd.getSponsoredTranslation());
        textView4.setText(nativeAd.getAdSocialContext());
        List<View> arrayList = new ArrayList<>();
        arrayList.add(button);
        arrayList.add(textView3);
        arrayList.add(textView4);
        arrayList.add(nativeAdMedia);
        arrayList.add(mediaView);
        arrayList.add(textView1);

        nativeAd.registerViewForInteraction(nativeAdLayout, nativeAdMedia, mediaView, arrayList);
    }

    public boolean isOnline(Activity activity) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }



    public void NoConnectionDialog(boolean z, Activity activity, Call_Back_Ads call_Back_Ads) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("No internet Connection");
            builder.setMessage("Please turn on internet connection to continue");
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            builder.setCancelable(false);

            builder.setNegativeButton("ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (isOnline(activity)) {
                        dialogInterface.dismiss();
                        loadOrShowAdmInterstial(z, activity, call_Back_Ads);
                        return;
                    } else {
                        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                        activity.startActivity(intent);
                    }
                    dialogInterface.dismiss();
//                    dialog.dismiss();
//                    builder.show();

                }
            });
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
