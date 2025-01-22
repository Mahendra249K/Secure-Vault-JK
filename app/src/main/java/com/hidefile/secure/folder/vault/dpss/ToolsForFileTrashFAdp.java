package com.hidefile.secure.folder.vault.dpss;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.hidefile.secure.folder.vault.AdActivity.Open_App_Manager;
import com.hidefile.secure.folder.vault.R;
import com.hidefile.secure.folder.vault.cluecanva.ConfigureSetWise;
import com.hidefile.secure.folder.vault.cluecanva.RDbhp;
import com.hidefile.secure.folder.vault.cluecanva.TooRfl;
import com.hidefile.secure.folder.vault.cluecanva.TreeRoot;
import com.hidefile.secure.folder.vault.cluecanva.VTv;

import java.io.File;
import java.util.ArrayList;

public class ToolsForFileTrashFAdp extends Fragment implements OnImageItemClickListner {
    View view;
    Context context;
    RDbhp RDbhp;
    LinearLayout ll_empty;
    RecyclerView rvPhotolist;
    private int countSelected;
    private ActionBar actionBar;
    ListFileAdp imageListAdapter;
    private ActionMode actionMode;
    boolean isSelectedMode = false;
    private final String idInter = "";


    RelativeLayout relativeNativeLayout;


    ArrayList<TreeRoot> fileList = new ArrayList<>();
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
                DeleteFiles(getActivity());
                return true;
            } else if (i == R.id.action_recover) {
                FromTrashRestoreFile(getActivity());
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TooRfl.deleteTempFolder();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.photo_trf, container, false);
        context = getActivity();
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

