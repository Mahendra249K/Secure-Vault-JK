package com.hidefile.secure.folder.vault.AdActivity;

import android.content.Context;
import android.content.SharedPreferences;

import com.facebook.appevents.AppEventsConstants;

public class Preference_Ads {
    public SharedPreferences.Editor editor;
    public SharedPreferences prefs;

    public Preference_Ads(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ADS_PREFS", 0);
        this.prefs = sharedPreferences;
        this.editor = sharedPreferences.edit();
    }

    public void setAdmBannerID(String str) {
        this.editor.putString("AM_BANNER_ID", str).commit();
    }

    public String getAdmBannerID() {
        return this.prefs.getString("AM_BANNER_ID", "");
    }

    public void setAdmInterstitialID(String str) {
        this.editor.putString("AM_INTERSTITIAL_ID", str).commit();
    }

    public String getAdmInterstitialID() {
        return this.prefs.getString("AM_INTERSTITIAL_ID", "");
    }

    public void setAdmNativeID(String str) {
        this.editor.putString("AM_NATIVE_ID", str).commit();
    }

    public String getAdmNativeID() {
        return this.prefs.getString("AM_NATIVE_ID", "");
    }

    public void setAppOpenSplashEnable(boolean z) {
        this.editor.putBoolean("APP_OPEN_ENABLE", z).commit();
    }

    public boolean getAppOpenSplashEnable() {
        return this.prefs.getBoolean("APP_OPEN_ENABLE", false);
    }

    public void setAdmNativeVideoID(String str) {
        this.editor.putString("AM_NATIVE_VIDEO_ID", str).commit();
    }

    public String getAdmNativeVideoID() {
        return this.prefs.getString("AM_NATIVE_VIDEO_ID", "");
    }


    public void setFbNativeBannerID(String str) {
        this.editor.putString("FB_NATIVE_BANNER_ID", str).commit();
    }

    public String getFbNativeBannerID() {
        return this.prefs.getString("FB_NATIVE_BANNER_ID", "");
    }

    public void setAdmAppOpenID(String str) {
        this.editor.putString("APP_OPEN_ID", str).commit();
    }



    public String getAdmAppOpenID() {
        return this.prefs.getString("APP_OPEN_ID", "");
    }

    public void setbanner_enable(boolean z) {
        this.editor.putBoolean("BANNER_ENABLE", z).commit();
    }

    public boolean getbanner_enable() {
        return this.prefs.getBoolean("BANNER_ENABLE", false);
    }

    public void set_Custom_Inter(boolean z) {
        this.editor.putBoolean("CUSTOM_INTERSTITIAl_ENABLE", z).commit();
    }

    public boolean get_Custom_Inter() {
        return this.prefs.getBoolean("CUSTOM_INTERSTITIAl_ENABLE", false);
    }

    public void setFbBannerID(String str) {
        this.editor.putString("FB_BANNER_ID", str).commit();
    }

    public String getFbBannerID() {
        return this.prefs.getString("FB_BANNER_ID", "");
    }

    public void setFbInterstitialID(String str) {
        this.editor.putString("FB_INTERSTITIAL_ID", str).commit();
    }

    public String getFbInterstitialID() {
        return this.prefs.getString("FB_INTERSTITIAL_ID", "");
    }

    public void setFbNativeID(String str) {
        this.editor.putString("FB_NATIVE_ID", str).commit();
    }

    public String getFbNativeID() {
        return this.prefs.getString("FB_NATIVE_ID", "");
    }

    public void setInterstitialBackPressClickGap(String str) {
        this.editor.putString("INTERSTITIAL_BACKPRESS_CLICK_MODULE", str).commit();
    }

    public String getInterstitialBackPressClickGap() {
        return this.prefs.getString("INTERSTITIAL_BACKPRESS_CLICK_MODULE", AppEventsConstants.EVENT_PARAM_VALUE_NO);
    }

    public void setInterstitialClickGap(String str) {
        this.editor.putString("INTERSTITIAL_CLICK_MODULE", str).commit();
    }

    public String getInterstitialClickGap() {
        return this.prefs.getString("INTERSTITIAL_CLICK_MODULE", AppEventsConstants.EVENT_PARAM_VALUE_NO);
    }

    public void setinterstitial_enable(boolean z) {
        this.editor.putBoolean("INTERSTITIAL_ENABLE", z).commit();
    }

    public boolean getinterstitial_enable() {
        return this.prefs.getBoolean("INTERSTITIAL_ENABLE", false);
    }

    public void setInterstitialEnableBackPress(boolean z) {
        this.editor.putBoolean("INTERSTITIAL_ENABLE_BACKPRESS", z).commit();
    }

    public boolean getInterstitialEnableBackPress() {
        return this.prefs.getBoolean("INTERSTITIAL_ENABLE_BACKPRESS", false);
    }

