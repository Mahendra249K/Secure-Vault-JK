package com.hidefile.secure.folder.vault.dashex;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.hidefile.secure.folder.vault.cluecanva.PreUpyogi;
import com.hidefile.secure.folder.vault.cluecanva.SupPref;

import java.util.Locale;

public class FoundationActivity extends AppCompatActivity {

    public Activity activity;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        }
//
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        activity = this;


        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            updateLanguage(FoundationActivity.this);
        }

        setStatusBarTextColor();
    }

    private void setStatusBarTextColor() {

        if (SupPref.getBooleanValue(this, SupPref.isDarkModeOn, false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // Set the status bar text color to dark
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            SupPref.setBooleanValue(this, SupPref.isDarkModeOn, false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().getDecorView().setSystemUiVisibility(0);
            }
        }
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(updateLanguage(newBase));
    }

    public Context updateLanguage(Context context) {
        String BhashaKod = PreUpyogi.getLanguageCode(context);
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();

        if (BhashaKod == null || BhashaKod.trim().isEmpty()) {
            try {
                BhashaKod = configuration.locale.getLanguage();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        if (BhashaKod != null && !BhashaKod.trim().isEmpty()) {
            BhashaKod = BhashaKod.toLowerCase();
            Locale locale = new Locale(BhashaKod);
//            Locale.setDefault(locale);

            configuration.setLocale(locale);
            configuration.setLayoutDirection(locale);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                return context.createConfigurationContext(configuration);
            } else {
                resources.updateConfiguration(configuration, resources.getDisplayMetrics());
            }
        }
        return context;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


    }

}