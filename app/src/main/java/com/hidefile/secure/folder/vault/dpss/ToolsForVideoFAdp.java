package com.hidefile.secure.folder.vault.dpss;

import static android.app.Activity.RESULT_OK;
import static com.hidefile.secure.folder.vault.edptrs.InSafe.isGridlayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Joaquin.thiago.ListItVid;
import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.hidefile.secure.folder.vault.AdActivity.Call_Back_Ads;
import com.hidefile.secure.folder.vault.AdActivity.Common_Adm;
import com.hidefile.secure.folder.vault.R;
import com.hidefile.secure.folder.vault.cluecanva.ConfigureSetWise;
import com.hidefile.secure.folder.vault.cluecanva.EntryAux;
import com.hidefile.secure.folder.vault.cluecanva.RDbhp;
import com.hidefile.secure.folder.vault.cluecanva.SupPref;
import com.hidefile.secure.folder.vault.cluecanva.TillsPth;
import com.hidefile.secure.folder.vault.cluecanva.TooRfl;
import com.hidefile.secure.folder.vault.cluecanva.VTv;
import com.hidefile.secure.folder.vault.dashex.PhotoAndVideoAlbumSelection;
import com.hidefile.secure.folder.vault.dashex.TrashBin;
import com.hidefile.secure.folder.vault.dashex.VidPlay;
import com.hidefile.secure.folder.vault.edptrs.InSafe;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class ToolsForVideoFAdp extends Fragment implements View.OnClickListener, OnImageItemClickListner{
    Context mContext;
    RDbhp RDbhp;
    public Drive service;
    RecyclerView rvVidList;
    boolean isbackup = false;
    private int countSelected;
    private ActionMode actionMode;
    FloatingActionButton iv_photo;
    boolean isSelectedMode = false;
    VideoListAdp imageListAdapter;
    private LinearLayout layoutOptions;
    GridLayoutManager gridLayoutManager;

    RelativeLayout relativeNativeLayout;


    LinearLayout linEmpty, linAddNewPic;
    RelativeLayout adContainerBanner;





    final ArrayList<ListItVid> videoList = new ArrayList<>();
    private final ActionMode.Callback callback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater menuInflater = mode.getMenuInflater();
            menuInflater.inflate(R.menu.emen_unet, menu);
            actionMode = mode;
            countSelected = 0;
            linAddNewPic.setVisibility(View.GONE);
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
            linAddNewPic.setVisibility(View.VISIBLE);
            layoutOptions.setVisibility(View.GONE);
            actionMode = null;
        }
    };

    View view;
    @SuppressLint({"NotifyDataSetChanged", "NonConstantResourceId"})
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.photo_fr, container, false);
        mContext = getActivity();

