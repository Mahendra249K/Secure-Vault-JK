package com.hidefile.secure.folder.vault.dashex

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.appcompat.widget.Toolbar
import androidx.multidex.BuildConfig
import com.calldorado.Calldorado
import com.google.android.ump.ConsentInformation
import com.google.android.ump.FormError
import com.google.android.ump.UserMessagingPlatform
import com.hidefile.secure.folder.vault.R
import com.hidefile.secure.folder.vault.cluecanva.EntryAux
import com.hidefile.secure.folder.vault.cluecanva.PlcMngr
import com.hidefile.secure.folder.vault.cluecanva.SupPref
import com.hidefile.secure.folder.vault.cluecanva.TransINam
import com.hidefile.secure.folder.vault.cluecanva.VTv
import com.hidefile.secure.folder.vault.databinding.ConfigurationBinding
import com.hidefile.secure.folder.vault.dpss.SpinAdp
import com.hidefile.secure.folder.vault.edptrs.ComeBack

class Configuration : FoundationActivity(), View.OnClickListener, ComeBack.OnCheckedChangeListener {
    lateinit var binding: ConfigurationBinding
    var disablePackageNames: MutableList<String>? = null
    var spinner: Spinner? = null
    var mContext: Context? = null
    var appNameList = arrayOf("Smart Vault", "Clock", "Message", "Reminder", "Weather")
    var appImageList = arrayOf(
        R.drawable.ic_logo,
        R.drawable.appicon_4,
        R.drawable.appicon_3,
        R.drawable.appicon_2,
        R.drawable.appicon_5
    )
    var llPinChange: LinearLayout? = null
    var Launguagechange: LinearLayout? = null
    var llCallerIDSetting: LinearLayout? = null
    var llRevokeConsent: LinearLayout? = null
    var llVibrator: LinearLayout? = null
    var llHiddenSelfieSound: LinearLayout? = null
    var swHiddenSelfieSound: ComeBack? = null
    var sw_vibrate: ComeBack? = null
//
//
//


    var policyManager: PlcMngr? = null
    private var oldPackageName = "com.hidefile.secure.folder.vault.activity.PinActivity_icon1"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ConfigurationBinding.inflate(layoutInflater)
        setContentView(binding.root)


        mContext = this@Configuration
//        Common_Adm.getInstance().AmNativeLoad(this, findViewById<View>(R.id.llNativeLarge) as ViewGroup, true)

