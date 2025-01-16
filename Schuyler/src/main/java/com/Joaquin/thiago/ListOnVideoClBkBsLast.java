package com.Joaquin.thiago;
import static android.provider.BaseColumns._ID;
import static android.provider.MediaStore.MediaColumns.DATA;
import static android.provider.MediaStore.MediaColumns.DATE_MODIFIED;
import static android.provider.MediaStore.MediaColumns.DISPLAY_NAME;
import static android.provider.MediaStore.MediaColumns.MIME_TYPE;
import static android.provider.MediaStore.MediaColumns.SIZE;
import static android.provider.MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME;
import static android.provider.MediaStore.Video.VideoColumns.BUCKET_ID;
import static android.provider.MediaStore.Video.VideoColumns.DURATION;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.loader.content.Loader;

import java.util.ArrayList;
import java.util.List;
public abstract class ListOnVideoClBkBsLast extends ListBsLast<ListVidReal> {
    @Override
    public void onLoadFinish(Loader<Cursor> loader, Cursor data) {
        List<ListVedFl> folders = new ArrayList<>();
        ListVedFl folder;
        ListItVid item;
        long sum_size = 0;
        List<ListItVid> items = new ArrayList<>();
        while (data.moveToNext()) {
            long duration = data.getLong(data.getColumnIndexOrThrow(DURATION));
            if (duration != 0) {
                String folderId = data.getString(data.getColumnIndexOrThrow(BUCKET_ID));
                String folderName = data.getString(data.getColumnIndexOrThrow(BUCKET_DISPLAY_NAME));
                int videoId = data.getInt(data.getColumnIndexOrThrow(_ID));
                String name = data.getString(data.getColumnIndexOrThrow(DISPLAY_NAME));
                String path = data.getString(data.getColumnIndexOrThrow(DATA));
                Log.e("onLoadFinish", "duration: " + duration);
                long size = data.getLong(data.getColumnIndexOrThrow(SIZE));
                long modified = data.getLong(data.getColumnIndexOrThrow(DATE_MODIFIED));
                String mimeType = data.getString(data.getColumnIndexOrThrow(MIME_TYPE));
                item = new ListItVid(videoId, name, path, size, modified, duration, mimeType);
                folder = new ListVedFl();
                folder.setId(folderId);
                folder.setName(folderName);
                if (folders.contains(folder)) {
                    folders.get(folders.indexOf(folder)).addItem(item);
                } else {
                    folder.addItem(item);
                    folders.add(folder);
                }
                items.add(item);
                sum_size += size;
                onResult(new ListVidReal(folders, items, sum_size));
            }
        }
    }
    @Override
    public String[] getSelectProjection() {
        String[] PROJECTION = {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.BUCKET_ID,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DURATION,
                MediaStore.MediaColumns.SIZE,
                MediaStore.Video.Media.DATE_MODIFIED,
                MediaStore.Video.Media.MIME_TYPE};return PROJECTION;}
    @Override
    public Uri getQueryUri() {
        return MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
    }}

