package com.hidefile.secure.folder.vault.calldorado;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.calldorado.Calldorado;

public class SetupFragmentReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {
            Calldorado.setAftercallCustomView(context,new AftercallCustomView(context));
        }
    }
}