package com.hidefile.secure.folder.vault.cluecanva;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.usage.StorageStatsManager;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfRenderer;
import android.media.MediaMetadataRetriever;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.provider.BaseColumns;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.documentfile.provider.DocumentFile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
public class TillsFl {
    public static final String DOCUMENTS_DIR = "documents";
    private static final int OPERATION_FILE_TYPE_AUDIO = 0;
    private static final int OPERATION_FILE_TYPE_IMAGE = 1;
    private static final int OPERATION_FILE_TYPE_OTHER = 2;
    private static final int OPERATION_FILE_TYPE_VIDEO = 3;
    private static final int RETURN_TYPE_FREE_MEMORY = 4;
    private static final int RETURN_TYPE_FREE_TOTAL_COUNT = 5;
    private static final int RETURN_TYPE_TOTAL_MEMORY = 6;
    private static final int RETURN_TYPE_USED_MEMORY = 7;
    private static final int RETURN_TYPE_USED_TOTAL_COUNT = 8;
    private static final String SD_URI = "";
    private static final boolean DEBUG = false;
    private static final String PRIMARY_VOLUME_NAME = "primary";
    static String TAG = "TAG";
    private static ParcelFileDescriptor mFileDescriptor;
    private static PdfRenderer mPdfRenderer;
    private static PdfRenderer.Page mCurrentPage;
    private static int copyType, moveType, updateType;
    private static int counter = 1;

    private TillsFl() {
        throw new AssertionError("No Instances");
    }

