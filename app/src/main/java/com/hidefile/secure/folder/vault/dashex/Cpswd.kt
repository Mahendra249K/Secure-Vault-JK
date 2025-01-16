package com.hidefile.secure.folder.vault.dashex

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.*
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import com.hidefile.secure.folder.vault.AdActivity.Call_Back_Ads
import com.hidefile.secure.folder.vault.AdActivity.Common_Adm
import com.hidefile.secure.folder.vault.R
import com.hidefile.secure.folder.vault.cluecanva.RDbhp
import com.hidefile.secure.folder.vault.cluecanva.SupPref
import com.hidefile.secure.folder.vault.cluecanva.TilsCo

class Cpswd : FoundationActivity(), View.OnClickListener,
    View.OnTouchListener {
    var tv_1: TextView? = null
    var tv_2: TextView? = null
    var tv_3: TextView? = null
    var tv_4: TextView? = null
    var tv_5: TextView? = null
    var tv_6: TextView? = null
    var tv_7: TextView? = null
    var tv_8: TextView? = null
    var tv_9: TextView? = null
    var tv_0: TextView? = null
    var progress_bar :ProgressBar?=null
    var tv_pinhint: TextView? = null
    var iv_doen: ImageView? = null
    var iv_cam: ImageView? = null
    var pinNo = ""
    var existPin: String? = ""
    var mContext: Context? = null
    var ElsePin = false
    var idNative = ""
    var vibrator: Vibrator? = null
    var iv_pin1: ImageView? = null
    var iv_pin2: ImageView? = null
    var iv_pin3: ImageView? = null
    var iv_pin4: ImageView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pswd)
        mContext = this@Cpswd

        TilsCo.isStr = false
        existPin = intent.getStringExtra("existPin")
        pinNo = ""
//         Common_Adm.getInstance().AmNativeLoad(this, findViewById<View>(R.id.llNativeSmall) as ViewGroup, false)
        Init()
        updateLanguage(this@Cpswd)



