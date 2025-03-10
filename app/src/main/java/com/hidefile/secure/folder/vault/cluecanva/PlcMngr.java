package com.hidefile.secure.folder.vault.cluecanva;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;

public class PlcMngr {

    public static final int DPM_ACTIVATION_REQUEST_CODE = 100;
    private final Context mContext;
    private final DevicePolicyManager mDPM;
    private final ComponentName adminComponent;
    public PlcMngr(Context context) {
        this.mContext = context;
        mDPM = (DevicePolicyManager) mContext.getSystemService(Context.DEVICE_POLICY_SERVICE);
        adminComponent = new ComponentName(mContext.getPackageName(), "com.securefolder.lockfolder.privatefolder.ui.utils.AdmRec");
    }
    public boolean isAdminActive() {
        return mDPM.isAdminActive(adminComponent);
    }
    public ComponentName getAdminComponent() {
        return adminComponent;
    }
    public void disableAdmin() {
        mDPM.removeActiveAdmin(adminComponent);
    }
}