package com.hidefile.secure.folder.vault.cluecanva;

import android.Manifest;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.hidefile.secure.folder.vault.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HandPrmt {
    private static final String TAG = HandPrmt.class.getSimpleName();
    private static HandPrmt sInstance;
    private AlertDialog dialog;

    public static HandPrmt getInstance() {
        if (sInstance == null) {
            synchronized (HandPrmt.class) {
                sInstance = new HandPrmt();
            }
        }
        return sInstance;
    }
    public void requestPermissions(final Context context, final OnListener listener) {
        Log.i(TAG, "requestPermissions: ");
        Collection<String> permissionList = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionList.add(Manifest.permission.READ_MEDIA_AUDIO);
            permissionList.add(Manifest.permission.READ_MEDIA_VIDEO);
            permissionList.add(Manifest.permission.READ_MEDIA_IMAGES);
        } else {

            if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
                permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            } else {
                permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }

        }
        permissionList.add(Manifest.permission.CAMERA);
        hideDialog();
        Dexter.withContext(context)
                .withPermissions(permissionList)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            if (listener != null) {
                                listener.onPermissionGranted();
                            }
                        } else {
                            if (listener != null) {
                                listener.onPermissionDenied();
                            }
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            hideDialog();

                            View view = LayoutInflater.from(context).inflate(R.layout.dig_permit, null);
                            TextView txtCancel = view.findViewById(R.id.txtCancel);
                            TextView txtSubmit = view.findViewById(R.id.txtSubmit);
                            txtCancel.setOnClickListener(v -> hideDialog());

                            txtSubmit.setOnClickListener(v -> {
                                hideDialog();
                                if (listener != null) {
                                    listener.onOpenSettings();
                                }
                            });

                            dialog = new AlertDialog.Builder(context)
                                    .setView(view)
                                    .setCancelable(false)
                                    .show();
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).withErrorListener(dexterError -> Log.i(TAG, "onError: ")).check();
    }

    public void requestCameraPermission(final Context context, final OnListener listener) {
        Log.i(TAG, "requestCameraPermission: ");

        Collection<String> permissionList = new ArrayList<>();
        permissionList.add(Manifest.permission.CAMERA);

        hideDialog();
        Dexter.withContext(context)
                .withPermissions(permissionList)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        Log.i(TAG, "onPermissionsChecked: ");
                        if (report.areAllPermissionsGranted()) {
                            if (listener != null) {
                                listener.onPermissionGranted();
                            }
                        } else {
                            if (listener != null) {
                                listener.onPermissionDenied();
                            }
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            hideDialog();

                            View view = LayoutInflater.from(context).inflate(R.layout.dig_permit, null);
                            TextView txtCancel = view.findViewById(R.id.txtCancel);
                            TextView txtSubmit = view.findViewById(R.id.txtSubmit);

                            txtCancel.setOnClickListener(v -> hideDialog());

                            txtSubmit.setOnClickListener(v -> {
                                hideDialog();
                                if (listener != null) {
                                    listener.onOpenSettings();
                                }
                            });

                            dialog = new AlertDialog.Builder(context)
                                    .setView(view)
                                    .setCancelable(false)
                                    .show();
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).withErrorListener(dexterError -> Log.i(TAG, "onError: ")).check();
    }

    public void requestGetAccounts(final Context context, final OnListener listener) {
        Log.i(TAG, "requestPermissions: ");

        Collection<String> permissionList = new ArrayList<>();
        permissionList.add(Manifest.permission.GET_ACCOUNTS);

        hideDialog();

        Dexter.withContext(context)
                .withPermissions(permissionList)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        Log.i(TAG, "onPermissionsChecked: ");
                        if (report.areAllPermissionsGranted()) {
                            if (listener != null) {
                                listener.onPermissionGranted();
                            }
                        } else {
                            if (listener != null) {
                                listener.onPermissionDenied();
                            }
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            hideDialog();

                            View view = LayoutInflater.from(context).inflate(R.layout.dig_permit, null);
                            TextView txtCancel = view.findViewById(R.id.txtCancel);
                            TextView txtSubmit = view.findViewById(R.id.txtSubmit);
                            TextView txtMsg = view.findViewById(R.id.txtMsg);

                            txtMsg.setText(R.string.permission_account_msg);

                            txtCancel.setOnClickListener(v -> hideDialog());
                            txtSubmit.setOnClickListener(v -> {
                                hideDialog();
                                if (listener != null) {
                                    listener.onOpenSettings();
                                }
                            });

                            dialog = new AlertDialog.Builder(context)
                                    .setView(view)
                                    .setCancelable(false)
                                    .show();
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).withErrorListener(dexterError -> Log.i(TAG, "onError: ")).check();
    }

    public void hideDialog() {
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            dialog = null;
        }
    }

    public interface OnListener {
        void onPermissionGranted();

        void onPermissionDenied();

        void onOpenSettings();
    }
}
