package com.hidefile.secure.folder.vault.dashex

import android.R
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import com.hidefile.secure.folder.vault.cluecanva.TilsCo

class ActPrsthDefault :FoundationActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)
        TilsCo.isStr = true
        val i = Intent(this@ActPrsthDefault, Str::class.java)
        startActivity(i)
        finish()
        overridePendingTransition(0, R.anim.fade_out)
    }
}