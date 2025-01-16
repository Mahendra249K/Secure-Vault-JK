package com.hidefile.secure.folder.vault.dashex;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.Joaquin.thiago.ListIdPic;
import com.Joaquin.thiago.ListItVid;
import com.hidefile.secure.folder.vault.AdActivity.Call_Back_Ads;
import com.hidefile.secure.folder.vault.AdActivity.Common_Adm;
import com.hidefile.secure.folder.vault.R;
import com.hidefile.secure.folder.vault.cluecanva.HandPrmt;
import com.hidefile.secure.folder.vault.cluecanva.VTv;
import com.hidefile.secure.folder.vault.dpss.PhotoSelectAdp;
import com.hidefile.secure.folder.vault.dpss.VideoSelectAdp;
import com.hidefile.secure.folder.vault.edptrs.InSafe;

import java.util.ArrayList;

;

public class PhotoAndVideoSelection extends FoundationActivity {
    Intent mIntent;
    int selectType = 0;
    private String album;
    private GridView gridView;
    private int countSelected;
    private ActionBar actionBar;
    private ActionMode actionMode;


    private TextView errorDisplay;
    private ProgressBar progressBar;
    private PhotoSelectAdp photoSelectAdp;
    private VideoSelectAdp videoSelectAdp;
    ActivityResultLauncher<Intent> resultLauncher;


    private ArrayList<ListIdPic> listIdPics = new ArrayList<>();
    private ArrayList<ListItVid> listItVids = new ArrayList<>();
    private final ActionMode.Callback callback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater menuInflater = mode.getMenuInflater();
            menuInflater.inflate(R.menu.emen_bar, menu);

            actionMode = mode;
            savephotovideo.setVisibility(View.VISIBLE);
            toolbar.setVisibility(View.GONE);



            return true;}
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;}
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int i = item.getItemId();
            if (i == R.id.menu_item_add_image) {
                sendIntent();
                return true;}
            return false;}
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            if (selectType == 1) {
                if (countSelected > 0) {
                    AllImageDselect();}
            } else {
                if (countSelected > 0) {
                    AllVideoDselect();}}
            savephotovideo.setVisibility(View.GONE);
            toolbar.setVisibility(View.VISIBLE);
            actionMode = null;}};
    private Thread thread;
    private Handler handler;
    private ContentObserver observer;
    private LinearLayout llSaveNewPhotos;

    private ImageView savephotovideo;
    private Toolbar toolbar;

    View view;





    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.clear();}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.photo_and_video_selection);
        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

