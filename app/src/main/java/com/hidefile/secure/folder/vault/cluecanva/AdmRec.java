package com.hidefile.secure.folder.vault.cluecanva;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;

import com.hidefile.secure.folder.vault.R;

public class AdmRec extends DeviceAdminReceiver {

    @Override
    public void onDisabled(Context context, Intent intent) {
        // TODO Auto-generated method stub
        super.onDisabled(context, intent);
        EntryAux.showToast(context, R.string.device_ad_disabled_msg);
    }

    @Override
    public void onEnabled(Context context, Intent intent) {
        // TODO Auto-generated method stub
        super.onEnabled(context, intent);
        EntryAux.showToast(context, R.string.device_ad_enabled_msg);
    }

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        // TODO Auto-generated method stub
        EntryAux.showToast(context, "disable request");
        return super.onDisableRequested(context, intent);
    }
}