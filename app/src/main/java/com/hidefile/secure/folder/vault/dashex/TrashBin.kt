package com.hidefile.secure.folder.vault.dashex

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.hidefile.secure.folder.vault.AdActivity.SharedPref
import com.hidefile.secure.folder.vault.R
import com.hidefile.secure.folder.vault.cluecanva.SupPref
import com.hidefile.secure.folder.vault.dpss.ToolsForFileTrashFAdp
import com.hidefile.secure.folder.vault.dpss.ToolsForImageTrashFAdp
import com.hidefile.secure.folder.vault.dpss.ToolsForVideoTrashFAdp

class TrashBin : FoundationActivity() {
    var Type: String? = "trashphotos"
    var currentFragment = ""
    var fragmentManager: FragmentManager? = null


    var idNative = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        requestWindowFeature(Window.FEATURE_NO_TITLE)
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            WindowManager.LayoutParams.FLAG_FULLSCREEN
//        )
        setContentView(R.layout.bin_trash)
        SharedPref.AppOpenShow = false


        Type = intent.getStringExtra("Type")
        val toolbar = findViewById<Toolbar>(R.id.main_toolbar)
        setSupportActionBar(toolbar)
        val iv_back = findViewById<ImageView>(R.id.iv_back)
        iv_back.setOnClickListener { v: View? -> onBackPressed() }
        findViewById<View>(R.id.iv_option).visibility = View.GONE
        Init()



    }



    fun Init() {
        var androidFragment: Fragment = ToolsForImageTrashFAdp()
        if (Type.equals("trashphotos", ignoreCase = true)) {
            androidFragment = ToolsForImageTrashFAdp()
            currentFragment = "ImageTrashFrag"
        } else if (Type.equals("trashvideos", ignoreCase = true)) {
            androidFragment =
                ToolsForVideoTrashFAdp()
            currentFragment = "ToolsForVideoFAdp"
        } else if (Type.equals("trashfiles", ignoreCase = true)) {
            androidFragment =
                ToolsForFileTrashFAdp()
            currentFragment = "ToolsForFileFAdp"
        }
        replaceFragment(androidFragment)
    }

    fun replaceFragment(destFragment: Fragment?) {
        fragmentManager = this.supportFragmentManager
        val fragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction.replace(R.id.trash_frame_layout, destFragment!!)
        fragmentTransaction.commit() }


    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_HOME) {
        }
        return super.onKeyDown(keyCode, event)
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
    override fun onBackPressed() {
        setResult(Activity.RESULT_OK)
        when (currentFragment) {
            "ImageTrashFrag" -> {
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
                return
            }
            else -> {}
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    override fun onResume() {
        super.onResume()
        SharedPref.AppOpenShow = false
        Log.d("Message","Resume");



    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    override fun onStart() {
        super.onStart()
        Log.d("Message","onstart");

        SharedPref.AppOpenShow = false
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    override fun onPause() {
        super.onPause()
        Log.d("Message","onpause");

        SharedPref.AppOpenShow = false
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    override fun onStop() {
        super.onStop()
        Log.d("Message","stop");
        SharedPref.AppOpenShow = false

    }
}