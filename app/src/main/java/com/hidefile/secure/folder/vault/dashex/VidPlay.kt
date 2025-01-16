package com.hidefile.secure.folder.vault.dashex


import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.OpenableColumns
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.hidefile.secure.folder.vault.AdActivity.Common_Adm
import com.hidefile.secure.folder.vault.AdActivity.SharedPref
import com.hidefile.secure.folder.vault.R
import com.hidefile.secure.folder.vault.cluecanva.*

class VidPlay : AppCompatActivity(), View.OnClickListener {
    var viewVid: VideoView? = null

    private var actionBar: ActionBar? = null
    var handler = Handler()
    private var reachTime: TextView? = null
    private var totalTime: TextView? = null
    private var play_btn: ImageView? = null
    private var play_ahead: ImageView? = null
    private var play_back: ImageView? = null
    var pb: AnPbHr? = null


    var idNative = ""
    private fun setProgress() {
        handler.postDelayed({
            val reach = viewVid!!.currentPosition
            var duration = 0.1f
            if (viewVid!!.duration != 0) {
                duration = viewVid!!.duration.toFloat()
            }
            val progress = (reach * 50 / duration).toInt()
            reachTime!!.text =
                TillsPth.stringForTime(viewVid!!.currentPosition.toLong())
            pb!!.progress = progress
            if (viewVid!!.isPlaying) {
                play_btn!!.setImageResource(R.drawable.pause)
            } else {
                play_btn!!.setImageResource(R.drawable.play)
            }
            setProgress()
        }, 1000)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        requestWindowFeature(Window.FEATURE_NO_TITLE)
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            WindowManager.LayoutParams.FLAG_FULLSCREEN
//        )
        setContentView(R.layout.vid_play)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)


        Common_Adm.getInstance().loadBanner(this@VidPlay, findViewById<ViewGroup?>(R.id.llBanner),findViewById<ViewGroup?>(R.id.adSimmer1))
        SharedPref.AppOpenShow = false

        val toolbar = findViewById<Toolbar>(R.id.main_toolbar)
        setSupportActionBar(toolbar)
        actionBar = supportActionBar
        if (actionBar != null) {
        }
        val iv_back = findViewById<ImageView>(R.id.iv_back)
        iv_back.setOnClickListener { onBackPressed() }
        findViewById<View>(R.id.iv_option).visibility = View.GONE
        val uri = Uri.parse(intent.getStringExtra("filepath"))
        val tv_tital = findViewById<VTv>(R.id.tv_tital)
        try {
            var fileName = getFileName(uri)
            fileName = TillsFl.getOriginalFileName(fileName)
            tv_tital.text = fileName
        } catch (npe: NullPointerException) {
            tv_tital.text = "Video Player"
        }
        reachTime = findViewById(R.id.reach_time)
        reachTime!!.setText(TillsPth.stringForTime(0))
        totalTime = findViewById(R.id.total_time)
        totalTime!!.setText(TillsPth.stringForTime(0))
        play_btn = findViewById(R.id.play_btn)
        play_ahead = findViewById(R.id.play_ahead)
        play_back = findViewById(R.id.play_back)
        play_btn!!.setOnClickListener(this)
        play_ahead!!.setOnClickListener(this)
        play_back!!.setOnClickListener(this)
        pb = findViewById(R.id.progressBar)
        pb!!.setProgress(0)
        pb!!.setMax(100)
        viewVid = findViewById(R.id.player_vid)
        viewVid!!.setVideoURI(uri)
        viewVid!!.setOnPreparedListener(MediaPlayer.OnPreparedListener { mediaPlayer1 ->
            playVideo()
            mediaPlayer1.isLooping = true
            totalTime!!.setText(TillsPth.stringForTime(viewVid!!.getDuration().toLong()))
            setProgress()
        })
    }

    @SuppressLint("Range")
    fun getFileName(uri: Uri): String {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = contentResolver.query(uri, null, null, null, null)
            if (cursor != null) {
                try {
                    if (cursor.moveToFirst()) {
                        result =
                            cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    }
                } finally {
                    cursor.close()
                }
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result!!.lastIndexOf('/')
            if (cut != -1) {
                result = result.substring(cut + 1)
            }
        }
        return result
    }



    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_HOME) {
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.play_btn -> if (viewVid!!.isPlaying) {
                pauseVideo()
            } else {
                playVideo()
            }
            R.id.play_ahead -> if (viewVid!!.isPlaying) {
                aheadVideo()
                Toast.makeText(this@VidPlay, "Enable Coming Soon", Toast.LENGTH_SHORT).show()
            } else {
                playVideo()
            }
            R.id.play_back -> if (viewVid!!.isPlaying) {
                backVideo()
                Toast.makeText(this@VidPlay, "Enable Coming Soon", Toast.LENGTH_SHORT).show()

            } else {
                playVideo()
            }
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
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    override fun onStop() {
        super.onStop()
        Log.d("Message","stop");
        SharedPref.AppOpenShow = false
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    override fun onPause() {
        super.onPause()
        pauseVideo()
        handler.removeCallbacksAndMessages(handler)
        SharedPref.AppOpenShow = false
    }

    private fun playVideo() {
        if (viewVid != null && play_btn != null) {
            viewVid!!.start()
            play_btn!!.setImageResource(R.drawable.pause)
        }
    }

    private fun pauseVideo() {
        if (viewVid != null && play_btn != null) {
            viewVid!!.pause()
            play_btn!!.setImageResource(R.drawable.play)
        }
    }

    private fun aheadVideo() {
        if (viewVid != null && play_btn != null) {
            viewVid!!.seekTo(viewVid!!.currentPosition - 5000)
            viewVid!!.canSeekForward()
            play_btn!!.setImageResource(R.drawable.pause)
        }
    }

    private fun backVideo() {
        if (viewVid != null && play_btn != null) {
            viewVid!!.seekTo(viewVid!!.currentPosition + 5000)
            viewVid!!.canSeekBackward()
            play_btn!!.setImageResource(R.drawable.pause)
        }
    }


}