        disablePackageNames = ArrayList()
        disablePackageNames?.add("com.hidefile.secure.folder.vault.activity.PinActivity_icon1")
        disablePackageNames?.add("com.hidefile.secure.folder.vault.activity.PinActivity_icon2")
        disablePackageNames?.add("com.hidefile.secure.folder.vault.activity.PinActivity_icon3")
        disablePackageNames?.add("com.hidefile.secure.folder.vault.activity.PinActivity_icon4")
        disablePackageNames?.add("com.hidefile.secure.folder.vault.activity.PinActivity_icon5")
        val toolbar = findViewById<Toolbar>(R.id.main_toolbar)
        val iv_back = findViewById<ImageView>(R.id.iv_back)
        iv_back.setOnClickListener(this)
        findViewById<View>(R.id.iv_option).visibility = View.GONE
        setSupportActionBar(toolbar)
        val tv_titel = findViewById<VTv>(R.id.tv_tital)
        tv_titel.setText(R.string.tital_setting)
        policyManager = PlcMngr(this)
        Init()
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_HOME) {
        }
        return super.onKeyDown(keyCode, event)
    }


    private fun Init() {
        spinner = findViewById(R.id.spinner)
        llPinChange = findViewById(R.id.pinchange)
        Launguagechange = findViewById(R.id.Launguagechange)
        llCallerIDSetting = findViewById(R.id.llCallerIDSetting)
        llRevokeConsent = findViewById(R.id.llRevokeConsent)
        llVibrator = findViewById(R.id.llVibrator)
        llHiddenSelfieSound = findViewById(R.id.llHiddenSelfieSound)
        sw_vibrate = findViewById(R.id.sw_vibreat)
        swHiddenSelfieSound = findViewById(R.id.swHiddenSelfieSound)
        val a = SupPref.getBooleanValue(mContext, SupPref.VIBRATOR, true)
        sw_vibrate?.setChecked(a)
        val b = SupPref.getBooleanValue(mContext, SupPref.Hidden_selfie, true)
        swHiddenSelfieSound?.setChecked(b)
        val adapter = SpinAdp(this, R.layout.layout_spin, appNameList, appImageList)
        spinner?.adapter = adapter
        val index = SupPref.getIntValue(mContext, SupPref.ICON_INDEX, 0)
        spinner?.setSelection(index)

        try {
            spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View, position: Int, id: Long
                ) {
                    val newPackage = disablePackageNames!![position]
                    if (!newPackage.equals("", ignoreCase = true) && !newPackage.equals(
                            oldPackageName, ignoreCase = true
                        )
                    ) {
                        val disableNames: MutableList<String> = ArrayList(disablePackageNames)
                        disableNames.remove(newPackage)
                        Log.e("TAG", "onItemSelected->1: " + newPackage)
                        Log.e("TAG", "onItemSelected->2: " + disableNames)
                        Log.e("TAG", "onItemSelected->3: " + position)
                        setAppIcon(newPackage, disableNames, position)
                        oldPackageName = newPackage
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        llPinChange?.setOnClickListener(this)
        Launguagechange?.setOnClickListener(this)
        llCallerIDSetting?.setOnClickListener(this)
        llRevokeConsent?.setOnClickListener(this)
        llHiddenSelfieSound?.setOnClickListener(this)
        llVibrator?.setOnClickListener(this)
        sw_vibrate?.setOnCheckedChangeListener(object : ComeBack.OnCheckedChangeListener {
            override fun onCheckedChanged(view: ComeBack?, isChecked: Boolean) {
                if (isChecked) {
                    SupPref.setBooleanValue(mContext, SupPref.VIBRATOR, true)
                    sw_vibrate!!.setChecked(true)
                } else {
                    SupPref.setBooleanValue(mContext, SupPref.VIBRATOR, false)
                    sw_vibrate!!.setChecked(false)
                }
            }
        })

        swHiddenSelfieSound?.setOnCheckedChangeListener(object : ComeBack.OnCheckedChangeListener {
            override fun onCheckedChanged(view: ComeBack?, isChecked: Boolean) {
                if (isChecked) {
                    SupPref.setBooleanValue(
                        mContext, SupPref.Hidden_selfie, true
                    )
                    swHiddenSelfieSound!!.setChecked(true)
                } else {
                    SupPref.setBooleanValue(
                        mContext, SupPref.Hidden_selfie, false
                    )
                    swHiddenSelfieSound!!.setChecked(false)
                }
            }
        })

        try {
            val consentInformationss =
                UserMessagingPlatform.getConsentInformation(this@Configuration)
            if (consentInformationss.privacyOptionsRequirementStatus == ConsentInformation.PrivacyOptionsRequirementStatus.REQUIRED) {
                llRevokeConsent?.visibility = View.VISIBLE

            } else {
                llRevokeConsent?.visibility = View.GONE

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setAppIcon(activeName: String?, disableNames: List<String>?, iconIndex: Int) {
        TransINam.Builder(this@Configuration).activeName(activeName).disableNames(disableNames)
            .packageName(BuildConfig.APPLICATION_ID).build().setNow()
        EntryAux.showToast(mContext, "App icon changed successfully" + appNameList[iconIndex])
        SupPref.setIntValue(mContext, SupPref.ICON_INDEX, iconIndex)
    }

    @SuppressLint("NonConstantResourceId")
    override fun onClick(v: View) {
        when (v.id) {
            R.id.iv_back -> onBackPressed()
            R.id.pinchange -> {
                val intent = Intent(mContext, Pswd::class.java)
                intent.putExtra("ChangePin", true)
                startActivityForResult(intent, 205)
            }

            R.id.Launguagechange -> {
                SupPref.putString(this@Configuration, "launguageBack", false)

                val intent = Intent(mContext, MangamtiBhasaPasandKarvaniActivity::class.java)
                intent.putExtra("ShuConfirgurationMathiChe", true)
                startActivity(intent)
            }


            R.id.llCallerIDSetting -> {
                Calldorado.createSettingsActivity(this@Configuration)
            }

            R.id.llRevokeConsent -> {
                try {
                    UserMessagingPlatform.showPrivacyOptionsForm(
                        this@Configuration
                    ) { formError: FormError? -> }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }


            R.id.llVibrator -> if (SupPref.getBooleanValue(
                    mContext, SupPref.VIBRATOR, true
                )
            ) {
                SupPref.setBooleanValue(mContext, SupPref.VIBRATOR, false)
                sw_vibrate!!.isChecked = false
            } else {
                SupPref.setBooleanValue(mContext, SupPref.VIBRATOR, true)
                sw_vibrate!!.isChecked = true
            }

            R.id.llHiddenSelfieSound -> if (SupPref.getBooleanValue(
                    mContext, SupPref.Hidden_selfie, true
                )
            ) {
                SupPref.setBooleanValue(mContext, SupPref.Hidden_selfie, false)
                swHiddenSelfieSound!!.isChecked = false
            } else {
                SupPref.setBooleanValue(mContext, SupPref.Hidden_selfie, true)
                swHiddenSelfieSound!!.isChecked = true
            }

            else -> {}
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

    override fun onCheckedChanged(view: ComeBack?, isChecked: Boolean) {}
}