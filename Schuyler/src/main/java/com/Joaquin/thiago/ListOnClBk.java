package com.Joaquin.thiago;
import android.database.Cursor;

import androidx.loader.content.Loader;

public abstract class ListOnClBk implements ListIdLdr {
    public abstract void onLoadFinish(Loader<Cursor> loader, Cursor data);
    public void onLoaderReset(){}}
