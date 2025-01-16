package com.hidefile.secure.folder.vault.cluecanva;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.FontRes;
import androidx.annotation.StringRes;
import androidx.core.content.res.ResourcesCompat;

import com.hidefile.secure.folder.vault.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
public class EntryAux {
    private static final String TAG = EntryAux.class.getSimpleName();
    private static final SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US);
    private static ProgressDialog progressDialog;

    public static void showToast(Context context, @StringRes int msg) {
        if (context != null) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    }

    public static void showToast(Context context, String msg) {
        if (context != null && msg != null && !msg.trim().isEmpty()) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    }

    public static void showProgressDialog(Context context, int resID) {
        try {
            if (context != null) {
                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(context, R.style.progressDialog_style);
                    progressDialog.setMessage(context.getString(resID));
                    progressDialog.setCancelable(false);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setIndeterminate(false);
                    progressDialog.show();
                } else {
                    progressDialog.setMessage(context.getString(resID));
                    progressDialog.setCancelable(false);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setIndeterminate(false);

                    if (!progressDialog.isShowing()) {
                        progressDialog.show();
                    }
                }
            }
        } catch (Throwable throwable) {
            Log.i(TAG, "showProgressBar: throwable :- " + throwable.getMessage());
        }
    }

    public static void hideProgressDialog() {
        try {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {
            Log.i(TAG, "hideProgressDialog: error :- " + e.getMessage());
        }
        progressDialog = null;
    }

    public static boolean isProgressLoading() {
        if (progressDialog != null) {
            return progressDialog.isShowing();
        }
        return false;
    }

    public static String getTimeStamp() {
        return timeStampFormat.format(new Date());
    }


    public static Spannable getSpannableForTwoStrings(Context mContext, String firstString, String secondString, @FontRes int firstFontFamily, @FontRes int secondFontFamily) {

        String wholeString = firstString + " " + secondString;
        int mainStart = 0;
        int mainEnd = wholeString.length();
        int firstStartIndex = 0;
        int firstEndIndex = wholeString.length() - secondString.length();
        int secondStartIndex = firstEndIndex;
        int secondEndIndex = mainEnd;
        Spannable mainSpannableString = new SpannableString(wholeString);
        mainSpannableString.setSpan(new TooFoot("", ResourcesCompat.getFont(mContext, firstFontFamily)), firstStartIndex, firstEndIndex, 0);
        mainSpannableString.setSpan(new TooFoot("", ResourcesCompat.getFont(mContext, secondFontFamily)), secondStartIndex, secondEndIndex, 0);
        return mainSpannableString;
    }

}
