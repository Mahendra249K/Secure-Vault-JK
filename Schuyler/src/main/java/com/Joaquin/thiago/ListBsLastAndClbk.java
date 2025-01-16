package com.Joaquin.thiago;
import static android.provider.BaseColumns._ID;
import static android.provider.MediaStore.Audio.AudioColumns.DURATION;
import static android.provider.MediaStore.MediaColumns.DATA;
import static android.provider.MediaStore.MediaColumns.DATE_MODIFIED;
import static android.provider.MediaStore.MediaColumns.DISPLAY_NAME;
import static android.provider.MediaStore.MediaColumns.SIZE;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.loader.content.Loader;

import java.util.ArrayList;
import java.util.List;
public abstract class ListBsLastAndClbk extends ListBsLast<ListAdRels> {
    @Override
    public void onLoadFinish(Loader<Cursor> loader, Cursor data) {
        List<ListAudMd> result = new ArrayList<>();
        ListAudMd item;
        long sum_size = 0;
        while (data.moveToNext()) {
            item = new ListAudMd();
            int audioId = data.getInt(data.getColumnIndexOrThrow(_ID));
            String name = data.getString(data.getColumnIndexOrThrow(DISPLAY_NAME));
            String path = data.getString(data.getColumnIndexOrThrow(DATA));
            long duration = data.getLong(data.getColumnIndexOrThrow(DURATION));
            long size = data.getLong(data.getColumnIndexOrThrow(SIZE));
            long modified = data.getLong(data.getColumnIndexOrThrow(DATE_MODIFIED));
            item.setId(audioId);
            item.setDisplayName(name);
            item.setPath(path);
            item.setDuration(duration);
            item.setSize(size);
            item.setModified(modified);
            result.add(item);
            sum_size += size;}onResult(new ListAdRels(sum_size,result));}
    @Override
    public Uri getQueryUri() {
        return MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    }
    @Override
    public String[] getSelectProjection() {
        String[] PROJECTION = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION,
                MediaStore.MediaColumns.SIZE,
                MediaStore.Audio.Media.DATE_MODIFIED,
                MediaStore.Video.Media.MIME_TYPE};return PROJECTION;}}