//        Common_Adm.getInstance().AmNativeLoad(getActivity(),view.findViewById(R.id.llNativeSmall),false);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            VTv tv_tital = getActivity().findViewById(R.id.tv_tital);
            tv_tital.setText("Secure Video");
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
                            Intent intent = new Intent(mContext, TrashBin.class);
                            intent.putExtra("Type", "trashvideos");
                            startActivityForResult(intent, 120);
                            return true;
                        case R.id.menu_sort:
                            dialogSort();
                            return true;
                        case R.id.menu_select_all:
                            if (videoList.size() > 0) {
                                isSelectedMode = true;
                                imageListAdapter.setSelectionMode(isSelectedMode);
                                imageListAdapter.notifyDataSetChanged();
                                if (actionMode == null) {

//                                    actionMode = getActivity().startActionMode(callback);
                                    linAddNewPic.setVisibility(View.GONE);
                                    layoutOptions.setVisibility(View.VISIBLE);

                                }
                                selectAll();
//                                actionMode.setTitle(countSelected + " " + getString(R.string.selected));
                            } else {
                                EntryAux.showToast(mContext, R.string.no_file);
                            }
                            return true;
                        case R.id.menu_list_to_grid: {
                            if (gridLayoutManager.getSpanCount() == 1) {
                                gridLayoutManager.setSpanCount(4);
                                imageListAdapter.setSpanCount(4);
                                isGridlayout = true;
                            } else {
                                gridLayoutManager.setSpanCount(1);
                                imageListAdapter.setSpanCount(1);
                                isGridlayout = false;
                            }

                            String text1 = isGridlayout ? "Grid to list" : "List to grid";
                            item.setTitle(text1);

                            if (imageListAdapter != null) {
                                imageListAdapter.notifyDataSetChanged();
                            }
                            return true;
                        }
                    }
                    return false;
                });

                popupMenu.show();
            });
        }
        RDbhp = RDbhp.getInstance(mContext);
        createImageDir();



        setHasOptionsMenu(true);
        iv_photo = view.findViewById(R.id.iv_img);
        relativeNativeLayout = view.findViewById(R.id.relativeSmall);
        iv_photo.setOnClickListener(this);
        linAddNewPic = view.findViewById(R.id.linImportPic);
        linAddNewPic.setOnClickListener(this);
        linEmpty = view.findViewById(R.id.emptyLin);
        rvVidList = view.findViewById(R.id.rvPhotoList);
        int no = isGridlayout ? 4 : 1;
        gridLayoutManager = new GridLayoutManager(mContext, no);
        rvVidList.setLayoutManager(gridLayoutManager);
        ConfigureSetWise itemDecoration = new ConfigureSetWise(mContext, R.dimen.item_space);
        rvVidList.addItemDecoration(itemDecoration);
        imageListAdapter = new VideoListAdp(mContext, videoList);
        imageListAdapter.setItemClickEvent(this);
        imageListAdapter.setImageResize(TillsPth.getImageResize(mContext, rvVidList));
        rvVidList.setAdapter(imageListAdapter);
        layoutOptions = view.findViewById(R.id.optionsLayout);
        LinearLayout linUnHide = view.findViewById(R.id.linUnHide);
        LinearLayout linDelete = view.findViewById(R.id.linDelete);
        linUnHide.setVisibility(View.VISIBLE);
        linDelete.setVisibility(View.VISIBLE);
        linUnHide.setOnClickListener(this);
        linDelete.setOnClickListener(this);
