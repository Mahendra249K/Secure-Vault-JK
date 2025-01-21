package com.hidefile.secure.folder.vault.cluecanva;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class SupPref {
    public static String PREF_NAME = "vault";
    public static SharedPreferences AppPreference;
    public static String ICON_INDEX = "ICON_INDEX";
    public static String isFirstTime = "isFirstTime";
    public static String AccountName = "AccountName";
    public static final String VIBRATOR = "VIBRATOR";
    public static final String isDarkModeOn = "isDarkModeOn";
    public static final String interstitalShowOnlyfirst_time = "interstitalShowOnlyfirst_time";

    public static final String isRating = "isRating";

    public static final String ExitNativeAd = "ExitNativeAd";
    public static final String FirstTimeUser = "FirstTimeUser";

    public static final String appRatedPermently = "appRatedPermently";
    public static final String ExitNativeAdAfterAd = "ExitNativeAdAfterAd";

    public static final String appRated = "appRated";
    public static final String Prasth = "Prasth";
    public static final String IvFullScreenView = "IvFullScreenView";
    public static final String PhotoAndVideoAlbumSelection = "PhotoAndVideoAlbumSelection";
    public static final String NewAppAddition = "NewAppAddition";
    public static final String VidPlay = "VidPlay";
    public static final String MangamtiBhasaPasandKarvaniActivity = "MangamtiBhasaPasandKarvaniActivity";
    public static final String PhotoAndVideoSelection = "PhotoAndVideoSelection";
    public static final String ToolsForVideoFAdp = "ToolsForVideoFAdp";
    public static final String ToolsForImageFAdp = "ToolsForImageFAdp";
    public static final String ToolsForFileFAdp = "ToolsForFileFAdp";
    public static final String launguageBack = "launguageBack";
    public static final String REQUEST_CODE_IMAGE = "REQUEST_CODE_IMAGE";
    public static final String REQUEST_CODE_VIDEO = "REQUEST_CODE_VIDEO";
    public static final String PhotoSelection = "PhotoSelection";
    public static final String VideoSelection = "VideoSelection";
    public static final String Str11 = "Str11";

    public static final String MyWebView = "MyWebView";
    public static final String BordMain = "BordMain";

    public static final String SelfieImageView = "SelfieImageView";


    public static final String isFromPin = "isFromPin";
    public static final String isRatting = "isRatting";
    public static final String pref_AppData = "pref_AppData";
    public static final String Hidden_selfie = "Hidden_selfie";
    public static final String isStorageServicePrm = "isstorageserviceprm";


    public static void putValue(Context context, String key, String value) {
        AppPreference = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = AppPreference.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void putString(Context context, String key, Boolean defaultValue) {
        AppPreference = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = AppPreference.edit();
//        editor.putString(key,defaultValue);
        editor.putBoolean(key, defaultValue);
        editor.apply();
    }

    public static boolean getString(Context context, String key, Boolean defaultValue) {
        AppPreference = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return AppPreference.getBoolean(key, defaultValue);
    }


    public static void saveToUserDefaults(Context context, String key, String value) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static int getIntValue(Context context, String key, int defaultValue) {
        AppPreference = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return AppPreference.getInt(key, defaultValue);
    }

    public static String getHideUri(Context context) {
        AppPreference = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String str = AppPreference.getString(isStorageServicePrm, "");
        return str;
    }

    public static String getburyUri(Context context) {
        AppPreference = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return AppPreference.getString(isStorageServicePrm, "");
    }

    public static String getValue(Context context, String key, String defaultValue) {
        AppPreference = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return AppPreference.getString(key, defaultValue);
    }

    public static boolean getBooleanValue(Context context, String key, boolean defaultValue) {
        AppPreference = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return AppPreference.getBoolean(key, defaultValue);
    }

    public static void setBooleanValue(Context context, String key, boolean value) {
        AppPreference = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = AppPreference.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static void setIntValue(Context context, String key, int value) {
        AppPreference = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = AppPreference.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static String getFromUserDefaults(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return preferences.getString(key, "");
    }

    public static LinkedHashMap<String, RecentList> getAppDataMap(Context context) {
        String jsonStroing = getFromUserDefaults(context, pref_AppData);
        Gson gson = new Gson();
        Type type = new TypeToken<LinkedHashMap<String, RecentList>>() {
        }.getType();
        return gson.fromJson(jsonStroing, type);
    }

    public static ArrayList<RecentList> getAppData(Context context) {
        ArrayList<RecentList> recentListitems = new ArrayList<>();
        String jsonStroing = getFromUserDefaults(context, pref_AppData);
        Gson gson = new Gson();
        Type type = new TypeToken<LinkedHashMap<String, RecentList>>() {
        }.getType();
        if (!jsonStroing.isEmpty()) {
            LinkedHashMap<String, RecentList> fromJson = gson.fromJson(jsonStroing, type);

            if (fromJson.size() > 0) {
                recentListitems = new ArrayList<>(fromJson.values());
            }
        }
        return recentListitems;
    }

    public static void saveAppData(Context context, LinkedHashMap<String, RecentList> hMap) {
        Gson gson = new Gson();
        Type type = new TypeToken<LinkedHashMap<String, RecentList>>() {
        }.getType();
        String json = gson.toJson(hMap, type);
        saveToUserDefaults(context, pref_AppData, json);
        System.out.println(json);
    }



    public static void putHideUri(Context context, String value) {
        AppPreference = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = AppPreference.edit();
        editor.putString(isStorageServicePrm, value);
        editor.apply();
    }
}
