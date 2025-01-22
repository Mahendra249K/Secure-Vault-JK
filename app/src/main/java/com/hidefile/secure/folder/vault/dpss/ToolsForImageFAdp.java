package com.hidefile.secure.folder.vault.dpss;

import static android.app.Activity.RESULT_OK;
import static com.hidefile.secure.folder.vault.dashex.Pswd.CAMERA_PIC_REQUEST;
import static com.hidefile.secure.folder.vault.edptrs.InSafe.isGridlayout;
import static com.hidefile.secure.folder.vault.cluecanva.TillsPth.cameraImage;
import static com.hidefile.secure.folder.vault.cluecanva.TillsPth.getFileType;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.FileProvider;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;




import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;


import com.hidefile.secure.folder.vault.AdActivity.Call_Back_Ads;
import com.hidefile.secure.folder.vault.AdActivity.Common_Adm;
import com.hidefile.secure.folder.vault.AdActivity.Open_App_Manager;
//import com.hidefile.secure.folder.vault.BuildConfig;
import com.hidefile.secure.folder.vault.cluecanva.TillsFl;
import com.hidefile.secure.folder.vault.cluecanva.TillsPth;
import com.hidefile.secure.folder.vault.dashex.IvFullScreenView;
import com.hidefile.secure.folder.vault.dashex.PhotoAndVideoAlbumSelection;
import com.hidefile.secure.folder.vault.cluecanva.SupPref;
import com.Joaquin.thiago.ListIdPic;
import com.hidefile.secure.folder.vault.R;
import com.hidefile.secure.folder.vault.dashex.Pswd;
import com.hidefile.secure.folder.vault.dashex.TrashBin;
import com.hidefile.secure.folder.vault.cluecanva.RDbhp;
import com.hidefile.secure.folder.vault.edptrs.InSafe;
import com.hidefile.secure.folder.vault.cluecanva.EntryAux;
import com.hidefile.secure.folder.vault.cluecanva.PopUpTils;
import com.hidefile.secure.folder.vault.cluecanva.TooRfl;
import com.hidefile.secure.folder.vault.cluecanva.HandPrmt;
import com.hidefile.secure.folder.vault.cluecanva.VTv;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class ToolsForImageFAdp extends Fragment implements View.OnClickListener, OnImageItemClickListner {
    Context context;
    View layRgb;
    RDbhp RDbhp;
    public Drive service;
    boolean isbackup = false;
    boolean isFABOpen = false;
    private int countSelected;
    RecyclerView rvPhotoList;
    private ActionBar actionBar;
    private ActionMode actionMode;
    boolean isSelectedMode = false;
    ViewListImageAdp viewListImageAdp;

    RelativeLayout relativeNativeLayout;
    GridLayoutManager gridLayoutManager;

    FloatingActionButton iv_img, rgbq3, rgb4;

    final ArrayList<ListIdPic> imagesList = new ArrayList<>();
    LinearLayout emptyLin, linImportPic, rgb3, rgbQ4;
    private static final String TAG = ToolsForImageFAdp.class.getSimpleName();
    private final ActionMode.Callback callback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater menuInflater = mode.getMenuInflater();
            menuInflater.inflate(R.menu.emen_unet, menu);
            actionMode = mode;
            countSelected = 0;
            linImportPic.setVisibility(View.GONE);
            optionsLayout.setVisibility(View.VISIBLE);

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int i = item.getItemId();
            if (i == R.id.action_unhide) {
                dialogUnHideFile(getActivity());
                return true;
            } else if (i == R.id.action_delete) {
                dialogDeletePhotoView(getActivity());
                PopUpTils.showDialogWithIcon(context, "Delete Photos?", "Sure to delete Permanently the selected 14 photos?",
                        "Cancel", "Delete", true);
                return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            if (countSelected > 0) {
                deselectAll();
            }
            linImportPic.setVisibility(View.VISIBLE);
            optionsLayout.setVisibility(View.GONE);
            actionMode = null;
        }
    };
    private GoogleAccountCredential credential;
    private String blankFilePath;
    private LinearLayout optionsLayout, linUnHide, linDelete, llShare;

    View view;

    @SuppressLint({"NotifyDataSetChanged", "NonConstantResourceId", "SetTextI18n"})
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.photo_fr, container, false);
        context = getActivity();



