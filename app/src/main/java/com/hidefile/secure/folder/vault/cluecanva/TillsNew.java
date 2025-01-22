package com.hidefile.secure.folder.vault.cluecanva;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import androidx.core.content.ContextCompat;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class TillsNew {

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;
    public static String RECYCLE_BIN_PATH = Environment.getExternalStorageDirectory() + "/.ESFileExplorer/.RecycleBin/";
    public static String RECYCLE_BIN_PATH1 = Environment.getExternalStorageDirectory() + "/.ESFileExplorer/.RecycleBin";
    Context mContext;

    public TillsNew() {
    }

    public TillsNew(Context mContext) {
        this.mContext = mContext;
    }

    public static boolean hasFeature(Context context, String feature) {
        return context.getPackageManager().hasSystemFeature(feature);
    }

    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    public static boolean hasJellyBeanMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
    }

    public static boolean hasJellyBeanMR2() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2;
    }

    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean hasLollipopMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1;
    }

    public static boolean hasMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static boolean hasNougat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }

    public static boolean hasNougatMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1;
    }

    public static boolean hasOreo() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }

    public static boolean hasOreoMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1;
    }

    public static boolean hasPie() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P;
    }

    public static boolean hasWiFi(Context context) {
        return hasFeature(context, PackageManager.FEATURE_WIFI);
    }

    public static String formateSize(long size) {
        if (size <= 0)
            return "0";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }


    public static String convertTimeFromUnixTimeStamp(String date) {

        DateFormat inputFormat = new SimpleDateFormat("EE MMM dd HH:mm:ss zz yyy");
        Date d = null;
        try {
            d = inputFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat outputFormat = new SimpleDateFormat("EEE, dd MMM, yyyy h:mm a");
        if (d != null) {
            return outputFormat.format(d);
        } else {
            return null;
        }


    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String[] getExtSdCardPathsForActivity(Context context) {
        List<String> paths = new ArrayList<String>();
        for (File file : context.getExternalFilesDirs("external")) {
            if (file != null) {
                int index = file.getAbsolutePath().lastIndexOf("/Android/data");
                if (index < 0) {
                    Log.w("TillsFl", "Unexpected external manager dir: " + file.getAbsolutePath());
                } else {
                    String path = file.getAbsolutePath().substring(0, index);
                    try {
                        path = new File(path).getCanonicalPath();
                    } catch (IOException e) {
                        // Keep non-canonical path.
                    }
                    paths.add(path);
                }
            }
        }
        if (paths.isEmpty()) paths.add("/storage/sdcard1");
        return paths.toArray(new String[0]);
    }

    public static final String makeShortTimeString(final Context context, long secs) {
        int totalSeconds = (int) (secs);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        return hours > 0 ? String.format("%02d:%02d:%02d", hours, minutes, seconds) : String.format("%02d:%02d", minutes, seconds);
    }

    public static boolean externalMemoryAvailable(Activity context) {
        File[] storages = ContextCompat.getExternalFilesDirs(context, null);
        if (storages.length > 1 && storages[0] != null && storages[1] != null) {
            Log.e("TillsPth", "storages: " + storages.toString());
            return true;
        } else
            return false;
    }

    public static long getThisMonth() {
        long timeNow = System.currentTimeMillis();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    public static long getSixMonth() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -6);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    public static long getTodayRange() {
        long timeNow = System.currentTimeMillis();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    public static long getTimestamp(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    public static long getThreeDays() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -3);
        return calendar.getTimeInMillis();
    }

    public static long getSevenDays() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        return calendar.getTimeInMillis();
    }

    public static String getDate(long milliSeconds) {
        DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = inputFormat.format(new Date(milliSeconds));
        return dateString;
    }

    public static long getAfterTenDays(long milliSeconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 10);

        DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = inputFormat.format(new Date(calendar.getTimeInMillis()));
        return calendar.getTimeInMillis();
    }

    public static String getFilenameFromPath(String path) {
        return path.substring(path.lastIndexOf("/") + 1);
    }

    public static String getParentPath(String path) {
        if (path.endsWith(getFilenameFromPath(path))) {
            return path.substring(0, path.length() - getFilenameFromPath(path).length());
        }
        return "";
    }

    public static String setDuration(long timeMs) {
        String hourString = "";
        String minuteString = "00";
        String secondString = "00";
        int hours = (int) (timeMs / 3600000);
        int minutes = ((int) (timeMs % 3600000)) / 60000;
        int seconds = (int) (((timeMs % 3600000) % 60000) / 1000);

        if (hours < 10 && hours != 0) {
            hourString = "0" + hours + ":";
        } else if (hours >= 10) {
            hourString = hours + ":";
        }

        if (minutes < 10) {
            minuteString = "0" + minutes;
        } else {
            minuteString = minutes + "";
        }

        if (seconds < 10) {
            secondString = "0" + seconds;
        } else {
            secondString = seconds + "";
        }

        return hourString + minuteString + ":" + secondString;
    }

    public static String getDateWithTime(long time) {
        DateFormat inputFormat = new SimpleDateFormat("dd MMM");
        DateFormat inputFormatWithYear = new SimpleDateFormat("dd MMM yyyy");

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        int year = cal.get(Calendar.YEAR);

        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }

        // TODO: localize
        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else if (diff < 5 * DAY_MILLIS) {
            return diff / DAY_MILLIS + " days ago";
        } else if (year == currentYear) {
            return inputFormat.format(new Date(time));
        } else {
            return inputFormatWithYear.format(new Date(time));
        }
    }

    public static String bytesToHuman(long size) {
        long Kb = 1 * 1024;
        long Mb = Kb * 1024;
        long Gb = Mb * 1024;
        long Tb = Gb * 1024;
        long Pb = Tb * 1024;
        long Eb = Pb * 1024;
        if (size < Kb) return floatForm(size) + " byte";
        if (size >= Kb && size < Mb) return floatForm((double) size / Kb) + " KB";
        if (size >= Mb && size < Gb) return floatForm((double) size / Mb) + " MB";
        if (size >= Gb && size < Tb) return floatForm((double) size / Gb) + " GB";
        if (size >= Tb && size < Pb) return floatForm((double) size / Tb) + " TB";
        if (size >= Pb && size < Eb) return floatForm((double) size / Pb) + " PB";
        if (size >= Eb) return floatForm((double) size / Eb) + " Eb";
        return "???";
    }

    public static String getSize(long size) {
        long n = 1000;
        String s = "";
        double kb = size / n;
        double mb = kb / n;
        double gb = mb / n;
        double tb = gb / n;
        if (size < n) {
            s = size + " Bytes";
        } else if (size >= n && size < (n * n)) {
            s = String.format("%.2f", kb) + " KB";
        } else if (size >= (n * n) && size < (n * n * n)) {
            s = String.format("%.2f", mb) + " MB";
        } else if (size >= (n * n * n) && size < (n * n * n * n)) {
            s = String.format("%.2f", gb) + " GB";
        } else if (size >= (n * n * n * n)) {
            s = String.format("%.2f", tb) + " TB";
        }
        return s;
    }

    public static String floatForm(double d) {
        return new DecimalFormat("#.###").format(d);
    }

    public static boolean appInstalledOrNot(Context context, String uri) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static Unit convertFileSize(long size) {
        /**
         *
         * several hundred Bytes
         * several hundred KB
         * What time MB
         * A few o'clock MB
         * Dozens of MB
         * several hundred MB
         * What time GB
         * A dozen GB
         */
        String count = "";
        String unit = "";
        if (size < 1024) {
            //Less than 1K
            count = "" + size;
            unit = "Bytes";
        } else if (size < 1024 * 1024) {
            //Less than 1M
            count = "" + (size / 1024);
            unit = "KB";
        } else if (size < 1024 * 1024 * 10) {
            //Less than 10M
            count = String.format("%.2f", size / (1024.0 * 1024.0));
            unit = "MB";
        } else if (size < 1024 * 1024 * 100) {
            // Less than 100M
            count = String.format("%.1f", size / (1024.0 * 1024.0));
            unit = "MB";
        } else if (size < 1024 * 1024 * 1024) {
            // Less than 1GB
            count = "" + (size / 1024 / 1024);
            unit = "MB";
        } else {
            // Less than 1GB
            count = String.format("%.1f", size / (1024.0 * 1024.0 * 1024));
            unit = "GB";
        }

        return new Unit(count, unit);
    }