//        loadBigNative()
    }




    @SuppressLint("ClickableViewAccessibility")
    private fun Init() {
        if (intent.extras != null) {
            ElsePin = intent.getBooleanExtra("ElsePin", false)
        }
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        Log.e("TAG", "Init: ElsePin $ElsePin")
        tv_pinhint = findViewById(R.id.tv_pinhint)
        iv_pin1 = findViewById(R.id.ivPsw1)
        iv_pin2 = findViewById(R.id.ivPsw2)
        iv_pin3 = findViewById(R.id.ivPsw3)
        iv_pin4 = findViewById(R.id.ivPsw4)
        tv_1 = findViewById(R.id.num_1)
        tv_2 = findViewById(R.id.num_2)
        tv_3 = findViewById(R.id.num_3)
        tv_4 = findViewById(R.id.num_4)
        tv_5 = findViewById(R.id.num_5)
        tv_6 = findViewById(R.id.num_6)
        tv_7 = findViewById(R.id.num_7)
        tv_8 = findViewById(R.id.num_8)
        tv_9 = findViewById(R.id.num_9)
        tv_0 = findViewById(R.id.num_0)
        iv_doen = findViewById(R.id.ivBack)
        iv_cam = findViewById(R.id.ivCamera)
        iv_cam?.setVisibility(View.INVISIBLE)
        findViewById<View>(R.id.iv_more).visibility = View.GONE
        tv_pinhint?.setText(resources.getString(R.string.reagain_Enter_your_new_password))
        tv_1?.setOnTouchListener(this)
        tv_2?.setOnTouchListener(this)
        tv_3?.setOnTouchListener(this)
        tv_4?.setOnTouchListener(this)
        tv_5?.setOnTouchListener(this)
        tv_6?.setOnTouchListener(this)
        tv_7?.setOnTouchListener(this)
        tv_8?.setOnTouchListener(this)
        tv_9?.setOnTouchListener(this)
        tv_0?.setOnTouchListener(this)
        iv_doen?.setOnClickListener(this)
        progress_bar=findViewById(R.id.progress_bar)

    }

    override fun onClick(v: View) {
        if (v.id == R.id.ivBack) {
            backButtonClick()
        }
        setVibrator()
    }

    fun setVibrator() {
        if (SupPref.getBooleanValue(mContext, SupPref.VIBRATOR, true)) {
            if (Build.VERSION.SDK_INT >= 26) {
                vibrator!!.vibrate(
                    VibrationEffect.createOneShot(
                        100,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                vibrator!!.vibrate(100)
            }
        }
    }
    override fun onStart() {
        super.onStart()

    }

    private fun donePin() {
        if (existPin.equals(pinNo, ignoreCase = true)) {
            if (ElsePin) {
                val RDbhp = RDbhp.getInstance(mContext)
                RDbhp.updatePIN(pinNo)
                Toast.makeText(mContext, "PIN Changed Successfully", Toast.LENGTH_SHORT).show()
                val i = Intent(
                    this@Cpswd,
                    BordMain::class.java
                )
                i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(i)
                setResult(Activity.RESULT_OK)
                finish()
            } else {
                progress_bar?.setVisibility(View.VISIBLE)
                Handler().postDelayed({

                    Common_Adm.getInstance()
                        .everytimeInterstialAdShow(true, this@Cpswd, object : Call_Back_Ads {
                            override fun onAdsClose() {
                                val intent = Intent(
                                    mContext,
                                    QuestionSecurity::class.java
                                )
                                intent.putExtra("existPin", pinNo)
                                intent.putExtra("QType", "Set")
                                intent.putExtra("Activity", "FirstActivity")
                                startActivityForResult(intent, 202)
                                finish()
                            }

                            override fun onLoading() {
                                val intent = Intent(
                                    mContext,
                                    QuestionSecurity::class.java
                                )
                                intent.putExtra("existPin", pinNo)
                                intent.putExtra("QType", "Set")
                                intent.putExtra("Activity", "FirstActivity")
                                startActivityForResult(intent, 202)
                                finish()
                            }

                            override fun onAdsFail() {
                                val intent = Intent(
                                    mContext,
                                    QuestionSecurity::class.java
                                )
                                intent.putExtra("existPin", pinNo)
                                intent.putExtra("QType", "Set")
                                intent.putExtra("Activity", "FirstActivity")
                                startActivityForResult(intent, 202)
                                finish()
                            }
                        })


                }, 400)
            }
        } else {
            pinNo = ""
            setPinText()
            Toast.makeText(
                applicationContext,
                resources.getString(R.string.mismatched_psd),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 202) {
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }

    private fun backButtonClick() {
        if (pinNo.length > 0) {
            pinNo = pinNo.substring(0, pinNo.length - 1)
            setPinText()
        } else {
        }
    }

    fun setPinText() {
        if (pinNo.length == 1) {
            iv_pin1!!.setImageResource(R.drawable.ic_star_circle)
            iv_pin2!!.setImageResource(R.drawable.ic_circle)
            iv_pin3!!.setImageResource(R.drawable.ic_circle)
            iv_pin4!!.setImageResource(R.drawable.ic_circle)
        } else if (pinNo.length == 2) {
            iv_pin1!!.setImageResource(R.drawable.ic_star_circle)
            iv_pin2!!.setImageResource(R.drawable.ic_star_circle)
            iv_pin3!!.setImageResource(R.drawable.ic_circle)
            iv_pin4!!.setImageResource(R.drawable.ic_circle)
        } else if (pinNo.length == 3) {
            iv_pin1!!.setImageResource(R.drawable.ic_star_circle)
            iv_pin2!!.setImageResource(R.drawable.ic_star_circle)
            iv_pin3!!.setImageResource(R.drawable.ic_star_circle)
            iv_pin4!!.setImageResource(R.drawable.ic_circle)
        } else if (pinNo.length == 4) {
            iv_pin1!!.setImageResource(R.drawable.ic_star_circle)
            iv_pin2!!.setImageResource(R.drawable.ic_star_circle)
            iv_pin3!!.setImageResource(R.drawable.ic_star_circle)
            iv_pin4!!.setImageResource(R.drawable.ic_star_circle)
            donePin()
        } else {
            pinNo = ""
            iv_pin1!!.setImageResource(R.drawable.ic_circle)
            iv_pin2!!.setImageResource(R.drawable.ic_circle)
            iv_pin3!!.setImageResource(R.drawable.ic_circle)
            iv_pin4!!.setImageResource(R.drawable.ic_circle)
        }
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                when (v.id) {
                    R.id.num_1 -> { pinNo += "1"
                        setPinText() }
                    R.id.num_2 -> { pinNo += "2"
                        setPinText() }
                    R.id.num_3 -> { pinNo += "3"
                        setPinText() }
                    R.id.num_4 -> { pinNo += "4"
                        setPinText() }
                    R.id.num_5 -> { pinNo += "5"
                        setPinText() }
                    R.id.num_6 -> { pinNo += "6"
                        setPinText() }
                    R.id.num_7 -> { pinNo += "7"
                        setPinText() }
                    R.id.num_8 -> { pinNo += "8"
                        setPinText() }
                    R.id.num_9 -> { pinNo += "9"
                        setPinText() }
                    R.id.num_0 -> { pinNo += "0"
                        setPinText() }
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
                textView.background = ContextCompat.getDrawable(this, R.drawable.password_button_unclick_background)
            } else {
                textView.setTextColor(resources.getColor(R.color.wooden_dark_text_perment))
                textView.background = ContextCompat.getDrawable(this, R.drawable.password_button_background)
            }
        }
    }
}