    public static String getImagePathFromContentUri(Context context, Uri uri) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri,
                    filePathColumn, null, null, null);

            if (cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    public static String getMusicPathFromContentUri(Context context, Uri uri) {
        String[] filePathColumn = {MediaStore.Audio.Media.DATA};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri,
                    filePathColumn, null, null, null);

            if (cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    public static String getVideoPathFromContentUri(Context context, Uri uri) {
        String[] filePathColumn = {MediaStore.Video.Media.DATA};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri,
                    filePathColumn, null, null, null);

            if (cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    public static String getExtension(String path) {
        String extension = "";
        if (path != null) {
            int i = path.lastIndexOf('.');
            if (i > 0) {
                extension = path.substring(i + 1);
            }
        }
        return extension;
    }

    public static String getBaseFileName(String fileName) {
        if (TextUtils.isEmpty(fileName) || !fileName.contains(".") || fileName.endsWith("."))
            return null;
        return fileName.substring(0, fileName.lastIndexOf("."));
    }

    public static String readableFileSize(String path) {
        if (path == null) {
            return "";
        }
        return readableFileSize(new File(path).length());
    }


//
//    public static Intent getOpenFileIntent(Context context, String filePath) {
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setDataAndType(FileUtilsNew.getUriFromFile(context, new File(filePath)), getMimeType(filePath));
//        return intent;
//    }

    public static int getFilesList(String filePath) {

        File f = new File(filePath);
        File[] files = f.listFiles();
        if (files != null) {

            return files.length;
        } else
            return 0;
    }

    public static long folderSize(File directory) {
        long length = 0;
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile())
                    length += file.length();
                else
                    length += folderSize(file);
            }
        }

        return length;
    }

    public static String readableFileSize(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "kB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    /**
     * Get the file size in a human-readable string.
     *
     * @param size
     * @return
     * @author paulburke
     */
    public static String getReadableFileSize(int size) {
        final int BYTES_IN_KILOBYTES = 1024;
        final DecimalFormat dec = new DecimalFormat("###.#");
        final String KILOBYTES = " KB";
        final String MEGABYTES = " MB";
        final String GIGABYTES = " GB";
        float fileSize = 0;
        String suffix = KILOBYTES;

        if (size > BYTES_IN_KILOBYTES) {
            fileSize = size / BYTES_IN_KILOBYTES;
            if (fileSize > BYTES_IN_KILOBYTES) {
                fileSize = fileSize / BYTES_IN_KILOBYTES;
                if (fileSize > BYTES_IN_KILOBYTES) {
                    fileSize = fileSize / BYTES_IN_KILOBYTES;
                    suffix = GIGABYTES;
                } else {
                    suffix = MEGABYTES;
                }
            }
        }
        return String.valueOf(dec.format(fileSize) + suffix);
    }

    public static int getMediaDuration(String path) throws IOException {
        if (TextUtils.isEmpty(path)) {
            return 0;
        }

        MediaMetadataRetriever mmr = null;
        try {
            mmr = new MediaMetadataRetriever();
            mmr.setDataSource(path);
            String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            return (int) (Long.parseLong(durationStr) / 1000);
        } finally {
            if (mmr != null) {
                mmr.release();
            }
        }
    }

    public static String getMediaDurationString(String path) {
        try {
            long duration = getMediaDuration(path);
            return mediaDurationToString((int) duration);
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public static String mediaDurationToString(int duration) {
        int minutes = duration / 60;
        int seconds = duration % 60;
        return String.format("%02d", minutes) + ":"
                + String.format("%02d", seconds);
    }

    public static final String makeShortTimeString(final Context context, long secs) {
        int totalSeconds = (int) (secs);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        return hours > 0 ? String.format("%02d:%02d:%02d", hours, minutes, seconds) : String.format("%02d:%02d", minutes, seconds);
    }

    public static String duration(long duration) {
        //convert the song duration into string reading hours, mins seconds
        int dur = (int) duration;

        int hrs = (dur / 3600000);
        int mns = (dur / 60000) % 60000;
        int scs = dur % 60000 / 1000;

        return String.format("%02d:%02d:%02d", hrs, mns, scs);
    }

    public static boolean deleteFile(Context context, String path) {
        boolean isDeleteFile = false;
        isDeleteFile = new File(path).delete();
        if (!isDeleteFile && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            DocumentFile document = getDocumentFile(new File(path), false, context);
            isDeleteFile = document != null && document.delete();
        }
        rescanMediaStore(context, new String[]{path});
        return isDeleteFile;
    }

    public static boolean deleteFiles(Context context, List<String> paths) {
        boolean isDeleteFile = false;
        for (String path : paths) {
            isDeleteFile = new File(path).delete();
            if (!isDeleteFile && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                DocumentFile document = getDocumentFile(new File(path), false, context);
                isDeleteFile = document != null && document.delete();
            }
        }
        rescanMediaStore(context, paths.toArray(new String[paths.size()]));
        return isDeleteFile;
    }

    /**
     * Update in gallery
     */
    public static void rescanMediaStore(Context context, String[] paths) {
        MediaScannerConnection.scanFile(context, paths, null, null);
    }

    public static void scanFileManager(Context context, String filePath) {
        MediaScannerConnection.scanFile(context, new String[]{filePath}, null, (str, uri) -> {

        });
    }

//    public static void copy(Context context, Uri srcUri, File dstFile) {
//        try {
//            InputStream inputStream = context.getContentResolver().openInputStream(srcUri);
//            if (inputStream == null) return;
//            OutputStream outputStream = new FileOutputStream(dstFile);
//            IOUtils.copy(inputStream, outputStream);
//            inputStream.close();
//            outputStream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static Uri getUriFromFile(Context context, File file) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            try {
//                return FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
//            } catch (IllegalArgumentException e) {
//                Timber.d("getUriFromFile : " + e.toString());
//                return null;
//            }
//        } else {
//            return Uri.fromFile(file);
//        }
//    }
//
//    public static String getFilePathFromURI(Context context, Uri contentUri) {
//        //copy file and send new file path as getting file from source Uri is not working in Android N
//        String fileName = FileUtilsNew.getFileName(contentUri);
//        if (!TextUtils.isEmpty(fileName)) {
//            File dir = new File(Config.HOME_DIR_PATH, Config.HIDDEN_IMAGE_DIR);
//            if (!dir.exists()) dir.mkdirs();
//
//            File dstFile = new File(dir.getPath() + File.separator + fileName);
//            FileUtilsNew.copy(context, contentUri, dstFile);
//            return dstFile.getAbsolutePath();
//        }
//        return null;
//    }

    public static void scanMedia(Context context, File file) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(getUriFromFile(context, file.getAbsolutePath()));
        context.sendBroadcast(mediaScanIntent);
    }

    public static File saveDrawableToFile(Context context, int drawableResc, File file) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawableResc);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

            fos.close();

        } catch (IOException e) {
            Log.e("app", e.getMessage());
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return file;
    }

    public static boolean copy(File src, File dst) {
        try {
            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dst);

            // Transfer bytes from in to out
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getFileName(Uri uri) {
        if (uri == null) return null;
        String fileName = null;
        String path = uri.getPath();
        int cut = path.lastIndexOf('/');
        if (cut != -1) {
            fileName = path.substring(cut + 1);
        }
        return fileName;
    }

    public static String getFileName(File file) {
        if (file == null) return null;
        String fileName = null;
        String path = file.getAbsolutePath();
        int cut = path.lastIndexOf('/');
        if (cut != -1) {
            fileName = path.substring(cut + 1);
        }
        return fileName;
    }

    public static boolean copyFileToOtherLocation(Context context, int operationFileType, File oldFile, File newFile) {
        boolean isCopiedSuccess = false;
        String oldPath = "", newPath = "";
        oldPath = oldFile.getAbsolutePath();
        newPath = newFile.getAbsolutePath();

        if (TillsFl.copyFolder(context, oldFile, newFile, true)) {
            if (operationFileType == OPERATION_FILE_TYPE_IMAGE) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.MediaColumns.DATA, newPath);
                values.put(MediaStore.MediaColumns.TITLE, getFileName(newFile));
                values.put(MediaStore.MediaColumns.DATE_MODIFIED, System.currentTimeMillis());
                context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                Uri uri = TillsFl.getUriFromFile(context, newPath);
                context.getContentResolver().notifyChange(uri, null);
                TillsFl.scanMedia(context, newFile);
                isCopiedSuccess = true;

            } else if (operationFileType == OPERATION_FILE_TYPE_VIDEO) {

                ContentValues values = new ContentValues();
                values.put(MediaStore.MediaColumns.DATA, newPath);
                values.put(MediaStore.MediaColumns.TITLE, getFileName(newFile));
                values.put(MediaStore.MediaColumns.DATE_MODIFIED, System.currentTimeMillis());
                context.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);

                Uri uri = TillsFl.getUriFromFile(context, newPath);
                context.getContentResolver().notifyChange(uri, null);
                TillsFl.scanMedia(context, newFile);
                isCopiedSuccess = true;

            } else if (operationFileType == OPERATION_FILE_TYPE_AUDIO) {

                ContentValues values = new ContentValues();
                values.put(MediaStore.Audio.Media.DATA, newPath);
                values.put(MediaStore.MediaColumns.TITLE, getFileName(newFile));
                values.put(MediaStore.Audio.Media.DATE_MODIFIED, System.currentTimeMillis());
                int row = context.getContentResolver().update(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values, MediaStore.Audio.Media.DATA + "=?", new String[]{oldPath});

                Uri uri = TillsFl.getUriFromFile(context, newPath);
                context.getContentResolver().notifyChange(uri, null);
                TillsFl.scanMedia(context, newFile);
                isCopiedSuccess = true;

            } else if (operationFileType == OPERATION_FILE_TYPE_OTHER) {

                ContentValues values = new ContentValues();
                values.put(MediaStore.Files.FileColumns.DATA, newPath);
                values.put(MediaStore.Files.FileColumns.TITLE, getFileName(newFile));
                values.put(MediaStore.Files.FileColumns.DATE_MODIFIED, System.currentTimeMillis());
                context.getContentResolver().insert(MediaStore.Files.getContentUri("external"), values);

                Uri uri = TillsFl.getUriFromFile(context, newPath);
                context.getContentResolver().notifyChange(uri, null);
                TillsFl.scanMedia(context, newFile);
                isCopiedSuccess = true;

            } else {
                if (oldPath.endsWith("pdf") || oldPath.endsWith("doc") || oldPath.endsWith("docx") ||
                        oldPath.endsWith("xlsx") || oldPath.endsWith("xls") || oldPath.endsWith("xld") ||
                        oldPath.endsWith("ppt") || oldPath.endsWith("pptx") || oldPath.endsWith("ppsx") ||
                        oldPath.endsWith("pptm") || oldPath.endsWith("txt") || oldPath.endsWith("text") ||
                        oldPath.endsWith("html") || oldPath.endsWith("java") || oldPath.endsWith("htm") ||
                        oldPath.endsWith("xml") || oldPath.endsWith("php") || oldPath.endsWith("zip") ||
                        oldPath.endsWith("rar") || oldPath.endsWith("7z") || oldPath.endsWith("apk")) {

                    copyType = OPERATION_FILE_TYPE_OTHER;
                } else if (oldPath.endsWith("jpg") || oldPath.endsWith("jpeg") || oldPath.endsWith("png") || oldPath.endsWith("gif") ||
                        oldPath.endsWith("bmp") || oldPath.endsWith("heif") || oldPath.endsWith("heic") || oldPath.endsWith("webp")) {

                    copyType = OPERATION_FILE_TYPE_IMAGE;

                } else if (oldPath.endsWith("aac") || oldPath.endsWith("mp3") || oldPath.endsWith("wav") || oldPath.endsWith("ogg")) {

                    copyType = OPERATION_FILE_TYPE_AUDIO;

                } else if (oldPath.endsWith("mkv") || oldPath.endsWith("mp4") || oldPath.endsWith("3gp")) {

                    copyType = OPERATION_FILE_TYPE_VIDEO;
                }
                copyFileToOtherLocation(context, copyType, oldFile, newFile);
            }

        }

        return isCopiedSuccess;
    }

    public static boolean moveFileToOtherLocation(Context context, int operationFileType, File oldFile, File newFile) {
        boolean isMovedSuccess = false;
        String oldPath = "", newPath = "";
        oldPath = oldFile.getAbsolutePath();
        newPath = newFile.getAbsolutePath();
        if (TillsFl.moveFolder(context, oldFile, newFile, true)) {
            if (operationFileType == OPERATION_FILE_TYPE_IMAGE) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.MediaColumns.DATA, newPath);
                values.put(MediaStore.MediaColumns.TITLE, getFileName(newFile));
                values.put(MediaStore.MediaColumns.DATE_MODIFIED, System.currentTimeMillis());
                context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                context.getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        MediaStore.Images.Media.DATA + "='" + oldPath + "'",
                        null);

                Uri uri = TillsFl.getUriFromFile(context, newPath);
                context.getContentResolver().notifyChange(uri, null);
                TillsFl.scanMedia(context, newFile);
                isMovedSuccess = true;

            } else if (operationFileType == OPERATION_FILE_TYPE_VIDEO) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.MediaColumns.DATA, newPath);
                values.put(MediaStore.MediaColumns.TITLE, getFileName(newFile));
                values.put(MediaStore.MediaColumns.DATE_MODIFIED, System.currentTimeMillis());
                context.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);

                context.getContentResolver().delete(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        MediaStore.Video.Media.DATA + "='" + oldPath + "'",
                        null);

                Uri uri = TillsFl.getUriFromFile(context, newPath);
                context.getContentResolver().notifyChange(uri, null);
                TillsFl.scanMedia(context, newFile);
                isMovedSuccess = true;

            } else if (operationFileType == OPERATION_FILE_TYPE_AUDIO) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Audio.Media.DATA, newPath);
                values.put(MediaStore.MediaColumns.TITLE, getFileName(newFile));
                values.put(MediaStore.Audio.Media.DATE_MODIFIED, System.currentTimeMillis());
                int row = context.getContentResolver().update(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values, MediaStore.Audio.Media.DATA + "=?", new String[]{oldPath});
                Uri uri = TillsFl.getUriFromFile(context, newPath);
                context.getContentResolver().notifyChange(uri, null);
                TillsFl.scanMedia(context, newFile);
                isMovedSuccess = true;

            } else if (operationFileType == OPERATION_FILE_TYPE_OTHER) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Files.FileColumns.DATA, newPath);
                values.put(MediaStore.Files.FileColumns.TITLE, getFileName(newFile));
                values.put(MediaStore.Files.FileColumns.DATE_MODIFIED, System.currentTimeMillis());
                context.getContentResolver().insert(MediaStore.Files.getContentUri("external"), values);
                context.getContentResolver().delete(MediaStore.Files.getContentUri("external"),
                        MediaStore.Files.FileColumns.DATA + "='" + oldPath + "'",
                        null);

                Uri uri = TillsFl.getUriFromFile(context, newPath);
                context.getContentResolver().notifyChange(uri, null);
                TillsFl.scanMedia(context, newFile);
                isMovedSuccess = true;
            } else {
                if (oldPath.endsWith("pdf") || oldPath.endsWith("doc") || oldPath.endsWith("docx") ||
                        oldPath.endsWith("xlsx") || oldPath.endsWith("xls") || oldPath.endsWith("xld") ||
                        oldPath.endsWith("ppt") || oldPath.endsWith("pptx") || oldPath.endsWith("ppsx") ||
                        oldPath.endsWith("pptm") || oldPath.endsWith("txt") || oldPath.endsWith("text") ||
                        oldPath.endsWith("html") || oldPath.endsWith("java") || oldPath.endsWith("htm") ||
                        oldPath.endsWith("xml") || oldPath.endsWith("php") || oldPath.endsWith("zip") ||
                        oldPath.endsWith("rar") || oldPath.endsWith("7z") || oldPath.endsWith("apk")) {

                    moveType = OPERATION_FILE_TYPE_OTHER;
                } else if (oldPath.endsWith("jpg") || oldPath.endsWith("jpeg") || oldPath.endsWith("png") || oldPath.endsWith("gif") ||
                        oldPath.endsWith("bmp") || oldPath.endsWith("heif") || oldPath.endsWith("heic") || oldPath.endsWith("webp")) {

                    moveType = OPERATION_FILE_TYPE_IMAGE;

                } else if (oldPath.endsWith("aac") || oldPath.endsWith("mp3") || oldPath.endsWith("wav") || oldPath.endsWith("ogg")) {

                    moveType = OPERATION_FILE_TYPE_AUDIO;

                } else if (oldPath.endsWith("mkv") || oldPath.endsWith("mp4") || oldPath.endsWith("3gp")) {

                    moveType = OPERATION_FILE_TYPE_VIDEO;
                }
                moveFileToOtherLocation(context, moveType, oldFile, newFile);
            }
        }

        return isMovedSuccess;
    }

    public static boolean updateFileToOtherLocation(Context context, int operationFileType, File oldFile, File newFile) {
        boolean isUpdatedSuccess = false;
        String oldPath = "", newPath = "";
        oldPath = oldFile.getAbsolutePath();
        newPath = newFile.getAbsolutePath();
        if (operationFileType == OPERATION_FILE_TYPE_IMAGE) {

            boolean isUpdated = renameFolder(context, oldFile, newFile);

            if (!isUpdated) {
                isUpdated = copyFolder(context, oldFile, newFile, false);

                if (isUpdated && newFile.length() < oldFile.length()) {
                    isUpdated = copyFolder(context, oldFile, newFile, false);
                }

                if (isUpdated) {
                    isUpdated = deleteFile(oldFile, context);
                }
            }
            if (isUpdated) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.MediaColumns.DATA, newPath);
                values.put(MediaStore.MediaColumns.TITLE, getFileName(newFile));
                values.put(MediaStore.MediaColumns.DATE_MODIFIED, System.currentTimeMillis());

                context.getContentResolver().update(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values, MediaStore.Images.Media.DATA + "=?", new String[]{oldPath});

                Uri uri = TillsFl.getUriFromFile(context, newPath);
                context.getContentResolver().notifyChange(uri, null);
                TillsFl.scanMedia(context, newFile);
                isUpdatedSuccess = true;
            }


        } else if (operationFileType == OPERATION_FILE_TYPE_VIDEO) {
            boolean isUpdated = renameFolder(context, oldFile, newFile);
            if (!isUpdated) {
                isUpdated = copyFolder(context, oldFile, newFile, false);

                if (isUpdated && newFile.length() < oldFile.length()) {
                    isUpdated = copyFolder(context, oldFile, newFile, false);
                }

                if (isUpdated) {
                    isUpdated = deleteFile(oldFile, context);
                }
            }
            if (isUpdated) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.MediaColumns.DATA, newPath);
                values.put(MediaStore.MediaColumns.TITLE, getFileName(newFile));
                values.put(MediaStore.MediaColumns.DATE_MODIFIED, System.currentTimeMillis());

                context.getContentResolver().update(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values, MediaStore.Video.Media.DATA + "=?", new String[]{oldPath});

                Uri uri = TillsFl.getUriFromFile(context, newPath);
                context.getContentResolver().notifyChange(uri, null);
                TillsFl.scanMedia(context, newFile);
                isUpdatedSuccess = true;
            }


        } else if (operationFileType == OPERATION_FILE_TYPE_AUDIO) {
            boolean isUpdated = renameFolder(context, oldFile, newFile);

            if (!isUpdated) {
                isUpdated = copyFolder(context, oldFile, newFile, false);

                if (isUpdated && newFile.length() < oldFile.length()) {
                    isUpdated = copyFolder(context, oldFile, newFile, false);
                }

                if (isUpdated) {
                    isUpdated = deleteFile(oldFile, context);
                }
            }
            if (isUpdated) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.MediaColumns.DATA, newPath);
                values.put(MediaStore.MediaColumns.TITLE, getFileName(newFile));
                values.put(MediaStore.MediaColumns.DATE_MODIFIED, System.currentTimeMillis());

                context.getContentResolver().update(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values, MediaStore.Audio.Media.DATA + "=?", new String[]{oldPath});

                Uri uri = TillsFl.getUriFromFile(context, newPath);
                context.getContentResolver().notifyChange(uri, null);
                TillsFl.scanMedia(context, newFile);
                isUpdatedSuccess = true;
            }

        } else if (operationFileType == OPERATION_FILE_TYPE_OTHER) {
            boolean isUpdated = renameFolder(context, oldFile, newFile);

            if (!isUpdated) {
                isUpdated = copyFolder(context, oldFile, newFile, false);

                if (isUpdated && newFile.length() < oldFile.length()) {
                    isUpdated = copyFolder(context, oldFile, newFile, false);
                }

                if (isUpdated) {
                    isUpdated = deleteFile(oldFile, context);
                }
            }
            if (isUpdated) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Files.FileColumns.DATA, newPath);
                values.put(MediaStore.Files.FileColumns.TITLE, getFileName(newFile));
                values.put(MediaStore.Files.FileColumns.DATE_MODIFIED, System.currentTimeMillis());

                context.getContentResolver().update(MediaStore.Files.getContentUri("external"), values, MediaStore.Files.FileColumns.DATA + "=?", new String[]{oldPath});

                Uri uri = TillsFl.getUriFromFile(context, newPath);
                context.getContentResolver().notifyChange(uri, null);
                TillsFl.scanMedia(context, newFile);
                isUpdatedSuccess = true;
            }

        } else {
            if (oldPath.endsWith("pdf") || oldPath.endsWith("doc") || oldPath.endsWith("docx") ||
                    oldPath.endsWith("xlsx") || oldPath.endsWith("xls") || oldPath.endsWith("xld") ||
                    oldPath.endsWith("ppt") || oldPath.endsWith("pptx") || oldPath.endsWith("ppsx") ||
                    oldPath.endsWith("pptm") || oldPath.endsWith("txt") || oldPath.endsWith("text") ||
                    oldPath.endsWith("html") || oldPath.endsWith("java") || oldPath.endsWith("htm") ||
                    oldPath.endsWith("xml") || oldPath.endsWith("php") || oldPath.endsWith("zip") ||
                    oldPath.endsWith("rar") || oldPath.endsWith("7z") || oldPath.endsWith("apk")) {

                updateType = OPERATION_FILE_TYPE_OTHER;

            } else if (oldPath.endsWith("jpg") || oldPath.endsWith("jpeg") || oldPath.endsWith("png") || oldPath.endsWith("gif") ||
                    oldPath.endsWith("bmp") || oldPath.endsWith("heif") || oldPath.endsWith("heic") || oldPath.endsWith("webp")) {

                updateType = OPERATION_FILE_TYPE_IMAGE;

            } else if (oldPath.endsWith("aac") || oldPath.endsWith("mp3") || oldPath.endsWith("wav") || oldPath.endsWith("ogg")) {

                updateType = OPERATION_FILE_TYPE_AUDIO;

            } else if (oldPath.endsWith("mkv") || oldPath.endsWith("mp4") || oldPath.endsWith("3gp")) {

                updateType = OPERATION_FILE_TYPE_VIDEO;
            }
            updateFileToOtherLocation(context, updateType, oldFile, newFile);
        }
        return isUpdatedSuccess;
    }

    /**
     * Rename a folder. In case of extSdCard in Kitkat, the old folder stays in place, but files are moved.
     *
     * @param source The source folder.
     * @param target The target folder.
     * @return true if the renaming was successful.
     */
    public static boolean renameFolder(Context context, @NonNull final File source, @NonNull final File target) {
        // First try the normal rename.
        if (source.renameTo(target)) {
            return true;
        }
        if (target.exists()) {
            return false;
        }

        // Try the Storage Access Framework if it is just a rename within the same parent folder.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && source.getParent().equals(target.getParent())) {
            DocumentFile document = getDocumentFile(source, true, context);
            if (document != null && document.renameTo(target.getName())) {
                return true;
            }
        }

        // Try the manual way, moving files individually.
        if (!mkdir(context, target)) {
            return false;
        }

        File[] sourceFiles = source.listFiles();

        if (sourceFiles == null) {
            return true;
        }

        for (File sourceFile : sourceFiles) {
            String fileName = sourceFile.getName();
            File targetFile = new File(target, fileName);
            if (!copyFile(sourceFile, targetFile, context)) {
                // stop on first error
                return false;
            }
        }
        // Only after successfully copying all files, delete files on source folder.
        for (File sourceFile : sourceFiles) {
            if (!deleteFile(sourceFile, context)) {
                // stop on first error
                return false;
            }
        }
        return true;
    }

    /**
     * Create a folder. The folder may even be on external SD card for Kitkat.
     *
     * @param file The folder to be created.
     * @return True if creation was successful.
     */
    public static boolean mkdir(Context context, @NonNull final File file) {
        boolean isCreated = false;
        if (file.exists()) {
            // nothing to create.
            isCreated = file.isDirectory();
        }

        // Try the normal way
        if (file.mkdir()) {
            isCreated = true;
        }

        // Try with Storage Access Framework.
        if (!isCreated && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            DocumentFile document = getDocumentFile(file, true, context);
            // getDocumentFile implicitly creates the directory.
            return document != null && document.exists();
        }


        return false;
    }

   /* public static boolean copyFolder(Context context, File source, File target, boolean replaceFiles) {
        boolean isCopy = false;
        if (copyFolder(source, target, replaceFiles)) {
            isCopy = true;
        }
        if (!isCopy && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            DocumentFile sourceDocument = getDocumentFile(source, source.isDirectory(), context);
            isCopy = sourceDocument.renameTo(target.getName());
        }
        return isCopy;
    }*/

    /**
     * Create a folder. The folder may even be on external SD card for Kitkat.
     *
     * @param file The folder to be created.
     * @return True if creation was successful.
     */
    public static boolean mkfile(Context context, @NonNull final File file) {
        boolean isCreated = false;
        if (file.exists()) {
            // nothing to create.
            isCreated = file.isFile();
        }

        // Try the normal way
        try {
            if (file.createNewFile()) {
                isCreated = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            isCreated = false;
        }

        if (!isCreated) {
            int i = 0;
            File newFile;
            do {
                String fileName = TillsFl.getBaseFileName(file.getName());
                newFile = new File(file.getParent(), fileName + "." + getExtension(file.getPath()));
            }
            while (newFile.exists());
            isCreated = true;
        }

        // Try with Storage Access Framework.
        if (!isCreated && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            DocumentFile document = getDocumentFile(file, true, context);
            // getDocumentFile implicitly creates the directory.
            return document != null && document.exists();
        }


        return false;
    }

    /**
     * copying the entire folder with its contents
     *
     * @param source       source folder
     * @param target       target folder
     * @param replaceFiles target folder files that should be replaced or not
     */
    public static boolean copyFolder(Context context, File source, File target, boolean replaceFiles) {
        boolean isCopied = false;
        if (source.isDirectory()) {
            if (!target.exists()) {
                target.mkdir();
            }

            String[] children = source.list();
            for (int i = 0; i < source.listFiles().length; i++) {

                copyFolder(context, new File(source, children[i]),
                        new File(target, children[i]), replaceFiles);
                isCopied = true;
            }
        } else if (replaceFiles || !target.exists()) {
            isCopied = copyFile(source, target, context);
        }
        return isCopied;
    }

    /**
     * Copy a manager. The target manager may even be on external SD card for Kitkat.
     *
     * @param source The source manager
     * @param target The target manager
     * @return true if the copying was successful.
     */
    @SuppressWarnings("null")
    public static boolean copyFile(final File source, final File target, Context context) {
        FileInputStream inStream = null;
        OutputStream outStream = null;
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            inStream = new FileInputStream(source);

            // First try the normal way
            if (isWritable(target)) {
                // standard way
                outStream = new FileOutputStream(target);
                inChannel = inStream.getChannel();
                outChannel = ((FileOutputStream) outStream).getChannel();
                inChannel.transferTo(0, inChannel.size(), outChannel);
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    // Storage Access Framework
                    DocumentFile targetDocument = getDocumentFile(target, false, context);
                    outStream =
                            context.getContentResolver().openOutputStream(targetDocument.getUri());
                } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
                    // Workaround for Kitkat ext SD card
                    Uri uri = getUriFromFile(target.getAbsolutePath(), context);
                    outStream = context.getContentResolver().openOutputStream(uri);
                } else {
                    return false;
                }

                if (outStream != null) {
                    // Both for SAF and for Kitkat, write to output stream.
                    byte[] buffer = new byte[16384]; // MAGIC_NUMBER
                    int bytesRead;
                    while ((bytesRead = inStream.read(buffer)) != -1) {
                        outStream.write(buffer, 0, bytesRead);
                    }
                }

            }
        } catch (Exception e) {
            Log.e("AmazeFileUtils",
                    "Error when copying manager from " + source.getAbsolutePath() + " to " + target.getAbsolutePath(), e);
            return false;
        } finally {
            try {
                inStream.close();
            } catch (Exception e) {
                // ignore exception
            }
            try {
                outStream.close();
            } catch (Exception e) {
                // ignore exception
            }
            try {
                inChannel.close();
            } catch (Exception e) {
                // ignore exception
            }
            try {
                outChannel.close();
            } catch (Exception e) {
                // ignore exception
            }
        }
        return true;
    }

    /**
     * moveing the entire folder with its contents
     *
     * @param source       source folder
     * @param target       target folder
     * @param replaceFiles target folder files that should be replaced or not
     */
    public static boolean moveFolder(Context context, File source, File target, boolean replaceFiles) {
        boolean isMove = false;
        if (source.isDirectory()) {
            if (!target.exists()) {
                target.mkdir();
            }

            String[] children = source.list();
            for (int i = 0; i < source.listFiles().length; i++) {

                moveFolder(context, new File(source, children[i]),
                        new File(target, children[i]), replaceFiles);
                isMove = true;
            }
        } else if (replaceFiles || !target.exists()) {
            isMove = moveFiles(context, source, target);
        }
        return isMove;
    }

    private static boolean moveFiles(Context context, File source, File target) {
        boolean success = source.renameTo(target);

        if (!success && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && source.getParent().equals(target.getParent())) {
// Storage Access Framework
            DocumentFile sourceDocument = getDocumentFile(source, false, context);
            success = sourceDocument.renameTo(target.getName());
        }

        if (!success) {
            success = copyFile(source, target, context);

            if (success && target.length() < source.length()) {
                success = copyFile(source, target, context);
            }
            if (success) {
                success = deleteFile(source, context);
            }
        }
        return success;
    }

    public static void moveFile(File file, File newFile) {
        Log.e("moveFile", "file:" + file.getPath());
        Log.e("moveFile", "newFile:" + newFile.getPath());

        //File newFile = new File(dir, file.getName());
        FileChannel outputChannel = null;
        FileChannel inputChannel = null;
        try {
            outputChannel = new FileOutputStream(newFile).getChannel();
            inputChannel = new FileInputStream(file).getChannel();
            inputChannel.transferTo(0, inputChannel.size(), outputChannel);
            inputChannel.close();

            file.delete();
        } catch (IOException e) {
            Log.e("Exception", "moveFile:" + e);
            e.printStackTrace();
            //return false;
        } finally {
            try {
                if (inputChannel != null) inputChannel.close();
                if (outputChannel != null) outputChannel.close();
            } catch (IOException e) {
                Log.e("Exception", "moveFile:" + e);
                e.printStackTrace();
                //return false;
            }
        }
        //return true;
    }

    public static File createUnZipFile(String strFile, String filepath) {
        File newFolder = new File(filepath + "/" + strFile);
        if (!newFolder.exists()) {
            newFolder.mkdir();
            return newFolder;
        } else {
            return UnZipFile(strFile, filepath);
        }
    }

    private static File UnZipFile(String folderName, String filepath) {
        File newFolder1 = new File(filepath + "/" + folderName + "(" + counter + ")");
        if (!newFolder1.exists()) {
            newFolder1.mkdir();
            return newFolder1;
        } else {
            counter++;
            File file = UnZipFile(folderName, filepath);
            return file;
        }
    }