//    public static String getDateWithTime(long milliSeconds) {
//        DateFormat inputFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
//        String dateString = inputFormat.format(new Date(milliSeconds));
//        return dateString;
//    }

    public Bitmap convertDrawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public String getString(Context c, int a) {
        return c.getResources().getString(a);
    }



    public List<String> getStorageDirectories() {

        final Pattern DIR_SEPARATOR = Pattern.compile("/");
        // Final set of paths
        final ArrayList<String> rv = new ArrayList<>();
        // Primary physical SD-CARD (not emulated)
        final String rawExternalStorage = System.getenv("EXTERNAL_STORAGE");
        // All Secondary SD-CARDs (all exclude primary) separated by ":"
        final String rawSecondaryStoragesStr = System.getenv("SECONDARY_STORAGE");
        // Primary emulated SD-CARD
        final String rawEmulatedStorageTarget = System.getenv("EMULATED_STORAGE_TARGET");
        if (TextUtils.isEmpty(rawEmulatedStorageTarget)) {
            // Device has physical external storage; use plain paths.
            if (TextUtils.isEmpty(rawExternalStorage)) {
                // EXTERNAL_STORAGE undefined; falling back to default.
                rv.add("/storage/sdcard0");
            } else {
                rv.add(rawExternalStorage);
            }
        } else {
            // Device has emulated storage; external storage paths should have
            // userId burned into them.
            final String rawUserId;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
                rawUserId = "";
            } else {
                final String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                final String[] folders = DIR_SEPARATOR.split(path);
                final String lastFolder = folders[folders.length - 1];
                boolean isDigit = false;
                try {
                    Integer.valueOf(lastFolder);
                    isDigit = true;
                } catch (NumberFormatException ignored) {
                }
                rawUserId = isDigit ? lastFolder : "";
            }
            // /storage/emulated/0[1,2,...]
            if (TextUtils.isEmpty(rawUserId)) {
                rv.add(rawEmulatedStorageTarget);
            } else {
                rv.add(rawEmulatedStorageTarget + File.separator + rawUserId);
            }
        }
        // Add all secondary storages
        if (!TextUtils.isEmpty(rawSecondaryStoragesStr)) {
            // All Secondary SD-CARDs splited into array
            final String[] rawSecondaryStorages = rawSecondaryStoragesStr.split(File.pathSeparator);
            Collections.addAll(rv, rawSecondaryStorages);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            rv.clear();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            String strings[] = getExtSdCardPathsForActivity(mContext);
            for (String s : strings) {
                File f = new File(s);
                if (!rv.contains(s) && canListFiles(f))
                    rv.add(s);
            }
        }

      /*  rootmode = Sp.getBoolean("rootmode", false);
        if (rootmode)
            rv.add("/");*/
        File usb = getUsbDrive();
        if (usb != null && !rv.contains(usb.getPath())) rv.add(usb.getPath());

        /*for (String path : rv) {
            Log.e("Storage Paths", path);
            //Toast.makeText(mContext, path, Toast.LENGTH_SHORT).show();
        }*/
        return rv;
    }

    public boolean canListFiles(File f) {
        try {
            if (f.canRead() && f.isDirectory())
                return true;
            else
                return false;
        } catch (Exception e) {
            return false;
        }
    }

    public String StoragePath(String StorageType) {
        List<String> paths = getStorageDirectories();
        if (paths.size() > 0) {
            try {
                if (StorageType.equalsIgnoreCase("InternalStorage")) {
                    return paths.get(0);

                } else if (StorageType.equalsIgnoreCase("ExternalStorage")) {
                    if (paths.size() >= 1)
                        return paths.get(1);

                } else if (StorageType.equalsIgnoreCase("UsbStorage")) {
                    if (paths.size() >= 2)
                        return paths.get(2);
                } else {

                }
            } catch (Exception e) {
            }
        }
        return "";
    }
//
//    public static boolean isProVersion() {
//        return BuildConfig.FLAVOR.contains("Pro") || BuildConfig.FLAVOR.contains("Underground");
//    }

    public File getUsbDrive() {
        File parent;
        parent = new File("/storage");

        try {
            for (File f : parent.listFiles()) {
                if (f.exists() && f.getName().toLowerCase().contains("usb") && f.canExecute()) {
                    return f;
                }
            }
        } catch (Exception e) {
        }
        parent = new File("/mnt/sdcard/usbStorage");
        if (parent.exists() && parent.canExecute())
            return (parent);

        parent = new File("/mnt/sdcard/usb_storage");
        if (parent.exists() && parent.canExecute())
            return parent;

        return null;
    }


    public static class Unit {
        String count;
        String unit;

        Unit(String count, String unit) {
            this.count = count;
            this.unit = unit;
        }

        public String toString() {
            return count + unit;
        }

        /**
         * @return the count
         */
        public String getCount() {
            return count;
        }

        /**
         * @return the unit
         */
        public String getUnit() {
            return unit;
        }
    }
}
