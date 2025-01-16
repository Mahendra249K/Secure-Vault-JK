package com.hidefile.secure.folder.vault.AdActivity;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {

    public static final String PREF_NAME="MY_APPLICATION";
    public static final String RATING_STATUS="status";
    public static final String CATEGORY_STATUS="status";


    public static final String LANGUAGE = "language";

    public static  boolean AppOpenShow = false;


    SharedPreferences sharedPreferences;

    private static String SHARED_PREFS_FILE_NAME = "SECURE_FOLDER";

    public static final String LANGUAGENAME = "languagename";


    SharedPreferences.Editor editor;

    Context context;

    public SharedPref(Context context) {
        this.context = context;

        sharedPreferences=context.getSharedPreferences(PREF_NAME,context.MODE_PRIVATE);

        editor= sharedPreferences.edit();

    }
    public boolean getRatingstatus(){
        return sharedPreferences.getBoolean(RATING_STATUS,false);
    }
    public boolean getCategoryStatus(){
        return sharedPreferences.getBoolean(CATEGORY_STATUS,false);
    }

    public static void save(Context context, String key, String value) {
        getPrefs(context).edit().putString(key, value).commit();
    }

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE);
    }


    public static String getString(Context context, String key) {
        return getPrefs(context).getString(key, "");
    }

    public static boolean contain(Context context, String key) {
        return getPrefs(context).contains(key);
    }

    public static void save(Context context, String key, boolean value) {
        getPrefs(context).edit().putBoolean(key, value).commit();
    }


    public String getLanguageCode() {
        return sharedPreferences.getString("pref_selected_language", "");
    }


}
