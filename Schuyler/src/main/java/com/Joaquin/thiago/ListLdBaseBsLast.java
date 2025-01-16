package com.Joaquin.thiago;
import android.net.Uri;
import android.provider.MediaStore;

public abstract class ListLdBaseBsLast<T> extends ListBsLast<T> {
    public static final String VOLUME_NAME = "external";
    private ListFlPrpt mProperty;
    public ListLdBaseBsLast(){
        this(new ListFlPrpt(null,null));
    }
    public ListLdBaseBsLast(ListFlTp type){
        this(type.getProperty());
    }
    public ListLdBaseBsLast(ListFlPrpt property){
        this.mProperty = property;
    }
    @Override
    public Uri getQueryUri() {
        return MediaStore.Files.getContentUri(VOLUME_NAME);
    }
    @Override
    public String getSelections() {
        if(mProperty!=null)
            return mProperty.createSelection();
        return null;}
    @Override
    public String[] getSelectProjection() {
        return new String[]{
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.SIZE,
                MediaStore.Files.FileColumns.DISPLAY_NAME,
                MediaStore.Files.FileColumns.MIME_TYPE,
                MediaStore.Files.FileColumns.DATE_MODIFIED};}
    @Override
    public String[] getSelectionsArgs() {
        if(mProperty!=null)
            return mProperty.createSelectionArgs();
        return null;}
}