//        Common_Adm.getInstance().AmNativeLoad(getActivity(),view.findViewById(R.id.llNativeSmall),false);



        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            VTv tv_tital = getActivity().findViewById(R.id.tv_tital);
            tv_tital.setText("Secure Photo");
            ImageView iv_back = getActivity().findViewById(R.id.iv_back);
            iv_back.setOnClickListener(this);
            ImageView iv_option = getActivity().findViewById(R.id.iv_option);
            iv_option.setOnClickListener(v -> {

                PopupMenu popupMenu = new PopupMenu(getActivity(), iv_option);
                popupMenu.inflate(R.menu.emen_cover);
                MenuItem menuItem = popupMenu.getMenu().findItem(R.id.menu_list_to_grid);
                String text = isGridlayout ? "Grid to list" : "List to grid";
                menuItem.setTitle(text);
                popupMenu.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.menu_open_recover:
                            Intent intent = new Intent(context, TrashBin.class);
                            intent.putExtra("Type", "trashphotos");
                            startActivityForResult(intent, 120);
                            return true;
                        case R.id.menu_select_all:
                            if (imagesList.size() > 0) {
                                isSelectedMode = true;
                                viewListImageAdp.setSelectionMode(isSelectedMode);
                                if (viewListImageAdp != null) {
                                    viewListImageAdp.notifyDataSetChanged();
                                }
                                if (actionMode == null) {
//                                    actionMode = getActivity().startActionMode(callback);
                                    linImportPic.setVisibility(View.GONE);
                                    optionsLayout.setVisibility(View.VISIBLE);
                                }
                                selectAll();
//                                actionMode.setTitle(countSelected + " " + getString(R.string.selected));
                            } else {
                                EntryAux.showToast(context, R.string.no_file);
                            }
                            return true;
                        case R.id.menu_sort:
                            dialogSort();
                            return true;
                        case R.id.menu_list_to_grid: {
                            if (gridLayoutManager.getSpanCount() == 1) {
                                gridLayoutManager.setSpanCount(4);
                                viewListImageAdp.setSpanCount(4);
                                isGridlayout = true;
                            } else {
                                gridLayoutManager.setSpanCount(1);
                                viewListImageAdp.setSpanCount(1);
                                isGridlayout = false;
                            }

                            String text1 = isGridlayout ? "Grid to list" : "List to grid";
                            item.setTitle(text1);

                            if (viewListImageAdp != null) {
                                viewListImageAdp.notifyDataSetChanged();
                            }
                            return true;
                        }
                    }
                    return false;
                });

                popupMenu.show();
            });
        }

        RDbhp = RDbhp.getInstance(context);
        createImageDir();
        setHasOptionsMenu(true);


        emptyLin = view.findViewById(R.id.emptyLin);
        relativeNativeLayout = view.findViewById(R.id.relativeSmall);
        rvPhotoList = view.findViewById(R.id.rvPhotoList);
        iv_img = view.findViewById(R.id.iv_img);
        iv_img.setOnClickListener(this);
        rgb3 = view.findViewById(R.id.rgb3);
        rgb3.setOnClickListener(this);
        rgbq3 = view.findViewById(R.id.rgbq3);
        rgbq3.setOnClickListener(this);
        rgbQ4 = view.findViewById(R.id.rgbQ4);
        rgbQ4.setOnClickListener(this);
        rgb4 = view.findViewById(R.id.rgb4);
        rgb4.setOnClickListener(this);
        layRgb = view.findViewById(R.id.layRgb);
        linImportPic = view.findViewById(R.id.linImportPic);
        linImportPic.setOnClickListener(this);
        optionsLayout = view.findViewById(R.id.optionsLayout);
        linUnHide = view.findViewById(R.id.linUnHide);
        linDelete = view.findViewById(R.id.linDelete);
        llShare = view.findViewById(R.id.llShare);
        linUnHide.setVisibility(View.VISIBLE);
        linDelete.setVisibility(View.VISIBLE);
        llShare.setVisibility(View.GONE);
        linUnHide.setOnClickListener(this);
        linDelete.setOnClickListener(this);
        llShare.setOnClickListener(this);
        int no = isGridlayout ? 4 : 1;
        gridLayoutManager = new GridLayoutManager(context, no);
        rvPhotoList.setHasFixedSize(true);
        rvPhotoList.setLayoutManager(gridLayoutManager);
        viewListImageAdp = new ViewListImageAdp(getActivity(), imagesList);
        viewListImageAdp.setItemClickEvent(this);
        viewListImageAdp.setImageResize(TillsPth.getImageResize(context, rvPhotoList));
        rvPhotoList.setAdapter(viewListImageAdp);

        Init();
