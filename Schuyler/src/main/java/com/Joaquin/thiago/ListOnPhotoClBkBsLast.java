package com.Joaquin.thiago;
import static android.provider.BaseColumns._ID;
import static android.provider.MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME;
import static android.provider.MediaStore.Images.ImageColumns.BUCKET_ID;
import static android.provider.MediaStore.MediaColumns.DATA;
import static android.provider.MediaStore.MediaColumns.DATE_MODIFIED;
import static android.provider.MediaStore.MediaColumns.DISPLAY_NAME;
import static android.provider.MediaStore.MediaColumns.MIME_TYPE;
import static android.provider.MediaStore.MediaColumns.SIZE;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.loader.content.Loader;

import java.util.ArrayList;
import java.util.List;
public abstract class ListOnPhotoClBkBsLast extends ListBsLast<ListPtRslt> {
    @Override
    public void onLoadFinish(Loader<Cursor> loader, Cursor data) {
        List<ListPtFld> folders = new ArrayList<>();
        List<ListIdPic> allPhotos = new ArrayList<>();
        if(data == null){
            onResult(new ListPtRslt(folders,allPhotos));
            return;}
        ListPtFld folder;
        ListIdPic item;
        long sum_size = 0;
        while (data.moveToNext()) {
            String folderId = data.getString(data.getColumnIndexOrThrow(BUCKET_ID));
            String folderName = data.getString(data.getColumnIndexOrThrow(BUCKET_DISPLAY_NAME));
            int imageId = data.getInt(data.getColumnIndexOrThrow(_ID));
            String name = data.getString(data.getColumnIndexOrThrow(DISPLAY_NAME));
            long size = data.getLong(data.getColumnIndexOrThrow(SIZE));
            String path = data.getString(data.getColumnIndexOrThrow(DATA));
            long modified = data.getLong(data.getColumnIndexOrThrow(DATE_MODIFIED));
            String mimeType = data.getString(data.getColumnIndexOrThrow(MIME_TYPE));
            folder = new ListPtFld();
            folder.setId(folderId);
            folder.setName(folderName);
            item = new ListIdPic(imageId,name,path,size,modified,mimeType);
            if(folders.contains(folder)){
                folders.get(folders.indexOf(folder)).addItem(item);
            }else{
                folder.setCover(path);
                folder.addItem(item);
                folders.add(folder);}
            allPhotos.add(item);
            sum_size += size;}onResult(new ListPtRslt(folders,allPhotos,sum_size));}
    @Override
    public String[] getSelectProjection() {
        String[] PROJECTION = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media.DATE_MODIFIED,
                MediaStore.Video.Media.MIME_TYPE};
        return PROJECTION;}
    @Override
    public Uri getQueryUri() {
        return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    }}
