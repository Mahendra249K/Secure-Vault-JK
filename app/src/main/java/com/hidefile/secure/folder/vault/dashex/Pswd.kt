package com.hidefile.secure.folder.vault.dashex

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.provider.Settings
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.Joaquin.thiago.ListPtFld
import com.hidefile.secure.folder.vault.AdActivity.SharedPref
import com.hidefile.secure.folder.vault.R
import com.hidefile.secure.folder.vault.cluecanva.*
import com.hidefile.secure.folder.vault.cluecanva.HandPrmt.OnListener
import com.hidefile.secure.folder.vault.edptrs.*
import me.aflak.libraries.callback.FingerprintSecureCallback
import me.aflak.libraries.utils.FingerprintToken
import java.io.File
import java.io.FileOutputStream

class Pswd : HiddenFootActivity(), View.OnClickListener, FingerprintSecureCallback,
    View.OnTouchListener {
    var num_1: TextView? = null
    var num_2: TextView? = null
    var num_3: TextView? = null
    var num_4: TextView? = null
    var num_5: TextView? = null
    var num_6: TextView? = null
    var num_7: TextView? = null
    var num_8: TextView? = null
    var num_9: TextView? = null
    var num_0: TextView? = null
    var tv_senser: VTv? = null
    var iv_pswd1: ImageView? = null
    var iv_pswd2: ImageView? = null
    var iv_pswd3: ImageView? = null
    var iv_pswd4: ImageView? = null
    var iv_back: ImageView? = null
    var iv_camera: ImageView? = null
    var progress_bar: ProgressBar? = null
    private var listPtFlds: ArrayList<ListPtFld?>? = ArrayList()

    var idNative = ""
    var existPin = ""
    var ChangePin = false
    var oldPin = ""
    var RDbhp: RDbhp? = null
    var mContext: Context? = null
    var vibrator: Vibrator? = null
    var tv_pinhint: VTv? = null


    var tvForgotPassword: TextView? = null
    var ElsePin = false
    var resultLauncher: ActivityResultLauncher<Intent>? = null
    private var mPrintConfig: PrintConfig? = null
    private var isFromBackground = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pswd)
        //check if first time app open (for load ad data)

        updateLanguage(this@Pswd)
        TilsCo.isStr = false
        mContext = this@Pswd
        SharedPref.AppOpenShow = true



