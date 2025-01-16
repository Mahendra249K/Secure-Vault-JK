package com.Joaquin.thiago;
import static android.provider.MediaStore.Files.FileColumns.DATE_MODIFIED;
import static android.provider.MediaStore.Files.FileColumns.MIME_TYPE;

import android.database.Cursor;
import android.provider.BaseColumns;
import android.provider.MediaStore;

import androidx.loader.content.Loader;

import java.util.ArrayList;
import java.util.List;
public abstract class ListBsLastVedFlLstld extends ListLdBaseBsLast<ListFlRlt> {
    public ListBsLastVedFlLstld() {}
    public ListBsLastVedFlLstld(ListFlTp type) {
        super(type);
    }
    public ListBsLastVedFlLstld(ListFlPrpt property) {
        super(property);
    }
    @Override
    public void onLoadFinish(Loader<Cursor> loader, Cursor data) {
        List<ListFlTerm> result = new ArrayList<>();
        ListFlTerm item;
        long sum_size = 0;
        while (data.moveToNext()) {
            item = new ListFlTerm();
            int audioId = data.getInt(data.getColumnIndexOrThrow(BaseColumns._ID));
            String path = data.getString(data.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
            long size = data.getLong(data.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE));
            String name = data.getString(data.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME));
            String mime = data.getString(data.getColumnIndexOrThrow(MIME_TYPE));
            long modified = data.getLong(data.getColumnIndexOrThrow(DATE_MODIFIED));
            item.setId(audioId);
            item.setDisplayName(name);
            item.setPath(path);
            item.setSize(size);
            item.setMime(mime);
            item.setModified(modified);
            result.add(item);
            sum_size += size;
        }
        onResult(new ListFlRlt(sum_size,result));
    }

}