//        Common_Adm.getInstance().AmNativeLoad(getActivity(),  view.findViewById(R.id.llNativeSmall), false);
        Init();


        return view;
    }




    public void Init() {
        int no = isGridlayout ? 4 : 1;
        gridLayoutManager.setSpanCount(no);
        videoList.clear();
        videoList.addAll(RDbhp.getAllVideos());
        if (imageListAdapter != null) {
            imageListAdapter.notifyDataSetChanged();
        }
        if (videoList.size() > 0) {
            rvVidList.setVisibility(View.VISIBLE);
            relativeNativeLayout.setVisibility(View.VISIBLE);
            linEmpty.setVisibility(View.GONE);
        } else {
            rvVidList.setVisibility(View.GONE);
            linEmpty.setVisibility(View.VISIBLE);
            relativeNativeLayout.setVisibility(View.GONE);
        }
    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                getActivity().onBackPressed();
                break;
            case R.id.iv_img:
            case R.id.linImportPic: {
                Common_Adm.getInstance();
                Common_Adm.mInterstitialAdClickCount++;
                Common_Adm.getInstance().loadOrShowAdmInterstial(true, getActivity(), new Call_Back_Ads() {
                    @Override
                    public void onAdsClose() {
                        Intent intent = new Intent(mContext, PhotoAndVideoAlbumSelection.class);
                        intent.putExtra(InSafe.INTENT_EXTRA_LIMIT, 10);
                        intent.putExtra(InSafe.INTENT_EXTRA_SELECT_TYPE, 2);
                        startActivityForResult(intent, InSafe.REQUEST_CODE_VIDEO);
                    }

                    @Override
                    public void onLoading() {
                        Intent intent = new Intent(mContext, PhotoAndVideoAlbumSelection.class);
                        intent.putExtra(InSafe.INTENT_EXTRA_LIMIT, 10);
                        intent.putExtra(InSafe.INTENT_EXTRA_SELECT_TYPE, 2);
                        startActivityForResult(intent, InSafe.REQUEST_CODE_VIDEO);
                    }

                    @Override
                    public void onAdsFail() {
                        Intent intent = new Intent(mContext, PhotoAndVideoAlbumSelection.class);
                        intent.putExtra(InSafe.INTENT_EXTRA_LIMIT, 10);
                        intent.putExtra(InSafe.INTENT_EXTRA_SELECT_TYPE, 2);
                        startActivityForResult(intent, InSafe.REQUEST_CODE_VIDEO);
                    }
                });


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
//                actionMode = getActivity().startActionMode(callback);
                linAddNewPic.setVisibility(View.GONE);
                layoutOptions.setVisibility(View.VISIBLE);
            }
            toggleSelection(position);
//            actionMode.setTitle(countSelected + " " + getString(R.string.selected));

            if (countSelected == 0) {
//                actionMode.finish();
                isSelectedMode = false;
                imageListAdapter.setSelectionMode(isSelectedMode);
                imageListAdapter.notifyDataSetChanged();
                linAddNewPic.setVisibility(View.VISIBLE);
                layoutOptions.setVisibility(View.GONE);
            }
        } else {
            Intent intent = new Intent(mContext, VidPlay.class);
            intent.putExtra("filepath", videoList.get(position).getNewPath());
            startActivity(intent);
        }
    }
    @Override
    public void onImageItemLongClick(int position) {
        isSelectedMode = true;
        imageListAdapter.setSelectionMode(isSelectedMode);
        if (actionMode == null) {
//            actionMode = getActivity().startActionMode(callback);
            linAddNewPic.setVisibility(View.GONE);
            layoutOptions.setVisibility(View.VISIBLE);
        }
        toggleSelection(position);
//        actionMode.setTitle(countSelected + " " + getString(R.string.selected));

        if (countSelected == 0) {
//            actionMode.finish();
            isSelectedMode = false;
            imageListAdapter.setSelectionMode(isSelectedMode);
            linAddNewPic.setVisibility(View.VISIBLE);
            layoutOptions.setVisibility(View.GONE);
        }
        imageListAdapter.notifyDataSetChanged();
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
                Intent intent = new Intent(mContext, TrashBin.class);
                intent.putExtra("Type", "trashvideos");
                startActivityForResult(intent, 120);
                return true;
            case R.id.menu_sort:
                dialogSort();
                return true;
            case R.id.menu_select_all:
                if (videoList.size() > 0) {
                    isSelectedMode = true;
                    imageListAdapter.setSelectionMode(isSelectedMode);
                    imageListAdapter.notifyDataSetChanged();
                    if (actionMode == null) {
//                        actionMode = getActivity().startActionMode(callback);
                        linAddNewPic.setVisibility(View.GONE);
                        layoutOptions.setVisibility(View.VISIBLE);
                    }
                    selectAll();
//                    actionMode.setTitle(countSelected + " " + getString(R.string.selected));
                } else {
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.no_file), Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.menu_list_to_grid: {
                if (gridLayoutManager.getSpanCount() == 1) {
                    gridLayoutManager.setSpanCount(4);
                    imageListAdapter.setSpanCount(4);
                    isGridlayout = true;
                } else {
                    gridLayoutManager.setSpanCount(1);
                    imageListAdapter.setSpanCount(1);
                    isGridlayout = false;
                }

                String text = isGridlayout ? "Grid to list" : "List to grid";
                item.setTitle(text);

                if (imageListAdapter != null) {
                    imageListAdapter.notifyDataSetChanged();
                }
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("SetTextI18n")
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

    @SuppressLint("NonConstantResourceId")
    public void sortData(RadioButton radioButton) {
        if (radioButton == null) {
            return;
        }
        switch (radioButton.getId()) {
            case R.id.tv_sort_name:
                Collections.sort(videoList, new Comparator<ListItVid>() {
                    @Override
                    public int compare(ListItVid lhs, ListItVid rhs) {
                        return lhs.getDisplayName().compareTo(rhs.getDisplayName());
                    }
                });
                imageListAdapter.setData(videoList);
                break;
            case R.id.tv_sort_size:

                Collections.sort(videoList, new Comparator<ListItVid>() {
                    @Override
                    public int compare(ListItVid o1, ListItVid o2) {
                        return Integer.compare((int) (o2.getSize() / 1000), (int) (o1.getSize() / 1000));
                    }
                });
                imageListAdapter.setData(videoList);
                break;

            case R.id.tv_sort_date:
                Collections.sort(videoList, new Comparator<ListItVid>() {
                    @Override
                    public int compare(ListItVid o1, ListItVid o2) {
                        return Integer.valueOf((int) (new File(o1.getNewPath()).lastModified() / 1000)).compareTo(Integer.valueOf((int) (new File(o2.getNewPath()).lastModified() / 1000)));
                    }
                });
                imageListAdapter.setData(videoList);
                break;
        }
        imageListAdapter.notifyDataSetChanged();
    }

    private void toggleSelection(int position) {
        videoList.get(position).checked = !videoList.get(position).isChecked();
        if (videoList.get(position).checked) {
            countSelected++;
        } else {
            countSelected--;
        }
        imageListAdapter.notifyDataSetChanged();
    }

    private void deselectAll() {
        for (int i = 0, l = videoList.size(); i < l; i++) {
            videoList.get(i).checked = false;
        }
        isSelectedMode = false;
        imageListAdapter.setSelectionMode(isSelectedMode);
        countSelected = 0;
        imageListAdapter.notifyDataSetChanged();
    }

    private void selectAll() {
        for (int i = 0, l = videoList.size(); i < l; i++) {
            videoList.get(i).checked = true;
        }
        isSelectedMode = true;
        imageListAdapter.setSelectionMode(isSelectedMode);
        countSelected = videoList.size();
        imageListAdapter.notifyDataSetChanged();
    }

    private ArrayList<ListItVid> getSelected() {
        ArrayList<ListItVid> selectedImages = new ArrayList<>();
        for (int i = 0, l = videoList.size(); i < l; i++) {
            if (videoList.get(i).isChecked()) {
                selectedImages.add(videoList.get(i));
            }
        }
        return selectedImages;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == InSafe.REQUEST_CODE_VIDEO && resultCode == RESULT_OK) {
            ArrayList<ListItVid> items = (ArrayList<ListItVid>) data.getSerializableExtra(InSafe.INTENT_EXTRA_VIDEOS);
            hideAllImage(items);
        } else if (requestCode == 1001) {
            EntryAux.hideProgressDialog();
            EntryAux.showToast(mContext, R.string.hide_success_msg);
            Init();
        }
        if (requestCode == 120) {
            Init();
        }
    }

    private void hideAllImage(List<ListItVid> listItVidList) {
        if (listItVidList != null && listItVidList.size() > 0) {

            EntryAux.showProgressDialog(getActivity(), R.string.please_wait_msg);

            new Thread(() -> {
                File file;
                List<String> pathList = new ArrayList<>();
                for (ListItVid listItVid : listItVidList) {
                    file = new File(listItVid.getPath());
                    if (file.exists()) {
                        pathList.add(listItVid.getPath());
                    }
                }

                String[] stringPathArray = new String[pathList.size()];
                pathList.toArray(stringPathArray);

                HashMap<String, Uri> selectedList = new HashMap<>();
                MediaScannerConnection.scanFile(mContext, stringPathArray, null, new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) {

                        selectedList.put(path, uri);

                        if (selectedList.size() == stringPathArray.length) {
                            final ArrayList<Uri> deleteRequestList = TooRfl.hideFiles(mContext, selectedList, RDbhp, 2);

                            getActivity().runOnUiThread(() -> {
                                EntryAux.hideProgressDialog();
                                Init();
                                EntryAux.showToast(mContext, R.string.hide_success_msg);

                                if (deleteRequestList.size() > 0) {
                                    requestDeletePermission(deleteRequestList);
                                } else {
                                    EntryAux.showToast(mContext, R.string.hide_success_msg);
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

    public void createImageDir() {
        File myDirectory = new File(TillsPth.nohideVideo);
        if (!myDirectory.exists()) {
            myDirectory.mkdirs();
        } else {
        }
    }
    public boolean makeUnhideDirForVideo() {
        File myDirectory = new File(TillsPth.restorePathImage);
        if (!myDirectory.exists()) {
            return myDirectory.mkdirs();
        } else {
            return true;
        }
    }

    @SuppressLint("SetTextI18n")
    public void dialogUnHideFile(final Activity act) {

        BottomSheetDialog unHideBottomSheetDialog = new BottomSheetDialog(act, R.style.BottomSheetDialogTheme);
        View dialogView = View.inflate(act, R.layout.dig_delete, null);
        LottieAnimationView animationView = dialogView.findViewById(R.id.animation_view);
        AppCompatButton button_positive = dialogView.findViewById(R.id.btn_positive);
        AppCompatButton button_negative = dialogView.findViewById(R.id.btn_negative);
        TextView title = dialogView.findViewById(R.id.tv_sort_title);
        TextView textView_message = dialogView.findViewById(R.id.textView_message);
        title.setText("Sure to Unhide Videos?");
        title.setVisibility(View.VISIBLE);
        textView_message.setText("Do you Really Want to Unhide selected videos?");
        button_positive.setText("Unhide");
        button_negative.setText("Cancel");
        animationView.setAnimation(R.raw.animation_no_root);
        button_positive.setOnClickListener(view -> {
            EntryAux.showProgressDialog(getActivity(), R.string.please_wait_msg);
            if(makeUnhideDirForVideo()) {
                new Thread(() -> {
                    ArrayList<ListItVid> selected = getSelected();
                    ListItVid listItVid;
                    boolean isDeleted;
                    for (int i = 0; i < selected.size(); i++) {
                        listItVid = selected.get(i);
                        isDeleted = TooRfl.makeUnHide(mContext, listItVid.getPath(), listItVid.getNewPath(), TillsPth.restorePathVideo + listItVid.getDisplayName(), 2);
                        if (isDeleted) {
                            RDbhp.deleteVideoItem(listItVid.getId());
                        }
                    }
                    getActivity().runOnUiThread(() -> {
                        if (actionMode != null)
                            countSelected = 0;
                        EntryAux.hideProgressDialog();
                        EntryAux.showToast(mContext, R.string.un_hide_success_msg);
                        linAddNewPic.setVisibility(View.VISIBLE);
                        layoutOptions.setVisibility(View.GONE);

                        Init();
                    });
                }).start();
            }
            unHideBottomSheetDialog.dismiss();
        });
        button_negative.setOnClickListener(view -> {
            unHideBottomSheetDialog.dismiss();
        });
        unHideBottomSheetDialog.setContentView(dialogView);
        unHideBottomSheetDialog.show();
    }
    @SuppressLint({"SetTextI18n", "SuspiciousIndentation"})
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
        ArrayList<ListItVid> selected = getSelected();
        animationView.setAnimation(R.raw.animation_delete);
        title.setText("Sure to Delete Videos?");
        title.setVisibility(View.VISIBLE);
        textView_message.setText("Do you Really Want to Delete the selected " + selected.size() + " Videos?");
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
//                actionMode.finish();
            countSelected = 0;
            bottomSheetDialog.dismiss();



        });
        button_positive.setOnClickListener(v -> {
            getGoogleService();
            new DeleteCloudFileTask(mContext, chk_trash.isChecked(), chk_cloud.isChecked()).execute();
            bottomSheetDialog.dismiss();
            linAddNewPic.setVisibility(View.VISIBLE);
            layoutOptions.setVisibility(View.GONE);
        });
        chk_trash.setVisibility(View.VISIBLE);
        bottomSheetDialog.setContentView(dialogView);
        bottomSheetDialog.show();
    }

    private void getGoogleService() {
        if (TillsPth.isNetworkAvailable(mContext)) {
            GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(mContext, Arrays.asList(DriveScopes.DRIVE_FILE));
            String accountName = SupPref.getValue(mContext, SupPref.AccountName, "");
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
            ArrayList<ListItVid> selectedList = getSelected();
            for (int i = 0; i < selectedList.size(); i++) {

                ListItVid selected = selectedList.get(i);
                TooRfl.deleteImageVideoFile(mContext, selected.getId(), selected.getNewPath(), RDbhp, isCheckedTrash, isCheckCloud, service, 2);

                videoList.remove(selected);
            }
            return "null";
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            imageListAdapter.notifyDataSetChanged();
            Toast.makeText(mContext, "Files Deleted successfully", Toast.LENGTH_SHORT).show();
            if (actionMode != null)
//                actionMode.finish();
            countSelected = 0;

            if (videoList.size() > 0) {
                rvVidList.setVisibility(View.VISIBLE);
                relativeNativeLayout.setVisibility(View.VISIBLE);
                linEmpty.setVisibility(View.GONE);
            } else {
                rvVidList.setVisibility(View.GONE);
                linEmpty.setVisibility(View.VISIBLE);
                relativeNativeLayout.setVisibility(View.GONE);
            }

        }
    }
}