//
//    public static void zipAndReplaceFile(File file) {
//        String fileName = getFileName(file);
//        zip(new File[]{file}, file.getParent() + File.separator + FilenameUtils.removeExtension(fileName) + ".zip");
//        file.delete();
//    }

    /**
     * @param directory parent directory
     * @param extension for example, ".csv"
     * @return list of all files having extension from parent directory
     */
    public static ArrayList<File> getFiles(File directory, String extension) {
        ArrayList<File> inFiles = new ArrayList<File>();
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (!file.isDirectory() && file.getName().endsWith(extension)) {
                    inFiles.add(file);
                }
            }
        }
        return inFiles;
    }

    public static void zip(File[] files, String zipFilePath) {
        int buffer = 1024;
        try {
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(zipFilePath);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
            byte data[] = new byte[buffer];

            for (int i = 0; i < files.length; i++) {
                FileInputStream fi = new FileInputStream(files[i]);
                origin = new BufferedInputStream(fi, buffer);

                String path = files[i].getAbsolutePath();
                ZipEntry entry = new ZipEntry(path.substring(path.lastIndexOf("/") + 1));
                out.putNextEntry(entry);
                int count;

                while ((count = origin.read(data, 0, buffer)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }

            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                if (id != null && id.startsWith("raw:")) {
                    return id.substring(4);
                }
                if (id != null && id.startsWith("msf:")) {
                    // Case: Android 10 emulator, a video file downloaded via Chrome app.
                    // No knowledge how to reconstruct the file path. So just fail fast.
                    return null;
                }
                String[] contentUriPrefixesToTry = new String[]{
                        "content://downloads/public_downloads",
                        "content://downloads/my_downloads"
                };

                for (String contentUriPrefix : contentUriPrefixesToTry) {
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse(contentUriPrefix), Long.valueOf(id));
                    try {
                        String path = getDataColumn(context, contentUri, null, null);
                        if (path != null) {
                            return path;
                        }
                    } catch (Exception e) {
                    }
                }
                // path could not be retrieved using ContentResolver, therefore copy file to accessible cache using streams
                String fileName = getFileName(context, uri);
                File cacheDir = getDocumentCacheDir(context);
                File file = generateFileName(fileName, cacheDir);
                String destinationPath = null;
                if (file != null) {
                    destinationPath = file.getAbsolutePath();
                    saveFileFromUri(context, uri, destinationPath);
                }

                return destinationPath;
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);

            } else if (isGoogleDriveUri(uri)) {
                return getDriveFilePath(uri, context);
            }
        }
