package com.hidefile.secure.folder.vault.dpss;

import static com.hidefile.secure.folder.vault.edptrs.InSafe.isGridlayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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



import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.api.services.drive.Drive;

import com.hidefile.secure.folder.vault.AdActivity.Common_Adm;
//import com.hidefile.secure.folder.vault.BuildConfig;
import com.hidefile.secure.folder.vault.cluecanva.ConfigureSetWise;
import com.Joaquin.thiago.ListItVid;
import com.hidefile.secure.folder.vault.R;
import com.hidefile.secure.folder.vault.dashex.VidPlay;
import com.hidefile.secure.folder.vault.cluecanva.RDbhp;
import com.hidefile.secure.folder.vault.cluecanva.TillsPth;
import com.hidefile.secure.folder.vault.cluecanva.VTv;
import java.io.File;
import java.util.ArrayList;

public class ToolsForVideoTrashFAdp extends Fragment implements OnImageItemClickListner {
    View view;
    Context context;
    RDbhp RDbhp;
    public Drive service;
    LinearLayout ll_empty;
    boolean isbackup = false;
    private int countSelected;
    RecyclerView rv_photolist;
    private ActionBar actionBar;
    private ActionMode actionMode;
    boolean isSelectedMode = false;
    VideoListAdp imageListAdapter;
    GridLayoutManager gridLayoutManager;




    RelativeLayout relativeNativeLayout;
    ArrayList<ListItVid> imagesList = new ArrayList<>();
    private LinearLayout layoutOptions, llDelete, llRestore;
    private final ActionMode.Callback callback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater menuInflater = mode.getMenuInflater();
            menuInflater.inflate(R.menu.emen_trash, menu);
            actionMode = mode;
            countSelected = 0;
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
            if (i == R.id.action_delete) {
                DeleteVideos(getActivity());
                return true;
            } else if (i == R.id.action_recover) {
                FromTrashRestoreVideo(getActivity());
                return true;
            }
            return false;
        }
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            if (countSelected > 0) {
                deselectAll();
            }
            layoutOptions.setVisibility(View.GONE);
            actionMode = null;
        }
    };
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.photo_trf, container, false);
        context = getActivity();


