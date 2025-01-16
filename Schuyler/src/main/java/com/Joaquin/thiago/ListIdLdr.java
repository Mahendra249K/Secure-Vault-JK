package com.Joaquin.thiago;
import android.net.Uri;
public interface ListIdLdr {
    String[] getSelectProjection();
    Uri getQueryUri();
    String getSortOrderSql();
    String getSelections();
    String[] getSelectionsArgs();}