//        else if (isGoogleDriveUri(uri)){
//            return getDriveFilePath(uri, context);
//        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            if (isGoogleDriveUri(uri)) {
                return getDriveFilePath(uri, context);
            } else {
                return getDataColumn(context, uri, null, null);
            }
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param uri The Uri to query.
     * @return The value of the _data column, which is typically a file path.
     */

    private static boolean isGoogleDriveUri(Uri uri) {
        return "com.google.android.apps.docs.storage".equals(uri.getAuthority()) || "com.google.android.apps.docs.storage.legacy".equals(uri.getAuthority());
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = MediaStore.MediaColumns.DATA;
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static String getFileName(@NonNull Context context, Uri uri) {
        String mimeType = context.getContentResolver().getType(uri);
        String filename = null;

        if (mimeType == null && context != null) {
            String path = getPath(context, uri);
            if (path == null) {
                filename = getName(uri.toString());
            } else {
                File file = new File(path);
                filename = file.getName();
            }
        } else {
            Cursor returnCursor = context.getContentResolver().query(uri, null,
                    null, null, null);
            if (returnCursor != null) {
                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                returnCursor.moveToFirst();
                filename = returnCursor.getString(nameIndex);
                returnCursor.close();
            }
        }

        return filename;
    }

    public static String getName(String filename) {
        if (filename == null) {
            return null;
        }
        int index = filename.lastIndexOf('/');
        return filename.substring(index + 1);
    }

    private static void saveFileFromUri(Context context, Uri uri, String destinationPath) {
        InputStream is = null;
        BufferedOutputStream bos = null;
        try {
            is = context.getContentResolver().openInputStream(uri);
            bos = new BufferedOutputStream(new FileOutputStream(destinationPath, false));
            byte[] buf = new byte[1024];
            is.read(buf);
            do {
                bos.write(buf);
            } while (is.read(buf) != -1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static File getDocumentCacheDir(@NonNull Context context) {
        File dir = new File(context.getCacheDir(), DOCUMENTS_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        logDir(context.getCacheDir());
        logDir(dir);

        return dir;
    }

    private static void logDir(File dir) {
        if (!DEBUG) return;
        File[] files = dir.listFiles();
        for (File file : files) {

        }
    }

    @Nullable
    public static File generateFileName(@Nullable String name, File directory) {
        if (name == null) {
            return null;
        }

        File file = new File(directory, name);

        if (file.exists()) {
            String fileName = name;
            String extension = "";
            int dotIndex = name.lastIndexOf('.');
            if (dotIndex > 0) {
                fileName = name.substring(0, dotIndex);
                extension = name.substring(dotIndex);
            }

            int index = 0;

            while (file.exists()) {
                index++;
                name = fileName + '(' + index + ')' + extension;
                file = new File(directory, name);
            }
        }

        try {
            if (!file.createNewFile()) {
                return null;
            }
        } catch (IOException e) {
            return null;
        }

        logDir(directory);

        return file;
    }

    private static String getDriveFilePath(Uri uri, Context context) {
        Uri returnUri = uri;
        Cursor returnCursor = context.getContentResolver().query(returnUri, null, null, null, null);
        /*
         * Get the column indexes of the data in the Cursor,
         *     * move to the first row in the Cursor, get the data,
         *     * and display it.
         * */
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();
        String name = (returnCursor.getString(nameIndex));
        String size = (Long.toString(returnCursor.getLong(sizeIndex)));
        File file = new File(context.getCacheDir(), name);
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            FileOutputStream outputStream = new FileOutputStream(file);
            int read = 0;
            int maxBufferSize = 1 * 1024 * 1024;
            int bytesAvailable = inputStream.available();

            //int bufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);

            final byte[] buffers = new byte[bufferSize];
            while ((read = inputStream.read(buffers)) != -1) {
                outputStream.write(buffers, 0, read);
            }
            Log.e("File Size", "Size " + file.length());
            inputStream.close();
            outputStream.close();
            Log.e("File Path", "Path " + file.getPath());
            Log.e("File Size", "Size " + file.length());
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
        return file.getPath();
    }

    /**
     * Remove a directory and all of its contents.
     * <p>
     * The results of executing File.delete() on a File object
     * that represents a directory seems to be platform
     * dependent. This method removes the directory
     * and all of its contents.
     *
     * @return true if the complete directory was removed, false if it could not be.
     * If false is returned then some of the files in the directory may have been removed.
     */
    public static boolean removeDirectory(File directory) {

        // System.out.println("removeDirectory " + directory);

        if (directory == null)
            return false;
        if (!directory.exists())
            return true;
        if (!directory.isDirectory())
            return false;

        String[] list = directory.list();

        // Some JVMs return null for File.list() when the
        // directory is empty.
        if (list != null) {
            for (int i = 0; i < list.length; i++) {
                File entry = new File(directory, list[i]);

                //        System.out.println("\tremoving entry " + entry);

                if (entry.isDirectory()) {
                    if (!removeDirectory(entry))
                        return false;
                } else {
                    if (!entry.delete())
                        return false;
                }
            }
        }

        return directory.delete();
    }

    public static boolean deleteFolder(Context context, File directory) {
        boolean isDeleted = false;
        if (removeDirectory(directory)) {
            isDeleted = true;
        }
        if (!isDeleted && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            DocumentFile document = getDocumentFile(directory, directory.isDirectory(), context);
            isDeleted = document != null && document.delete();
        }
        return isDeleted;
    }

    public static void deleteDirectory(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            if (children != null) {
                for (String child : children) {
                    new File(dir, child).delete();
                }
            }
        }
    }

    public static void openFile(Context context, File file) {
        try {
            Uri uri = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
            } else {
                uri = Uri.fromFile(file);
            }
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, getMimeType(context, Uri.parse(file.getPath())));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(Intent.createChooser(intent, "Open with"));
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static Uri getUriFromFile(Context context, String filePath) {
        try {
            File file = new File(filePath);
            Uri uri = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
            } else {
                uri = Uri.fromFile(file);
            }
            return uri;
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static void installApk(Context context, File file) {
        try {
            Uri uri = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
            } else {
                uri = Uri.fromFile(file);
            }
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_INSTALL_PACKAGE);
            intent.setDataAndType(uri, getMimeType(context, Uri.parse(file.getPath())));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Bitmap retriveVideoFrameFromVideo(String videoPath) throws Throwable {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }

    public static void sendFiles(Context context, ArrayList<String> paths) {
        ArrayList<Uri> uris = new ArrayList<>();
        Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);

        for (int i = 0; i < paths.size(); i++) {
            Uri uri = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", new File(paths.get(i)));
            } else {
                uri = Uri.fromFile(new File(paths.get(i)));
            }
            uris.add(uri);
        }

        intent.setType("*/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(Intent.createChooser(intent, "Share with..."));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void shareFile(Context context, String path) {
        ArrayList<Uri> uris = new ArrayList<>();
        Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);

        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", new File(path));
        } else {
            uri = Uri.fromFile(new File(path));
        }
        uris.add(uri);

        intent.setType("*/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, "Share with..."));

    }

    public static void openParticularFile(Context context, File url) throws IOException {
        // Create URI
        File file = url;

        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(
                    context,
                    context.getApplicationContext()
                            .getPackageName() + ".provider", file);
        } else {
            uri = Uri.fromFile(file);
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        // Check what kind of file you are trying to open, by comparing the url with extensions.
        // When the if condition is matched, plugin sets the correct intent (mime) type,
        // so Android knew what application to use to open the file
        if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
            // Word document
            intent.setDataAndType(uri, "application/msword");
        } else if (url.toString().contains(".pdf")) {
            // PDF file
            intent.setDataAndType(uri, "application/pdf");
        } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
            // Powerpoint file
            intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
            // Excel file
            intent.setDataAndType(uri, "application/vnd.ms-excel");
        } else if (url.toString().contains(".zip") || url.toString().contains(".rar")) {
            // WAV audio file
            intent.setDataAndType(uri, "application/x-wav");
        } else if (url.toString().contains(".rtf")) {
            // RTF file
            intent.setDataAndType(uri, "application/rtf");
        } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
            // WAV audio file
            intent.setDataAndType(uri, "audio/x-wav");
        } else if (url.toString().contains(".gif")) {
            // GIF file
            intent.setDataAndType(uri, "image/gif");
        } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
            // JPG file
            intent.setDataAndType(uri, "image/jpeg");
        } else if (url.toString().contains(".txt")) {
            // Text file
            intent.setDataAndType(uri, "text/plain");
        } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") || url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
            // Video files
            intent.setDataAndType(uri, "video/*");
        } else {
            //if you want you can also define the intent type for any other file

            //additionally use else clause below, to manage other unknown extensions
            //in this case, Android will show all applications installed on the device
            //so you can choose which application to use
            intent.setDataAndType(uri, "*/*");
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(intent);
    }

    public static void saveImage(String str, Bitmap bitmap) {
        if (bitmap != null && !isWhiteBitmap(bitmap)) {
            File file = new File(str);
            if (file.exists()) {
                file.delete();
            }
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                Log.v("saving", str);
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean isWhiteBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return true;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        for (int i = 0; i < width; i++) {
            for (int i2 = 0; i2 < height; i2++) {
                if (bitmap.getPixel(i, i2) != -1) {
                    return false;
                }
            }
        }
        return true;
    }

    public static String StoragePath(Context context, String StorageType) {
        List<String> paths = getStorageDirectories(context);
        if (paths.size() > 0) {
            try {
                if (StorageType.equalsIgnoreCase("InternalStorage")) {
                    return paths.get(0);

                } else if (StorageType.equalsIgnoreCase("ExternalStorage")) {
                    if (paths.size() > 1) {
                        String sdCardPath = paths.get(1);
                        if (sdCardPath == null) {
                            sdCardPath = new TillsNew().getStoragePaths("ExternalStorage");
                        }
                        return sdCardPath;
                    }
                } else if (StorageType.equalsIgnoreCase("UsbStorage")) {
                    if (paths.size() >= 2)
                        return paths.get(2);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static List<String> getStorageDirectories(Context context) {

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
            String strings[] = getExtSdCardPathsForActivity(context);
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
            //Toast.makeText(mContext, path, Toast.LENGTH_LONG).show();
        }*/
        return rv;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String[] getExtSdCardPathsForActivity(Context context) {
        List<String> paths = new ArrayList<String>();
        for (File file : context.getExternalFilesDirs("external")) {
            if (file != null) {
                int index = file.getAbsolutePath().lastIndexOf("/Android/data");
                if (index < 0) {
                    Log.w("TillsFl", "Unexpected external file dir: " + file.getAbsolutePath());
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

    public static boolean canListFiles(File f) {
        try {
            if (f.canRead() && f.isDirectory())
                return true;
            else
                return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isSDCardMounted(Context context) {
        String path = StoragePath(context, "ExternalStorage");
        return path.equals("") ? false : true;
    }

    private static File getUsbDrive() {
        File parent = new File("/storage");//storage

        File[] tmp = parent.listFiles();
        if (tmp != null && tmp.length > 0) {
            try {
                for (File f : tmp) {
                    if (f.exists() && f.getName().toLowerCase().contains("usb") && f.canExecute()) {
                        return f;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        parent = new File("/mnt/sdcard/usbStorage");
        if (parent.exists() && parent.canExecute())
            return (parent);

        parent = new File("/mnt/sdcard/usb_storage");
        if (parent.exists() && parent.canExecute())
            return parent;

        return null;
    }

    public static String getStoragePaths(String StorageType) {
        String Path = "";
        List<StorageUtils.StorageInfo> data = StorageUtils.getStorageList();

        //  StorageUtils.StorageInfo list = data.get(i);

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {

            if (StorageType.equalsIgnoreCase("ExternalStorage")) {

                try {
                    StorageUtils.StorageInfo externalPaths = data.get(0);
                    Path = externalPaths.getStoragePath();

                    if (data.size() > 2) {
                        Path = "/storage/sdcard0";
                    } else if (data.size() == 2) // if SdCard not Atteched.
                        Path = "Sdcard not found";
                    else
                        Path = "/storage/sdcard1";
                } catch (Exception e) {
                    Path = "Sdcard not found";
                }


            } else {
                if (data.size() == 2) // if SdCard not Atteched.
                    Path = "/storage/sdcard0";
                else
                    Path = "/storage/sdcard1";
            }

        } else {

            if (StorageType.equalsIgnoreCase("ExternalStorage")) {
                try {

                    StorageUtils.StorageInfo externalPaths = data.get(1);
                    Path = externalPaths.getStoragePath();

                    String tmp = Path.substring(Path.lastIndexOf("/"));
                    Path = "/storage" + tmp;
                } catch (Exception e) {
                    Path = "Sdcard not found";
                }
            } else {
                StorageUtils.StorageInfo externalPaths = data.get(0);
                Path = externalPaths.getStoragePath();
            }
        }
        return Path;
    }

    public static String getInternalFreeUpSpace(Context context, int returnType) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            StorageStatsManager storageStatsManager = (StorageStatsManager) context.getSystemService(Context.STORAGE_STATS_SERVICE);
            StorageManager storageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
            if (storageManager == null || storageStatsManager == null) {
                return "";
            }
            List<StorageVolume> storageVolumes = storageManager.getStorageVolumes();
            for (StorageVolume storageVolume : storageVolumes) {
                final String uuidStr = storageVolume.getUuid();

                if (storageVolume.isPrimary()) {
                    final UUID uuid = uuidStr == null ? StorageManager.UUID_DEFAULT : UUID.fromString(uuidStr);
                    try {
                        long freeMemory = storageStatsManager.getFreeBytes(uuid);
                        long totalMemory = storageStatsManager.getTotalBytes(uuid);
                        long usedUsd = storageStatsManager.getTotalBytes(uuid) - storageStatsManager.getFreeBytes(uuid);

                        switch (returnType) {
                            case RETURN_TYPE_TOTAL_MEMORY:
                                return String.valueOf(totalMemory);

                            case RETURN_TYPE_FREE_MEMORY:
                                return String.valueOf(freeMemory);

                            case RETURN_TYPE_USED_MEMORY:
                                return String.valueOf(usedUsd);

                            case RETURN_TYPE_FREE_TOTAL_COUNT:
                                return Formatter.formatFileSize(context, freeMemory) + " / " + Formatter.formatShortFileSize(context, totalMemory);

                            case RETURN_TYPE_USED_TOTAL_COUNT:
                                return Formatter.formatFileSize(context, usedUsd) + " / " + Formatter.formatShortFileSize(context, totalMemory);

                        }


                    } catch (Exception e) {
                        return getBelowOreoFreeUpSpace(context, returnType);
                    }
                }

            }
        } else {
            return getBelowOreoFreeUpSpace(context, returnType);
        }
        return "";
    }

    private static String getBelowOreoFreeUpSpace(Context context, int returnType) {
        File path = new File(String.valueOf(Environment.getDataDirectory()));
        StatFs stat = new StatFs(path.getPath());
        long BlockSize = 0;
        long availableBlocks = 0;
        long TotalBlocks = 0;
        BlockSize = stat.getBlockSizeLong();
        availableBlocks = stat.getAvailableBlocksLong();
        TotalBlocks = stat.getBlockCountLong();

        long freeMemory = availableBlocks * BlockSize;
        long totalMemory = TotalBlocks * BlockSize;
        long usedMemory = totalMemory - freeMemory;
        switch (returnType) {
            case RETURN_TYPE_TOTAL_MEMORY:
                return String.valueOf(totalMemory);

            case RETURN_TYPE_FREE_MEMORY:
                return String.valueOf(freeMemory);

            case RETURN_TYPE_USED_MEMORY:
                return String.valueOf(usedMemory);

            case RETURN_TYPE_FREE_TOTAL_COUNT:
                return Formatter.formatFileSize(context, freeMemory) + " / " + Formatter.formatShortFileSize(context, totalMemory);

            case RETURN_TYPE_USED_TOTAL_COUNT:
                return Formatter.formatFileSize(context, usedMemory) + " / " + Formatter.formatShortFileSize(context, totalMemory);
        }
        return "";
    }


    public static String getSdCardFreeUpSpace(Context context, int returnType) {
        File path = new File(Objects.requireNonNull(StoragePath(context, "ExternalStorage")));
        StatFs stat = new StatFs(path.getPath());

        long BlockSize = 0;
        long availableBlocks = 0;
        long TotalBlocks = 0;
        BlockSize = stat.getBlockSizeLong();
        availableBlocks = stat.getAvailableBlocksLong();
        TotalBlocks = stat.getBlockCountLong();
        long freeMemory = availableBlocks * BlockSize;
        long totalMemory = TotalBlocks * BlockSize;
        long usedMemory = totalMemory - freeMemory;
        switch (returnType) {
            case RETURN_TYPE_TOTAL_MEMORY:
                return String.valueOf(totalMemory);

            case RETURN_TYPE_FREE_MEMORY:
                return String.valueOf(freeMemory);

            case RETURN_TYPE_USED_MEMORY:
                return String.valueOf(usedMemory);

            case RETURN_TYPE_FREE_TOTAL_COUNT:
                return Formatter.formatFileSize(context, freeMemory) + " / " + Formatter.formatShortFileSize(context, totalMemory);

            case RETURN_TYPE_USED_TOTAL_COUNT:
                return Formatter.formatFileSize(context, usedMemory) + " / " + Formatter.formatShortFileSize(context, totalMemory);

        }
        return "";

    }

    public static Drawable getApplicationIcon(Context mContext, String path) {

        Drawable icon = null;
        try {

            try {
               /* ApplicationInfo app = mContext.getPackageManager().getApplicationInfo(pkgname, 0);
                icon = mContext.getPackageManager().getApplicationIcon(app);*/

                String APKFilePath = path; //For example...
                PackageManager pm = mContext.getPackageManager();
                PackageInfo pi = pm.getPackageArchiveInfo(APKFilePath, 0);

                // the secret are these two lines....
                pi.applicationInfo.sourceDir = APKFilePath;
                pi.applicationInfo.publicSourceDir = APKFilePath;

                icon = pi.applicationInfo.loadIcon(pm);
                //String   AppName = (String)pi.applicationInfo.loadLabel(pm);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (Exception e) {
        }

        return icon;
    }

    /**
     * Check is a manager is writable. Detects write issues on external SD card.
     *
     * @param file The manager
     * @return true if the manager is writable.
     */
    public static final boolean isWritable(final File file) {
        if (file == null)
            return false;
        boolean isExisting = file.exists();

        try {
            FileOutputStream output = new FileOutputStream(file, true);
            try {
                output.close();
            } catch (IOException e) {
                // do nothing.
            }
        } catch (FileNotFoundException e) {
            return false;
        }
        boolean result = file.canWrite();

        // Ensure that manager is not created during this process.
        if (!isExisting) {
            file.delete();
        }

        return result;
    }

    /**
     * Determine if a manager is on external sd card. (Kitkat or higher.)
     *
     * @param file The manager.
     * @return true if on external sd card.
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean isOnExtSdCard(final File file, Context c) {
        return getExtSdCardFolder(file, c) != null;
    }

    /**
     * Determine the main folder of the external SD card containing the given manager.
     *
     * @param file the manager.
     * @return The main folder of the external SD card containing this manager, if the manager is on an SD card. Otherwise,
     * null is returned.
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getExtSdCardFolder(final File file, Context context) {
        String[] extSdPaths = getExtSdCardPaths(context);
        try {
            for (int i = 0; i < extSdPaths.length; i++) {
                if (file.getCanonicalPath().startsWith(extSdPaths[i])) {
                    return extSdPaths[i];
                }
            }
        } catch (IOException e) {
            return null;
        }
        return null;
    }

    /**
     * Get a list of external SD card paths. (Kitkat or higher.)
     *
     * @return A list of external SD card paths.
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String[] getExtSdCardPaths(Context context) {
        List<String> paths = new ArrayList<String>();
        for (File file : ContextCompat.getExternalFilesDirs(context, "external")) {
            if (file != null && !file.equals(context.getExternalFilesDir("external"))) {
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

    /**
     * Check for a directory if it is possible to create files within this directory, either via normal writing or via
     * Storage Access Framework.
     *
     * @param folder The directory
     * @return true if it is possible to write in this directory.
     */
    public static final boolean isWritableNormalOrSaf(final File folder, Context c) {
        // Verify that this is a directory.
        if (folder == null)
            return false;
        if (!folder.exists() || !folder.isDirectory()) {
            return false;
        }

        // Find a non-existing manager in this directory.
        int i = 0;
        File file;
        do {
            String fileName = "AugendiagnoseDummyFile" + (++i);
            file = new File(folder, fileName);
        }
        while (file.exists());

        // First check regular writability
        if (isWritable(file)) {
            return true;
        }

        // Next check SAF writability.
        DocumentFile document = getDocumentFile(file, false, c);

        if (document == null) {
            return false;
        }

        // This should have created the manager - otherwise something is wrong with access URL.
        boolean result = document.canWrite() && file.exists();

        // Ensure that the dummy manager is not remaining.
        deleteFile(file, c);
        return result;
    }

    /**
     * Get a DocumentFile corresponding to the given manager (for writing on ExtSdCard on Android 5). If the manager is not
     * existing, it is created.
     *
     * @param file        The manager.
     * @param isDirectory flag indicating if the manager should be a directory.
     * @return The DocumentFile
     */
    public static DocumentFile getDocumentFile(final File file, final boolean isDirectory, Context context) {
        String baseFolder = getExtSdCardFolder(file, context);
        boolean originalDirectory = false;
        if (baseFolder == null) {
            return null;
        }

        String relativePath = null;
        try {
            String fullPath = file.getCanonicalPath();
            if (!baseFolder.equals(fullPath))
                relativePath = fullPath.substring(baseFolder.length() + 1);
            else originalDirectory = true;
        } catch (IOException e) {
            return null;
        } catch (Exception f) {
            originalDirectory = true;
            //continue
        }
        String as = (String) SupPref.getValue(context, SD_URI, "");

        Uri treeUri = null;
        if (as != null && !TextUtils.isEmpty(as)) treeUri = Uri.parse(as);
        if (treeUri == null) {
            return null;
        }

        // start with root of SD card and then parse through document tree.
        DocumentFile document = DocumentFile.fromTreeUri(context, treeUri);
        if (originalDirectory) return document;
        String[] parts = relativePath.split("\\/");
        if (document != null) {
            for (int i = 0; i < parts.length; i++) {
                if (parts[i] != null) {
                    if (document != null) {
                        DocumentFile nextDocument = document.findFile(parts[i]);

                        if (nextDocument == null) {
                            if ((i < parts.length - 1) || isDirectory) {
                                nextDocument = document.createDirectory(parts[i]);
                            } else {
                                nextDocument = document.createFile("image", parts[i]);
                            }
                        }
                        document = nextDocument;
                    }
                }
            }
        }
        return document;
    }

    /**
     * Delete a manager. May be even on external SD card.
     *
     * @param file the manager to be deleted.
     * @return True if successfully deleted.
     */
    public static final boolean deleteFile(@NonNull final File file, Context context) {
        // First try the normal deletion.
        boolean fileDelete = deleteFilesInFolder(file, context);
        if (file.delete() || fileDelete)
            return true;

        // Try with Storage Access Framework.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && TillsFl.isOnExtSdCard(file, context)) {

            DocumentFile document = getDocumentFile(file, false, context);
            if (document != null) {
                return document.delete();
            } else {
                return false;
            }

        }
        return !file.exists();
    }

    public static Uri getUriFromFile(final String path, Context context) {
        ContentResolver resolver = context.getContentResolver();

        Cursor filecursor = resolver.query(MediaStore.Files.getContentUri("external"),
                new String[]{BaseColumns._ID}, MediaStore.MediaColumns.DATA + " = ?",
                new String[]{path}, MediaStore.MediaColumns.DATE_ADDED + " desc");
        filecursor.moveToFirst();

        if (filecursor.isAfterLast()) {
            filecursor.close();
            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DATA, path);
            return resolver.insert(MediaStore.Files.getContentUri("external"), values);
        } else {
            int imageId = filecursor.getInt(filecursor.getColumnIndex(BaseColumns._ID));
            Uri uri = MediaStore.Files.getContentUri("external").buildUpon().appendPath(
                    Integer.toString(imageId)).build();
            filecursor.close();
            return uri;
        }
    }

    /**
     * Delete all files in a folder.
     *
     * @param folder the folder
     * @return true if successful.
     */
    public static final boolean deleteFilesInFolder(final File folder, Context context) {
        boolean totalSuccess = true;
        if (folder == null)
            return false;
        if (folder.isDirectory()) {
            for (File child : folder.listFiles()) {
                deleteFilesInFolder(child, context);
            }

            if (!folder.delete())
                totalSuccess = false;
        } else {

            if (!folder.delete())
                totalSuccess = false;
        }
        return totalSuccess;
    }

    @Nullable
    public static String getFullPathFromTreeUri(@Nullable final Uri treeUri, Context con) {
        if (treeUri == null) return null;
        String volumePath = getVolumePath(getVolumeIdFromTreeUri(treeUri), con);
        if (volumePath == null) return File.separator;
        if (volumePath.endsWith(File.separator))
            volumePath = volumePath.substring(0, volumePath.length() - 1);

        String documentPath = getDocumentPathFromTreeUri(treeUri);
        if (documentPath.endsWith(File.separator))
            documentPath = documentPath.substring(0, documentPath.length() - 1);

        if (documentPath.length() > 0) {
            if (documentPath.startsWith(File.separator))
                return volumePath + documentPath;
            else
                return volumePath + File.separator + documentPath;
        } else return volumePath;
    }

    @SuppressLint("ObsoleteSdkInt")
    private static String getVolumePath(final String volumeId, Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return null;
        try {
            StorageManager mStorageManager =
                    (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
            Class<?> storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
            Method getUuid = storageVolumeClazz.getMethod("getUuid");
            Method getPath = storageVolumeClazz.getMethod("getPath");
            Method isPrimary = storageVolumeClazz.getMethod("isPrimary");
            Object result = getVolumeList.invoke(mStorageManager);

            final int length = Array.getLength(result);
            for (int i = 0; i < length; i++) {
                Object storageVolumeElement = Array.get(result, i);
                String uuid = (String) getUuid.invoke(storageVolumeElement);
                Boolean primary = (Boolean) isPrimary.invoke(storageVolumeElement);

                // primary volume?
                if (primary && PRIMARY_VOLUME_NAME.equals(volumeId))
                    return (String) getPath.invoke(storageVolumeElement);

                // other volumes?
                if (uuid != null && uuid.equals(volumeId))
                    return (String) getPath.invoke(storageVolumeElement);
            }
            // not found.
            return null;
        } catch (Exception ex) {
            return null;
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static String getVolumeIdFromTreeUri(final Uri treeUri) {
        final String docId = DocumentsContract.getTreeDocumentId(treeUri);
        final String[] split = docId.split(":");
        if (split.length > 0) return split[0];
        else return null;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static String getDocumentPathFromTreeUri(final Uri treeUri) {
        final String docId = DocumentsContract.getTreeDocumentId(treeUri);
        final String[] split = docId.split(":");
        if ((split.length >= 2) && (split[1] != null)) return split[1];
        else return File.separator;
    }

    public static String getFileNameFromSource(String url) {
        if (url != null) {
            return url.substring(url.lastIndexOf('/') + 1);
        }
        return "";
    }

    public static String getMimeType(Context context, Uri uri) {
        String mimeType;
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
            ContentResolver cr = context.getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }

        if (mimeType == null || mimeType.trim().isEmpty()) {
            mimeType = "*/*";
        }

        return mimeType;
    }

    /**
     * Get a file from a Uri.
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     */
    public static File getFileFromUri(final Context context, final Uri uri) throws Exception {

        String path = null;

        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (DocumentsContract.isDocumentUri(context, uri)) { // TODO: 2015. 11. 17. KITKAT


                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];


                    if ("primary".equalsIgnoreCase(type)) {
                        path = Environment.getExternalStorageDirectory() + "/" + split[1];
                    }

                    // TODO handle non-primary volumes

                } else if (isDownloadsDocument(uri)) { // DownloadsProvider

                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    path = getDataColumn(context, contentUri, null, null);

                } else if (isMediaDocument(uri)) { // MediaProvider


                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{
                            split[1]
                    };

                    path = getDataColumn(context, contentUri, selection, selectionArgs);

                }/* else if (isGoogleDrive(uri)) { // Google Drive
                    String TAG = "isGoogleDrive";
                    path = TAG;
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(";");
                    final String acc = split[0];
                    final String doc = split[1];

                    *//*
                 * @details google drive document data. - acc , docId.
                 * *//*

                    return saveFileIntoExternalStorageByUri(context, uri);


                }*/ // MediaStore (and general)
            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                path = getDataColumn(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                path = uri.getPath();
            }

            return new File(path);
        } else {

            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            return new File(cursor.getString(cursor.getColumnIndex("_data")));
        }

    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is GoogleDrive.
     */

    public static boolean isGoogleDrive(Uri uri) {
        return uri.getAuthority().equalsIgnoreCase("com.google.android.apps.docs.storage");
    }

    public static File getTargetLocation(String filePath) {
        File file = new File(filePath);

        int count = 1;
        String ext, name;
        while (file.exists()) {
            ext = TillsFl.getExtension(file.getPath());
            name = file.getName();
            if (name.contains(".")) {
                name = name.substring(0, name.lastIndexOf("."));
            }
            name = name + " (" + count + ")." + ext;
            file = new File(file.getParent(), name);
        }

        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        return file;
    }

    public static void copy(InputStream in, OutputStream out) {

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                android.os.FileUtils.copy(in, out);
            } else {
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "copy: error :- " + e.getMessage());
        } finally {
            try {
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getOriginalFileName(String name) {
        String fileName = name;
        if (fileName.startsWith(".")) {
            fileName = fileName.substring(1);
        }

        int i = fileName.lastIndexOf(".bin");
        if (i > 0) {
            fileName = fileName.substring(0, i);
        }

        return fileName;
    }

    public String convertDuration(long duration) {
        String out = null;
        long hours = 0;
        try {
            hours = (duration / 3600000);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return out;
        }
        long remaining_minutes = (duration - (hours * 3600000)) / 60000;
        String minutes = String.valueOf(remaining_minutes);
        if (minutes.equals(0)) {
            minutes = "00";
        }
        long remaining_seconds = (duration - (hours * 3600000) - (remaining_minutes * 60000));
        String seconds = String.valueOf(remaining_seconds);
        if (seconds.length() < 2) {
            seconds = "00";
        } else {
            seconds = seconds.substring(0, 2);
        }

        if (hours > 0) {
            out = hours + ":" + minutes + ":" + seconds;
        } else {
            out = minutes + ":" + seconds;
        }

        return out;

    }

    public static class StorageUtils {

        private static final String TAG = "StorageUtils";

        public static List<StorageInfo> getStorageList() {

            List<StorageInfo> list = new ArrayList<StorageInfo>();
            String def_path = Environment.getExternalStorageDirectory().getPath();
            boolean def_path_removable = Environment.isExternalStorageRemovable();
            String def_path_state = Environment.getExternalStorageState();
            boolean def_path_available = def_path_state.equals(Environment.MEDIA_MOUNTED)
                    || def_path_state.equals(Environment.MEDIA_MOUNTED_READ_ONLY);
            boolean def_path_readonly = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY);

            HashSet<String> paths = new HashSet<String>();
            int cur_removable_number = 1;

            if (def_path_available) {
                paths.add(def_path);
                list.add(0, new StorageInfo(def_path, def_path_readonly, def_path_removable, def_path_removable ? cur_removable_number++ : -1));
            }

            BufferedReader buf_reader = null;
            try {
                buf_reader = new BufferedReader(new FileReader("/proc/mounts"));
                String line;

                while ((line = buf_reader.readLine()) != null) {

                    if (line.contains("vfat") || line.contains("/mnt")) {
                        StringTokenizer tokens = new StringTokenizer(line, " ");
                        String unused = tokens.nextToken(); //device
                        String mount_point = tokens.nextToken(); //mount point
                        if (paths.contains(mount_point)) {
                            continue;
                        }
                        unused = tokens.nextToken(); //file system
                        List<String> flags = Arrays.asList(tokens.nextToken().split(",")); //flags
                        boolean readonly = flags.contains("ro");

                        if (line.contains("/dev/block/vold")) {
                            if (!line.contains("/mnt/secure")
                                    && !line.contains("/mnt/asec")
                                    && !line.contains("/mnt/obb")
                                    && !line.contains("/dev/mapper")
                                    && !line.contains("tmpfs")) {
                                paths.add(mount_point);
                                list.add(new StorageInfo(mount_point, readonly, true, cur_removable_number++));
                            }
                        }
                    }
                }

            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                if (buf_reader != null) {
                    try {
                        buf_reader.close();
                    } catch (IOException ex) {
                    }
                }
            }
            return list;
        }


        public static class StorageInfo {

            public final String path;
            public final boolean readonly;
            public final boolean removable;
            public final int number;

            StorageInfo(String path, boolean readonly, boolean removable, int number) {
                this.path = path;
                this.readonly = readonly;
                this.removable = removable;
                this.number = number;
            }

            public int getStorageNumber() {
                return number;
            }

            public String getStoragePath() {
                return path;
            }

            public String getDisplayName() {
                StringBuilder res = new StringBuilder();
                if (!removable) {
                    res.append("Internal SD card" + " Path :- " + path);
                } else if (number > 1) {
                    res.append("SD card " + number + " Path :- " + path);
                } else {
                    res.append("SD card" + " Path:- " + path);
                }
                if (readonly) {
                    res.append(" (Read only)");
                }
                return res.toString();
            }
        }
    }

}
