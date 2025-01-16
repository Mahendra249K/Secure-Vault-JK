package com.hidefile.secure.folder.vault.dpss;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hidefile.secure.folder.vault.R;
import com.hidefile.secure.folder.vault.dashex.NewAppAddition;
import com.hidefile.secure.folder.vault.dashex.MyBrowser;
import com.hidefile.secure.folder.vault.dashex.MyWebView;
import com.hidefile.secure.folder.vault.cluecanva.RecentList;
import com.hidefile.secure.folder.vault.cluecanva.SupPref;
import com.hidefile.secure.folder.vault.cluecanva.TillsPth;
import java.util.ArrayList;

public class RecentViewFtr extends Fragment implements View.OnClickListener, View.OnLongClickListener {
    View view;
    Context context;
    private RecyclerView siteGird;
    LinearLayout ll_top, ll_bottom;
    private boolean isClickEvent = true;
    public RecentAppListAdp recentAppListAd;
    public ArrayList<RecentList> appList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.recent_view_fr, container, false);
        context = getActivity();

        Init();
        return view;
    }

    private void Init() {
        ll_top = view.findViewById(R.id.lin_tool_top);
        ll_bottom = view.findViewById(R.id.lin_tool_bottom);
        siteGird = view.findViewById(R.id.site_grid);
        siteGird.setLayoutManager(new GridLayoutManager(context, 3));
        appList = new ArrayList<>();
        appList = SupPref.getAppData(context);
        appList.add(new RecentList("ic_plus", "", "Add"));
        recentAppListAd = new RecentAppListAdp(getActivity(), appList, this, this);
        siteGird.setAdapter(recentAppListAd);
        ll_bottom.setOnClickListener(this);

    }

    @Override
    public boolean getUserVisibleHint() {
        MyBrowser myBrowser = (MyBrowser) getActivity();
        myBrowser.mainLlSearchView.setVisibility(View.VISIBLE);

        return super.getUserVisibleHint();
    }

    @Override
    public void onClick(View v) {
        int pos;
        switch (v.getId()) {
            case R.id.ivIconApp:
            case R.id.ll_app_main:
                if (isClickEvent) {
                    pos = (int) v.getTag();
                    if (pos == appList.size() - 1) {
                        startActivityForResult(new Intent(context, NewAppAddition.class), 223);
                    } else {
                        if (!appList.get(pos).getUrl().equals("")) {
                            goSearch2(appList.get(pos).getUrl());
                        }
                    }
                }
                break;
            case R.id.lin_tool_bottom:
                ll_top.setVisibility(View.GONE);
                ll_bottom.setVisibility(View.GONE);
                MyBrowser myBrowser = (MyBrowser) getActivity();

                myBrowser.mainLlToolbar.setVisibility(View.VISIBLE);
                myBrowser.mainLlSearchView.setVisibility(View.VISIBLE);
                myBrowser.tabLayout.setVisibility(View.VISIBLE);


                isClickEvent = true;
                RecentAppListAdp.isLongPress = false;
                recentAppListAd.addLast();
                break;
        }
    }


    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.ll_app_main:
                isClickEvent = false;
                ll_top.setVisibility(View.VISIBLE);
                ll_bottom.setVisibility(View.VISIBLE);
                MyBrowser myBrowser = (MyBrowser) getActivity();
                myBrowser.mainLlSearchView.setVisibility(View.GONE);
                myBrowser.mainLlToolbar.setVisibility(View.GONE);
                myBrowser.tabLayout.setVisibility(View.GONE);
                RecentAppListAdp.isLongPress = true;
                recentAppListAd.removeLast();
                break;
        }
        return false;
    }

    public void goSearch2(String query) {
        if (TillsPth.isNetworkAvailable(context)) {
            Intent intent = new Intent(context, MyWebView.class);
            intent.putExtra("Query", query);
            startActivity(intent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 223) {
                appList = new ArrayList<>();
                appList = SupPref.getAppData(context);
                appList.add(new RecentList("ic_plus", "", "Add"));
                recentAppListAd = new RecentAppListAdp(getActivity(), appList, this, this);
                siteGird.setAdapter(recentAppListAd);
            }
        }
    }
}