//        Common_Adm.getInstance().AmNativeLoad(getActivity(),  view.findViewById(R.id.llNativeSmall), false);

        return view;
    }








    @SuppressLint("NotifyDataSetChanged")
    void Init() {

        int no = isGridlayout ? 4 : 1;
        gridLayoutManager.setSpanCount(no);
        imagesList.clear();
        imagesList.addAll(RDbhp.getAllImages());
        if (viewListImageAdp != null) {
            viewListImageAdp.notifyDataSetChanged();
        }
        if (imagesList.size() > 0) {
            rvPhotoList.setVisibility(View.VISIBLE);
            emptyLin.setVisibility(View.GONE);
            relativeNativeLayout.setVisibility(View.VISIBLE);

        } else {
            rvPhotoList.setVisibility(View.GONE);
            emptyLin.setVisibility(View.VISIBLE);
            relativeNativeLayout.setVisibility(View.GONE);
        }
    }
    @SuppressLint("Range")
    public long getFileModifiedDate(Uri uri) {
        long result = 0;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                try {
                    if (cursor.moveToFirst()) {
                        result = cursor.getLong(cursor.getColumnIndex(DocumentsContract.Document.COLUMN_LAST_MODIFIED));
                    }
                } finally {
                    cursor.close();
                }
            }
        }
        return result;
    }

    @SuppressLint("Range")
    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                try {
                    if (cursor.moveToFirst()) {
                        result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                } finally {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public void showIn(final View v) {
        isFABOpen = true;
        v.setVisibility(View.VISIBLE);
        v.setAlpha(0f);
        v.setTranslationY(v.getHeight());
        v.animate()
                .setDuration(200)
                .translationY(0)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                    }
                })
                .alpha(1f)
                .start();
    }

    public ArrayList<ListIdPic> getImages() {
        ArrayList<ListIdPic> imagesList = new ArrayList<>();
        Uri treeUri = Uri.parse(SupPref.getHideUri(getActivity()));
        if (treeUri != null) {
            getActivity().getContentResolver().takePersistableUriPermission(treeUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            DocumentFile tree = DocumentFile.fromTreeUri(getActivity(), treeUri);
            if (tree != null && tree.isDirectory()) {
                for (DocumentFile file : tree.listFiles()) {
                    if (TillsPth.isImage(getFileType(file.getName()))) {
                        ListIdPic media_data = new ListIdPic();
                        media_data.setDisplayName(getFileName(file.getUri()));
                        media_data.setNewPath(file.getUri().toString());
                        File mFile = new File(file.getUri().getPath());
                        media_data.setSize(mFile.length());
                        media_data.setModified(getFileModifiedDate(file.getUri()));
                        media_data.setMimeType(TillsPth.getMimeType(file.getName()));
                        imagesList.add(media_data);
                    }
                }
            }
        }
        return imagesList;
    }

    public void showOut(final View v) {
        isFABOpen = false;
        v.setVisibility(View.VISIBLE);
        v.setAlpha(1f);
        v.setTranslationY(0);
        v.animate()
                .setDuration(200)
                .translationY(v.getHeight())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        v.setVisibility(View.GONE);
                        super.onAnimationEnd(animation);
                    }
                }).alpha(0f)
                .start();
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                getActivity().onBackPressed();
                break;
            case R.id.iv_img:

                if (!isFABOpen) {
                    iv_img.animate().rotation(45);
                    showIn(rgb3);
                    showIn(rgbQ4);
                } else {
                    iv_img.animate().rotation(0);
                    showOut(rgb3);
                    showOut(rgbQ4);
                }

                break;
            case R.id.linImportPic:
                Common_Adm.getInstance();
                Common_Adm.mInterstitialAdClickCount++;
                Common_Adm.getInstance().loadOrShowAdmInterstial(true, getActivity(), new Call_Back_Ads() {
                    @Override
                    public void onAdsClose() {
                        Intent intent1 = new Intent(context, PhotoAndVideoAlbumSelection.class);
                        intent1.putExtra(InSafe.INTENT_EXTRA_LIMIT, 10);
                        intent1.putExtra(InSafe.INTENT_EXTRA_SELECT_TYPE, 1);
                        startActivityForResult(intent1, InSafe.REQUEST_CODE_IMAGE);
                    }

                    @Override
                    public void onLoading() {
                        Intent intent1 = new Intent(context, PhotoAndVideoAlbumSelection.class);
                        intent1.putExtra(InSafe.INTENT_EXTRA_LIMIT, 10);
                        intent1.putExtra(InSafe.INTENT_EXTRA_SELECT_TYPE, 1);
                        startActivityForResult(intent1, InSafe.REQUEST_CODE_IMAGE);
                    }

                    @Override
                    public void onAdsFail() {
                        Intent intent1 = new Intent(context, PhotoAndVideoAlbumSelection.class);
                        intent1.putExtra(InSafe.INTENT_EXTRA_LIMIT, 10);
                        intent1.putExtra(InSafe.INTENT_EXTRA_SELECT_TYPE, 1);
                        startActivityForResult(intent1, InSafe.REQUEST_CODE_IMAGE);
                    }
                });


                break;
            case R.id.rgbq3:
            case R.id.rgb3:
                Intent intent = new Intent(context, PhotoAndVideoAlbumSelection.class);
                intent.putExtra(InSafe.INTENT_EXTRA_LIMIT, 10);
                intent.putExtra(InSafe.INTENT_EXTRA_SELECT_TYPE, 1);
                startActivityForResult(intent, InSafe.REQUEST_CODE_IMAGE);
                iv_img.animate().rotation(0);
                showOut(rgb3);
                showOut(rgbQ4);
                break;
            case R.id.rgb4:
            case R.id.rgbQ4:
                HandPrmt.getInstance().requestCameraPermission(context, new HandPrmt.OnListener() {
                    @Override
                    public void onPermissionGranted() {

                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                        String createBlankFileName = "IMG_" + EntryAux.getTimeStamp();
                        File file = TillsFl.getTargetLocation(cameraImage + File.separator + createBlankFileName + ".jpg");
                        blankFilePath = file.getPath();
                        Uri blankFileUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);

                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, blankFileUri);
                        startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);

                        try {
                            iv_img.animate().rotation(0);
                            showOut(rgb3);
                            showOut(rgbQ4);
                        } catch (Exception e) {
                        }
                    }

                    @Override
                    public void onPermissionDenied() {
                        EntryAux.showToast(context, "Please Allow permission");
                    }

                    @Override
                    public void onOpenSettings() {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                });
                break;
            case R.id.linUnHide:
                dialogUnHideFile(getActivity());
                break;
            case R.id.linDelete:
                dialogDeletePhotoView(getActivity());
                break;
            case R.id.llShare:
                TooRfl.shareMultipleImages(context, getSelected(), "application/image");
                break;
            default:
                break;
        }
    }

    @Override
    public void onImageItemClick(int position) {
        if (isSelectedMode) {
            if (actionMode == null) {
                linImportPic.setVisibility(View.GONE);
                optionsLayout.setVisibility(View.VISIBLE);
            }
            toggleSelection(position);
            if (countSelected == 0) {
                isSelectedMode = false;
                viewListImageAdp.setSelectionMode(isSelectedMode);
                viewListImageAdp.notifyDataSetChanged();
                linImportPic.setVisibility(View.VISIBLE);
                optionsLayout.setVisibility(View.GONE);
            }
        } else {

            Intent intent = new Intent(context, IvFullScreenView.class);
            intent.putExtra("postion", position);
            intent.putExtra("listIdPics", imagesList);
            startActivityForResult(intent, 120);
        }
    }

    @Override
    public void onImageItemLongClick(int position) {
        isSelectedMode = true;
        viewListImageAdp.setSelectionMode(isSelectedMode);
        if (actionMode == null) {
            linImportPic.setVisibility(View.GONE);
            optionsLayout.setVisibility(View.VISIBLE);
        }
        toggleSelection(position);
        if (countSelected == 0) {
            isSelectedMode = false;
            viewListImageAdp.setSelectionMode(isSelectedMode);
            linImportPic.setVisibility(View.VISIBLE);
            optionsLayout.setVisibility(View.GONE);
        }
        viewListImageAdp.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_open_recover:
                Intent intent = new Intent(context, TrashBin.class);
                intent.putExtra("Type", "trashphotos");
                startActivityForResult(intent, 120);
                return true;
            case R.id.menu_select_all:
                if (imagesList.size() > 0) {
                    isSelectedMode = true;
                    viewListImageAdp.setSelectionMode(isSelectedMode);
                    viewListImageAdp.notifyDataSetChanged();
                    if (actionMode == null) {
                        linImportPic.setVisibility(View.GONE);
                        optionsLayout.setVisibility(View.VISIBLE);
                    }
                    selectAll();
                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.no_file), Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.menu_sort:
                dialogSort();
                return true;
            case R.id.menu_list_to_grid: {
                if (gridLayoutManager.getSpanCount() == 1) {
                    gridLayoutManager.setSpanCount(4);
                    viewListImageAdp.setSpanCount(4);
                    isGridlayout = true;
                } else {
                    gridLayoutManager.setSpanCount(1);
                    viewListImageAdp.setSpanCount(1);
                    isGridlayout = false;
                }
                String text = isGridlayout ? "Grid to list" : "List to grid";
                item.setTitle(text);

                if (viewListImageAdp != null) {
                    viewListImageAdp.notifyDataSetChanged();
                }
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void dialogSort() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialogTheme);
        View dialogView = View.inflate(getActivity(), R.layout.dig_sort, null);
        AppCompatButton button_negative = dialogView.findViewById(R.id.btn_negative);
        AppCompatButton button_positive = dialogView.findViewById(R.id.btn_positive);
        TextView title = dialogView.findViewById(R.id.tv_sort_title);
        TextView textView_message = dialogView.findViewById(R.id.textView_message);
        title.setText("Sort data");
        textView_message.setText("");
        RadioGroup radioGroup = dialogView.findViewById(R.id.radioGroup);

        button_negative.setText("Cancel");
        button_positive.setText("Sort");
        button_negative.setOnClickListener(v -> bottomSheetDialog.dismiss());
        button_positive.setOnClickListener(v -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            RadioButton radioButton = dialogView.findViewById(selectedId);
            sortData(radioButton);
            bottomSheetDialog.dismiss();
        });
        bottomSheetDialog.setContentView(dialogView);
        bottomSheetDialog.show();
    }

    public void sortData(RadioButton radioButton) {
        if (radioButton == null) return;
        switch (radioButton.getId()) {
            case R.id.tv_sort_name:
                Collections.sort(imagesList, new Comparator<ListIdPic>() {
                    @Override
                    public int compare(ListIdPic lhs, ListIdPic rhs) {
                        return lhs.getDisplayName().toLowerCase().compareTo(rhs.getDisplayName().toLowerCase());
                    }
                });
                viewListImageAdp.setData(imagesList);
                break;
            case R.id.tv_sort_size:
                Collections.sort(imagesList, new Comparator<ListIdPic>() {
                    @Override
                    public int compare(ListIdPic o1, ListIdPic o2) {
                        return Integer.compare((int) (o1.getSize() / 1000), (int) (o2.getSize() / 1000));
                    }
                });
                viewListImageAdp.setData(imagesList);
                break;
            case R.id.tv_sort_date:
                Collections.sort(imagesList, new Comparator<ListIdPic>() {
                    @Override
                    public int compare(ListIdPic o1, ListIdPic o2) {
                        return Integer.compare((int) (new File(o1.getNewPath()).lastModified() / 1000), (int) (new File(o2.getNewPath()).lastModified() / 1000));
                    }
                });
                viewListImageAdp.setData(imagesList);
                break;
        }
        viewListImageAdp.notifyDataSetChanged();

    }

    private void toggleSelection(int position) {
        imagesList.get(position).checked = !imagesList.get(position).isChecked();
        if (imagesList.get(position).checked) {
            countSelected++;
        } else {
            countSelected--;
        }
        viewListImageAdp.notifyDataSetChanged();
    }

    private void deselectAll() {
        for (int i = 0, l = imagesList.size(); i < l; i++) {
            imagesList.get(i).checked = false;
        }
        isSelectedMode = false;
        viewListImageAdp.setSelectionMode(isSelectedMode);

        countSelected = 0;
        viewListImageAdp.notifyDataSetChanged();
    }

    private void selectAll() {
        for (int i = 0, l = imagesList.size(); i < l; i++) {
            imagesList.get(i).checked = true;
        }
        isSelectedMode = true;
        viewListImageAdp.setSelectionMode(isSelectedMode);

        countSelected = imagesList.size();
        viewListImageAdp.notifyDataSetChanged();
    }

    private ArrayList<ListIdPic> getSelected() {
        ArrayList<ListIdPic> selectedImages = new ArrayList<>();
        for (int i = 0, l = imagesList.size(); i < l; i++) {
            if (imagesList.get(i).isChecked()) {
                selectedImages.add(imagesList.get(i));
            }
        }
        return selectedImages;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == InSafe.REQUEST_CODE_IMAGE && resultCode == RESULT_OK) {
            ArrayList<ListIdPic> images = (ArrayList<ListIdPic>) data.getSerializableExtra(InSafe.INTENT_EXTRA_IMAGES);
            hideAllImage(images);
        } else if (requestCode == CAMERA_PIC_REQUEST) {
            if (resultCode == RESULT_OK) {
                if (data != null && data.getExtras() != null && data.getExtras().get("data") != null && data.getExtras().get("data") instanceof Bitmap) {
                    Bitmap image = (Bitmap) data.getExtras().get("data");

                    if (image != null && !image.isRecycled()) {

                        String createBlankFileName = "IMG_" + EntryAux.getTimeStamp();
                        File file = TillsFl.getTargetLocation(cameraImage + File.separator + createBlankFileName + ".jpg");
                        String filePath = file.getPath();

                        if (file.exists()) file.delete();
                        try {
                            FileOutputStream out = new FileOutputStream(file);
                            image.compress(Bitmap.CompressFormat.JPEG, 100, out);
                            out.flush();
                            out.close();

                            MediaScannerConnection.scanFile(context, new String[]{file.getAbsolutePath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    hideCaptureImage(filePath);
                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        hideCaptureImage(blankFilePath);
                    }
                } else {
                    hideCaptureImage(blankFilePath);
                }
            } else {
            }
        } else if (requestCode == 1001) {
            EntryAux.hideProgressDialog();
            EntryAux.showToast(context, R.string.hide_success_msg);
            Init();
        } else {
            if (requestCode == InSafe.REQUEST_CODE_VIDEO && resultCode == RESULT_OK) {
            }
        }
        if (requestCode == 120) {
            Init();
        }
    }

    private void hideCaptureImage(final String blankFilePath) {
        if (blankFilePath != null && !blankFilePath.trim().isEmpty()) {
            List<ListIdPic> hideItemsList = new ArrayList<>();
            File file = new File(blankFilePath);
            ListIdPic listIdPic = new ListIdPic();
            listIdPic.setDisplayName(TillsFl.getFileNameFromSource(file.getPath()));
            listIdPic.setPath(blankFilePath);
            listIdPic.setSize(file.length());
            listIdPic.setMimeType(TillsFl.getMimeType(context, Uri.parse(blankFilePath)));
            hideItemsList.add(listIdPic);
            hideAllImage(hideItemsList);
        }
    }

    private void hideAllImage(List<ListIdPic> images) {
        if (images != null && images.size() > 0) {
            EntryAux.showProgressDialog(getActivity(), R.string.please_wait_msg);
            new Thread(() -> {
                File file;
                List<String> pathList = new ArrayList<>();
                for (ListIdPic listIdPic : images) {
                    file = new File(listIdPic.getPath());
                    if (file.exists()) {
                        pathList.add(listIdPic.getPath());
                    }
                }

                String[] stringPathArray = new String[pathList.size()];
                pathList.toArray(stringPathArray);

                HashMap<String, Uri> selectedList = new HashMap<>();
                MediaScannerConnection.scanFile(context, stringPathArray, null, new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) {

                        selectedList.put(path, uri);

                        if (selectedList.size() == stringPathArray.length) {
                            final ArrayList<Uri> deleteRequestList = TooRfl.hideFiles(context, selectedList, RDbhp, 1);

                            getActivity().runOnUiThread(() -> {
                                EntryAux.hideProgressDialog();
                                Init();

                                if (deleteRequestList.size() > 0) {
                                    requestDeletePermission(deleteRequestList);
                                } else {
                                    EntryAux.showToast(context, R.string.hide_success_msg);
                                }

                            });
                        }
                    }
                });
            }).start();
        }
    }

    private void requestDeletePermission(List<Uri> uriList) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                PendingIntent pi = MediaStore.createDeleteRequest(getActivity().getContentResolver(), uriList);
                startIntentSenderForResult(pi.getIntentSender(), 1001, null, 0, 0, 0, null);
            }
        } catch (Exception e) {
            System.out.println("AFTER HIDE : " + e.getMessage());
        }
    }

    public void createImageDir() {
        File myDirectory = new File(TillsPth.hideImage);
        if (!myDirectory.exists()) {
            myDirectory.mkdirs();
        } else {
        }
        createNoImageDir();
    }
    public boolean makeUnhideDirForImahe() {
        File myDirectory = new File(TillsPth.restorePathImage);
        if (!myDirectory.exists()) {
            return myDirectory.mkdirs();
        } else {
            return true;
        }
    }

    public void createNoImageDir() {
        File myDirectory = new File(TillsPth.nohideImage);
        if (!myDirectory.exists()) {
            myDirectory.mkdirs();
        } else {
        }
    }

    @SuppressLint("SetTextI18n")
    public void dialogUnHideFile(final Activity act) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(act, R.style.BottomSheetDialogTheme);
        View dialogView = View.inflate(act, R.layout.dig_delete, null);
        LottieAnimationView animationView = dialogView.findViewById(R.id.animation_view);
        AppCompatButton button_positive = dialogView.findViewById(R.id.btn_positive);
        AppCompatButton button_negative = dialogView.findViewById(R.id.btn_negative);
        TextView title = dialogView.findViewById(R.id.tv_sort_title);
        TextView textView_message = dialogView.findViewById(R.id.textView_message);
        title.setText("Sure to Unhide Pictures?");
        title.setVisibility(View.VISIBLE);
        textView_message.setText("Do you Really Want to Unhide selected Pictures ??");
        button_positive.setText("Unhide");
        button_negative.setText("Cancel");
        animationView.setAnimation(R.raw.animation_no_root);



        button_positive.setOnClickListener(view -> {

            EntryAux.showProgressDialog(getActivity(), R.string.please_wait_msg);
            if (makeUnhideDirForImahe()) {
                new Thread(() -> {
                    ArrayList<ListIdPic> selected = getSelected();
                    ListIdPic listIdPic;
                    boolean isDeleted;
                    for (int i = 0; i < selected.size(); i++) {
                        listIdPic = selected.get(i);
                        isDeleted = TooRfl.makeUnHide(context, listIdPic.getPath(), listIdPic.getNewPath(), TillsPth.restorePathImage + listIdPic.getDisplayName(), 1);

                        if (isDeleted) {
                            RDbhp.deletePhotoItem(listIdPic.getId());
                        }
                    }
                    getActivity().runOnUiThread(() -> {
                        if (actionMode != null)
//                            actionMode.finish();
                            countSelected = 0;
                        EntryAux.hideProgressDialog();
                        EntryAux.showToast(context, R.string.un_hide_success_msg);
                        linImportPic.setVisibility(View.VISIBLE);
                        optionsLayout.setVisibility(View.GONE);
                        Init();
                    });
                }).start();
            }
            bottomSheetDialog.dismiss();

        });
        button_negative.setOnClickListener(view -> {
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.setContentView(dialogView);
        bottomSheetDialog.show();
    }

    @SuppressLint({"SetTextI18n", "SuspiciousIndentation"})
    public void dialogDeletePhotoView(final Activity act) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(act, R.style.BottomSheetDialogTheme);
        View dialogView = View.inflate(act, R.layout.dig_delete, null);
        LottieAnimationView animationView = dialogView.findViewById(R.id.animation_view);
        AppCompatButton button_negative = dialogView.findViewById(R.id.btn_negative);
        AppCompatButton button_positive = dialogView.findViewById(R.id.btn_positive);
        TextView title = dialogView.findViewById(R.id.tv_sort_title);
        TextView textView_message = dialogView.findViewById(R.id.textView_message);
        LinearLayout lin_careful = dialogView.findViewById(R.id.lin_careful);
        CheckBox chk_trash = dialogView.findViewById(R.id.chk_trash);
        CheckBox chk_cloud = dialogView.findViewById(R.id.chk_cloud);
        LinearLayout lin_ckbox = dialogView.findViewById(R.id.lin_ckbox);
        lin_ckbox.setVisibility(View.VISIBLE);
        ArrayList<ListIdPic> selected = getSelected();
        animationView.setAnimation(R.raw.animation_delete);

        title.setText("Sure to Delete Pictures?");
        title.setVisibility(View.VISIBLE);
        textView_message.setText("Do you Really Want to Delete the selected " + selected.size() + " Pictures?");
        if (isbackup)
            chk_cloud.setVisibility(View.VISIBLE);
        else
            chk_cloud.setVisibility(View.GONE);

        chk_trash.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                lin_careful.setVisibility(View.GONE);
            } else {
                if (isbackup) {
                    if (chk_cloud.isChecked())
                        lin_careful.setVisibility(View.GONE);
                    else
                        lin_careful.setVisibility(View.VISIBLE);
                } else {
                    lin_careful.setVisibility(View.VISIBLE);
                }
            }
        });

        chk_cloud.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                lin_careful.setVisibility(View.GONE);
            } else {
                if (isbackup) {
                    if (chk_trash.isChecked())
                        lin_careful.setVisibility(View.GONE);
                    else
                        lin_careful.setVisibility(View.VISIBLE);
                } else {
                    lin_careful.setVisibility(View.VISIBLE);
                }
            }
        });
        button_negative.setText("Cancel");
        button_positive.setText("Delete");
        button_negative.setOnClickListener(v -> {
            if (actionMode != null)

            countSelected = 0;
            bottomSheetDialog.dismiss();
        });
        button_positive.setOnClickListener(v -> {
            getGoogleService();
            new DeleteCloudFileTask(chk_trash.isChecked(), chk_cloud.isChecked()).execute();
            bottomSheetDialog.dismiss();
            linImportPic.setVisibility(View.VISIBLE);
            optionsLayout.setVisibility(View.GONE);


        });
        chk_trash.setVisibility(View.VISIBLE);
        bottomSheetDialog.setContentView(dialogView);
        bottomSheetDialog.show();
    }
    private void getGoogleService() {
        if (TillsPth.isNetworkAvailable(context)) {
            credential = GoogleAccountCredential.usingOAuth2(context, Arrays.asList(DriveScopes.DRIVE_FILE));
            String accountName = SupPref.getValue(context, SupPref.AccountName, "");
            if (accountName.length() > 0) {
                credential.setSelectedAccountName(accountName);
                service = getDriveService(credential);
            } else {
            }
        } else {
        }
    }
    private Drive getDriveService(GoogleAccountCredential credential) {
        return new Drive.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), credential).setApplicationName(getString(R.string.app_setting_name)).build();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EntryAux.hideProgressDialog();
    }
    public class DeleteCloudFileTask extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        boolean isCheckedTrash;
        boolean isCheckCloud;
        public DeleteCloudFileTask(boolean isCheckedTrash, boolean isCheckCloud) {
            this.isCheckedTrash = isCheckedTrash;
            this.isCheckCloud = isCheckCloud;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(getActivity());
            pd.setTitle(getString(R.string.connected_server_msg));
            pd.setMessage(getString(R.string.please_wait_msg));
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();
        }
        @Override
        protected String doInBackground(String... strings) {
            ArrayList<ListIdPic> selectedList = getSelected();
            for (int i = 0; i < selectedList.size(); i++) {
                ListIdPic selected = selectedList.get(i);
                TooRfl.deleteImageVideoFile(context, selected.getId(), selected.getNewPath(), RDbhp, isCheckedTrash, isCheckCloud, service, 1);
                imagesList.remove(selected);
            }
            return "null";
        }
        @SuppressLint("SuspiciousIndentation")
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            viewListImageAdp.notifyDataSetChanged();
            Toast.makeText(context, "Files Deleted successfully", Toast.LENGTH_SHORT).show();
            if (actionMode != null)
//                actionMode.finish();

            countSelected = 0;
            if (imagesList.size() > 0) {
                rvPhotoList.setVisibility(View.VISIBLE);
                emptyLin.setVisibility(View.GONE);
                relativeNativeLayout.setVisibility(View.VISIBLE);

            } else {
                rvPhotoList.setVisibility(View.GONE);
                emptyLin.setVisibility(View.VISIBLE);
                relativeNativeLayout.setVisibility(View.GONE);
            }
        }
    }
}