//        Common_Adm.getInstance().AmNativeLoad(getActivity(),view.findViewById(R.id.llNativeSmall),false);

        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            VTv tv_tital = getActivity().findViewById(R.id.tv_tital);
            tv_tital.setText("Trash Video");
            ImageView iv_back = getActivity().findViewById(R.id.iv_back);
            iv_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().onBackPressed();
                }
            });
            getActivity().findViewById(R.id.iv_option).setVisibility(View.GONE);
        }
        RDbhp = RDbhp.getInstance(context);

        Init();
        return view;
    }




    public void Init() {
        relativeNativeLayout = view.findViewById(R.id.relativeSmall);
        ll_empty = view.findViewById(R.id.emptyLin);
        rv_photolist = view.findViewById(R.id.rvPhotoList);
        int no = isGridlayout ? 3 : 1;
        gridLayoutManager = new GridLayoutManager(context, no);
        rv_photolist.setLayoutManager(gridLayoutManager);
        ConfigureSetWise itemDecoration = new ConfigureSetWise(context, R.dimen.item_space);
        rv_photolist.addItemDecoration(itemDecoration);
        imagesList = RDbhp.getTrashVideo();
        imageListAdapter = new VideoListAdp(getActivity(), imagesList);
        if (imagesList.size() > 0) {
            rv_photolist.setVisibility(View.VISIBLE);
            ll_empty.setVisibility(View.GONE);
            relativeNativeLayout.setVisibility(View.VISIBLE);
        } else {
            rv_photolist.setVisibility(View.GONE);
            ll_empty.setVisibility(View.VISIBLE);
            relativeNativeLayout.setVisibility(View.GONE);
        }
        layoutOptions = view.findViewById(R.id.optionsLayout);
        llDelete = view.findViewById(R.id.linDelete);
        llDelete.setVisibility(View.VISIBLE);
        llRestore = view.findViewById(R.id.llRestore);
        llRestore.setVisibility(View.VISIBLE);
        llDelete.setOnClickListener(v -> {
            DeleteVideos(getActivity());
        });
        llRestore.setOnClickListener(v -> {
            FromTrashRestoreVideo(getActivity());
        });
        imageListAdapter.setItemClickEvent(this);
        imageListAdapter.setImageResize(TillsPth.getImageResize(context, rv_photolist));
        rv_photolist.setAdapter(imageListAdapter);
    }


    @Override
    public void onImageItemClick(int position) {
        if (isSelectedMode) {
            if (actionMode == null) {
                layoutOptions.setVisibility(View.VISIBLE);
            }
            toggleSelection(position);
            if (countSelected == 0) {
                isSelectedMode = false;
                imageListAdapter.setSelectionMode(isSelectedMode);
                imageListAdapter.notifyDataSetChanged();
                layoutOptions.setVisibility(View.GONE);
            }
        } else {
            Intent intent = new Intent(context, VidPlay.class);
            intent.putExtra("filepath", imagesList.get(position).getNewPath());
            intent.putExtra("isFromTrash", true);
            startActivity(intent);
        }
    }

    @Override
    public void onImageItemLongClick(int position) {
        isSelectedMode = true;
        imageListAdapter.setSelectionMode(isSelectedMode);
        if (actionMode == null) {
          layoutOptions.setVisibility(View.VISIBLE);
        }
        toggleSelection(position);
        if (countSelected == 0) {
            isSelectedMode = false;
            imageListAdapter.setSelectionMode(isSelectedMode);
            layoutOptions.setVisibility(View.GONE);
        }
        imageListAdapter.notifyDataSetChanged();
    }

    private void toggleSelection(int position) {
        imagesList.get(position).checked = !imagesList.get(position).isChecked();
        if (imagesList.get(position).checked) {
            countSelected++;
        } else {
            countSelected--;
        }
        imageListAdapter.notifyDataSetChanged();
    }

    private void deselectAll() {
        for (int i = 0, l = imagesList.size(); i < l; i++) {
            imagesList.get(i).checked = false;
        }
        isSelectedMode = false;
        imageListAdapter.setSelectionMode(isSelectedMode);
        countSelected = 0;
        imageListAdapter.notifyDataSetChanged();
    }

    private ArrayList<ListItVid> getSelected() {
        ArrayList<ListItVid> selectedImages = new ArrayList<>();
        for (int i = 0, l = imagesList.size(); i < l; i++) {
            if (imagesList.get(i).isChecked()) {
                selectedImages.add(imagesList.get(i));
            }
        }
        return selectedImages;
    }


    @SuppressLint("SetTextI18n")
    public void DeleteVideos(final Activity act) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(act, R.style.BottomSheetDialogTheme);
        View dialogView = View.inflate(act, R.layout.dig_delete, null);
        LottieAnimationView animationView = dialogView.findViewById(R.id.animation_view);
        AppCompatButton button_negative = dialogView.findViewById(R.id.btn_negative);
        AppCompatButton button_positive = dialogView.findViewById(R.id.btn_positive);
        TextView title = dialogView.findViewById(R.id.tv_sort_title);
        TextView textView_message = dialogView.findViewById(R.id.textView_message);
        title.setText("Delete Forever?");
        title.setVisibility(View.VISIBLE);
        textView_message.setText("Do you Really sure to delete selected video permanently?\n\n WARNING :-  It will Not Recovered after Delete\n");
        button_negative.setText("Cancel");
        button_positive.setText("Delete");
        animationView.setAnimation(R.raw.animation_delete);
        button_negative.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
        });


        button_positive.setOnClickListener(v -> {
            new DeleteVideoTask().execute();
            bottomSheetDialog.dismiss();
            layoutOptions.setVisibility(View.GONE);
        });

        bottomSheetDialog.setContentView(dialogView);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.show();

    }

    public void FromTrashRestoreVideo(final Activity act) {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(act, R.style.BottomSheetDialogTheme);
        View dialogView = View.inflate(act, R.layout.dig_delete, null);
        LottieAnimationView animationView = dialogView.findViewById(R.id.animation_view);
        AppCompatButton button_negative = dialogView.findViewById(R.id.btn_negative);
        AppCompatButton button_positive = dialogView.findViewById(R.id.btn_positive);
        TextView title = dialogView.findViewById(R.id.tv_sort_title);
        TextView textView_message = dialogView.findViewById(R.id.textView_message);
        title.setText("Restore Video ?");
        title.setVisibility(View.VISIBLE);
        textView_message.setText("Do you Really Want Restore selected " + countSelected + " Videos From Trash To App?");
        button_negative.setText("Cancel");
        button_positive.setText("Restore");
        animationView.setAnimation(R.raw.animation_againrebuilt);
        button_negative.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
        });
        button_positive.setOnClickListener(v -> {
            new RecoverImageTask().execute();
            bottomSheetDialog.dismiss();
            layoutOptions.setVisibility(View.GONE);

        });
        bottomSheetDialog.setContentView(dialogView);
        bottomSheetDialog.show();
    }

    public class DeleteVideoTask extends AsyncTask<String, Integer, String> {
        ProgressDialog pd;

        public DeleteVideoTask() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(getActivity());
            pd.setTitle("Delete to Album");
            pd.setMessage("Please wait....");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            ArrayList<ListItVid> selected = getSelected();
            for (int i = 0; i < selected.size(); i++) {
                if (!selected.get(i).getCouldId().equalsIgnoreCase("null1")) {
                    RDbhp.deleteVideoTrash(selected.get(i).getId());
                } else {
                    File file = new File(selected.get(i).getNewPath());
                    if (file.exists())
                        file.delete();
                    RDbhp.deleteVideoItem(selected.get(i).getId());
                }
                imagesList.remove(selected.get(i));
            }
            return "null";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            imageListAdapter.notifyDataSetChanged();
            Toast.makeText(context, "Delete to Album successfully", Toast.LENGTH_SHORT).show();
            if (actionMode != null)
//                actionMode.finish();
            countSelected = 0;

            Init();
        }
    }


    public class RecoverImageTask extends AsyncTask<String, Integer, String> {
        ProgressDialog pd;

        public RecoverImageTask() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(getActivity());
            pd.setTitle("Recover to Album");
            pd.setMessage("Please wait....");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            ArrayList<ListItVid> selected = getSelected();
            for (int i = 0; i < selected.size(); i++) {
                RDbhp.reCoverVideo(selected.get(i).getId());
                imagesList.remove(selected.get(i));
            }
            return "null";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            imageListAdapter.notifyDataSetChanged();
            Toast.makeText(context, "Video Recover successfully", Toast.LENGTH_SHORT).show();
            if (actionMode != null)
//                actionMode.finish();
            countSelected = 0;
            Init();
        }
    }
}
