package com.hidefile.secure.folder.vault.calldorado;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.calldorado.ui.aftercall.CallerIdActivity;
import com.calldorado.ui.views.custom.CalldoradoCustomView;
import com.hidefile.secure.folder.vault.AdActivity.MyApplication;
import com.hidefile.secure.folder.vault.R;

import static android.view.View.inflate;


public class AftercallCustomView extends CalldoradoCustomView {
    private final Context context;
    private View view;


    public AftercallCustomView(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public View getRootView() {
        view = inflate(context, R.layout.aftercall_native_layout, null);

        view.findViewById(R.id.linShare).setOnClickListener(view -> {
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_setting_name));
                String linkMokalvi = "\nHey,Check out this " + context.getString(R.string.app_setting_name) + "\n\n";
                linkMokalvi = linkMokalvi + "https://play.google.com/store/apps/details?id=" + MyApplication.Companion.getAPPLICATION_ID() + "\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, linkMokalvi);
                ((CallerIdActivity)getCalldoradoContext()).startActivity(Intent.createChooser(shareIntent, "choose one"));

            } catch (Exception e) {
                e.printStackTrace();
            }
        });


//        view.findViewById(R.id.MobileFalashlightOnOffLayout).setOnClickListener(view -> {
//            try {
//                Intent i = new Intent(getCalldoradoContext(), IncomingKall_PhleshLightActivity.class);
//                getCalldoradoContext().startActivity(i);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });

        return view;
    }


}