    public void setInterstitialGapEnable(boolean z) {
        this.editor.putBoolean("INTERSTITIAL_GAP_ENABLE", z).commit();
    }

    public boolean getInterstitialGapEnable() {
        return this.prefs.getBoolean("INTERSTITIAL_GAP_ENABLE", false);
    }

    public void setQurekaEnable(boolean z) {
        this.editor.putBoolean("INTERSTITIAL_QUREKA_ENABLE", z).commit();
    }

    public boolean getQurekaEnable() {
        return this.prefs.getBoolean("INTERSTITIAL_QUREKA_ENABLE", false);
    }

    public void set_qureka_time(String str) {
        this.editor.putString("INTERSTITIAL_QUREKA_TIME", str).commit();
    }

    public String get_qureka_time() {
        return this.prefs.getString("INTERSTITIAL_QUREKA_TIME", "");
    }

    public void setQureka_link(String str) {
        this.editor.putString("LINK_QUREKA", str).commit();
    }

    public String getQureka_link() {
        return this.prefs.getString("LINK_QUREKA", "");
    }

    public void setnative_ads_enable(boolean z) {
        this.editor.putBoolean("NATIVE_ENABLE", z).commit();
    }

    public boolean getnative_ads_enable() {
        return this.prefs.getBoolean("NATIVE_ENABLE", false);
    }

    public void setnative_qureka_enable(boolean z) {
        this.editor.putBoolean("NATIVE_QUREKA_ENABLE", z).commit();
    }

    public boolean getnative_qureka_enable() {
        return this.prefs.getBoolean("NATIVE_QUREKA_ENABLE", false);
    }

    public void set_new_app_version(String str) {
        this.editor.putString("NEW_APP_VERSION_CHECK", str).commit();
    }

    public String get_new_app_version() {
        return this.prefs.getString("NEW_APP_VERSION_CHECK", "");
    }

    public void set_new_link(String str) {
        this.editor.putString("NEW_LINK", str).commit();
    }

    public String get_new_link() {
        return this.prefs.getString("NEW_LINK", "");
    }

    public void set_priority(String str) {
        this.editor.putString("PRIORITY", str).commit();
    }

    public String get_priority() {
        return this.prefs.getString("PRIORITY", "");
    }

    public void set_Update_dialog_close_enable(boolean z) {
        this.editor.putBoolean("UPDATE_DIALOG_CLOSE_ENABLE", z).commit();
    }

    public boolean get_Update_dialog_close_enable() {
        return this.prefs.getBoolean("UPDATE_DIALOG_CLOSE_ENABLE", false);
    }

    public void setIsFirstTime(boolean z) {
        this.editor.putBoolean("isFirstTime", z).commit();
    }

    public boolean getIsFirstTime() {
        return this.prefs.getBoolean("isFirstTime", false);
    }

    public void setonesignal_app_id(String str) {
        this.editor.putString("ONESIGNAL_APP_ID", str).commit();
    }

    public String getonesignal_app_id() {
        return this.prefs.getString("ONESIGNAL_APP_ID", "");
    }

    public void set_extra_screen_enable(boolean z) {
        this.editor.putBoolean("EXTRA_SCREEN_ENABLE", z).commit();
    }

    public boolean get_extra_screen_enable() {
        return this.prefs.getBoolean("EXTRA_SCREEN_ENABLE", false);
    }

    public void setNativeListGap(long j) {
        if (j == 0) {
            this.editor.putLong("NATIVE_LIST_GAP", 1).commit();
        } else {
            this.editor.putLong("NATIVE_LIST_GAP", j).commit();
        }
    }

    public long getNativeListGap() {
        return this.prefs.getLong("NATIVE_LIST_GAP", 2);
    }

    public void set_newapi_on_off(String str) {
        this.editor.putString("NEW_API_ON_OFF", str).commit();
    }

    public String get_newapi_on_off() {
        return this.prefs.getString("NEW_API_ON_OFF", "");
    }

    public void set_livetv_ON_OFF(String str) {
        this.editor.putString("LIVETV_ON_OFF", str).commit();
    }

    public String get_livetv_ON_OFF() {
        return this.prefs.getString("LIVETV_ON_OFF", "");
    }

    public void setNativeListEnable(boolean z) {
        this.editor.putBoolean("NATIVE_LIST_ENABLE", z).commit();
    }

    public boolean getNativeListEnable() {
        return this.prefs.getBoolean("NATIVE_LIST_ENABLE", false);
    }

    public void setPrivacy_link(String str) {
        this.editor.putString("LINK_PRIVACY_POLICY", str).commit();
    }

    public String getPrivacy_link() {
        return this.prefs.getString("LINK_PRIVACY_POLICY", "");
    }
}
