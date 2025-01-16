package com.hidefile.secure.folder.vault.cluecanva;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.Joaquin.thiago.ListIdPic;
import com.Joaquin.thiago.ListItVid;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RDbhp extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "StayHide.db";
    private static final int DATABASE_VERSION = 2;
    private static RDbhp sInstance;
    public RDbhp(Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }
    public static RDbhp getInstance(Context context) {
        if (sInstance == null) {
            synchronized (RDbhp.class) {
                sInstance = new RDbhp(context);
            }
        }
        return sInstance;
    }

    public static boolean checkDataBase(Context mContext) {
        File databasePath = mContext.getDatabasePath(TillsPth.hideImage + "/" + DATABASE_NAME);
        return databasePath.exists();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ListIdPic.CREATE_TABLE);
        db.execSQL(ListItVid.CREATE_TABLE);
        db.execSQL(TreeRoot.CREATE_TABLE);
        db.execSQL(Rutin.MAKETBL);
        db.execSQL(BookmarkEm.CREATE_PASS_BOOKMARK);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion == 2) {
            db.execSQL(Rutin.STRING1);
            db.execSQL(Rutin.STRING2);
        } else {
            db.execSQL("DROP TABLE IF EXISTS " + ListIdPic.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + ListItVid.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + TreeRoot.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + Rutin.ABLAME);
            db.execSQL("DROP TABLE IF EXISTS " + BookmarkEm.TABLE_BOOKMARKS_PASS);
            onCreate(db);
        }
    }
    public long insertUser(String pwd, String squestion, String answer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Rutin.CLPWD, pwd);
        values.put(Rutin.CLMSQCM, squestion);
        values.put(Rutin.CLNANS, answer);
        long id = db.insert(Rutin.ABLAME, null, values);
        db.close();
        return id;
    }

    public long insertUser(String pwd, String squestion, String answer, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Rutin.CLPWD, pwd);
        values.put(Rutin.CLMSQCM, squestion);
        values.put(Rutin.CLNANS, answer);
        values.put(Rutin.CLMAIL, email);
        long id = db.insert(Rutin.ABLAME, null, values);
        db.close();
        return id;
    }

    public int updateUserEmail(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Rutin.CLMAIL, email);
        return db.update(
                Rutin.ABLAME,
                values,
                Rutin.MNID + "=?",
                new String[]{String.valueOf(1)});
    }

    public Rutin getUser(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Rutin.ABLAME,
                new String[]{
                        Rutin.MNID,
                        Rutin.CLPWD,
                        Rutin.CLMSQCM,
                        Rutin.CLNANS,
                        Rutin.CLMAIL,
                        Rutin.CLMFPIN
                }, Rutin.MNID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        Rutin note = new Rutin(0, "", "", "", "", "");
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                note = new Rutin(
                        cursor.getInt(cursor.getColumnIndex(Rutin.MNID)),
                        cursor.getString(cursor.getColumnIndex(Rutin.CLPWD)),
                        cursor.getString(cursor.getColumnIndex(Rutin.CLMSQCM)),
                        cursor.getString(cursor.getColumnIndex(Rutin.CLNANS)),
                        cursor.getString(cursor.getColumnIndex(Rutin.CLMAIL)),
                        cursor.getString(cursor.getColumnIndex(Rutin.CLMFPIN)));
            }
            cursor.close();
        }

        db.close();
        return note;
    }

    public int updatePIN(String pin) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Rutin.CLPWD, pin);
        // updating row
        return db.update(Rutin.ABLAME, values, Rutin.MNID + " = ?", new String[]{String.valueOf(1)});
    }

    public Rutin getUserData() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Rutin.ABLAME,
                new String[]{
                        Rutin.MNID,
                        Rutin.CLPWD,
                        Rutin.CLMSQCM,
                        Rutin.CLNANS,
                        Rutin.CLMAIL,
                        Rutin.CLMFPIN
                },
                null, null, null, null, null);

        Rutin note = new Rutin(0, "", "", "", "", "");
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                note = new Rutin(
                        cursor.getInt(cursor.getColumnIndex(Rutin.MNID)),
                        cursor.getString(cursor.getColumnIndex(Rutin.CLPWD)),
                        cursor.getString(cursor.getColumnIndex(Rutin.CLMSQCM)),
                        cursor.getString(cursor.getColumnIndex(Rutin.CLNANS)),
                        cursor.getString(cursor.getColumnIndex(Rutin.CLMAIL)),
                        cursor.getString(cursor.getColumnIndex(Rutin.CLMFPIN)));
            }
            cursor.close();
        }
        db.close();
        return note;
    }

    public int updateUserForgotPin(String forgotPin) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Rutin.CLMFPIN, forgotPin);
        // updating row
        return db.update(Rutin.ABLAME, values, Rutin.MNID + " = ?", new String[]{String.valueOf(1)});
    }

    public int updateUserPinSecurityQue(String pin, String questionValue, String ans) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Rutin.CLPWD, pin);
        values.put(Rutin.CLMSQCM, questionValue);
        values.put(Rutin.CLNANS, ans);
        // updating row
        return db.update(Rutin.ABLAME, values, Rutin.MNID + " = ?", new String[]{String.valueOf(1)});
    }

    //============= tab TrueCopy============
    public long insertImage(String displayName, String oripath, String newpath, long size, String mimeType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ListIdPic.COLUMN_DNAME, displayName);
        values.put(ListIdPic.COLUMN_ORIPATH, oripath);
        values.put(ListIdPic.COLUMN_NEWPATH, newpath);
        values.put(ListIdPic.COLUMN_SIZE, size);
        values.put(ListIdPic.COLUMN_MIMETYPE, mimeType);
        values.put(ListIdPic.COLUMN_CLOUD_FILE_ID, "null1");
        values.put(ListIdPic.COLUMN_TRASH, 0);
        long id = db.insert(ListIdPic.TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public ListIdPic getImages(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(ListIdPic.TABLE_NAME,
                new String[]{ListIdPic.COLUMN_ID, ListIdPic.COLUMN_DNAME, ListIdPic.COLUMN_ORIPATH, ListIdPic.COLUMN_NEWPATH, ListIdPic.COLUMN_SIZE, ListIdPic.COLUMN_MIMETYPE, ListIdPic.COLUMN_CLOUD_FILE_ID, ListIdPic.COLUMN_TRASH}, ListIdPic.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        ListIdPic note = null;
        if (cursor != null) {
            cursor.moveToFirst();
            note = new ListIdPic(
                    cursor.getInt(cursor.getColumnIndex(ListIdPic.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(ListIdPic.COLUMN_DNAME)),
                    cursor.getString(cursor.getColumnIndex(ListIdPic.COLUMN_ORIPATH)),
                    cursor.getString(cursor.getColumnIndex(ListIdPic.COLUMN_NEWPATH)),
                    cursor.getLong(cursor.getColumnIndex(ListIdPic.COLUMN_SIZE)),
                    cursor.getString(cursor.getColumnIndex(ListIdPic.COLUMN_MIMETYPE)),
                    cursor.getString(cursor.getColumnIndex(ListIdPic.COLUMN_CLOUD_FILE_ID)),
                    cursor.getInt(cursor.getColumnIndex(ListIdPic.COLUMN_TRASH)));
            cursor.close();
        }
        return note;
    }

    public ArrayList<ListIdPic> getAllImages() {
        ArrayList<ListIdPic> notes = new ArrayList<>();
        String[] columns = new String[]{ListIdPic.COLUMN_ID, ListIdPic.COLUMN_DNAME, ListIdPic.COLUMN_ORIPATH, ListIdPic.COLUMN_NEWPATH, ListIdPic.COLUMN_SIZE, ListIdPic.COLUMN_MIMETYPE, ListIdPic.COLUMN_CLOUD_FILE_ID, ListIdPic.COLUMN_TRASH};
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(ListIdPic.TABLE_NAME, columns, ListIdPic.COLUMN_TRASH + "=?", new String[]{"0"}, null, null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                do {
                    ListIdPic note = new ListIdPic();
                    note.setId(cursor.getInt(cursor.getColumnIndex(ListIdPic.COLUMN_ID)));
                    note.setDisplayName(cursor.getString(cursor.getColumnIndex(ListIdPic.COLUMN_DNAME)));
                    note.setPath(cursor.getString(cursor.getColumnIndex(ListIdPic.COLUMN_ORIPATH)));
                    note.setNewPath(cursor.getString(cursor.getColumnIndex(ListIdPic.COLUMN_NEWPATH)));
                    note.setSize(cursor.getLong(cursor.getColumnIndex(ListIdPic.COLUMN_SIZE)));
                    note.setMimeType(cursor.getString(cursor.getColumnIndex(ListIdPic.COLUMN_MIMETYPE)));
                    note.setCouldId(cursor.getString(cursor.getColumnIndex(ListIdPic.COLUMN_CLOUD_FILE_ID)));
                    note.setIsTrash(cursor.getInt(cursor.getColumnIndex(ListIdPic.COLUMN_TRASH)));
                    notes.add(note);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();
        return notes;
    }

    public ArrayList<ListIdPic> getTrashImages() {
        ArrayList<ListIdPic> notes = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + ListIdPic.TABLE_NAME + " ORDER BY " + ListIdPic.COLUMN_ID + " DESC";
        String[] columns = new String[]{ListIdPic.COLUMN_ID, ListIdPic.COLUMN_DNAME, ListIdPic.COLUMN_ORIPATH, ListIdPic.COLUMN_NEWPATH, ListIdPic.COLUMN_SIZE, ListIdPic.COLUMN_MIMETYPE, ListIdPic.COLUMN_CLOUD_FILE_ID, ListIdPic.COLUMN_TRASH};

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(ListIdPic.TABLE_NAME, columns, ListIdPic.COLUMN_TRASH + "=?", new String[]{"1"}, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    ListIdPic note = new ListIdPic();
                    note.setId(cursor.getInt(cursor.getColumnIndex(ListIdPic.COLUMN_ID)));
                    note.setDisplayName(cursor.getString(cursor.getColumnIndex(ListIdPic.COLUMN_DNAME)));
                    note.setPath(cursor.getString(cursor.getColumnIndex(ListIdPic.COLUMN_ORIPATH)));
                    note.setNewPath(cursor.getString(cursor.getColumnIndex(ListIdPic.COLUMN_NEWPATH)));
                    note.setSize(cursor.getLong(cursor.getColumnIndex(ListIdPic.COLUMN_SIZE)));
                    note.setMimeType(cursor.getString(cursor.getColumnIndex(ListIdPic.COLUMN_MIMETYPE)));
                    note.setCouldId(cursor.getString(cursor.getColumnIndex(ListIdPic.COLUMN_CLOUD_FILE_ID)));
                    note.setIsTrash(cursor.getInt(cursor.getColumnIndex(ListIdPic.COLUMN_TRASH)));
//                    File file = new File(note.getNewPath());
//                    if (file.isFile()) {
                    notes.add(note);
//                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();
        return notes;
    }

    public ArrayList<ListIdPic> getBackupImages() {
        ArrayList<ListIdPic> notes = new ArrayList<>();
        String[] columns = new String[]{ListIdPic.COLUMN_ID, ListIdPic.COLUMN_DNAME, ListIdPic.COLUMN_ORIPATH, ListIdPic.COLUMN_NEWPATH, ListIdPic.COLUMN_SIZE, ListIdPic.COLUMN_MIMETYPE, ListIdPic.COLUMN_CLOUD_FILE_ID, ListIdPic.COLUMN_TRASH};
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(ListIdPic.TABLE_NAME, columns, ListIdPic.COLUMN_CLOUD_FILE_ID + "=?", new String[]{"null1"}, null, null, null);
        if (cursor != null) {
            Log.e("TAG", "getBackupImages: " + cursor.getCount());
            if (cursor.moveToFirst()) {
                do {
                    ListIdPic note = new ListIdPic();
                    note.setId(cursor.getInt(cursor.getColumnIndex(ListIdPic.COLUMN_ID)));
                    note.setDisplayName(cursor.getString(cursor.getColumnIndex(ListIdPic.COLUMN_DNAME)));
                    note.setPath(cursor.getString(cursor.getColumnIndex(ListIdPic.COLUMN_ORIPATH)));
                    note.setNewPath(cursor.getString(cursor.getColumnIndex(ListIdPic.COLUMN_NEWPATH)));
                    note.setSize(cursor.getLong(cursor.getColumnIndex(ListIdPic.COLUMN_SIZE)));
                    note.setMimeType(cursor.getString(cursor.getColumnIndex(ListIdPic.COLUMN_MIMETYPE)));
                    note.setCouldId(cursor.getString(cursor.getColumnIndex(ListIdPic.COLUMN_CLOUD_FILE_ID)));
                    note.setIsTrash(cursor.getInt(cursor.getColumnIndex(ListIdPic.COLUMN_TRASH)));
                    notes.add(note);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();
        return notes;
    }

    public ArrayList<ListIdPic> getRestoreImages() {
        ArrayList<ListIdPic> notes = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + ListIdPic.TABLE_NAME + " ORDER BY " + ListIdPic.COLUMN_ID + " DESC";
        String[] columns = new String[]{ListIdPic.COLUMN_ID, ListIdPic.COLUMN_DNAME, ListIdPic.COLUMN_ORIPATH, ListIdPic.COLUMN_NEWPATH, ListIdPic.COLUMN_SIZE, ListIdPic.COLUMN_MIMETYPE, ListIdPic.COLUMN_CLOUD_FILE_ID, ListIdPic.COLUMN_TRASH};

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(ListIdPic.TABLE_NAME, columns, ListIdPic.COLUMN_TRASH + "=?", new String[]{"2"}, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    ListIdPic note = new ListIdPic();
                    note.setId(cursor.getInt(cursor.getColumnIndex(ListIdPic.COLUMN_ID)));
                    note.setDisplayName(cursor.getString(cursor.getColumnIndex(ListIdPic.COLUMN_DNAME)));
                    note.setPath(cursor.getString(cursor.getColumnIndex(ListIdPic.COLUMN_ORIPATH)));
                    note.setNewPath(cursor.getString(cursor.getColumnIndex(ListIdPic.COLUMN_NEWPATH)));
                    note.setSize(cursor.getLong(cursor.getColumnIndex(ListIdPic.COLUMN_SIZE)));
                    note.setMimeType(cursor.getString(cursor.getColumnIndex(ListIdPic.COLUMN_MIMETYPE)));
                    note.setCouldId(cursor.getString(cursor.getColumnIndex(ListIdPic.COLUMN_CLOUD_FILE_ID)));
                    note.setIsTrash(cursor.getInt(cursor.getColumnIndex(ListIdPic.COLUMN_TRASH)));
                    notes.add(note);
                } while (cursor.moveToNext());
            }

            cursor.close();
        }
        db.close();
        return notes;
    }

    public int getNotesCount() {
        String countQuery = "SELECT  * FROM " + ListIdPic.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = 0;
        if (cursor != null) {
            count = cursor.getCount();
            cursor.close();
        }
        db.close();
        return count;
    }

    public int updateImgCloudID(int id, String couldfileide) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ListIdPic.COLUMN_CLOUD_FILE_ID, couldfileide);
        int update = db.update(ListIdPic.TABLE_NAME, values, ListIdPic.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return update;
    }

    public int moveTrashPhoto(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ListIdPic.COLUMN_TRASH, 1);
        int update = db.update(ListIdPic.TABLE_NAME, values, ListIdPic.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return update;
    }

    public int reCoverPhoto(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ListIdPic.COLUMN_TRASH, 0);
        int update = db.update(ListIdPic.TABLE_NAME, values, ListIdPic.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return update;
    }


    public int deletePhotoTrash(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ListIdPic.COLUMN_TRASH, 2);
        int update = db.update(ListIdPic.TABLE_NAME, values, ListIdPic.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return update;
    }

    public void deletePhotoItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ListIdPic.TABLE_NAME, ListIdPic.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
    public long insertVideo(String displayName, String oripath, String newpath, long size, String mimeType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ListItVid.COLUMN_DNAME, displayName);
        values.put(ListItVid.COLUMN_ORIPATH, oripath);
        values.put(ListItVid.COLUMN_NEWPATH, newpath);
        values.put(ListItVid.COLUMN_SIZE, size);
        values.put(ListItVid.COLUMN_MIMETYPE, mimeType);
        values.put(ListItVid.COLUMN_CLOUD_FILE_ID, "null1");
        values.put(ListItVid.COLUMN_TRASH, 0);

        long id = db.insert(ListItVid.TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public ArrayList<ListItVid> getAllVideos() {
        ArrayList<ListItVid> notes = new ArrayList<>();
        String[] columns = new String[]{ListItVid.COLUMN_ID, ListItVid.COLUMN_DNAME, ListItVid.COLUMN_ORIPATH, ListItVid.COLUMN_NEWPATH, ListItVid.COLUMN_SIZE, ListItVid.COLUMN_MIMETYPE, ListItVid.COLUMN_CLOUD_FILE_ID, ListItVid.COLUMN_TRASH};
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(ListItVid.TABLE_NAME, columns, ListItVid.COLUMN_TRASH + "=?", new String[]{"0"}, null, null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        ListItVid note = new ListItVid();
                        note.setId(cursor.getInt(cursor.getColumnIndex(ListItVid.COLUMN_ID)));
                        note.setDisplayName(cursor.getString(cursor.getColumnIndex(ListItVid.COLUMN_DNAME)));
                        note.setPath(cursor.getString(cursor.getColumnIndex(ListItVid.COLUMN_ORIPATH)));
                        note.setNewPath(cursor.getString(cursor.getColumnIndex(ListItVid.COLUMN_NEWPATH)));
                        note.setSize(cursor.getLong(cursor.getColumnIndex(ListItVid.COLUMN_SIZE)));
                        note.setMimeType(cursor.getString(cursor.getColumnIndex(ListItVid.COLUMN_MIMETYPE)));
                        note.setCouldId(cursor.getString(cursor.getColumnIndex(ListItVid.COLUMN_CLOUD_FILE_ID)));
                        note.setIsTrash(cursor.getInt(cursor.getColumnIndex(ListItVid.COLUMN_TRASH)));
                        notes.add(note);
                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
        }
        db.close();
        return notes;
    }

    public ArrayList<ListItVid> getBackupVideos() {
        ArrayList<ListItVid> notes = new ArrayList<>();
        String[] columns = new String[]{ListItVid.COLUMN_ID, ListItVid.COLUMN_DNAME, ListItVid.COLUMN_ORIPATH, ListItVid.COLUMN_NEWPATH, ListItVid.COLUMN_SIZE, ListItVid.COLUMN_MIMETYPE, ListItVid.COLUMN_CLOUD_FILE_ID, ListItVid.COLUMN_TRASH};
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(ListItVid.TABLE_NAME, columns, ListItVid.COLUMN_CLOUD_FILE_ID + "=?", new String[]{"null1"}, null, null, null);
        if (cursor != null) {
            Log.e("TAG", "getBackupImages: " + cursor.getCount());
            if (cursor.moveToFirst()) {
                do {
                    ListItVid note = new ListItVid();
                    note.setId(cursor.getInt(cursor.getColumnIndex(ListItVid.COLUMN_ID)));
                    note.setDisplayName(cursor.getString(cursor.getColumnIndex(ListItVid.COLUMN_DNAME)));
                    note.setPath(cursor.getString(cursor.getColumnIndex(ListItVid.COLUMN_ORIPATH)));
                    note.setNewPath(cursor.getString(cursor.getColumnIndex(ListItVid.COLUMN_NEWPATH)));
                    note.setSize(cursor.getLong(cursor.getColumnIndex(ListItVid.COLUMN_SIZE)));
                    note.setMimeType(cursor.getString(cursor.getColumnIndex(ListItVid.COLUMN_MIMETYPE)));
                    note.setCouldId(cursor.getString(cursor.getColumnIndex(ListItVid.COLUMN_CLOUD_FILE_ID)));
                    note.setIsTrash(cursor.getInt(cursor.getColumnIndex(ListItVid.COLUMN_TRASH)));
                    notes.add(note);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();
        return notes;
    }

    public ArrayList<ListItVid> getTrashVideo() {
        ArrayList<ListItVid> notes = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + ListItVid.TABLE_NAME + " ORDER BY " + ListItVid.COLUMN_ID + " DESC";
        String[] columns = new String[]{ListItVid.COLUMN_ID, ListItVid.COLUMN_DNAME, ListItVid.COLUMN_ORIPATH, ListItVid.COLUMN_NEWPATH, ListItVid.COLUMN_SIZE, ListItVid.COLUMN_MIMETYPE, ListItVid.COLUMN_CLOUD_FILE_ID, ListItVid.COLUMN_TRASH};

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(ListItVid.TABLE_NAME, columns, ListItVid.COLUMN_TRASH + "=?", new String[]{"1"}, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    ListItVid note = new ListItVid();
                    note.setId(cursor.getInt(cursor.getColumnIndex(ListItVid.COLUMN_ID)));
                    note.setDisplayName(cursor.getString(cursor.getColumnIndex(ListItVid.COLUMN_DNAME)));
                    note.setPath(cursor.getString(cursor.getColumnIndex(ListItVid.COLUMN_ORIPATH)));
                    note.setNewPath(cursor.getString(cursor.getColumnIndex(ListItVid.COLUMN_NEWPATH)));
                    note.setSize(cursor.getLong(cursor.getColumnIndex(ListItVid.COLUMN_SIZE)));
                    note.setMimeType(cursor.getString(cursor.getColumnIndex(ListItVid.COLUMN_MIMETYPE)));
                    note.setCouldId(cursor.getString(cursor.getColumnIndex(ListItVid.COLUMN_CLOUD_FILE_ID)));
                    note.setIsTrash(cursor.getInt(cursor.getColumnIndex(ListItVid.COLUMN_TRASH)));
                    notes.add(note);
//                    }
                } while (cursor.moveToNext());
            }

            cursor.close();
        }
        db.close();
        return notes;
    }

    public ArrayList<ListItVid> getRestoreVideo() {
        ArrayList<ListItVid> notes = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + ListItVid.TABLE_NAME + " ORDER BY " + ListItVid.COLUMN_ID + " DESC";
        String[] columns = new String[]{ListItVid.COLUMN_ID, ListItVid.COLUMN_DNAME, ListItVid.COLUMN_ORIPATH, ListItVid.COLUMN_NEWPATH, ListItVid.COLUMN_SIZE, ListItVid.COLUMN_MIMETYPE, ListItVid.COLUMN_CLOUD_FILE_ID, ListItVid.COLUMN_TRASH};

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(ListItVid.TABLE_NAME, columns, ListItVid.COLUMN_TRASH + "=?", new String[]{"2"}, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    ListItVid note = new ListItVid();
                    note.setId(cursor.getInt(cursor.getColumnIndex(ListItVid.COLUMN_ID)));
                    note.setDisplayName(cursor.getString(cursor.getColumnIndex(ListItVid.COLUMN_DNAME)));
                    note.setPath(cursor.getString(cursor.getColumnIndex(ListItVid.COLUMN_ORIPATH)));
                    note.setNewPath(cursor.getString(cursor.getColumnIndex(ListItVid.COLUMN_NEWPATH)));
                    note.setSize(cursor.getLong(cursor.getColumnIndex(ListItVid.COLUMN_SIZE)));
                    note.setMimeType(cursor.getString(cursor.getColumnIndex(ListItVid.COLUMN_MIMETYPE)));
                    note.setCouldId(cursor.getString(cursor.getColumnIndex(ListItVid.COLUMN_CLOUD_FILE_ID)));
                    note.setIsTrash(cursor.getInt(cursor.getColumnIndex(ListItVid.COLUMN_TRASH)));
                    notes.add(note);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();
        return notes;
    }


    public int updateVideoCloudID(int id, String couldfileide) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ListItVid.COLUMN_CLOUD_FILE_ID, couldfileide);
        int update = db.update(ListItVid.TABLE_NAME, values, ListItVid.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return update;

    }

    public int moveTrashVideo(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ListItVid.COLUMN_TRASH, 1);
        int update = db.update(ListItVid.TABLE_NAME, values, ListItVid.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return update;
    }

    public int reCoverVideo(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ListItVid.COLUMN_TRASH, 0);
        int update = db.update(ListItVid.TABLE_NAME, values, ListItVid.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return update;
    }

    public int deleteVideoTrash(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ListItVid.COLUMN_TRASH, 2);
        int update = db.update(ListItVid.TABLE_NAME, values, ListItVid.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return update;
    }

    public void deleteVideoItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ListItVid.TABLE_NAME, ListItVid.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
    public long insertFile(String displayName, String oripath, String newpath, long size, String mimeType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TreeRoot.COLUMN_DNAME, displayName);
        values.put(TreeRoot.COLUMN_ORIPATH, oripath);
        values.put(TreeRoot.COLUMN_NEWPATH, newpath);
        values.put(TreeRoot.COLUMN_SIZE, size);
        values.put(TreeRoot.COLUMN_CLOUD_FILE_ID, "null1");
        values.put(TreeRoot.COLUMN_TRASH, 0);

        values.put(TreeRoot.COLUMN_MIMETYPE, mimeType);
        long id = db.insert(TreeRoot.TABLE_NAME, null, values);

        db.close();
        return id;
    }

    public TreeRoot getFiles(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TreeRoot.TABLE_NAME,
                new String[]{TreeRoot.COLUMN_ID, TreeRoot.COLUMN_DNAME, TreeRoot.COLUMN_ORIPATH, TreeRoot.COLUMN_NEWPATH, TreeRoot.COLUMN_SIZE, TreeRoot.COLUMN_MIMETYPE}, TreeRoot.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        TreeRoot note = null;
        if (cursor != null) {
            cursor.moveToFirst();
            note = new TreeRoot(
                    cursor.getInt(cursor.getColumnIndex(TreeRoot.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(TreeRoot.COLUMN_DNAME)),
                    cursor.getString(cursor.getColumnIndex(TreeRoot.COLUMN_ORIPATH)),
                    cursor.getString(cursor.getColumnIndex(TreeRoot.COLUMN_NEWPATH)),
                    cursor.getLong(cursor.getColumnIndex(TreeRoot.COLUMN_SIZE)),
                    cursor.getString(cursor.getColumnIndex(TreeRoot.COLUMN_MIMETYPE)));
            cursor.close();
        }
        db.close();
        return note;
    }

    public ArrayList<TreeRoot> getAllFiles() {
        ArrayList<TreeRoot> notes = new ArrayList<>();
        String[] columns = new String[]{TreeRoot.COLUMN_ID, TreeRoot.COLUMN_DNAME, TreeRoot.COLUMN_ORIPATH, TreeRoot.COLUMN_NEWPATH, TreeRoot.COLUMN_SIZE, TreeRoot.COLUMN_MIMETYPE, TreeRoot.COLUMN_CLOUD_FILE_ID, TreeRoot.COLUMN_TRASH};
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TreeRoot.TABLE_NAME, columns, TreeRoot.COLUMN_TRASH + "=?", new String[]{"0"}, null, null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        TreeRoot note = new TreeRoot();
                        note.setId(cursor.getInt(cursor.getColumnIndex(TreeRoot.COLUMN_ID)));
                        note.setDisplayName(cursor.getString(cursor.getColumnIndex(TreeRoot.COLUMN_DNAME)));
                        note.setPath(cursor.getString(cursor.getColumnIndex(TreeRoot.COLUMN_ORIPATH)));
                        note.setNewPath(cursor.getString(cursor.getColumnIndex(TreeRoot.COLUMN_NEWPATH)));
                        note.setSize(cursor.getLong(cursor.getColumnIndex(TreeRoot.COLUMN_SIZE)));
                        note.setMimeType(cursor.getString(cursor.getColumnIndex(TreeRoot.COLUMN_MIMETYPE)));
                        note.setCouldId(cursor.getString(cursor.getColumnIndex(TreeRoot.COLUMN_CLOUD_FILE_ID)));
                        note.setIsTrash(cursor.getInt(cursor.getColumnIndex(TreeRoot.COLUMN_TRASH)));
                        notes.add(note);
                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
        }
        db.close();
        return notes;
    }

    public ArrayList<TreeRoot> getTrashFiels() {
        ArrayList<TreeRoot> notes = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TreeRoot.TABLE_NAME + " ORDER BY " + TreeRoot.COLUMN_ID + " DESC";
        String[] columns = new String[]{TreeRoot.COLUMN_ID, TreeRoot.COLUMN_DNAME, TreeRoot.COLUMN_ORIPATH, TreeRoot.COLUMN_NEWPATH, TreeRoot.COLUMN_SIZE, TreeRoot.COLUMN_MIMETYPE, TreeRoot.COLUMN_CLOUD_FILE_ID, TreeRoot.COLUMN_TRASH};

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TreeRoot.TABLE_NAME, columns, TreeRoot.COLUMN_TRASH + "=?", new String[]{"1"}, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    TreeRoot note = new TreeRoot();
                    note.setId(cursor.getInt(cursor.getColumnIndex(TreeRoot.COLUMN_ID)));
                    note.setDisplayName(cursor.getString(cursor.getColumnIndex(TreeRoot.COLUMN_DNAME)));
                    note.setPath(cursor.getString(cursor.getColumnIndex(TreeRoot.COLUMN_ORIPATH)));
                    note.setNewPath(cursor.getString(cursor.getColumnIndex(TreeRoot.COLUMN_NEWPATH)));
                    note.setSize(cursor.getLong(cursor.getColumnIndex(TreeRoot.COLUMN_SIZE)));
                    note.setMimeType(cursor.getString(cursor.getColumnIndex(TreeRoot.COLUMN_MIMETYPE)));
                    note.setCouldId(cursor.getString(cursor.getColumnIndex(TreeRoot.COLUMN_CLOUD_FILE_ID)));
                    note.setIsTrash(cursor.getInt(cursor.getColumnIndex(TreeRoot.COLUMN_TRASH)));
                    notes.add(note);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();
        return notes;
    }

    public ArrayList<TreeRoot> getBackupFiels() {
        ArrayList<TreeRoot> notes = new ArrayList<>();
        String[] columns = new String[]{TreeRoot.COLUMN_ID, TreeRoot.COLUMN_DNAME, TreeRoot.COLUMN_ORIPATH, TreeRoot.COLUMN_NEWPATH, TreeRoot.COLUMN_SIZE, TreeRoot.COLUMN_MIMETYPE, TreeRoot.COLUMN_CLOUD_FILE_ID, TreeRoot.COLUMN_TRASH};
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TreeRoot.TABLE_NAME, columns, TreeRoot.COLUMN_CLOUD_FILE_ID + "=?", new String[]{"null1"}, null, null, null);
        if (cursor != null) {
            Log.e("TAG", "getBackupImages: " + cursor.getCount());
            if (cursor.moveToFirst()) {
                do {
                    TreeRoot note = new TreeRoot();
                    note.setId(cursor.getInt(cursor.getColumnIndex(TreeRoot.COLUMN_ID)));
                    note.setDisplayName(cursor.getString(cursor.getColumnIndex(TreeRoot.COLUMN_DNAME)));
                    note.setPath(cursor.getString(cursor.getColumnIndex(TreeRoot.COLUMN_ORIPATH)));
                    note.setNewPath(cursor.getString(cursor.getColumnIndex(TreeRoot.COLUMN_NEWPATH)));
                    note.setSize(cursor.getLong(cursor.getColumnIndex(TreeRoot.COLUMN_SIZE)));
                    note.setMimeType(cursor.getString(cursor.getColumnIndex(TreeRoot.COLUMN_MIMETYPE)));
                    note.setCouldId(cursor.getString(cursor.getColumnIndex(TreeRoot.COLUMN_CLOUD_FILE_ID)));
                    note.setIsTrash(cursor.getInt(cursor.getColumnIndex(TreeRoot.COLUMN_TRASH)));
                    notes.add(note);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();
        return notes;
    }

    public ArrayList<TreeRoot> getRestoreFile() {
        ArrayList<TreeRoot> notes = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TreeRoot.TABLE_NAME + " ORDER BY " + TreeRoot.COLUMN_ID + " DESC";
        String[] columns = new String[]{TreeRoot.COLUMN_ID, TreeRoot.COLUMN_DNAME, TreeRoot.COLUMN_ORIPATH, TreeRoot.COLUMN_NEWPATH, TreeRoot.COLUMN_SIZE, TreeRoot.COLUMN_MIMETYPE, TreeRoot.COLUMN_CLOUD_FILE_ID, TreeRoot.COLUMN_TRASH};

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TreeRoot.TABLE_NAME, columns, TreeRoot.COLUMN_TRASH + "=?", new String[]{"2"}, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    TreeRoot note = new TreeRoot();
                    note.setId(cursor.getInt(cursor.getColumnIndex(TreeRoot.COLUMN_ID)));
                    note.setDisplayName(cursor.getString(cursor.getColumnIndex(TreeRoot.COLUMN_DNAME)));
                    note.setPath(cursor.getString(cursor.getColumnIndex(TreeRoot.COLUMN_ORIPATH)));
                    note.setNewPath(cursor.getString(cursor.getColumnIndex(TreeRoot.COLUMN_NEWPATH)));
                    note.setSize(cursor.getLong(cursor.getColumnIndex(TreeRoot.COLUMN_SIZE)));
                    note.setMimeType(cursor.getString(cursor.getColumnIndex(TreeRoot.COLUMN_MIMETYPE)));
                    note.setCouldId(cursor.getString(cursor.getColumnIndex(TreeRoot.COLUMN_CLOUD_FILE_ID)));
                    note.setIsTrash(cursor.getInt(cursor.getColumnIndex(TreeRoot.COLUMN_TRASH)));
                    notes.add(note);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();
        return notes;
    }

    public int updateFileCloudID(int id, String couldfileide) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TreeRoot.COLUMN_CLOUD_FILE_ID, couldfileide);
        int update = db.update(TreeRoot.TABLE_NAME, values, TreeRoot.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return update;
    }

    public int moveTrashFile(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TreeRoot.COLUMN_TRASH, 1);
        int update = db.update(TreeRoot.TABLE_NAME, values, TreeRoot.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return update;
    }

    public int reCoverFile(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TreeRoot.COLUMN_TRASH, 0);
        int update = db.update(TreeRoot.TABLE_NAME, values, TreeRoot.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return update;
    }

    public int deleteFileTrash(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TreeRoot.COLUMN_TRASH, 2);
        int update = db.update(TreeRoot.TABLE_NAME, values, TreeRoot.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return update;
    }

    public void deleteFileItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TreeRoot.TABLE_NAME, TreeRoot.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
    public List<BookmarkEm> listBookmark() {
        List<BookmarkEm> list = new ArrayList<>();

        String[] columns = new String[]{BookmarkEm.COLUMN_PASS_ID,
                BookmarkEm.COLUMN_PASS_TITLE,
                BookmarkEm.COLUMN_PASS_CONTENT,
                BookmarkEm.COLUMN_PASS_ICON,
                BookmarkEm.COLUMN_PASS_ATTACH,
                BookmarkEm.COLUMN_PASS_CREATION};
        SQLiteDatabase db = this.getWritableDatabase();
        String orderBy = BookmarkEm.COLUMN_PASS_CREATION + "," + BookmarkEm.COLUMN_PASS_TITLE + " COLLATE NOCASE ASC;";
        Cursor cursor = db.query(BookmarkEm.TABLE_BOOKMARKS_PASS,
                columns,
                null,
                null,
                null,
                null,
                orderBy);
        if (cursor == null) {
            return list;
        }

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(getBookmarkRecord(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return list;
    }

    private BookmarkEm getBookmarkRecord(Cursor cursor) {
        BookmarkEm record = new BookmarkEm();
        record.setId(cursor.getString(0));
        record.setTitle(cursor.getString(1));
        record.setContent(cursor.getString(2));
        record.setIcon(cursor.getString(3));
        record.setAttachment(cursor.getString(4));
        record.setCreation(cursor.getString(5));
        return record;
    }

    //delete data
    public void delete(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + BookmarkEm.TABLE_BOOKMARKS_PASS + " WHERE " + BookmarkEm.COLUMN_PASS_ID + "=" + id);
        db.close();
    }

    public void deleteAllBookmarks() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(BookmarkEm.TABLE_BOOKMARKS_PASS, null, null);
        db.close();
    }

    public void addBookmark(String pass_title, String pass_content, String pass_icon, String pass_attachment, String pass_creation) {

        if (!isExist(pass_content)) {
            SQLiteDatabase db = this.getWritableDatabase();
            if (pass_title == null
                    || pass_title.trim().isEmpty()
                    || pass_content == null
                    || pass_content.trim().isEmpty()
                    || pass_icon == null
                    || pass_icon.trim().isEmpty()
                    || pass_attachment == null
                    || pass_attachment.trim().isEmpty()
                    || pass_creation == null
                    || pass_creation.trim().isEmpty()) {
                return;
            }

            ContentValues values = new ContentValues();
            values.put(BookmarkEm.COLUMN_PASS_TITLE, pass_title.trim());
            values.put(BookmarkEm.COLUMN_PASS_CONTENT, pass_content.trim());
            values.put(BookmarkEm.COLUMN_PASS_ICON, pass_icon);
            values.put(BookmarkEm.COLUMN_PASS_ATTACH, pass_attachment);
            values.put(BookmarkEm.COLUMN_PASS_CREATION, pass_creation);
            db.insert(BookmarkEm.TABLE_BOOKMARKS_PASS, null, values);

            db.close();
        }
    }

    public boolean isExist(String pass_content) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + BookmarkEm.COLUMN_PASS_TITLE + " FROM " + BookmarkEm.TABLE_BOOKMARKS_PASS + " WHERE " + BookmarkEm.COLUMN_PASS_CONTENT + "='" + pass_content + "' LIMIT 1";
        @SuppressLint("Recycle") Cursor row = db.rawQuery(query, null);
        boolean isExist = false;
        if (row != null) {
            isExist = row.moveToFirst();
            row.close();
        }
        db.close();
        return isExist;
    }
}