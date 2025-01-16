package com.hidefile.secure.folder.vault.dpss;

import android.content.Context;
import android.content.SharedPreferences;

import com.hidefile.secure.folder.vault.AdActivity.MyApplication;


public class PrefHandler {

    private  static SharedPreferences sharedPreferences;
    Context context;
    public static final String NAME = MyApplication.Companion.getAPPLICATION_ID() + ".preferences";
    public static final String PRIVACY_ACCEPTED = "privacy_accepted";

    public static boolean getBooleanPref(Context context, String key) {
        sharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, false);
    }

    public static void setBooleanPref(Context context, String key, boolean value) {
        sharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }




}
