package com.Joaquin.thiago;
import android.provider.MediaStore;
public abstract class ListBsLast<T> extends ListOnClBk {
    public abstract void onResult(T result);
    @Override
    public String getSelections() {
        return MediaStore.MediaColumns.SIZE + " > ?";
    }
    @Override
    public String[] getSelectionsArgs() {
        return new String[]{"0"};
    }
    @Override
    public String getSortOrderSql() {
        return MediaStore.MediaColumns.DATE_MODIFIED + " DESC";
    }}
