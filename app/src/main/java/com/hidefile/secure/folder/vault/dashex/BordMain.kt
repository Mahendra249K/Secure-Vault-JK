package com.hidefile.secure.folder.vault.dashex

import android.accounts.AccountManager
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.telephony.TelephonyManager
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatTextView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.material.snackbar.Snackbar
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.hidefile.secure.folder.vault.AdActivity.Call_Back_Ads
import com.hidefile.secure.folder.vault.AdActivity.Common_Adm
import com.hidefile.secure.folder.vault.AdActivity.ExitDialogNative
import com.hidefile.secure.folder.vault.AdActivity.Open_App_Manager
import com.hidefile.secure.folder.vault.AdActivity.SharedPref
import com.hidefile.secure.folder.vault.R
import com.hidefile.secure.folder.vault.cluecanva.RDbhp
import com.hidefile.secure.folder.vault.cluecanva.RecentList
import com.hidefile.secure.folder.vault.cluecanva.SupPref
import com.hidefile.secure.folder.vault.cluecanva.SupPref.AccountName
import com.hidefile.secure.folder.vault.cluecanva.SupPref.ExitNativeAd
import com.hidefile.secure.folder.vault.cluecanva.SupPref.ExitNativeAdAfterAd
import com.hidefile.secure.folder.vault.cluecanva.SupPref.appRated
import com.hidefile.secure.folder.vault.cluecanva.SupPref.appRatedPermently
import com.hidefile.secure.folder.vault.cluecanva.SupPref.getBooleanValue
import com.hidefile.secure.folder.vault.cluecanva.SupPref.isDarkModeOn
import com.hidefile.secure.folder.vault.cluecanva.SupPref.isFirstTime
import com.hidefile.secure.folder.vault.cluecanva.SupPref.isFromPin
import com.hidefile.secure.folder.vault.cluecanva.SupPref.isRating
import com.hidefile.secure.folder.vault.cluecanva.SupPref.isRatting
import com.hidefile.secure.folder.vault.cluecanva.SupPref.putValue
import com.hidefile.secure.folder.vault.cluecanva.SupPref.setBooleanValue
import com.hidefile.secure.folder.vault.cluecanva.TillsPth
import com.hidefile.secure.folder.vault.cluecanva.TilsCo
import com.hidefile.secure.folder.vault.cluecanva.VTv
import java.io.File
import java.util.Locale


class BordMain : FoundationActivity() {
    var dbHelper: RDbhp? = null
    var mContext: Context? = null
    var iv_home: ImageView? = null
    var iv_setting: ImageView? = null

    //    var iv_daynight_theme: LottieAnimationView? = null
    var iv_daynight_theme: LinearLayout? = null
    var llSetting: LinearLayout? = null
    var llMyBrowser: LinearLayout? = null
    var llHiddenSelfie: LinearLayout? = null
    var llMixFiles: LinearLayout? = null
    var llMyPhoto: LinearLayout? = null
    var llVideo: LinearLayout? = null

    var arrAppIcons1 = arrayOf(
        "ic_google1",
        "ic_youtube",
        "ic_facebook",
        "ic_twitter",
        "ic_instagram",
    )
    var arrAppTitle = arrayOf("Google", "YouTube", "Facebook", "Twitter", "Instagram")
    var arrAppLinks = arrayOf(
        "https://www.google.com",
        "https://youtube.com",
        "https://facebook.com",
        "https://twitter.com/",
        "https://instagram.com",
    )
    var gbug = false
    var ratingme = 0f
    var feedback = ""
    private var isActivityPause = false
    private val credential: GoogleAccountCredential? = null

    private var dialog: AlertDialog? = null
    private var drawerLayout: DrawerLayout? = null


    private var tvAppVersion: AppCompatTextView? = null

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this@BordMain
        setContentView(R.layout.main_bord)

        TilsCo.isStr = false
        Pswd.createImageDir()
        dbHelper = RDbhp.getInstance(mContext)
        createImageDir()
        SharedPref.AppOpenShow = false
        val tv_tital = findViewById<VTv>(R.id.tv_tital)
        llMyBrowser = findViewById(R.id.llMyBrowser)
        llMyPhoto = findViewById(R.id.llPhoto)

