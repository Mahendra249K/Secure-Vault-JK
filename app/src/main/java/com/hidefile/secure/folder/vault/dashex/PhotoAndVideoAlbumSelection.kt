package com.hidefile.secure.folder.vault.dashex


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.database.ContentObserver
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.GridView
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.Joaquin.thiago.*
import com.hidefile.secure.folder.vault.AdActivity.SharedPref
import com.hidefile.secure.folder.vault.R
import com.hidefile.secure.folder.vault.cluecanva.HandPrmt
import com.hidefile.secure.folder.vault.cluecanva.HandPrmt.OnListener
import com.hidefile.secure.folder.vault.cluecanva.VTv
import com.hidefile.secure.folder.vault.dpss.ImageAlbumSelectAdp
import com.hidefile.secure.folder.vault.dpss.VideoAlbumSelectAdp
import com.hidefile.secure.folder.vault.edptrs.InSafe
import com.hidefile.secure.folder.vault.edptrs.InSafe.REQUEST_CODE_IMAGE

class PhotoAndVideoAlbumSelection : FoundationActivity() {
    var selectType = 0
    var resultLauncher: ActivityResultLauncher<Intent>? = null
    private var listPtFlds: ArrayList<ListPtFld?>? = ArrayList()
    private var listVedFls = ArrayList<ListVedFl?>()
    private var errorDisplay: TextView? = null
    private var progressBar: ProgressBar? = null
    private var gridView: GridView? = null
    private var imageAdapter: ImageAlbumSelectAdp? = null
    private var videoAdapter: VideoAlbumSelectAdp? = null
    private var actionBar: ActionBar? = null
    private var observer: ContentObserver? = null
    private var handler: Handler? = null
    private var thread: Thread? = null


