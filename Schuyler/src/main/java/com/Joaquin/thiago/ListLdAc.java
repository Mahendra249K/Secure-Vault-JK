package com.Joaquin.thiago;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import java.lang.ref.WeakReference;
public abstract class ListLdAc implements LoaderManager.LoaderCallbacks<Cursor> {
    private WeakReference<Context> context;
    private ListOnClBk listOnClBk;
    private int mLoaderId;
    public ListLdAc(Context context, ListOnClBk listOnClBk){
        this.context = new WeakReference<>(context);
        this.listOnClBk = listOnClBk;}
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        mLoaderId = id;
        return new ListTikTokLoad(context.get(), listOnClBk);}
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        listOnClBk.onLoadFinish(loader, data);
        destroyLoader();}
    private void destroyLoader(){
        try {
            if(context!=null){
                Context ctx = this.context.get();
                if(ctx!=null){
                    ((FragmentActivity)ctx).getSupportLoaderManager().destroyLoader(mLoaderId);}}}catch (Exception e){
            e.printStackTrace();}}
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        listOnClBk.onLoaderReset();
    }}