        llMixFiles = findViewById(R.id.llFiles)
        iv_daynight_theme = findViewById(R.id.iv_daynight_theme)
        llSetting = findViewById(R.id.ll_Setting)
        iv_setting = findViewById(R.id.iv_setting)
        llHiddenSelfie = findViewById(R.id.llHiddenSelfie)
        drawerLayout = findViewById(R.id.drawerLayout)
        llVideo = findViewById(R.id.llVideo)
        if (supportActionBar != null) supportActionBar!!.setDisplayHomeAsUpEnabled(false)

        iv_setting!!.setOnClickListener(View.OnClickListener { v: View? ->
            val popupMenu = PopupMenu(this, v)
            menuInflater.inflate(R.menu.emen_bord, popupMenu.menu)
            popupMenu.show()
            popupMenu.setOnMenuItemClickListener { menuItem -> // Toast message on menu item clicked
                when (menuItem.itemId) {
                    R.id.menu_bord_share -> {
                        val sharingIntent = Intent(Intent.ACTION_SEND)
                        sharingIntent.type = "text/plain"
                        val shareBody =
                            "Download the Hide File & Vault Secure app from play store https://play.google.com/store/apps/details?id=$packageName"
                        sharingIntent.putExtra(
                            Intent.EXTRA_SUBJECT, getString(R.string.app_setting_name)
                        )
                        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
                        startActivity(Intent.createChooser(sharingIntent, "Share via"))
                    }

                    R.id.menu_bord_rate -> {
                        RateMe(false)
                        Constant.checkRating = true
                    }

                    R.id.menu_bord_policy -> {
                        startActivity(
                            Intent(
                                mContext, PolicyShow::class.java
                            )
                        )
                    }
                }
                return@setOnMenuItemClickListener false
            }
            popupMenu.show()
        })

        llSetting?.setOnClickListener { v: View? ->
            Common_Adm.getInstance()
            Common_Adm.mInterstitialAdClickCount++
            Common_Adm.getInstance()
                .loadOrShowAdmInterstial(true, this@BordMain, object : Call_Back_Ads {
                    override fun onAdsClose() {
                        val intent = Intent(this@BordMain, Configuration::class.java)
                        startActivity(intent)
                    }

                    override fun onLoading() {
                        val intent = Intent(this@BordMain, Configuration::class.java)
                        startActivity(intent)
                    }

                    override fun onAdsFail() {
                        val intent = Intent(this@BordMain, Configuration::class.java)
                        startActivity(intent)
                    }
                })

        }
        llMyPhoto?.setOnClickListener { v: View? ->

            Common_Adm.getInstance()
            Common_Adm.mInterstitialAdClickCount++
            Common_Adm.getInstance()
                .loadOrShowAdmInterstial(true, this@BordMain, object : Call_Back_Ads {
                    override fun onAdsClose() {
                        val intent = Intent(this@BordMain, Prasth::class.java)
                        intent.putExtra("Type", "Photos")
                        startActivity(intent)
                    }

                    override fun onLoading() {
                        val intent = Intent(this@BordMain, Prasth::class.java)
                        intent.putExtra("Type", "Photos")
                        startActivity(intent)
                    }

                    override fun onAdsFail() {
                        val intent = Intent(this@BordMain, Prasth::class.java)
                        intent.putExtra("Type", "Photos")
                        startActivity(intent)
                    }
                })
        }