//        Common_Adm.getInstance().AmNativeLoad(PhotoAndVideoSelection.this,findViewById(R.id.llNativeSmall),false);


        mIntent = getIntent();
        if (mIntent == null) {
            finish();}

        album = mIntent.getStringExtra(InSafe.INTENT_EXTRA_ALBUM);
        selectType = mIntent.getIntExtra(InSafe.INTENT_EXTRA_SELECT_TYPE, 1);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            VTv tv_tital = findViewById(R.id.tv_tital);
            if (selectType == 1) tv_tital.setText(R.string.select_image);
            else tv_tital.setText(R.string.select_video);
            Log.e("TAGcountSelected", "onCreate: "+countSelected );
            ImageView iv_back = findViewById(R.id.iv_back);
            iv_back.setOnClickListener(v -> onBackPressed());
            findViewById(R.id.iv_option).setVisibility(View.GONE);
            savephotovideo=findViewById(R.id.Save_photovideo);

        }
        toolbar.setTitle(album);
        errorDisplay = findViewById(R.id.tv_error);
        errorDisplay.setVisibility(View.INVISIBLE);
        llSaveNewPhotos = findViewById(R.id.llSaveNewPhotos);



        progressBar = findViewById(R.id.progress_bar_image_select);
        gridView = findViewById(R.id.image_select_grid_view);
        gridView.setOnItemClickListener((parent, view, position, id) -> {
            if (selectType == 1) {
                if (actionMode == null) {
                    llSaveNewPhotos.setVisibility(View.VISIBLE);
                }
                ImageSelectionToggle(position);

                if (countSelected == 0) {
                    llSaveNewPhotos.setVisibility(View.GONE);
                    ;}} else {
                if (actionMode == null) {
                    llSaveNewPhotos.setVisibility(View.VISIBLE);
                }
                VideoSelectionToggle(position);
                if (countSelected == 0) {
                    llSaveNewPhotos.setVisibility(View.GONE);
                }}});
        llSaveNewPhotos.setOnClickListener(view -> sendIntent());



    }

    @SuppressLint("HandlerLeak")
    @Override
    protected void onStart() {
        super.onStart();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case InSafe.PERMISSION_GRANTED: {
                        loadImages();
                        break;}
                    case InSafe.FETCH_STARTED: {
                        progressBar.setVisibility(View.VISIBLE);
                        gridView.setVisibility(View.INVISIBLE);
                        break;}
                    case InSafe.FETCH_COMPLETED: {
                        if (selectType == 1) {
                            if (photoSelectAdp == null) {
                                photoSelectAdp = new PhotoSelectAdp(getApplicationContext(), listIdPics);
                                gridView.setAdapter(photoSelectAdp);
                                progressBar.setVisibility(View.INVISIBLE);
                                gridView.setVisibility(View.VISIBLE);
                                orientationBasedUI(getResources().getConfiguration().orientation);
                            } else {
                                photoSelectAdp.notifyDataSetChanged();
                                if (actionMode != null) {
                                    countSelected = msg.arg1;
//                                    actionMode.setTitle(countSelected + " " + getString(R.string.selected));
                                }}
                        } else {
                            if (videoSelectAdp == null) {
                                videoSelectAdp = new VideoSelectAdp(getApplicationContext(), listItVids);
                                gridView.setAdapter(videoSelectAdp);
                                progressBar.setVisibility(View.INVISIBLE);
                                gridView.setVisibility(View.VISIBLE);
                                orientationBasedUI(getResources().getConfiguration().orientation);
                            } else {
                                videoSelectAdp.notifyDataSetChanged();
                                if (actionMode != null) {
                                    countSelected = msg.arg1;
//                                    actionMode.setTitle(countSelected + " " + getString(R.string.selected));
                                }
                            }
                        }
                        break;}
                    case InSafe.ERROR: {
                        progressBar.setVisibility(View.INVISIBLE);
                        errorDisplay.setVisibility(View.VISIBLE);
                        break;}
                    default: {
                        super.handleMessage(msg);}
                }
            }
        };
        observer = new ContentObserver(handler) {
            @Override
            public void onChange(boolean selfChange) {
                loadImages();
            }};
        getContentResolver().registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, false, observer);
        requestPermissions();}
    @Override
    protected void onStop() {
        super.onStop();
        stopThread();
        getContentResolver().unregisterContentObserver(observer);
        observer = null;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;}}
    @Override
    protected void onDestroy() {
        super.onDestroy();
        endPermission();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(null);}
        if (selectType == 1) {
            listIdPics = null;
            if (photoSelectAdp != null) {
                photoSelectAdp.releaseResources();}
        } else {
            listItVids = null;
            if (videoSelectAdp != null) {
                videoSelectAdp.releaseResources();}}
        gridView.setOnItemClickListener(null);}
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        orientationBasedUI(newConfig.orientation);}
    private void orientationBasedUI(int orientation) {
        final WindowManager windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        final DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        if (selectType == 1) {
            if (photoSelectAdp != null) {
                int size = orientation == Configuration.ORIENTATION_PORTRAIT ? metrics.widthPixels / 4 : metrics.widthPixels / 5;
                photoSelectAdp.setLayoutParams(size);}
        } else {
            if (videoSelectAdp != null) {
                int size = orientation == Configuration.ORIENTATION_PORTRAIT ? metrics.widthPixels / 4 : metrics.widthPixels / 5;
                videoSelectAdp.setLayoutParams(size);}}
        gridView.setNumColumns(orientation == Configuration.ORIENTATION_PORTRAIT ? 4 : 5);}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                return true;}
            default: {
                return false;}
        }
    }
    private void ImageSelectionToggle(int position) {
        if (!listIdPics.get(position).isChecked()) {
            if (countSelected != 20) {
                listIdPics.get(position).checked = !listIdPics.get(position).isChecked();
                if (listIdPics.get(position).checked) {
                    countSelected++;} else {
                    countSelected--;}} else {
                Toast.makeText(PhotoAndVideoSelection.this, "Crossed Selection limit", Toast.LENGTH_SHORT).show();}} else {
            listIdPics.get(position).checked = !listIdPics.get(position).isChecked();
            if (listIdPics.get(position).checked) {
                countSelected++;} else {
                countSelected--;}}
        photoSelectAdp.notifyDataSetChanged();}
    private void VideoSelectionToggle(int position) {
        if (!listItVids.get(position).isChecked()) {
            if (countSelected != 20) {
                listItVids.get(position).checked = !listItVids.get(position).isChecked();
                if (listItVids.get(position).checked) {
                    countSelected++;} else {
                    countSelected--;}} else {
                Toast.makeText(PhotoAndVideoSelection.this, "Crossed Selection limit", Toast.LENGTH_SHORT).show();}} else {
            listItVids.get(position).checked = !listItVids.get(position).isChecked();
            if (listItVids.get(position).checked) {
                countSelected++;} else {
                countSelected--;}}
        videoSelectAdp.notifyDataSetChanged();}
    private void AllImageDselect() {
        for (int i = 0, l = listIdPics.size(); i < l; i++) {
            listIdPics.get(i).checked = false;}
        countSelected = 0;
        photoSelectAdp.notifyDataSetChanged();}
    private void photoSelectAll() {
        for (int i = 0, l = listIdPics.size(); i < l; i++) {
            listIdPics.get(i).checked = true;}
        photoSelectAdp.notifyDataSetChanged();
        countSelected = listIdPics.size();
        if (actionMode == null) {
            actionMode = PhotoAndVideoSelection.this.startActionMode(callback);}
//        actionMode.setTitle(countSelected + " " + getString(R.string.selected));
    }
    private void AllVideoDselect() {
        for (int i = 0, l = listItVids.size(); i < l; i++) {
            listItVids.get(i).checked = false;}
        countSelected = 0;
        videoSelectAdp.notifyDataSetChanged();}
    private void videoSelectAll() {
        for (int i = 0, l = listItVids.size(); i < l; i++) {
            listItVids.get(i).checked = true;}
        videoSelectAdp.notifyDataSetChanged();
        countSelected = listItVids.size();
        if (actionMode == null) {
            actionMode = PhotoAndVideoSelection.this.startActionMode(callback);
        }
//        actionMode.setTitle(countSelected + " " + getString(R.string.selected));
    }
    private ArrayList<ListIdPic> imageGetSelected() {
        ArrayList<ListIdPic> selectedImages = new ArrayList<ListIdPic>();
        if(listIdPics != null){
            for (int i = 0, l = listIdPics.size(); i < l; i++) {
                if (listIdPics.get(i).isChecked()) {
                    selectedImages.add(listIdPics.get(i));}
            }
        }

        return selectedImages;}
    private ArrayList<ListItVid> videoGetSelected() {
        ArrayList<ListItVid> selectedImages = new ArrayList<>();

        if (listItVids != null) {
            for (int i = 0, l = listItVids.size(); i < l; i++) {
                if (listItVids.get(i).isChecked()) {
                    selectedImages.add(listItVids.get(i));}}
        }
        return selectedImages;}


    private void sendIntent() {

        Common_Adm.getInstance();
        if (selectType == 1) {

            Common_Adm.getInstance().everytimeInterstialAdShow(true, PhotoAndVideoSelection.this, new Call_Back_Ads() {
                @Override
                public void onAdsClose() {
                    Intent intent = new Intent();
                    intent.putExtra(InSafe.INTENT_EXTRA_IMAGES, imageGetSelected());
                    setResult(RESULT_OK, intent);
                    finish();
                }

                @Override
                public void onLoading() {
                    Intent intent = new Intent();
                    intent.putExtra(InSafe.INTENT_EXTRA_IMAGES, imageGetSelected());
                    setResult(RESULT_OK, intent);
                    finish();
                }

                @Override
                public void onAdsFail() {

                    Intent intent = new Intent();
                    intent.putExtra(InSafe.INTENT_EXTRA_IMAGES, imageGetSelected());
                    setResult(RESULT_OK, intent);
                    finish();

                }
            });



        } else {

            Common_Adm.getInstance().everytimeInterstialAdShow(true, PhotoAndVideoSelection.this, new Call_Back_Ads() {
                @Override
                public void onAdsClose() {
                    Intent intent = new Intent();
                    intent.putExtra(InSafe.INTENT_EXTRA_VIDEOS, videoGetSelected());
                    setResult(RESULT_OK, intent);
                    finish();
                }

                @Override
                public void onLoading() {
                    Intent intent = new Intent();
                    intent.putExtra(InSafe.INTENT_EXTRA_VIDEOS, videoGetSelected());
                    setResult(RESULT_OK, intent);
                    finish();
                }

                @Override
                public void onAdsFail() {

                    Intent intent = new Intent();
                    intent.putExtra(InSafe.INTENT_EXTRA_VIDEOS, videoGetSelected());
                    setResult(RESULT_OK, intent);
                    finish();

                }
            });

        }
    }
    private void loadImages() {
        startThread(new ImageLoaderRunnable());
    }
    private void startThread(Runnable runnable) {
        stopThread();
        thread = new Thread(runnable);
        thread.start();}
    private void stopThread() {
        if (thread == null || !thread.isAlive()) {
            return;}
        thread.interrupt();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();}
    }
    private void sendMessage(int what) {
        sendMessage(what, 0);
    }
    private void sendMessage(int what, int arg1) {
        if (handler == null) {
            return;}
        Message message = handler.obtainMessage();
        message.what = what;
        message.arg1 = arg1;
        message.sendToTarget();}
    void requestPermissions() {
        HandPrmt.getInstance().requestPermissions(this, new HandPrmt.OnListener() {
            @Override
            public void onPermissionGranted() {
                sendMessage(InSafe.PERMISSION_GRANTED);
            }
            @Override
            public void onPermissionDenied() {
                progressBar.setVisibility(View.INVISIBLE);
                gridView.setVisibility(View.INVISIBLE);
                requestPermissions();}
            @Override
            public void onOpenSettings() {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                resultLauncher.launch(intent);}});}
    private void endPermission() {
        HandPrmt.getInstance().hideDialog();
        if (resultLauncher != null) {
            resultLauncher.unregister();}}

    public class ImageLoaderRunnable implements Runnable {
        @Override
        public void run() {
            android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
            if (selectType == 1) {
                if (photoSelectAdp == null) {
                    sendMessage(InSafe.FETCH_STARTED);}
                if (listIdPics == null) {
                    listIdPics = new ArrayList<>();}
                listIdPics = PhotoAndVideoAlbumSelection.listIdPics;} else {
                if (videoSelectAdp == null) {
                    sendMessage(InSafe.FETCH_STARTED);}
                if (listItVids == null) {
                    listItVids = new ArrayList<>();}
                listItVids = PhotoAndVideoAlbumSelection.listItVids;}
            sendMessage(InSafe.FETCH_COMPLETED);}
    }
}
