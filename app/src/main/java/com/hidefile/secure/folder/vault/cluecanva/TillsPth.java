package com.hidefile.secure.folder.vault.cluecanva;

import static org.apache.commons.io.FilenameUtils.getExtension;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hidefile.secure.folder.vault.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Formatter;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class TillsPth {

public static final String cameraImage = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "DCIM" + File.separator + "Camera";
    public static final String restorePathImage = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/VaultSecure/RebuildItem/";
    public static final String restorePathVideo =Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/VaultSecure/RebuildVideo/";
    public static final String restorePathFiles = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/VaultSecure/RebuildFiles/";
    public static final String hideImage = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/.StayHide";
    public static final String nohideImage = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/.StayHide/" + ".nomedia/" + ".Photo/";
    public static final String nohideVideo = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/.StayHide/" + ".nomedia/" + ".Video/";
    public static final String nohideFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/.StayHide/" + ".nomedia/" + ".Fiels/";
    public static final String worngPwd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/.StayHide/" + ".worngPwd/";
    public static final String restorePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SecureFolder/";
    public static final String hideVideo = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/.StayHide";
    public static final String hideFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/.StayHide";
    public static final String restoredMedia = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/.StayHide/" + ".restoredMedia/";

    public static boolean isNetworkAvailable(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            if (activeNetworkInfo.isConnected()) {
                return true;
            } else {
                EntryAux.showToast(mContext, R.string.internet_message);
                return false;
            }
        } else {
            EntryAux.showToast(mContext, R.string.internet_message);
            return false;
        }
    }

    public static byte[] getByte(String path) {
        File file = new File(path);
        int size = (int) file.length();

        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;

    }

    public static int dpToPx(Context c, float dipValue) {
        DisplayMetrics metrics = c.getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }

    public static int getImageResize(Context context, RecyclerView recyclerView) {
        int mImageResize;
        RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();
        int spanCount = ((GridLayoutManager) lm).getSpanCount();
        int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        mImageResize = screenWidth / spanCount;
        return mImageResize;
    }

    public static byte[] generateKey(String password) throws Exception {
        byte[] keyStart = password.getBytes(StandardCharsets.UTF_8);

        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
        sr.setSeed(keyStart);
        kgen.init(128, sr);
        SecretKey skey = kgen.generateKey();
        return skey.getEncoded();
    }

    public static byte[] encodeFile(byte[] key, byte[] fileData) throws Exception {

        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(fileData);

        return encrypted;
    }

    public static byte[] decodeFile(byte[] key, byte[] fileData) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);

        byte[] decrypted = cipher.doFinal(fileData);

        return decrypted;
    }

    public static String stringForTime(long timeMs) {
        Formatter mFormatter = new Formatter();
        try {
            long totalSeconds = timeMs / 1000;

            long seconds = totalSeconds % 60;
            long minutes = (totalSeconds / 60) % 60;
            long hours = totalSeconds / 3600;
            if (hours > 0) {
                return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
            } else {
                return mFormatter.format("%02d:%02d", minutes, seconds).toString();
            }
        } catch (Exception ex) {
            return mFormatter.format("%02d:%02d", 0, 0).toString();
        }
    }

    public static int StatusBarLightMode(Activity activity) {
        int result = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (MIUISetStatusBarLightMode(activity, true)) {
                result = 1;
            } else if (FlymeSetStatusBarLightMode(activity.getWindow(), true)) {
                result = 2;
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                result = 3;
            }
        }
        return result;
    }

    public static boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    public static boolean MIUISetStatusBarLightMode(Activity activity, boolean dark) {
        boolean result = false;
        Window window = activity.getWindow();
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);
                }
                result = true;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (dark) {
                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    } else {
                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static boolean isImage(String type) {
        if (type != null
                && (type.equals("jpg") || type.equals("gif")
                || type.equals("png") || type.equals("jpeg")
                || type.equals("bmp") || type.equals("wbmp")
                || type.equals("ico") || type.equals("jpe"))) {
            return true;
        }
        return false;
    }

    public static boolean isVideo(String type) {
        if (type != null
                && (type.equals("mp4") || type.equals("mov")
                || type.equals("wmv") || type.equals("avi")
                || type.equals("avchd") || type.equals("mkv")
                || type.equals("webm") || type.equals("flv"))) {
            return true;
        }
        return false;
    }

    public static String getMimeType(String name) {

        String extension = getExtension(name);

        if (extension.length() > 0)
            return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);

        return "application/octet-stream";
    }

    public static String getFileType(String fileName) {
        if (fileName != null) {
            int typeIndex = fileName.lastIndexOf(".");
            if (typeIndex != -1) {
                String fileType = fileName.substring(typeIndex + 1).toLowerCase();
                return fileType;
            }
        }
        return "";
    }
}