//        Common_Adm.getInstance().AmNativeLoad(getActivity(),view.findViewById(R.id.llNativeSmall),false);


        if (actionBar != null) {
            VTv tv_tital = getActivity().findViewById(R.id.tv_tital);
            tv_tital.setSpannableText(getString(R.string.tital_file_trash), getString(R.string.text_vault));
            ImageView iv_back = getActivity().findViewById(R.id.iv_back);
            iv_back.setOnClickListener(v -> getActivity().onBackPressed());
            getActivity().findViewById(R.id.iv_option).setVisibility(View.GONE);
        }
        RDbhp = com.hidefile.secure.folder.vault.cluecanva.RDbhp.getInstance(context);

        Init();
        return view;
    }


    public void Init() {
        relativeNativeLayout = view.findViewById(R.id.relativeSmall);
        ll_empty = view.findViewById(R.id.emptyLin);
        rvPhotolist = view.findViewById(R.id.rvPhotoList);
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(context);
        rvPhotolist.setLayoutManager(gridLayoutManager);
        ConfigureSetWise itemDecoration = new ConfigureSetWise(context, R.dimen.item_space);
        rvPhotolist.addItemDecoration(itemDecoration);
        fileList = RDbhp.getTrashFiels();
        imageListAdapter = new ListFileAdp(getActivity(), fileList);
        if (fileList.size() > 0) {
            rvPhotolist.setVisibility(View.VISIBLE);
            ll_empty.setVisibility(View.GONE);
            relativeNativeLayout.setVisibility(View.VISIBLE);
        } else {
            rvPhotolist.setVisibility(View.GONE);
            ll_empty.setVisibility(View.VISIBLE);
            relativeNativeLayout.setVisibility(View.GONE);

        }
        layoutOptions = view.findViewById(R.id.optionsLayout);
        llDelete = view.findViewById(R.id.linDelete);
        llDelete.setVisibility(View.VISIBLE);
        llRestore = view.findViewById(R.id.llRestore);
        llRestore.setVisibility(View.VISIBLE);
        llDelete.setOnClickListener(v -> {
            DeleteFiles(getActivity());
        });
        llRestore.setOnClickListener(v -> {
            FromTrashRestoreFile(getActivity());
        });

        imageListAdapter.setItemClickEvent(this);
        rvPhotolist.setAdapter(imageListAdapter);
        setHasOptionsMenu(true);
    }

    @Override
    public void onImageItemClick(int position) {
        if (isSelectedMode) {
            if (actionMode == null) {
                layoutOptions.setVisibility(View.VISIBLE);
            }
            toggleSelection(position);
            if (countSelected == 0) {
                layoutOptions.setVisibility(View.GONE);
                isSelectedMode = false;
                imageListAdapter.setSelectionMode(isSelectedMode);
            }
        } else {
            Open_App_Manager.doNotDisplayAds = true;
            Intent intent = TooRfl.shareImageReturnIntent(context, fileList.get(position).getNewPath(), fileList.get(position).getDisplayName());
            someActivityResultLauncher.launch(intent);
        }
    }


    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Open_App_Manager.doNotDisplayAds = false;
            }
        }, 3000);
    });

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
    }

    private void toggleSelection(int position) {
        fileList.get(position).checked = !fileList.get(position).isChecked();
        if (fileList.get(position).checked) {
            countSelected++;
        } else {
            countSelected--;
        }
        imageListAdapter.notifyDataSetChanged();
    }

    private void deselectAll() {
        for (int i = 0, l = fileList.size(); i < l; i++) {
            fileList.get(i).checked = false;
        }
        isSelectedMode = false;
        imageListAdapter.setSelectionMode(isSelectedMode);
        countSelected = 0;
        imageListAdapter.notifyDataSetChanged();
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

    @SuppressLint("SetTextI18n")
    public void DeleteFiles(final Activity act) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(act, R.style.BottomSheetDialogTheme);
        View dialogView = View.inflate(act, R.layout.dig_delete, null);
        LottieAnimationView animationView = dialogView.findViewById(R.id.animation_view);
        AppCompatButton button_negative = dialogView.findViewById(R.id.btn_negative);
        AppCompatButton button_positive = dialogView.findViewById(R.id.btn_positive);
        TextView title = dialogView.findViewById(R.id.tv_sort_title);
        TextView textView_message = dialogView.findViewById(R.id.textView_message);
        title.setText("Delete Forever?");
        title.setVisibility(View.VISIBLE);
        textView_message.setText("Do you Really sure to delete selected files permanently?\n\n WARNING :-  It will Not Recovered after Delete\n");
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
        bottomSheetDialog.show();
    }

    @SuppressLint("SetTextI18n")
    public void FromTrashRestoreFile(final Activity act) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(act, R.style.BottomSheetDialogTheme);
        View dialogView = View.inflate(act, R.layout.dig_delete, null);
        LottieAnimationView animationView = dialogView.findViewById(R.id.animation_view);
        AppCompatButton button_negative = dialogView.findViewById(R.id.btn_negative);
        AppCompatButton button_positive = dialogView.findViewById(R.id.btn_positive);
        TextView title = dialogView.findViewById(R.id.tv_sort_title);
        TextView textView_message = dialogView.findViewById(R.id.textView_message);
        title.setText("Restore File?");
        title.setVisibility(View.VISIBLE);
        ArrayList<TreeRoot> selected = getSelected();
        textView_message.setText("Do you Really Want Restore selected " + selected.size() + " Files From Trash To App?");
        button_negative.setText("Cancel");
        button_positive.setText("Restore");
        animationView.setAnimation(R.raw.animation_againrebuilt);
        button_negative.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
        });
        button_positive.setOnClickListener(v -> {

            new RecoverFilesTask().execute();
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
            pd.setTitle("Delete File");
            pd.setMessage("Please wait....");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            ArrayList<TreeRoot> selected = getSelected();
            for (int i = 0; i < selected.size(); i++) {
                if (!selected.get(i).getCouldId().equalsIgnoreCase("null1")) {
                    RDbhp.deleteFileTrash(selected.get(i).getId());
                } else {
                    File file = new File(selected.get(i).getNewPath());
                    if (file.exists()) file.delete();
                    RDbhp.deleteFileItem(selected.get(i).getId());
                }
                fileList.remove(selected.get(i));
            }
            return "null";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            imageListAdapter.notifyDataSetChanged();
            Toast.makeText(context, "File Deleted Successfully", Toast.LENGTH_SHORT).show();
            if (actionMode != null)
//                actionMode.finish();
                countSelected = 0;
            Init();
        }
    }

    public class RecoverFilesTask extends AsyncTask<String, Integer, String> {
        ProgressDialog pd;

        public RecoverFilesTask() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(getActivity());
            pd.setTitle("Recover File");
            pd.setMessage("Please wait....");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            ArrayList<TreeRoot> selected = getSelected();
            for (int i = 0; i < selected.size(); i++) {
                RDbhp.reCoverFile(selected.get(i).getId());
                fileList.remove(selected.get(i));
            }
            return "null";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            imageListAdapter.notifyDataSetChanged();
            Toast.makeText(context, "File Recovered To Album Successfully", Toast.LENGTH_SHORT).show();
            if (actionMode != null)
//                actionMode.finish();
                countSelected = 0;
            Init();
        }
    }
}