//        Common_Adm.getInstance().AmNativeLoad(this, findViewById<View>(R.id.llNativeSmall) as ViewGroup, false)
        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult? -> requestPermissions() }
        val intent = intent
        if (intent.hasExtra("isFromBackground")) {
            isFromBackground = intent.getBooleanExtra("isFromBackground", false)
        }
        requestPermissions()

    }

    private fun requestPermissions() {
        HandPrmt.getInstance().requestPermissions(this, object : OnListener {
            override fun onPermissionGranted() {
                Init()
            }

            override fun onPermissionDenied() {
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

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    private fun Init() {
        if (intent.extras != null) {
            ElsePin = intent.getBooleanExtra("ElsePin", false)
            ChangePin = intent.getBooleanExtra("ChangePin", false)
        }
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        createImageDir()
        RDbhp = com.hidefile.secure.folder.vault.cluecanva.RDbhp.getInstance(mContext)
        existPin = ""
        val userItem = RDbhp?.getUser(1)
        oldPin = userItem?.getPwd().toString()
        iv_pswd1 = findViewById(R.id.ivPsw1)
        iv_pswd2 = findViewById(R.id.ivPsw2)
        iv_pswd3 = findViewById(R.id.ivPsw3)
        iv_pswd4 = findViewById(R.id.ivPsw4)
        tv_pinhint = findViewById(R.id.tv_pinhint)
        tv_senser = findViewById(R.id.tv_fingerprint)
        num_1 = findViewById(R.id.num_1)
        num_2 = findViewById(R.id.num_2)
        num_3 = findViewById(R.id.num_3)
        num_4 = findViewById(R.id.num_4)
        num_5 = findViewById(R.id.num_5)
        num_6 = findViewById(R.id.num_6)
        num_7 = findViewById(R.id.num_7)
        num_8 = findViewById(R.id.num_8)
        num_9 = findViewById(R.id.num_9)
        num_0 = findViewById(R.id.num_0)
        tvForgotPassword = findViewById(R.id.tvForgotPassword)
        iv_back = findViewById(R.id.ivBack)
        iv_camera = findViewById(R.id.ivCamera)
        progress_bar = findViewById(R.id.progress_bar)
        if (ElsePin) {
            iv_camera?.visibility = View.INVISIBLE
//            tv_pinhint?.text = "Enter your new pin"
            tv_pinhint?.text = resources.getString(R.string.Enter_your_new_password)
        } else if (ChangePin) {
            iv_camera?.visibility = View.INVISIBLE
//            tv_pinhint?.text = "Enter your old pin"
            tv_pinhint?.text = resources.getString(R.string.Enter_your_old_pin)
        } else {
//            tv_pinhint?.text = "Enter Password"
            if(SupPref.getBooleanValue(mContext, SupPref.FirstTimeUser, true)){
                tv_pinhint?.text = resources.getString(R.string.Enter_your_new_password)

                }
                else{
                tv_pinhint?.text = resources.getString(R.string.Enter_Password)
            }


            if (oldPin.equals("", ignoreCase = true)) {
                iv_camera?.visibility = View.INVISIBLE
            } else {
                iv_camera?.visibility = View.INVISIBLE
            }
        }
        iv_back?.setOnClickListener(this)
        iv_camera?.setOnClickListener(this)
        num_1?.setOnTouchListener(this)
        num_2?.setOnTouchListener(this)
        num_3?.setOnTouchListener(this)
        num_4?.setOnTouchListener(this)
        num_5?.setOnTouchListener(this)
        num_6?.setOnTouchListener(this)
        num_7?.setOnTouchListener(this)
        num_8?.setOnTouchListener(this)
        num_9?.setOnTouchListener(this)
        num_0?.setOnTouchListener(this)
        mPrintConfig =
            PrintConfig().getBuilder(this).setCameraFacing(PrintFacing.FRONT_FACING_CAMERA)
                .setCameraResolution(PrintResolution.LOW_RESOLUTION)
                .setImageFormat(PrintFormat.FORMAT_PNG).setImageRotation(PrintRotation.ROTATION_270)
                .build()
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startCamera(mPrintConfig)
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.CAMERA), REQ_CODE_CAMERA_PERMISSION
            )
        }
        tvForgotPassword?.setOnClickListener { v: View? ->
            stopCamera()
            val intent = Intent(
                mContext, QuestionSecurity::class.java
            )
            intent.putExtra("QType", "Forgot")
            startActivityForResult(intent, 203)
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.ivBack -> backButtonClick()
            R.id.ivCamera -> {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST)
            }
        }
        setVibrator()
    }

    fun setVibrator() {
        if (SupPref.getBooleanValue(mContext, SupPref.VIBRATOR, true)) {
            if (Build.VERSION.SDK_INT >= 26) {
                vibrator!!.vibrate(
                    VibrationEffect.createOneShot(
                        100, VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                vibrator!!.vibrate(100)
            }
        }
    }

    private fun checkPin() {
        if (ElsePin || oldPin.equals("", ignoreCase = true)) {
            if (existPin.length >= 4) {
                progress_bar?.visibility = View.VISIBLE
                Handler().postDelayed({
                    val intent = Intent(
                        mContext, Cpswd::class.java
                    )
                    intent.putExtra("existPin", existPin)
                    intent.putExtra("ElsePin", ElsePin)
                    startActivityForResult(intent, 389)
                    finish()
                }, 400)

            } else {
                EntryAux.showToast(mContext, R.string.min_digit_msg)
            }
        } else {
            if (oldPin.equals(existPin, ignoreCase = true)) {
                if (ChangePin) {
                    val intent = intent
                    overridePendingTransition(0, 0)
                    intent.putExtra("ElsePin", true)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    finish()
                    overridePendingTransition(0, 0)
                    startActivity(intent)
                } else {
                    progress_bar?.visibility = View.VISIBLE
                    SupPref.setBooleanValue(this@Pswd, SupPref.isFromPin, true)
                    Handler().postDelayed({

                        existPin = ""
                        val intent = Intent(mContext, BordMain::class.java)
                        startActivity(intent)
                        finish()



                    }, 200)
                }
            } else {
                existPin = ""
                setPinText()
                val content = SpannableString(resources.getString(R.string.forgot_password))
                content.setSpan(UnderlineSpan(), 0, content.length, 0)
                tvForgotPassword!!.text = content
                tvForgotPassword!!.visibility = View.VISIBLE
                if (!ChangePin && SupPref.getBooleanValue(
                        mContext, SupPref.Hidden_selfie, true
                    )
                ) {
                    takePicture()
                }
                EntryAux.showToast(mContext, R.string.wrong_pwd)

            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        if (!ChangePin){
            System.exit(0)
        }
    }

    private fun backButtonClick() {
        if (existPin.length > 0) {
            existPin = existPin.substring(0, existPin.length - 1)
            setPinText()
        } else {
        }
    }

    fun setPinText() {
        if (existPin.length == 1) {
            iv_pswd1!!.setImageResource(R.drawable.ic_star_circle)
            iv_pswd2!!.setImageResource(R.drawable.ic_circle)
            iv_pswd3!!.setImageResource(R.drawable.ic_circle)
            iv_pswd4!!.setImageResource(R.drawable.ic_circle)
        } else if (existPin.length == 2) {
            iv_pswd1!!.setImageResource(R.drawable.ic_star_circle)
            iv_pswd2!!.setImageResource(R.drawable.ic_star_circle)
            iv_pswd3!!.setImageResource(R.drawable.ic_circle)
            iv_pswd4!!.setImageResource(R.drawable.ic_circle)
        } else if (existPin.length == 3) {
            iv_pswd1!!.setImageResource(R.drawable.ic_star_circle)
            iv_pswd2!!.setImageResource(R.drawable.ic_star_circle)
            iv_pswd3!!.setImageResource(R.drawable.ic_star_circle)
            iv_pswd4!!.setImageResource(R.drawable.ic_circle)
        } else if (existPin.length == 4) {
            iv_pswd1!!.setImageResource(R.drawable.ic_star_circle)
            iv_pswd2!!.setImageResource(R.drawable.ic_star_circle)
            iv_pswd3!!.setImageResource(R.drawable.ic_star_circle)
            iv_pswd4!!.setImageResource(R.drawable.ic_star_circle)
            checkPin()
        } else {
            existPin = ""
            iv_pswd1!!.setImageResource(R.drawable.ic_circle)
            iv_pswd2!!.setImageResource(R.drawable.ic_circle)
            iv_pswd3!!.setImageResource(R.drawable.ic_circle)
            iv_pswd4!!.setImageResource(R.drawable.ic_circle)
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 201 || requestCode == 203) {
                setResult(Activity.RESULT_OK)
                finish()
            } else if (requestCode == CAMERA_PIC_REQUEST) {
                if (data != null) {
                    val image = data.extras!!["data"] as Bitmap?
                    saveImage(image)
                }
            } else if (requestCode == REQUEST_HIDE_PHOTOS) {
                if (data!!.data != null) {
                    val treeUri = data.data
                    contentResolver.takePersistableUriPermission(
                        treeUri!!, Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    )
                    SupPref.putHideUri(this, treeUri.toString())
                }
            }
        }
    }

    private fun saveImage(finalBitmap: Bitmap?) {
        val myDir = File(TillsPth.nohideImage)
        myDir.mkdirs()
        val timeStamp = EntryAux.getTimeStamp()
        val fname = "IMG_$timeStamp.jpg.bin"
        val fname2 = "IMG_$timeStamp.jpg"
//          val fname = "IMG_"+ System.currentTimeMillis()+".jpg.bin"
//          val fname2 = "IMG_"+ System.currentTimeMillis()+"a.jpg.bin"


        val file = File(myDir, fname)
        if (file.exists()) file.delete()
        try {
            val out = FileOutputStream(file)
            finalBitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val file1 = File(Environment.getExternalStorageDirectory().absolutePath + "/" + "DCMI")
        file1.mkdirs()
        val oriPath = file1.absolutePath + "/" + fname2
        RDbhp!!.insertImage(fname2, oriPath, file.absolutePath, file.length(), "image/jpg")
    }

    override fun onImageCapture(imageFile: File) {
        val temp=imageFile.absolutePath
        Log.e("Temp",temp)
    }
    override fun onCameraError(errorCode: Int) {
        when (errorCode) {
            FootError.ERROR_CAMERA_OPEN_FAILED -> {}
            FootError.ERROR_IMAGE_WRITE_FAILED -> EntryAux.showToast(
                mContext, R.string.error_cannot_write
            )
            FootError.ERROR_CAMERA_PERMISSION_NOT_AVAILABLE -> EntryAux.showToast(
                mContext, R.string.error_cannot_get_permission
            )
            FootError.ERROR_DOES_NOT_HAVE_OVERDRAW_PERMISSION -> HiddenCameraUtils.openDrawOverPermissionSetting(
                this
            )
            FootError.ERROR_DOES_NOT_HAVE_FRONT_CAMERA -> EntryAux.showToast(
                mContext, R.string.error_not_having_camera
            )
        }
    }

    override fun onAuthenticationSucceeded() {


        if (!ElsePin && !ChangePin) {
            SupPref.setBooleanValue(mContext, SupPref.isFromPin, true)
            Toast.makeText(this, "Please Wait....", Toast.LENGTH_SHORT).show()
            Handler().postDelayed({
                val intent = Intent(
                    mContext, BordMain::class.java
                )
                startActivity(intent)
                finish()
            }, 1500)
        }
    }

    override fun onAuthenticationFailed() {}
    override fun onNewFingerprintEnrolled(token: FingerprintToken?) {}
    override fun onAuthenticationError(errorCode: Int, error: String?) {}
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                when (v.id) {
                    R.id.num_1 -> {
                        existPin += "1"
                        setPinText()
                    }
                    R.id.num_2 -> {
                        existPin += "2"
                        setPinText()
                    }
                    R.id.num_3 -> {
                        existPin += "3"
                        setPinText()
                    }
                    R.id.num_4 -> {
                        existPin += "4"
                        setPinText()
                    }
                    R.id.num_5 -> {
                        existPin += "5"
                        setPinText()
                    }
                    R.id.num_6 -> {
                        existPin += "6"
                        setPinText()
                    }
                    R.id.num_7 -> {
                        existPin += "7"
                        setPinText()
                    }
                    R.id.num_8 -> {
                        existPin += "8"
                        setPinText()
                    }
                    R.id.num_9 -> {
                        existPin += "9"
                        setPinText()
                    }
                    R.id.num_0 -> {
                        existPin += "0"
                        setPinText()
                    }
                }
                setVibrator()
                buttonPressEffect(v, true)
            }
            MotionEvent.ACTION_UP -> buttonPressEffect(v, false)
        }
        return true
    }

    private fun buttonPressEffect(view: View, isTouch: Boolean) {
        if (view is TextView) {
            val textView = view
            if (isTouch) {
                textView.setTextColor(resources.getColor(R.color.wooden_dark_text_perment))
                textView.background =
                    ContextCompat.getDrawable(this, R.drawable.password_button_unclick_background)
            } else {
                textView.setTextColor(resources.getColor(R.color.wooden_dark_text_perment))
                textView.background =
                    ContextCompat.getDrawable(this, R.drawable.password_button_background)
            }
        }
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


    companion object {
        const val CAMERA_PIC_REQUEST = 1111
        private const val REQ_CODE_CAMERA_PERMISSION = 1253
        private const val REQUEST_HIDE_PHOTOS = 2000

        @JvmStatic
        fun createImageDir() {
            val myDirectory = File(TillsPth.hideImage)
            if (!myDirectory.exists()) {
                myDirectory.mkdir()
            } else {
            }
            val myPwd = File(TillsPth.worngPwd)
            if (!myPwd.exists()) {
                myPwd.mkdirs()
            } else {
            }
        }
    }
}