package com.Joaquin.thiago;
import android.content.Context;

import androidx.loader.content.CursorLoader;

public class ListTikTokLoad extends CursorLoader {
    public ListTikTokLoad(Context context, ListIdLdr listIdLdr) {
        super(context);
        setProjection(listIdLdr.getSelectProjection());
        setUri(listIdLdr.getQueryUri());
        setSelection(listIdLdr.getSelections());
        setSelectionArgs(listIdLdr.getSelectionsArgs());
        setSortOrder(listIdLdr.getSortOrderSql());}}
