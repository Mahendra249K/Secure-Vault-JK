package com.hidefile.secure.folder.vault.dpss;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.hidefile.secure.folder.vault.R;
import com.hidefile.secure.folder.vault.dashex.MyWebView;
import com.hidefile.secure.folder.vault.dashex.MyBrowser;
import com.hidefile.secure.folder.vault.cluecanva.RDbhp;
import com.hidefile.secure.folder.vault.cluecanva.BookmarkEm;
import com.hidefile.secure.folder.vault.cluecanva.TillsPth;

import java.util.ArrayList;
import java.util.List;

public class MyBookMarkFtr extends Fragment implements OnImageItemClickListner, ListOfBookMarlAdp.OnItemLongClickListner {

    View view;
    Context mContext;
    RDbhp RDbhp;
    LinearLayout ll_Empty;
    RecyclerView bookmark_list;
    ListOfBookMarlAdp testAdapter;
    BottomSheetDialog bottomSheetDialog;
    List<BookmarkEm> list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.my_bookmark_fr, container, false);
        mContext = getActivity();
        Init();
        return view;
    }
    private void Init() {
        bookmark_list = view.findViewById(R.id.bookmark_list);
        ll_Empty = view.findViewById(R.id.emptyLin);
        RDbhp = RDbhp.getInstance(mContext);
        list = RDbhp.listBookmark();
        if (list.size() > 0) {
            ll_Empty.setVisibility(View.GONE);
            bookmark_list.setVisibility(View.VISIBLE);
        } else {
            ll_Empty.setVisibility(View.VISIBLE);
            bookmark_list.setVisibility(View.GONE);
        }
        bookmark_list.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        testAdapter = new ListOfBookMarlAdp(getActivity(), list, this);
        testAdapter.setOnItemLongClickListner(this);
        bookmark_list.setAdapter(testAdapter);
    }
    @Override
    public void onResume() {
        super.onResume();
        Init();
    }
    @Override
    public boolean getUserVisibleHint() {
        MyBrowser myBrowser = (MyBrowser) getActivity();
        myBrowser.mainLlSearchView.setVisibility(View.GONE);

        return super.getUserVisibleHint();
    }
    @Override
    public void onImageItemClick(int position) {

        goSearch2(testAdapter.getAllItems().get(position).getContent());
    }
    public void goSearch2(String query) {
        if (TillsPth.isNetworkAvailable(mContext)) {
            Intent intent = new Intent(mContext, MyWebView.class);
            intent.putExtra("Query", query);
            startActivity(intent);
        }
    }
    @Override
    public void onImageItemLongClick(int position) {
        list.get(position).getId();
        showBookmarkMenu(position, list.get(position).getId());
    }
    @SuppressLint("SetTextI18n")
    private void showBookmarkMenu(int position, final String bookmark_id) {
        bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialogTheme);
        View dialogView = View.inflate(getActivity(), R.layout.dig_act, null);
        TextView textView = dialogView.findViewById(R.id.dialog_text);
        textView.setText("Do You Want to Delete This Bookmark ?");
        Button action_ok = dialogView.findViewById(R.id.action_ok);
        action_ok.setOnClickListener(view -> {
            RDbhp = RDbhp.getInstance(mContext);
            RDbhp.delete(Integer.parseInt(bookmark_id));
            testAdapter.removeAt(position);
            bottomSheetDialog.dismiss();
            if (testAdapter.getAllItems().size() > 0) {
                ll_Empty.setVisibility(View.GONE);
                bookmark_list.setVisibility(View.VISIBLE);
            } else {
                ll_Empty.setVisibility(View.VISIBLE);
                bookmark_list.setVisibility(View.GONE);
            }
            Toast.makeText(mContext, getString(R.string.lable_delete_successful), Toast.LENGTH_SHORT).show();
        });
        Button action_cancel = dialogView.findViewById(R.id.action_cancel);
        action_cancel.setOnClickListener(view -> bottomSheetDialog.dismiss());
        bottomSheetDialog.setContentView(dialogView);
        bottomSheetDialog.show();
    }
    @Override
    public void onItemLongClick(int position) {
        list.get(position).getId();
        showBookmarkMenu(position, list.get(position).getId());
    }
}
