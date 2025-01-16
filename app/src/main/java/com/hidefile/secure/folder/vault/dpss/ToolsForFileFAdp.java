package com.hidefile.secure.folder.vault.dpss;

import static android.app.Activity.RESULT_OK;
import static android.os.Build.VERSION.SDK_INT;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.airbnb.lottie.LottieAnimationView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

import com.hidefile.secure.folder.vault.AdActivity.Common_Adm;

import com.hidefile.secure.folder.vault.cluecanva.TreeRoot;
import com.hidefile.secure.folder.vault.cluecanva.ConfigureSetWise;
import com.hidefile.secure.folder.vault.cluecanva.SupPref;
import com.hidefile.secure.folder.vault.cluecanva.TillsPth;
import com.Joaquin.thiago.ListIdPic;
import com.hidefile.secure.folder.vault.R;
import com.hidefile.secure.folder.vault.dashex.IvFullScreenView;

import com.hidefile.secure.folder.vault.dashex.VidPlay;
import com.hidefile.secure.folder.vault.dashex.TrashBin;
import com.hidefile.secure.folder.vault.cluecanva.RDbhp;
import com.hidefile.secure.folder.vault.cluecanva.EntryAux;
import com.hidefile.secure.folder.vault.cluecanva.TillsFl;
import com.hidefile.secure.folder.vault.cluecanva.TooRfl;
import com.hidefile.secure.folder.vault.cluecanva.VTv;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import xiomaradrawn.illemire.saoirse.ListChoseA;
import xiomaradrawn.illemire.saoirse.ListResPickStore;

public class ToolsForFileFAdp extends Fragment implements View.OnClickListener, OnImageItemClickListner  {
    Context context;
    RDbhp dbHelper;
    public Drive service;
    LinearLayout llNoData;
    boolean isBackup = false;
    RecyclerView rvListPhoto;
    private int countSelected;
    ListFileAdp ivListFileAdp;
    private ActionMode actionMode;
    boolean isSelectedMode = false;
    private LinearLayout layoutOptions;
    private LinearLayout addNewImageLl;
    RelativeLayout relativeNativeLayout;
    private String idInter = "";





    public static final int EX_FILE_PICKER_RESULT = 0;
    final ArrayList<TreeRoot> fileList = new ArrayList<>();
    private final ActionMode.Callback callback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater menuInflater = mode.getMenuInflater();
            menuInflater.inflate(R.menu.emen_unet, menu);
            actionMode = mode;
            countSelected = 0;
            addNewImageLl.setVisibility(View.GONE);
            layoutOptions.setVisibility(View.VISIBLE);
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
                dialogDeleteVideoView(getActivity());
                return true;
            }
            return false;
        }
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            if (countSelected > 0) {
                deselectAll();
            }
            addNewImageLl.setVisibility(View.VISIBLE);
            layoutOptions.setVisibility(View.GONE);
            actionMode = null;
        }
    };
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.photo_fr, container, false);
        context = getActivity();

