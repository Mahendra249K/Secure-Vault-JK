package com.Joaquin.thiago;
import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import androidx.fragment.app.FragmentActivity;
import androidx.loader.content.Loader;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
public class ListMidLod {
    private static final int DEFAULT_START_ID = 1000;
    private final String TAG = "ListMidLod";
    private static ListMidLod loader = new ListMidLod();
    private Map<String, Queue<LoaderTask>> mTaskGroup = new HashMap<>();
    private Map<String, Integer> mIds = new HashMap<>();
    private final int MSG_CODE_LOAD_FINISH = 101;
    private final int MSG_CODE_LOAD_START = 102;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_CODE_LOAD_FINISH:
                    Message message = Message.obtain();
                    message.what = MSG_CODE_LOAD_START;
                    message.obj = msg.obj;
                    sendMessage(message);
                    break;
                case MSG_CODE_LOAD_START:
                    String group = (String) msg.obj;
                    if (!TextUtils.isEmpty(group)) {
                        Queue<LoaderTask> queue = mTaskGroup.get(group);
                        LoaderTask task = queue.poll();
                        if (task != null) {
                            queueLoader(task.activity.get(), task.listOnClBk);}}break;}}};
    private ListMidLod() {}
    public static ListMidLod getLoader() {
        return loader;
    }
    private int checkIds(FragmentActivity activity) {
        String name = activity.getClass().getName();
        int id;
        if (!mIds.containsKey(name)) {
            id = DEFAULT_START_ID;
            mIds.put(name, id);
        } else {
            int preId = mIds.get(name);
            preId++;
            mIds.put(name, preId);
            id = preId;
        }
        return id;
    }

    private void loadMedia(FragmentActivity activity, ListLdAc listLdAc) {
        activity.getSupportLoaderManager().restartLoader(checkIds(activity), null, listLdAc);
    }

    private synchronized void load(FragmentActivity activity, ListOnClBk listOnClBk) {

        String name = activity.getClass().getSimpleName();
        Queue<LoaderTask> queue = mTaskGroup.get(name);
        LoaderTask task = new LoaderTask(new WeakReference<>(activity), listOnClBk);
        if (queue == null) {
            queue = new LinkedList<>();
            mTaskGroup.put(name, queue);
        }
        queue.offer(task);
        if (queue.size() == 1) {
            Message message = Message.obtain();
            message.what = MSG_CODE_LOAD_START;
            message.obj = name;
            mHandler.sendMessage(message);
        }
    }

    private void queueLoader(final FragmentActivity activity, ListOnClBk listOnClBk) {
        loadMedia(activity, new ListLdAc(activity, listOnClBk) {
            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                super.onLoaderReset(loader);
                Queue<LoaderTask> queue = mTaskGroup.get(activity.getClass().getSimpleName());
                if (queue != null) {
                    queue.clear();
                }
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                super.onLoadFinished(loader, data);
                Message message = Message.obtain();
                message.what = MSG_CODE_LOAD_FINISH;
                message.obj = activity.getClass().getSimpleName();
                mHandler.sendMessage(message);
            }
        });
    }

    public void loadPhotos(FragmentActivity activity, ListOnPhotoClBkBsLast onPhotoLoaderCallBack) {
        load(activity, onPhotoLoaderCallBack);
    }

    public void loadVideos(FragmentActivity activity, ListOnVideoClBkBsLast onVideoLoaderCallBack) {
        load(activity, onVideoLoaderCallBack);
    }

    public void loadAudios(FragmentActivity activity, ListBsLastAndClbk listAndClbk) {
        load(activity, listAndClbk);
    }

    public void loadFiles(FragmentActivity activity, ListBsLastVedFlLstld onFileLoaderCallBack) {
        load(activity, onFileLoaderCallBack);
    }

    public static class LoaderTask {
        public WeakReference<FragmentActivity> activity;
        public ListOnClBk listOnClBk;

        public LoaderTask(WeakReference<FragmentActivity> activity, ListOnClBk listOnClBk) {
            this.activity = activity;
            this.listOnClBk = listOnClBk;
        }
    }

}