    var idNative = ""
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.clear()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        requestWindowFeature(Window.FEATURE_NO_TITLE)
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            WindowManager.LayoutParams.FLAG_FULLSCREEN
//        )
        setContentView(R.layout.album_selection)

//         Common_Adm.getInstance().AmNativeLoad(this, findViewById<View>(R.id.llNativeSmall) as ViewGroup, false)
        SharedPref.AppOpenShow = false
        listItVids = ArrayList()
        listIdPics = ArrayList()
        val toolbar = findViewById<Toolbar>(R.id.main_toolbar)
        setSupportActionBar(toolbar)
        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult? -> requestPermissions() }
        val iv_back = findViewById<ImageView>(R.id.iv_back)
        iv_back.setOnClickListener { onBackPressed() }
        findViewById<View>(R.id.iv_option).visibility = View.GONE
        val intent = intent
        if (intent == null) {
            finish()
        }
        InSafe.limit =
            intent!!.getIntExtra(InSafe.INTENT_EXTRA_LIMIT, InSafe.DEFAULT_LIMIT)
        selectType = intent.getIntExtra(InSafe.INTENT_EXTRA_SELECT_TYPE, 1)
        actionBar = supportActionBar
        if (actionBar != null) {
            val tv_tital = findViewById<VTv>(R.id.tv_tital)
            if (selectType == 1) tv_tital.setSpannableText(
                getString(R.string.text_select), getString(
                    R.string.text_photo
                )
            ) else tv_tital.setSpannableText(
                getString(R.string.text_select), getString(
                    R.string.text_video
                )
            )
        }


        errorDisplay = findViewById(R.id.tv_error)
        errorDisplay!!.setVisibility(View.INVISIBLE)
        progressBar = findViewById(R.id.pb_album_select)
        gridView = findViewById(R.id.grid_view_album_select)
        gridView!!.setOnItemClickListener { parent, view, position, id ->
            // Toast.makeText(PhotoAndVideoAlbumSelection.this, ""+position, Toast.LENGTH_SHORT).show();
            val intent = Intent(applicationContext, PhotoAndVideoSelection::class.java)
            if (selectType == 1) {

                // listIdPics is a photos folder selection of gallery
                intent.putExtra(InSafe.INTENT_EXTRA_SELECT_TYPE, 1)
                intent.putExtra(
                    InSafe.INTENT_EXTRA_ALBUM, listPtFlds!![position]!!
                        .name
                )
                listIdPics = listPtFlds!![position]!!
                    .items as ArrayList<ListIdPic>
                startActivityForResult(intent, REQUEST_CODE_IMAGE)


            } else {

                // listItVids is a video folder selection of gallery
                intent.putExtra(InSafe.INTENT_EXTRA_SELECT_TYPE, 2)
                intent.putExtra(
                    InSafe.INTENT_EXTRA_ALBUM, listVedFls[position]!!
                        .name
                )
                listItVids = listVedFls[position]!!.items as ArrayList<ListItVid>
                startActivityForResult(intent, InSafe.REQUEST_CODE_VIDEO)

            }
        }
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    override fun onResume() {
        super.onResume()
        SharedPref.AppOpenShow = false
        Log.d("Message","Resume");



    }



    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    override fun onPause() {
        super.onPause()
        Log.d("Message","onpause");
        SharedPref.AppOpenShow = false


    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    override fun onStart() {
        super.onStart()
        SharedPref.AppOpenShow = false
        handler = object : Handler(Looper.myLooper()!!) {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    InSafe.PERMISSION_GRANTED -> {
                        loadAlbums()
                    }
                    InSafe.FETCH_STARTED -> {
                        progressBar!!.visibility = View.VISIBLE
                        gridView!!.visibility = View.INVISIBLE
                    }
                    InSafe.FETCH_COMPLETED -> {
                        if (selectType == 1) {
                            if (imageAdapter == null) {
                                imageAdapter = ImageAlbumSelectAdp(applicationContext, listPtFlds)
                                gridView!!.adapter = imageAdapter
                                progressBar!!.visibility = View.INVISIBLE
                                gridView!!.visibility = View.VISIBLE
                                orientationBasedUI(resources.configuration.orientation)
                            } else {
                                imageAdapter!!.notifyDataSetChanged()
                            }
                        } else {
                            if (videoAdapter == null) {
                                videoAdapter = VideoAlbumSelectAdp(applicationContext, listVedFls)
                                gridView!!.adapter = videoAdapter
                                progressBar!!.visibility = View.INVISIBLE
                                gridView!!.visibility = View.VISIBLE
                                orientationBasedUI(resources.configuration.orientation)
                            } else {
                                videoAdapter!!.notifyDataSetChanged()
                            }
                        }
                    }
                    InSafe.ERROR -> {
                        progressBar!!.visibility = View.INVISIBLE
                        errorDisplay!!.visibility = View.VISIBLE
                    }
                    else -> {
                        super.handleMessage(msg)
                    }
                }
            }
        }
        observer = object : ContentObserver(handler) {
            override fun onChange(selfChange: Boolean, uri: Uri?) {
                loadAlbums()
            }
        }
        contentResolver.registerContentObserver(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            false,
            observer as ContentObserver
        )
        requestPermissions()
    }

    fun requestPermissions() {
        HandPrmt.getInstance().requestPermissions(this, object : OnListener {
            override fun onPermissionGranted() {
                val message = handler!!.obtainMessage()
                message.what = InSafe.PERMISSION_GRANTED
                message.sendToTarget()
            }

            override fun onPermissionDenied() {
                progressBar!!.visibility = View.INVISIBLE
                gridView!!.visibility = View.INVISIBLE
                requestPermissions()
            }

            override fun onOpenSettings() {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                resultLauncher!!.launch(intent)
            }
        })
    }

    private fun endPermission() {
        HandPrmt.getInstance().hideDialog()
        if (resultLauncher != null) {
            resultLauncher!!.unregister()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    override fun onStop() {
        super.onStop()
        SharedPref.AppOpenShow = false
        stopThread()
        contentResolver.unregisterContentObserver(observer!!)
        observer = null
        if (handler != null) {
            handler!!.removeCallbacksAndMessages(null)
            handler = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        endPermission()
        if (actionBar != null) {
            actionBar!!.setHomeAsUpIndicator(null)
        }
        listPtFlds = null
        if (selectType == 1) {
            if (imageAdapter != null) {
                imageAdapter!!.releaseResources()
            }
        } else {
            if (videoAdapter != null) {
                videoAdapter!!.releaseResources()
            }
        }
        gridView!!.onItemClickListener = null
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        orientationBasedUI(newConfig.orientation)
    }

    private fun orientationBasedUI(orientation: Int) {
        val windowManager =
            applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        if (selectType == 1) {
            if (imageAdapter != null) {
                val size =
                    if (orientation == Configuration.ORIENTATION_PORTRAIT) metrics.widthPixels / 2 else metrics.widthPixels / 4
                imageAdapter!!.setLayoutParams(size)
            }
        } else {
            if (videoAdapter != null) {
                val size =
                    if (orientation == Configuration.ORIENTATION_PORTRAIT) metrics.widthPixels / 2 else metrics.widthPixels / 4
                videoAdapter!!.setLayoutParams(size)
            }
        }
        gridView!!.numColumns =
            if (orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 4
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            setResult(Activity.RESULT_OK, data)
            finish()
        } else if (requestCode == InSafe.REQUEST_CODE_VIDEO && resultCode == Activity.RESULT_OK && data != null) {
            setResult(Activity.RESULT_OK, data)
            finish()
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

    private fun loadAlbums() {
        startThread(AlbumLoaderRunnable())
    }

    private fun startThread(runnable: Runnable) {
        stopThread()
        thread = Thread(runnable)
        thread!!.start()
    }

    private fun stopThread() {
        if (thread == null || !thread!!.isAlive) {
            return
        }
        thread!!.interrupt()
        try {
            thread!!.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    private fun sendMessage(what: Int) {
        if (handler == null) {
            return
        }
        val message = handler!!.obtainMessage()
        message.what = what
        message.sendToTarget()
    }

    private inner class AlbumLoaderRunnable : Runnable {
        override fun run() {
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND)
            if (selectType == 1) {
                if (imageAdapter == null) {
                    sendMessage(InSafe.FETCH_STARTED) }
                listPtFlds = ArrayList()
                ListMidLod.getLoader()
                    .loadPhotos(this@PhotoAndVideoAlbumSelection, object : ListOnPhotoClBkBsLast() {
                        override fun onResult(result: ListPtRslt) {
                            val photoData = result.folders
                            for (i in photoData.indices) {
                                if (photoData[i].items.size > 0) listPtFlds!!.add(photoData[i]) }
                            sendMessage(InSafe.FETCH_COMPLETED) } }) } else if (selectType == 2) {
                listVedFls = ArrayList()
                ListMidLod.getLoader()
                    .loadVideos(this@PhotoAndVideoAlbumSelection, object : ListOnVideoClBkBsLast() {
                        override fun onResult(result: ListVidReal) {
                            val daata = result.folders
                            listVedFls = ArrayList()
                            listVedFls.addAll(daata)
                            for (i in daata.indices) {
                            }
                            sendMessage(InSafe.FETCH_COMPLETED) } })
            }
        }
    }

    companion object {
        @JvmField
        var listIdPics = ArrayList<ListIdPic>()
        @JvmField
        var listItVids = ArrayList<ListItVid>()
    }
}