//        Common_Adm.getInstance().AmNativeLoad(getActivity(),view.findViewById(R.id.llNativeSmall),false);


        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            VTv tv_tital = getActivity().findViewById(R.id.tv_tital);
            tv_tital.setText("Secure File");
            ImageView iv_back = getActivity().findViewById(R.id.iv_back);
            iv_back.setOnClickListener(this);
            ImageView iv_option = getActivity().findViewById(R.id.iv_option);
            iv_option.setOnClickListener(v -> {
                PopupMenu popupMenu = new PopupMenu(getActivity(), iv_option);
                popupMenu.inflate(R.menu.emen_cover);
                popupMenu.getMenu().findItem(R.id.menu_list_to_grid).setVisible(false);
                popupMenu.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.menu_open_recover:
                            Intent intent = new Intent(context, TrashBin.class);
                            intent.putExtra("Type", "trashfiles");
                            startActivityForResult(intent, 120);
                            return true;
                        case R.id.menu_sort:
                            dialogSort();
                            return true;
                        case R.id.menu_select_all:
                            if (fileList.size() > 0) {
                                isSelectedMode = true;
                                ivListFileAdp.setSelectionMode(isSelectedMode);
                                if (actionMode == null) {
//                                    actionMode = getActivity().startActionMode(callback);
                                    addNewImageLl.setVisibility(View.GONE);
                                    layoutOptions.setVisibility(View.VISIBLE);
                                }
                                selectAll();
//                                actionMode.setTitle(countSelected + " " + getString(R.string.selected));
                            } else {
                                EntryAux.showToast(context, R.string.no_file);
                            }
                            return true;
                    }
                    return false;
                });

                popupMenu.show();
            });
        }


        relativeNativeLayout = view.findViewById(R.id.relativeSmall);

        addNewImageLl = view.findViewById(R.id.linImportPic);
        addNewImageLl.setOnClickListener(this);
        layoutOptions = view.findViewById(R.id.optionsLayout);
        LinearLayout unHideLl = view.findViewById(R.id.linUnHide);
        LinearLayout deleteLl = view.findViewById(R.id.linDelete);
        unHideLl.setVisibility(View.VISIBLE);
        deleteLl.setVisibility(View.VISIBLE);
        unHideLl.setOnClickListener(this);
        deleteLl.setOnClickListener(this);
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(context);
        dbHelper = RDbhp.getInstance(context);
        createImageDir();
        setHasOptionsMenu(true);
        llNoData = view.findViewById(R.id.emptyLin);
        rvListPhoto = view.findViewById(R.id.rvPhotoList);
        rvListPhoto.setLayoutManager(gridLayoutManager);
        ConfigureSetWise itemDecoration = new ConfigureSetWise(context, R.dimen.item_space);
        rvListPhoto.addItemDecoration(itemDecoration);
        ivListFileAdp = new ListFileAdp(context, fileList);
        ivListFileAdp.setItemClickEvent(this);
        rvListPhoto.setAdapter(ivListFileAdp);

        Init();

        return view;
    }




    void Init() {
        fileList.clear();
        fileList.addAll(dbHelper.getAllFiles());
        if (ivListFileAdp != null) {
            ivListFileAdp.notifyDataSetChanged();
        }
        if (fileList.size() > 0) {

            rvListPhoto.setVisibility(View.VISIBLE);
            llNoData.setVisibility(View.GONE);
            relativeNativeLayout.setVisibility(View.VISIBLE);

        } else {
            rvListPhoto.setVisibility(View.GONE);
            llNoData.setVisibility(View.VISIBLE);
            relativeNativeLayout.setVisibility(View.GONE);

        }
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

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                getActivity().onBackPressed();
                break;
            case R.id.linImportPic:
            {
                ListChoseA listChoseA = new ListChoseA();
                listChoseA.setNewFolderButtonDisabled(true);
                listChoseA.setSortButtonDisabled(true);
                listChoseA.setQuitButtonEnabled(true);
                listChoseA.setSortingType(ListChoseA.SortingType.NAME_DESC);
                listChoseA.setHideHiddenFilesEnabled(true);
                listChoseA.setChoiceType(ListChoseA.ChoiceType.FILES);
                listChoseA.start(this, EX_FILE_PICKER_RESULT);
            }
            break;
            case R.id.linUnHide:
                dialogUnHideFile(getActivity());
                break;
            case R.id.linDelete:
                dialogDeleteVideoView(getActivity());
                break;
            default:
                break;
        }
    }
    @Override
    public void onImageItemClick(int position) {
        if (isSelectedMode) {
            if (actionMode == null) {
                addNewImageLl.setVisibility(View.GONE);
                layoutOptions.setVisibility(View.VISIBLE);
            }
            toggleSelection(position);

            if (countSelected == 0) {
                addNewImageLl.setVisibility(View.VISIBLE);
                layoutOptions.setVisibility(View.GONE);
                isSelectedMode = false;
                ivListFileAdp.setSelectionMode(isSelectedMode);
            }
        } else {
            String existingNewPath = fileList.get(position).newPath;
            File file = new File(existingNewPath);
            String ext = "" + fileExt(fileList.get(position).displayName);
            MimeTypeMap myMime = MimeTypeMap.getSingleton();
            Intent newIntent = new Intent(Intent.ACTION_VIEW);
            if (SDK_INT < Build.VERSION_CODES.R) {
                if (!ext.isEmpty()) {
                    String mimeType = myMime.getMimeTypeFromExtension(ext);
                    if (!TextUtils.isEmpty(mimeType)) {
                        Uri fileURI;
                        if (SDK_INT >= Build.VERSION_CODES.R) {
                            fileURI = Uri.parse(file.getPath());
                        } else {
                            fileURI = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
                        }
                        newIntent.setDataAndType(fileURI, mimeType);
                        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        newIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        try {
                            context.startActivity(newIntent);
                        } catch (ActivityNotFoundException e) {
                            EntryAux.showToast(context, "No Event for this file");
                        }
                    } else {
                        EntryAux.showToast(context, "No Event for this file");
                    }
                } else {
                    EntryAux.showToast(context, "No Event for this file");
                }
            } else {
                if (!TextUtils.isEmpty(ext)) {
                    String mimeType = myMime.getMimeTypeFromExtension(ext);
                    if (!TextUtils.isEmpty(mimeType)) {
                        if (mimeType.contains("image")) {
                            ArrayList<ListIdPic> imagesList = new ArrayList<>();
                            DocumentFile file1 = DocumentFile.fromSingleUri(getActivity(), Uri.parse(existingNewPath));
                            ListIdPic media_data = new ListIdPic();
                            media_data.setDisplayName(TillsFl.getOriginalFileName(getFileName(file1.getUri())));
                            media_data.setNewPath(file1.getUri().toString());
                            File mFile = new File(file1.getUri().getPath());
                            media_data.setSize(mFile.length());
                            media_data.setModified(getFileModifiedDate(file1.getUri()));
                            String fileName = "";
                            mFile.getName();
                            fileName = mFile.getName();
                            media_data.setMimeType(TillsPth.getMimeType(fileName));
                            imagesList.add(media_data);
                            Intent intent = new Intent(context, IvFullScreenView.class);
                            intent.putExtra("postion", 0);
                            intent.putExtra("isFromVault", true);
                            intent.putExtra("listIdPics", imagesList);
                            startActivity(intent);
                        } else if (mimeType.contains("video")) {
                            Intent intent = new Intent(context, VidPlay.class);
                            intent.putExtra("filepath", file.getPath());
                            startActivity(intent);
                        } else {
                            EntryAux.showToast(context, "Sorry, No able to read this type of file");
                        }
                    } else {
                        EntryAux.showToast(context, "Sorry, No able to read this type of file");
                    }
                } else {
                    EntryAux.showToast(context, "Sorry, No able to read this type of file");
                }
            }
        }
    }
    private String fileExt(String url) {
        if (url.indexOf("?") > -1) {
            url = url.substring(0, url.indexOf("?"));
        }
        if (url.lastIndexOf(".") == -1) {
            return null;
        } else {
            String ext = url.substring(url.lastIndexOf(".") + 1);
            if (ext.contains("%")) {
                ext = ext.substring(0, ext.indexOf("%"));
            }
            if (ext.indexOf("/") > -1) {
                ext = ext.substring(0, ext.indexOf("/"));
            }
            return ext.toLowerCase();

        }
    }

    @Override
    public void onImageItemLongClick(int position) {
        isSelectedMode = true;
        ivListFileAdp.setSelectionMode(isSelectedMode);
        if (actionMode == null) {
            addNewImageLl.setVisibility(View.GONE);
            layoutOptions.setVisibility(View.VISIBLE);
        }
        toggleSelection(position);
        if (countSelected == 0) {
            isSelectedMode = false;
            ivListFileAdp.setSelectionMode(isSelectedMode);
            addNewImageLl.setVisibility(View.VISIBLE);
            layoutOptions.setVisibility(View.GONE);
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

    }
    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_open_recover:
                Intent intent = new Intent(context, TrashBin.class);
                intent.putExtra("Type", "trashfiles");
                startActivityForResult(intent, 120);
                return true;
            case R.id.menu_sort:
                dialogSort();
                return true;
            case R.id.menu_select_all:
                if (fileList.size() > 0) {
                    isSelectedMode = true;
                    ivListFileAdp.setSelectionMode(isSelectedMode);
                    if (actionMode == null) {
                        addNewImageLl.setVisibility(View.GONE);
                        layoutOptions.setVisibility(View.VISIBLE);
                    }
                    selectAll();
                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.no_file), Toast.LENGTH_SHORT).show();
                }
                return true;
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
        button_negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        button_positive.setOnClickListener(v -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            RadioButton radioButton = dialogView.findViewById(selectedId);
            sortData(radioButton);
            bottomSheetDialog.dismiss();
        });
        bottomSheetDialog.setContentView(dialogView);
        bottomSheetDialog.show();
    }

    @SuppressLint("NonConstantResourceId")
    public void sortData(RadioButton radioButton) {
        ListChoseA.SortingType sortingType = ListChoseA.SortingType.NAME_ASC;
        if (radioButton != null) {
            switch (radioButton.getId()) {
                case R.id.tv_sort_name:
                    Collections.sort(fileList, new Comparator<TreeRoot>() {
                        @Override
                        public int compare(TreeRoot lhs, TreeRoot rhs) {
                            return lhs.getDisplayName().toLowerCase().compareTo(rhs.getDisplayName().toLowerCase());
                        }
                    });
                    ivListFileAdp.setData(fileList);
                    break;
                case R.id.tv_sort_size:

                    Collections.sort(fileList, new Comparator<TreeRoot>() {
                        @Override
                        public int compare(TreeRoot o1, TreeRoot o2) {
                            return Integer.compare((int) (o2.getSize() / 1000), (int) (o1.getSize() / 1000));
                        }
                    });
                    ivListFileAdp.setData(fileList);
                    break;
                case R.id.tv_sort_date:
                    Collections.sort(fileList, new Comparator<TreeRoot>() {
                        @Override
                        public int compare(TreeRoot o1, TreeRoot o2) {
                            return Integer.compare((int) (o2.getModified() / 1000), (int) (o1.getModified() / 1000));
                        }
                    });
                    ivListFileAdp.setData(fileList);
                    break;
            }
            ivListFileAdp.notifyDataSetChanged();
        }
    }


    private void toggleSelection(int position) {
        fileList.get(position).checked = !fileList.get(position).isChecked();
        if (fileList.get(position).checked) {
            countSelected++;
        } else {
            countSelected--;
        }
        ivListFileAdp.notifyDataSetChanged();
    }

    private void deselectAll() {
        for (int i = 0, l = fileList.size(); i < l; i++) {
            fileList.get(i).checked = false;
        }
        isSelectedMode = false;
        ivListFileAdp.setSelectionMode(isSelectedMode);

        countSelected = 0;
        ivListFileAdp.notifyDataSetChanged();
    }

    private void selectAll() {
        for (int i = 0, l = fileList.size(); i < l; i++) {
            fileList.get(i).checked = true;
        }
        isSelectedMode = true;
        ivListFileAdp.setSelectionMode(isSelectedMode);

        countSelected = fileList.size();
        ivListFileAdp.notifyDataSetChanged();
    }

    private ArrayList<TreeRoot> getSelected() {
        ArrayList<TreeRoot> selectedImages = new ArrayList<>();
        for (int i = 0, l = fileList.size(); i < l; i++) {
            if (fileList.get(i).isChecked()) {
                selectedImages.add(fileList.get(i));
            }
        }
        return selectedImages;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == EX_FILE_PICKER_RESULT) {
                ListResPickStore result = ListResPickStore.getFromIntent(data);

                List<TreeRoot> treeRootList = new ArrayList<>();
                if (result != null && result.getCount() > 0) {
                    TreeRoot treeRoot;
                    for (int i = 0; i < result.getCount(); i++) {
                        treeRoot = new TreeRoot();
                        treeRoot.setPath(result.getPath() + result.getNames().get(i));
                        treeRootList.add(treeRoot);
                    }
                }

                hideAllImage(treeRootList);
            }
        } else if (requestCode == 1001) {
            EntryAux.hideProgressDialog();
            EntryAux.showToast(context, R.string.hide_success_msg);
            Init();
        }
        if (requestCode == 120) {
            Init();
        }
    }

    private void hideAllImage(List<TreeRoot> videoItemList) {
        if (videoItemList != null && videoItemList.size() > 0) {
            EntryAux.showProgressDialog(getActivity(), R.string.please_wait_msg);
            new Thread(() -> {
                File file;
                List<String> pathList = new ArrayList<>();
                for (TreeRoot videoItem : videoItemList) {
                    file = new File(videoItem.getPath());
                    if (file.exists()) {
                        pathList.add(videoItem.getPath());
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
                            final ArrayList<Uri> deleteRequestList = TooRfl.hideFiles(context, selectedList, dbHelper, 3);

                            getActivity().runOnUiThread(() -> {
                                EntryAux.hideProgressDialog();
                                Init();
                                EntryAux.showToast(context, R.string.hide_success_msg);

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
        EntryAux.hideProgressDialog();
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                PendingIntent pi = MediaStore.createDeleteRequest(getActivity().getContentResolver(), uriList);
                startIntentSenderForResult(pi.getIntentSender(), 1001, null, 0, 0, 0, null);
            }
        } catch (Exception e) {
            System.out.println("AFTER HIDE : " + e.getMessage());
        }
    }
    public boolean makeUnhideDirForFile() {
        File myDirectory = new File(TillsPth.restorePathFiles);
        if (!myDirectory.exists()) {
            return myDirectory.mkdirs();
        } else {
            return true;
        }
    }


    public void createImageDir() {
        File myDirectory = new File(TillsPth.nohideFile);
        if (!myDirectory.exists()) {
            myDirectory.mkdirs();
            Log.e("TAG", "createImageDir: mkdir");
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
        title.setText("Sure to Unhide Files?");
        title.setVisibility(View.VISIBLE);
        textView_message.setText("Do you Really Want to Unhide selected file ??");
        button_positive.setText("Unhide");
        button_negative.setText("Cancel");
        animationView.setAnimation(R.raw.animation_no_root);

        button_positive.setOnClickListener(view -> {

            EntryAux.showProgressDialog(getActivity(), R.string.please_wait_msg);
            if(makeUnhideDirForFile()) {
                new Thread(() -> {
                    ArrayList<TreeRoot> selected = getSelected();
                    TreeRoot treeRoot;
                    boolean isDeleted;
                    for (int i = 0; i < selected.size(); i++) {
                        treeRoot = selected.get(i);
                        isDeleted = TooRfl.makeUnHide(context, treeRoot.getPath(), treeRoot.getNewPath(), TillsPth.restorePathFiles + treeRoot.getDisplayName(), 3);
                        if (isDeleted) {
                            dbHelper.deleteFileItem(treeRoot.getId());
                        }
                    }
                    getActivity().runOnUiThread(() -> {
                        if (actionMode != null)
                            countSelected = 0;
                        EntryAux.hideProgressDialog();
                        EntryAux.showToast(context, R.string.un_hide_success_msg);
                        addNewImageLl.setVisibility(View.VISIBLE);
                        layoutOptions.setVisibility(View.GONE);
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
    @SuppressLint("SetTextI18n")
    public void dialogDeleteVideoView(final Activity act) {
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
        animationView.setAnimation(R.raw.animation_delete);
        ArrayList<TreeRoot> selected = getSelected();
        title.setText("Sure to Delete files?");
        title.setVisibility(View.VISIBLE);
        textView_message.setText("Do you Really Want to Delete the selected " + selected.size() + " files?");
        if (isBackup)
            chk_cloud.setVisibility(View.VISIBLE);
        else
            chk_cloud.setVisibility(View.GONE);

        chk_trash.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                lin_careful.setVisibility(View.GONE);
            } else {
                if (isBackup) {
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
                if (isBackup) {
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
            new DeleteCloudFileTask(context, chk_trash.isChecked(), chk_cloud.isChecked()).execute();
            bottomSheetDialog.dismiss();
            layoutOptions.setVisibility(View.GONE);
            addNewImageLl.setVisibility(View.VISIBLE);

        });
        chk_trash.setVisibility(View.VISIBLE);
        bottomSheetDialog.setContentView(dialogView);
        bottomSheetDialog.show();
    }




    private void getGoogleService() {
        if (TillsPth.isNetworkAvailable(context)) {
            GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(context, Arrays.asList(DriveScopes.DRIVE_FILE));
            String accountName = SupPref.getValue(context, SupPref.AccountName, "");
            if (accountName.length() > 0) {
                credential.setSelectedAccountName(accountName);
                service = getDriveService(credential);
            } else {}} else {}}
    private Drive getDriveService(GoogleAccountCredential credential) {
        return new Drive.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), credential).setApplicationName(getString(R.string.app_name)).build();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EntryAux.hideProgressDialog();
    }

    @SuppressLint("StaticFieldLeak")
    public class DeleteCloudFileTask extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        boolean isCheckedTrash;
        boolean isCheckCloud;
        Context context;
        public DeleteCloudFileTask(Context context, boolean isCheckedTrash, boolean isCheckCloud) {
            this.isCheckedTrash = isCheckedTrash;
            this.isCheckCloud = isCheckCloud;
            this.context = context;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(getActivity());
            pd.setTitle(context.getString(R.string.connected_server_msg));
            pd.setMessage(context.getString(R.string.please_wait_msg));
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();
        }
        @Override
        protected String doInBackground(String... strings) {
            ArrayList<TreeRoot> selectedList = getSelected();
            for (int i = 0; i < selectedList.size(); i++) {

                TreeRoot selected = selectedList.get(i);
                TooRfl.deleteImageVideoFile(ToolsForFileFAdp.this.context, selected.getId(), selected.getNewPath(), dbHelper, isCheckedTrash, isCheckCloud, service, 3);

                fileList.remove(selected);
            }
            return "null";
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            ivListFileAdp.notifyDataSetChanged();
            Toast.makeText(ToolsForFileFAdp.this.context, "File Delete Successfully", Toast.LENGTH_SHORT).show();
            if (actionMode != null)
//                actionMode.finish();

            countSelected = 0;

            if (fileList.size() > 0) {

                rvListPhoto.setVisibility(View.VISIBLE);
                llNoData.setVisibility(View.GONE);
                relativeNativeLayout.setVisibility(View.VISIBLE);

            } else {
                rvListPhoto.setVisibility(View.GONE);
                llNoData.setVisibility(View.VISIBLE);
                relativeNativeLayout.setVisibility(View.GONE);

            }
        }
    }
}
