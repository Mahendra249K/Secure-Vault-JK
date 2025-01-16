package com.Joaquin.thiago;
public class ListIdPic extends ListTermBs {
    public boolean checked;
    public ListIdPic(int id, String displayName, String path) {
        super(id, displayName, path);
    }
    public ListIdPic(int id, String displayName, String path, long size) {
        super(id, displayName, path, size);}
    public ListIdPic(int id, String displayName, String path, long size, long modified, String mimeType) {
        super(id, displayName, path, size, modified, mimeType);}
    public ListIdPic(int anInt, String displayName, String oripath, String newpath, long size, String mimeType, String cId, int trash) {
        this.id = anInt;
        this.displayName = displayName;
        this.path = oripath;
        this.newPath = newpath;
        this.size = size;
        this.mimeType = mimeType;}
    public ListIdPic() {}
    public boolean isChecked() {
        return checked;
    }
    public void setChecked(boolean checked) {
        this.checked = checked;
    }
    @Override
    public String toString() {return "ListIdPic{" + "id=" + id + ", displayName='" + displayName + '\'' + ", path='" + path + '\'' + ", newPath='" + newPath + '\'' + ", size=" + size + ", modified=" + modified + ", mimeType='" + mimeType + '\'' + ", couldId='" + couldId + '\'' + ", isTrash=" + isTrash + ", checked=" + checked + '}';}
    public static final String TABLE_NAME = "tabImage";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DNAME = "displayName";
    public static final String COLUMN_ORIPATH = "oripath";
    public static final String COLUMN_NEWPATH = "newpath";
    public static final String COLUMN_SIZE = "filesize";
    public static final String COLUMN_MIMETYPE = "mimeType";
    public static final String COLUMN_CLOUD_FILE_ID = "cloud_file_id";
    public static final String COLUMN_TRASH = "trash";
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_DNAME + " TEXT," + COLUMN_ORIPATH + " TEXT," + COLUMN_NEWPATH + " TEXT," + COLUMN_SIZE + " LONG," + COLUMN_MIMETYPE + " TEXT," + COLUMN_CLOUD_FILE_ID + " TEXT DEFAULT null1," + COLUMN_TRASH + " INTEGER DEFAULT 0" + ")";}
