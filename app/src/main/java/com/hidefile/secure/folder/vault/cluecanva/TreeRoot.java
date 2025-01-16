package com.hidefile.secure.folder.vault.cluecanva;

import com.Joaquin.thiago.ListTermBs;

public class TreeRoot extends ListTermBs {

    public static final String TABLE_NAME = "tabFiles";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DNAME = "displayName";
    public static final String COLUMN_ORIPATH = "oripath";
    public static final String COLUMN_NEWPATH = "newpath";
    public static final String COLUMN_SIZE = "filesize";
    public static final String COLUMN_MIMETYPE = "mimeType";
    public static final String COLUMN_CLOUD_FILE_ID = "cloud_file_id";
    public static final String COLUMN_TRASH = "trash";
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_DNAME + " TEXT,"
                    + COLUMN_ORIPATH + " TEXT,"
                    + COLUMN_NEWPATH + " TEXT,"
                    + COLUMN_SIZE + " LONG,"
                    + COLUMN_MIMETYPE + " TEXT,"
                    + COLUMN_CLOUD_FILE_ID + " TEXT DEFAULT null1,"
                    + COLUMN_TRASH + " INTEGER DEFAULT 0"
                    + ")";
    public String displayName;
    public String path;
    public String newPath;
    public boolean checked;
    int id;
    long size;
    String mimeType;

    public TreeRoot(int anInt, String displayName, String oripath, String newpath, long size, String mimeType) {
        this.id = anInt;
        this.displayName = displayName;
        this.path = oripath;
        this.newPath = newpath;
        this.size = size;
        this.mimeType = mimeType;

    }

    public TreeRoot() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getNewPath() {
        return newPath;
    }

    public void setNewPath(String newPath) {
        this.newPath = newPath;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return "TreeRoot{" +
                "id=" + id +
                ", displayName='" + displayName + '\'' +
                ", path='" + path + '\'' +
                ", newPath='" + newPath + '\'' +
                ", size=" + size +
                ", mimeType='" + mimeType + '\'' +
                ", checked=" + checked +
                '}';
    }
}