        iv_daynight_theme?.setOnClickListener { v: View? ->

            if (getBooleanValue(mContext, isDarkModeOn, true)) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                setBooleanValue(mContext, isDarkModeOn, false)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // Set the status bar text color to dark
                    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                }

            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                setBooleanValue(mContext, isDarkModeOn, true)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    window.decorView.systemUiVisibility = 0
                }
            }
        }
        llVideo?.setOnClickListener { v: View? ->

            Common_Adm.getInstance()
            Common_Adm.mInterstitialAdClickCount++
            Common_Adm.getInstance()
                .loadOrShowAdmInterstial(true, this@BordMain, object : Call_Back_Ads {
                    override fun onAdsClose() {
                        val intent = Intent(this@BordMain, Prasth::class.java)
                        intent.putExtra("Type", "Videos")
                        startActivity(intent)
                    }

                    override fun onLoading() {
                        val intent = Intent(this@BordMain, Prasth::class.java)
                        intent.putExtra("Type", "Videos")
                        startActivity(intent)
                    }

                    override fun onAdsFail() {
                        val intent = Intent(this@BordMain, Prasth::class.java)
                        intent.putExtra("Type", "Videos")
                        startActivity(intent)
                    }
                })

        }
        llMixFiles?.setOnClickListener { v: View? ->
            Common_Adm.getInstance()
            Common_Adm.mInterstitialAdClickCount++
            Common_Adm.getInstance()
                .loadOrShowAdmInterstial(true, this@BordMain, object : Call_Back_Ads {
                    override fun onAdsClose() {
                        val intent = Intent(this@BordMain, Prasth::class.java)
                        intent.putExtra("Type", "Files")
                        startActivity(intent)
                    }

                    override fun onLoading() {
                        val intent = Intent(this@BordMain, Prasth::class.java)
                        intent.putExtra("Type", "Files")
                        startActivity(intent)
                    }

                    override fun onAdsFail() {
                        val intent = Intent(this@BordMain, Prasth::class.java)
                        intent.putExtra("Type", "Videos")
                        startActivity(intent)
                    }
                })

        }
        llMyBrowser?.setOnClickListener { v: View? ->
            Common_Adm.getInstance()
            Common_Adm.mInterstitialAdClickCount++
            Common_Adm.getInstance()
                .loadOrShowAdmInterstial(true, this@BordMain, object : Call_Back_Ads {
                    override fun onAdsClose() {
                        val intent = Intent(this@BordMain, MyBrowser::class.java)
                        startActivity(intent)
                    }

                    override fun onLoading() {
                        val intent = Intent(this@BordMain, MyBrowser::class.java)
                        startActivity(intent)
                    }

                    override fun onAdsFail() {
                        val intent = Intent(this@BordMain, MyBrowser::class.java)
                        startActivity(intent)
                    }
                })
        }
        llHiddenSelfie?.setOnClickListener { v: View? ->
            Common_Adm.getInstance()
            Common_Adm.mInterstitialAdClickCount++
            Common_Adm.getInstance()
                .loadOrShowAdmInterstial(true, this@BordMain, object : Call_Back_Ads {
                    override fun onAdsClose() {
                        val intent = Intent(this@BordMain, Image::class.java)
                        startActivity(intent)
                    }

                    override fun onLoading() {
                        val intent = Intent(this@BordMain, Image::class.java)
                        startActivity(intent)
                    }

                    override fun onAdsFail() {
                        val intent = Intent(this@BordMain, Image::class.java)
                        startActivity(intent)
                    }
                })
        }

        val RDbhp = RDbhp.getInstance(this)
        if (getBooleanValue(mContext, isFirstTime, true)) {
            val appList: LinkedHashMap<String, RecentList> = LinkedHashMap()
            for (i in arrAppTitle.indices) {
                appList[arrAppLinks[i]] =
                    RecentList(arrAppIcons1[i], arrAppLinks[i], arrAppTitle[i])
            }
            SupPref.saveAppData(applicationContext, appList)
            setBooleanValue(mContext, isFirstTime, false)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_ACCOUNT_PICKER -> if (resultCode == Activity.RESULT_OK && data != null && data.extras != null) {
                val accountName: String? = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME)
                if (accountName != null && !accountName.trim { it <= ' ' }.isEmpty()) {
                    putValue(mContext, AccountName, accountName)
                    credential!!.selectedAccountName = accountName
                    dbHelper!!.updateUserEmail(accountName)
                }
            }

            479 -> if (resultCode == Activity.RESULT_OK && data != null) {
                val treeUri: Uri? = data.data
                if (treeUri != null) {
                    applicationContext.contentResolver.takePersistableUriPermission(
                        treeUri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    )
                }
                setBooleanValue(this@BordMain, "sdcardapproved", true)
                putValue(
                    this@BordMain, "sdcarduri", treeUri.toString().toString() + ""
                )
            }

            362 -> if (resultCode == Activity.RESULT_OK && data != null) {
                val treeUri: Uri? = data.data
            }
        }
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    override fun onResume() {
        super.onResume()
        SharedPref.AppOpenShow = false

        Log.d("Message", "Resume")

        if (getBooleanValue(this@BordMain, isFromPin, false)) {
            setBooleanValue(this@BordMain, isFromPin, false)
        }

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    override fun onStart() {
        super.onStart()
        Log.d("Message", "onstart")
        SharedPref.AppOpenShow = false

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    override fun onPause() {
        super.onPause()
        Log.d("Message", "onpause")
        isActivityPause = true
        SharedPref.AppOpenShow = false
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    override fun onStop() {
        super.onStop()
        Log.d("Message", "stop")
        SharedPref.AppOpenShow = false


    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_HOME) {
        }
        return super.onKeyDown(keyCode, event)
    }

    fun createImageDir() {
        val myDirectory = File(TillsPth.nohideFile)
        if (!myDirectory.exists()) {
            myDirectory.mkdirs()
        } else {
        }
        val myDirectory1 = File(TillsPth.nohideVideo)
        if (!myDirectory1.exists()) {
            myDirectory1.mkdirs()
        } else {
        }
        val myDirectory2 = File(TillsPth.nohideImage)
        if (!myDirectory2.exists()) {
            myDirectory2.mkdirs()
        } else {
        }
    }

    override fun onBackPressed() {

        if (getBooleanValue(mContext, appRated, true) && getBooleanValue(
                mContext, appRatedPermently, true
            )
        ) {
            RateMe(false)
        } else if (getBooleanValue(mContext, ExitNativeAd, true) || getBooleanValue(
                mContext, ExitNativeAdAfterAd, true
            )
        ) {
            val exitDialogNative = ExitDialogNative()
            exitDialogNative.show(supportFragmentManager, exitDialogNative.tag)
//            this@BordMain.finishAffinity()
            finish()
//            System.exit(0)


        }
    }

    private fun RateMe(isFromBack: Boolean) {
        hideDialog()

        val dialogView: View = LayoutInflater.from(mContext).inflate(R.layout.dig_fdbk, null)
        val buttonPositive: TextView = dialogView.findViewById(R.id.btn_rate_now)
        val lay_feedback: LinearLayout = dialogView.findViewById(R.id.lay_feedback)
        val txtfeedback: EditText = dialogView.findViewById(R.id.txtfeedback)
        val btn_close: ImageView = dialogView.findViewById(R.id.btn_close)
        val rate_1: ImageView = dialogView.findViewById(R.id.rate_1)
        val rate_2: ImageView = dialogView.findViewById(R.id.rate_2)
        val rate_3: ImageView = dialogView.findViewById(R.id.rate_3)
        val rate_4: ImageView = dialogView.findViewById(R.id.rate_4)
        val rate_5: ImageView = dialogView.findViewById(R.id.rate_5)
        val color = 0xFFFFFF
        val drawable: Drawable = ColorDrawable(color)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            rate_1.foreground = drawable
            rate_2.foreground = drawable
            rate_3.foreground = drawable
            rate_4.foreground = drawable
            rate_5.foreground = drawable
        }
        rate_1.setPadding(20, 20, 20, 20)
        rate_2.setPadding(20, 20, 20, 20)
        rate_3.setPadding(20, 20, 20, 20)
        rate_4.setPadding(20, 20, 20, 20)
        rate_5.setPadding(20, 20, 20, 20)

        ratingme = 0f
        rate_1.setOnClickListener { v ->
            ratingme = 1f

            rate_1.setImageResource(R.drawable.ic_star_fill)
            rate_2.setImageResource(R.drawable.ic_star_blank)
            rate_3.setImageResource(R.drawable.ic_star_blank)
            rate_4.setImageResource(R.drawable.ic_star_blank)
            rate_5.setImageResource(R.drawable.ic_star_blank)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                rate_1.foreground = null
                rate_2.foreground = drawable
                rate_3.foreground = drawable
                rate_4.foreground = drawable
                rate_5.foreground = drawable
            }
            gbug = true
            buttonPositive.text = "Submit"


//            String url="https://play.google.com/store/apps/details?id=$packageName";
//            startActivity(new Intent(Intent.ACTION_SEND).setData(Uri.parse(url)));
            val sharedPreferences = getSharedPreferences(SharedPref.PREF_NAME, MODE_PRIVATE)

            val editor = sharedPreferences.edit()
            editor.putBoolean(SharedPref.RATING_STATUS, true)
            editor.commit()

        }
        rate_2.setOnClickListener { v ->
            ratingme = 2f

            rate_1.setImageResource(R.drawable.ic_star_fill)
            rate_2.setImageResource(R.drawable.ic_star_fill)
            rate_3.setImageResource(R.drawable.ic_star_blank)
            rate_4.setImageResource(R.drawable.ic_star_blank)
            rate_5.setImageResource(R.drawable.ic_star_blank)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                rate_1.foreground = null
                rate_2.foreground = null
                rate_3.foreground = drawable
                rate_4.foreground = drawable
                rate_5.foreground = drawable
            }
            gbug = true

            buttonPositive.text = "Submit"
            val sharedPreferences = getSharedPreferences(SharedPref.PREF_NAME, MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putBoolean(SharedPref.RATING_STATUS, true)
            editor.commit()
        }
        rate_3.setOnClickListener { v ->
            ratingme = 3f


            rate_1.setImageResource(R.drawable.ic_star_fill)
            rate_2.setImageResource(R.drawable.ic_star_fill)
            rate_3.setImageResource(R.drawable.ic_star_fill)
            rate_4.setImageResource(R.drawable.ic_star_blank)
            rate_5.setImageResource(R.drawable.ic_star_blank)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                rate_1.foreground = null
                rate_2.foreground = null
                rate_3.foreground = null
                rate_4.foreground = drawable
                rate_5.foreground = drawable
            }
            gbug = true
            buttonPositive.text = "Submit"
            val sharedPreferences = getSharedPreferences(SharedPref.PREF_NAME, MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putBoolean(SharedPref.RATING_STATUS, true)
            editor.commit()
        }
        rate_4.setOnClickListener { v ->
            ratingme = 4f

            rate_1.setImageResource(R.drawable.ic_star_fill)
            rate_2.setImageResource(R.drawable.ic_star_fill)
            rate_3.setImageResource(R.drawable.ic_star_fill)
            rate_4.setImageResource(R.drawable.ic_star_fill)
            rate_5.setImageResource(R.drawable.ic_star_blank)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                rate_1.foreground = null
                rate_2.foreground = null
                rate_3.foreground = null
                rate_4.foreground = null
                rate_5.foreground = drawable
            }
            gbug = false
            lay_feedback.visibility = View.GONE
            buttonPositive.text = "Rate Now"
            val sharedPreferences = getSharedPreferences(SharedPref.PREF_NAME, MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putBoolean(SharedPref.RATING_STATUS, true)
            editor.commit()
        }
        rate_5.setOnClickListener { v ->
            ratingme = 5f
            rate_1.setImageResource(R.drawable.ic_star_fill)
            rate_2.setImageResource(R.drawable.ic_star_fill)
            rate_3.setImageResource(R.drawable.ic_star_fill)
            rate_4.setImageResource(R.drawable.ic_star_fill)
            rate_5.setImageResource(R.drawable.ic_star_fill)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                rate_1.foreground = null
                rate_2.foreground = null
                rate_3.foreground = null
                rate_4.foreground = null
                rate_5.foreground = null
            }
            gbug = false
            lay_feedback.visibility = View.GONE
            buttonPositive.text = "Rate Now"
            val sharedPreferences = getSharedPreferences(SharedPref.PREF_NAME, MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putBoolean(SharedPref.RATING_STATUS, true)
            editor.commit()


        }
        buttonPositive.setOnClickListener { v ->
            if (gbug) {

                reportBug(
                    mContext, "Feedback/Suggestion", feedback, ratingme
                )
                hideDialog()

            } else {
                if (ratingme >= 4 && ratingme <= 5) {
                    setBooleanValue(mContext, isRatting, true)
                    setBooleanValue(mContext, isRating, false)
                    setBooleanValue(mContext, appRated, false)
                    setBooleanValue(mContext, appRatedPermently, false)
                    showRate(mContext)
                    hideDialog()
                } else if (ratingme == 0f) {
                    mContext?.getString(R.string.rate_app_zero_star_error)?.let {
                        Snackbar.make(
                            dialogView, it, Snackbar.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    setBooleanValue(mContext, isRatting, true)
                    showRate(mContext)
                    hideDialog()
                }
            }
        }
        btn_close.setOnClickListener { v ->

            setBooleanValue(this, ExitNativeAd, true)
            setBooleanValue(mContext, appRated, false)

            if (Constant.checkRating) {
//                dialog!!.dismiss()
                hideDialog()
                Constant.checkRating = false
                Log.d("Dig_Exit", "RateMe: 1")
            } else {
                Log.d("Dig_Exit", "RateMe: 2")
                finishAffinity()
//            hideDialog()
                if (isFromBack) {
                    setBooleanValue(mContext, isRatting, false)
                    val startMain = Intent(Intent.ACTION_MAIN)
                    startMain.addCategory(Intent.CATEGORY_HOME)
                    startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(startMain)
                    finish()
                }
            }
        }

        setBooleanValue(this, ExitNativeAd, true)
        setBooleanValue(mContext, appRated, false)
        dialog = AlertDialog.Builder(mContext).setView(dialogView).setCancelable(true).show()
        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }


    fun showRate(mContext: Context?) {
        try {
            if (mContext != null) {
                mContext.startActivity(
                    Intent(
                        Intent.ACTION_VIEW, Uri.parse("market://details?id=" + mContext.packageName)
                    )
                )
            }
        } catch (e: ActivityNotFoundException) {
            if (mContext != null) {
                mContext.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=" + mContext.packageName)
                    )
                )
            }
        }
    }

    private fun hideDialog() {
        if (dialog != null) {
            if (dialog!!.isShowing) {
                dialog!!.dismiss()
            }
            dialog = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        hideDialog()
    }

    companion object {
        private const val TAG = "DashboardActivity"
        private const val REQUEST_ACCOUNT_PICKER = 2011

    }

    fun reportBug(
        context: Context?, subject: String, errorMessage: String, ratingValue: Float
    ) {
        Open_App_Manager.doNotDisplayAds = true
        var subject = subject
        var pInfo: PackageInfo? = null
        try {
            if (context != null) {
                pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        val version: String = pInfo!!.versionName
        val deviceName: String = Build.MANUFACTURER.toString() + " " + Build.MODEL
        val osVersion: String = Build.VERSION.RELEASE
        val osAPI: Int = Build.VERSION.SDK_INT
        var country = ""
        try {
            val tm: TelephonyManager =
                context?.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            var countryCode = ""
            if (tm != null) {
                countryCode = tm.networkCountryIso
            }
            val loc = Locale("", countryCode)
            country = loc.displayCountry
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val mapPermission: HashMap<String, Boolean> = HashMap()
        val mIterator: Iterator<Map.Entry<String, Boolean>> = mapPermission.entries.iterator()
        var mExtraText = ""
        while (mIterator.hasNext()) {
            val (key, value) = mIterator.next()
            if (key.trim { it <= ' ' }.length > 0) {
                mExtraText += """$key : ${if (value) "YES" else "NO"}
                    """
            }
        }
        val body = """
                   Your message: $errorMessage
                   
                   Rating :$ratingValue
                   Device Information - ${
            context?.resources?.getString(R.string.app_setting_name)
        }
                   Version : $version
                   Device Name : $deviceName
                   Android API : $osAPI
                   Android Version : $osVersion
                   Country : $country
                   """.trimIndent()
        if (context != null) {
            subject = subject + " " + context.resources.getString(R.string.app_setting_name)
        }
        try {
            if (context != null) {

                val intent = Intent.createChooser(
                    Intent(
                        Intent.ACTION_SENDTO, Uri.parse(
                            "mailto:" + context.resources.getString(R.string.feedback_email)
                                .toString() + "?cc=&subject=" + Uri.encode(subject).toString()
                                .toString() + "&body=" + Uri.encode(body)
                        )
                    ), context.getString(R.string.email_choose_from_client)
                )
                emailChooserLauncher.launch(intent)
            }
        } catch (ex: ActivityNotFoundException) {
        }
    }

    private val emailChooserLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Handler(Looper.getMainLooper()).postDelayed({
                Open_App_Manager.doNotDisplayAds = false
            }, 3000)
        }
}