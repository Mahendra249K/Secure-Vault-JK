package com.hidefile.secure.folder.vault.dashex

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.hidefile.secure.folder.vault.AdActivity.SharedPref
import com.hidefile.secure.folder.vault.R
import com.hidefile.secure.folder.vault.cluecanva.HandPrmt
import com.hidefile.secure.folder.vault.cluecanva.HandPrmt.OnListener
import com.hidefile.secure.folder.vault.cluecanva.SupPref
import com.hidefile.secure.folder.vault.cluecanva.TillsPth
import com.hidefile.secure.folder.vault.dpss.ToolsForFileFAdp
import com.hidefile.secure.folder.vault.dpss.ToolsForImageFAdp
import com.hidefile.secure.folder.vault.dpss.ToolsForVideoFAdp
import java.io.File


class Prasth : FoundationActivity() {
    var mainFragment = ""
    var onResultLauncher: ActivityResultLauncher<Intent>? = null
    var toolbar: Toolbar? = null
    var Type: String? = "photos"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        requestWindowFeature(Window.FEATURE_NO_TITLE)
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            WindowManager.LayoutParams.FLAG_FULLSCREEN
//        )
        setContentView(R.layout.prasth)
        SharedPref.AppOpenShow = true

        Type = intent.getStringExtra("Type")
        toolbar = findViewById(R.id.main_toolbar)
        onResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult? -> requestPermissions() }
        requestPermissions()


        setToolbarData(true)



    }


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    override fun onStart() {
        super.onStart()
        Constant.isShowOpenAd = true
        SharedPref.AppOpenShow = true

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    override fun onResume() {
        super.onResume()
        SharedPref.AppOpenShow = true
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    override fun onPause() {
        super.onPause()
        SharedPref.AppOpenShow = true
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    override fun onStop() {
        super.onStop()
        SharedPref.AppOpenShow = true
        Log.d("cycle", "stop")
    }



    override fun onUserLeaveHint() {
        super.onUserLeaveHint() }
    override fun onUserInteraction() {
        super.onUserInteraction() }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_HOME) { }
        return super.onKeyDown(keyCode, event) }
    fun Init() {
        createImageDir()
        var androidFragment: Fragment =
            ToolsForVideoFAdp()
        if (Type.equals("Photos", ignoreCase = true)) {
            androidFragment =
                ToolsForImageFAdp()
            mainFragment = "ToolsForImageFAdp"
        } else if (Type.equals("Videos", ignoreCase = true)) {
            androidFragment =
                ToolsForVideoFAdp()
            mainFragment = "ToolsForVideoFAdp"
        } else if (Type.equals("Files", ignoreCase = true)) {
            androidFragment =
                ToolsForFileFAdp()
            mainFragment = "ToolsForFileFAdp" }
        replaceFragment(androidFragment) }
    private fun guideDialogForLEXA(context: Activity) {
        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setTitle(R.string.give_sd_card_permission)
        alertDialog.setMessage(R.string.give_seconday_permission_message)
        alertDialog.setPositiveButton(R.string.give_seconday_permission_btn) { dialogInterface, i ->
            triggerSDCardAccessPermission(context) }
        alertDialog.setNegativeButton(
            "Cancel") { dialogInterface: DialogInterface?, i: Int -> }
        alertDialog.show() }
    private fun triggerSDCardAccessPermission(context: Activity) {
        try {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivityForResult(intent, 555)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 555) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                val treeUri = data.data
                applicationContext.contentResolver.takePersistableUriPermission(
                    treeUri!!,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
                SupPref.setBooleanValue(this@Prasth, "sdcardapproved", true)
                SupPref.putValue(this@Prasth, "sdcarduri", treeUri.toString() + "")
            }
        }
    }
    fun replaceFragment(destFragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.trash_frame_layout, destFragment)
        fragmentTransaction.addToBackStack(destFragment.javaClass.name)
        fragmentTransaction.commitAllowingStateLoss()
    }
    fun createImageDir() {
        val myDirectory = File(TillsPth.hideImage)
        if (!myDirectory.exists()) {
            myDirectory.mkdirs()
        } else {
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> {
                false
            }
        }
    }
    fun setToolbarData(isBack: Boolean) {
        toolbar = findViewById(R.id.main_toolbar)
        setSupportActionBar(toolbar)
    }
    override fun onBackPressed() {

        setResult(Activity.RESULT_OK)
        when (mainFragment) {
            "ToolsForImageFAdp" -> {
                setResult(Activity.RESULT_OK)
                finish()
                return
            }
            "ToolsForVideoFAdp" -> {
                setResult(Activity.RESULT_OK)
                finish()
                return
            }
            "ToolsForFileFAdp" -> {
                setResult(Activity.RESULT_OK)
                finish()
                return }
            else -> {
                val intent = Intent(this@Prasth, BordMain::class.java)
                startActivity(intent)
            }
        }

    }
    fun requestPermissions() {
        HandPrmt.getInstance().requestPermissions(this, object : OnListener {
            override fun onPermissionGranted() {
                Init() }
            override fun onPermissionDenied() {}
            override fun onOpenSettings() {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                onResultLauncher!!.launch(intent) }
        }) }
    private fun endPermission() {
        HandPrmt.getInstance().hideDialog()
        if (onResultLauncher != null) {
            onResultLauncher!!.unregister() } }
    override fun onDestroy() {
        super.onDestroy()
        endPermission() }
    companion object {
        private val TAG = Prasth::class.java.simpleName }
}