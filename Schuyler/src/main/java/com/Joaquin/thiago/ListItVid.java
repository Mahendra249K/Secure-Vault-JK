package com.Joaquin.thiago;
public class ListItVid extends ListTermBs {
    private long duration;
    public boolean checked;
    public ListItVid(int anInt, String displayName, String oripath, String newpath, long size, long duration, String mimeType) {
            this.id = anInt;
            this.displayName = displayName;
            this.path = oripath;
            this.newPath = newpath;
            this.size = size;
            this.duration = duration;
            this.mimeType = mimeType;}
    public ListItVid(int id, String displayName, String path, long size, long duration, String mimeType) {
        super(id, displayName, path, size, duration, mimeType);
        this.duration = duration;}
    public ListItVid(int id, String displayName, String path, long size, long modified, long duration, String mimeType) {
        super(id, displayName, path, size, modified, mimeType);
        this.duration = duration;}
    public ListItVid() {}
    public long getDuration() {
        return duration;
    }
    public void setDuration(long duration) {
        this.duration = duration;
    }
    public boolean isChecked() {
        return checked;
    }
    public void setChecked(boolean checked) {
        this.checked = checked;
    }
    public static final String TABLE_NAME = "tabVideo";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DNAME = "displayName";
    public static final String COLUMN_ORIPATH = "oripath";
    public static final String COLUMN_NEWPATH = "newpath";
    public static final String COLUMN_SIZE = "filesize";
    public static final String COLUMN_MIMETYPE = "mimeType";
    public static final String COLUMN_CLOUD_FILE_ID = "cloud_file_id";
    public static final String COLUMN_TRASH = "trash";
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_DNAME + " TEXT," + COLUMN_ORIPATH + " TEXT," + COLUMN_NEWPATH + " TEXT," + COLUMN_SIZE + " LONG DEFAULT 0," + COLUMN_MIMETYPE + " TEXT," + COLUMN_CLOUD_FILE_ID + " TEXT DEFAULT null1," + COLUMN_TRASH + " INTEGER DEFAULT 0" + ")";}
