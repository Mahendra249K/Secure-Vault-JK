package com.hidefile.secure.folder.vault.AdActivity;

import static com.hidefile.secure.folder.vault.cluecanva.SupPref.ExitNativeAd;
import static com.hidefile.secure.folder.vault.cluecanva.SupPref.appRated;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.hidefile.secure.folder.vault.R;
import com.hidefile.secure.folder.vault.cluecanva.SupPref;
import com.hidefile.secure.folder.vault.databinding.AdExitdialogNavigationBinding;


import org.jetbrains.annotations.NotNull;

public class ExitDialogNative extends BottomSheetDialogFragment {
    View view;
    private boolean isExit = false;

    AdExitdialogNavigationBinding binding;
    public ExitDialogNative() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = AdExitdialogNavigationBinding.inflate(inflater,container,false);

//        Common_Adm.getInstance().AmNativeLoad(getActivity(),binding.llNativeLarge,true);

        initView();
        getDialog().setOnShowListener(dialog -> {
            new Handler(Looper.myLooper()).postDelayed(() -> {
                BottomSheetDialog d = (BottomSheetDialog) dialog;
            }, 0);
        });
        return binding.getRoot();
    }
   private void initView() {
       getActivity().finish();
//       System.exit(0);
//        binding.textExit.setOnClickListener(view -> {
//            dismiss();
//            isExit = true;
//            getActivity().finishAffinity();
//        });
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = dialog.getWindow();
            if (window != null) {
                DisplayMetrics metrics = new DisplayMetrics();
                window.getWindowManager().getDefaultDisplay().getMetrics(metrics);
                GradientDrawable dimDrawable = new GradientDrawable();
                GradientDrawable navigationBarDrawable = new GradientDrawable();
                navigationBarDrawable.setShape(GradientDrawable.RECTANGLE);
                navigationBarDrawable.setColor(getResources().getColor(R.color.white));
                Drawable[] layers = {dimDrawable, navigationBarDrawable};
                LayerDrawable windowBackground = new LayerDrawable(layers);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    windowBackground.setLayerInsetTop(1, metrics.heightPixels);
                }
                window.setBackgroundDrawable(windowBackground);
            }
        }
        return dialog;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }
    @Override
    public void onDismiss(@NonNull @NotNull DialogInterface dialog) {
        if(SupPref.getBooleanValue(getActivity(),ExitNativeAd,true)){
            SupPref.setBooleanValue(getActivity(),ExitNativeAd,false);
            SupPref.setBooleanValue(getActivity(),appRated,true);
        }
        super.onDismiss(dialog);
    }
}
