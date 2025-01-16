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
import android.telephony.TelephonyManager
import android.util.Log
import android.view.*
import android.widget.*
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
import com.hidefile.secure.folder.vault.AdActivity.SharedPref
import com.hidefile.secure.folder.vault.R
import com.hidefile.secure.folder.vault.cluecanva.*
import com.hidefile.secure.folder.vault.cluecanva.SupPref.*
import java.io.File
import java.util.*


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
    var arrAppTitle =
        arrayOf("Google", "YouTube", "Facebook", "Twitter", "Instagram")
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
        iv_daynight_theme=findViewById(R.id.iv_daynight_theme)
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
                        val sharingIntent =
                            Intent(Intent.ACTION_SEND)
                        sharingIntent.type = "text/plain"
                        val shareBody =
                            "Download the Hide File & Vault Secure app from play store https://play.google.com/store/apps/details?id=$packageName"
                        sharingIntent.putExtra(
                            Intent.EXTRA_SUBJECT,
                            getString(R.string.app_name)
                        )
                        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
                        startActivity(Intent.createChooser(sharingIntent, "Share via"))
                    }
                    R.id.menu_bord_rate ->{
                        RateMe(false)
                        Constant.checkRating = true;
                    }
                    R.id.menu_bord_policy -> {
                        startActivity(Intent(
                            mContext,
                            PolicyShow::class.java)
                        )
                    }
                }
                return@setOnMenuItemClickListener false
            }
            popupMenu.show()
        })

        llSetting?.setOnClickListener { v: View? ->
            Common_Adm.getInstance();
            Common_Adm.mInterstitialAdClickCount++
            Common_Adm.getInstance()
                .loadOrShowAdmInterstial(true, this@BordMain, object : Call_Back_Ads {
                    override fun onAdsClose() {
                        val intent = Intent(this@BordMain, Configuration::class.java)
                        startActivity(intent)
                    }

                    override fun onLoading() {
                        val intent = Intent(this@BordMain,Configuration::class.java)
                        startActivity(intent)
                    }

                    override fun onAdsFail() {
                        val intent = Intent(this@BordMain, Configuration::class.java)
                        startActivity(intent)
                    }
                })

        }
        llMyPhoto?.setOnClickListener { v: View? ->

            Common_Adm.getInstance();
            Common_Adm.mInterstitialAdClickCount++
            Common_Adm.getInstance()
                .loadOrShowAdmInterstial(true, this@BordMain, object : Call_Back_Ads {
                    override fun onAdsClose() {
                        val intent = Intent(this@BordMain, Prasth::class.java)
                        intent.putExtra("Type", "Photos")
                        startActivity(intent)
                    }

                    override fun onLoading() {
                        val intent = Intent(this@BordMain,Prasth::class.java)
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

            if (SupPref.getBooleanValue(mContext, isDarkModeOn,true)) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                SupPref.setBooleanValue(mContext, isDarkModeOn, false)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // Set the status bar text color to dark
                    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                }

            }
            else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                SupPref.setBooleanValue(mContext, isDarkModeOn, true)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    getWindow().getDecorView().setSystemUiVisibility(0);
                }
            }
        }
        llVideo?.setOnClickListener { v: View? ->

            Common_Adm.getInstance();
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
            Common_Adm.getInstance();
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
            Common_Adm.getInstance();
            Common_Adm.mInterstitialAdClickCount++
            Common_Adm.getInstance()
                .loadOrShowAdmInterstial(true, this@BordMain, object : Call_Back_Ads {
                    override fun onAdsClose() {
                        val intent = Intent(this@BordMain, MyBrowser::class.java)
                        startActivity(intent)
                    }
                    override fun onLoading() {
                        val intent = Intent(this@BordMain,MyBrowser::class.java)
                        startActivity(intent)
                    }
                    override fun onAdsFail() {
                        val intent = Intent(this@BordMain,MyBrowser::class.java)
                        startActivity(intent)
                    }
                })
        }
        llHiddenSelfie?.setOnClickListener { v: View? ->
            Common_Adm.getInstance();
            Common_Adm.mInterstitialAdClickCount++
            Common_Adm.getInstance()
                .loadOrShowAdmInterstial(true, this@BordMain, object : Call_Back_Ads {
                    override fun onAdsClose() {
                        val intent = Intent(this@BordMain,Image::class.java)
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
        if (SupPref.getBooleanValue(mContext, SupPref.isFirstTime, true)) {
            val appList: LinkedHashMap<String, RecentList> = LinkedHashMap()
            for (i in arrAppTitle.indices) {
                appList[arrAppLinks[i]] =
                    RecentList(arrAppIcons1[i], arrAppLinks[i], arrAppTitle[i])
            }
            SupPref.saveAppData(applicationContext, appList)
            SupPref.setBooleanValue(mContext, SupPref.isFirstTime, false)
        }
    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_ACCOUNT_PICKER -> if (resultCode == Activity.RESULT_OK && data != null && data.getExtras() != null) {
                val accountName: String? = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME)
                if (accountName != null && !accountName.trim { it <= ' ' }.isEmpty()) {
                    SupPref.putValue(mContext, SupPref.AccountName, accountName)
                    credential!!.selectedAccountName = accountName
                    dbHelper!!.updateUserEmail(accountName)
                }
            }
            479 -> if (resultCode == Activity.RESULT_OK && data != null) {
                val treeUri: Uri? = data.getData()
                if (treeUri != null) {
                    applicationContext.contentResolver.takePersistableUriPermission(
                        treeUri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    )
                }
                SupPref.setBooleanValue(this@BordMain, "sdcardapproved", true)
                SupPref.putValue(
                    this@BordMain,
                    "sdcarduri",
                    treeUri.toString().toString() + ""
                )
            }
            362 -> if (resultCode == Activity.RESULT_OK && data != null) {
                val treeUri: Uri? = data.getData()
            }
        }
    }



    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    override fun onResume() {
        super.onResume()
        SharedPref.AppOpenShow = false
        Log.d("Message","Resume");

        if (SupPref.getBooleanValue(this@BordMain, SupPref.isFromPin, false)) {
            SupPref.setBooleanValue(this@BordMain, SupPref.isFromPin, false)
        }

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
        isActivityPause = true
        SharedPref.AppOpenShow = false
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    override fun onStop() {
        super.onStop()
        Log.d("Message","stop");
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

        if (SupPref.getBooleanValue(mContext,appRated,true) && SupPref.getBooleanValue(mContext,appRatedPermently,true)) {
            RateMe(false);
        } else if (SupPref.getBooleanValue(mContext, ExitNativeAd, true) || SupPref.getBooleanValue(mContext, ExitNativeAdAfterAd, true)) {
            val exitDialogNative = ExitDialogNative()
            exitDialogNative.show(supportFragmentManager, exitDialogNative.getTag())
//            this@BordMain.finishAffinity()
           finish()
//            System.exit(0)


        }
    }

    private fun RateMe(isFromBack: Boolean) {
        hideDialog()

        val dialogView: View =
            LayoutInflater.from(mContext).inflate(R.layout.dig_fdbk, null)
        val buttonPositive: TextView = dialogView.findViewById(R.id.btn_rate_now) as TextView
        val lay_feedback: LinearLayout = dialogView.findViewById(R.id.lay_feedback) as LinearLayout
        val txtfeedback: EditText = dialogView.findViewById(R.id.txtfeedback) as EditText
        val btn_close: ImageView = dialogView.findViewById(R.id.btn_close) as ImageView
        val rate_1: ImageView = dialogView.findViewById(R.id.rate_1) as ImageView
        val rate_2: ImageView = dialogView.findViewById(R.id.rate_2) as ImageView
        val rate_3: ImageView = dialogView.findViewById(R.id.rate_3) as ImageView
        val rate_4: ImageView = dialogView.findViewById(R.id.rate_4) as ImageView
        val rate_5: ImageView = dialogView.findViewById(R.id.rate_5) as ImageView
        val color = 0xFFFFFF
        val drawable: Drawable = ColorDrawable(color)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            rate_1.setForeground(drawable)
            rate_2.setForeground(drawable)
            rate_3.setForeground(drawable)
            rate_4.setForeground(drawable)
            rate_5.setForeground(drawable)
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
                rate_1.setForeground(null)
                rate_2.setForeground(drawable)
                rate_3.setForeground(drawable)
                rate_4.setForeground(drawable)
                rate_5.setForeground(drawable)
            }
            gbug = true
            buttonPositive.setText("Submit")


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
                rate_1.setForeground(null)
                rate_2.setForeground(null)
                rate_3.setForeground(drawable)
                rate_4.setForeground(drawable)
                rate_5.setForeground(drawable)
            }
            gbug = true

              buttonPositive.setText("Submit")
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
                rate_1.setForeground(null)
                rate_2.setForeground(null)
                rate_3.setForeground(null)
                rate_4.setForeground(drawable)
                rate_5.setForeground(drawable)
            }
            gbug = true
               buttonPositive.setText("Submit")
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
                rate_1.setForeground(null)
                rate_2.setForeground(null)
                rate_3.setForeground(null)
                rate_4.setForeground(null)
                rate_5.setForeground(drawable)
            }
            gbug = false
            lay_feedback.setVisibility(View.GONE)
            buttonPositive.setText("Rate Now")
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
                rate_1.setForeground(null)
                rate_2.setForeground(null)
                rate_3.setForeground(null)
                rate_4.setForeground(null)
                rate_5.setForeground(null)
            }
            gbug = false
            lay_feedback.setVisibility(View.GONE)
            buttonPositive.setText("Rate Now")
            val sharedPreferences = getSharedPreferences(SharedPref.PREF_NAME, MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putBoolean(SharedPref.RATING_STATUS, true)
            editor.commit()



        }
        buttonPositive.setOnClickListener { v ->
            if (gbug) {

                    reportBug(
                        mContext,
                        "Feedback/Suggestion",
                        feedback,
                        ratingme
                    )
                    hideDialog()

            } else {
                if (ratingme >= 4 && ratingme <= 5) {
                    SupPref.setBooleanValue(mContext, SupPref.isRatting, true)
                    SupPref.setBooleanValue(mContext, SupPref.isRating, false)
                    SupPref.setBooleanValue(mContext, appRated, false)
                    SupPref.setBooleanValue(mContext, appRatedPermently, false)
                    showRate(mContext)
                    hideDialog()
                } else if (ratingme == 0f) {
                    mContext?.getString(R.string.rate_app_zero_star_error)?.let {
                        Snackbar.make(
                            dialogView,
                            it,
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    SupPref.setBooleanValue(mContext, SupPref.isRatting, true)
                    showRate(mContext)
                    hideDialog()
                }
            }
        }
        btn_close.setOnClickListener { v ->

            SupPref.setBooleanValue(this,ExitNativeAd,true)
            SupPref.setBooleanValue(mContext, appRated, false)

            if (Constant.checkRating){
//                dialog!!.dismiss()
                hideDialog()
                Constant.checkRating = false
                Log.d("Dig_Exit", "RateMe: 1")
            }
            else{
                Log.d("Dig_Exit", "RateMe: 2")
                finishAffinity()
//            hideDialog()
                if (isFromBack) {
                    SupPref.setBooleanValue(mContext, SupPref.isRatting, false)
                    val startMain = Intent(Intent.ACTION_MAIN)
                    startMain.addCategory(Intent.CATEGORY_HOME)
                    startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(startMain)
                    finish()
                }
            }
        }

        SupPref.setBooleanValue(this,ExitNativeAd,true)
        SupPref.setBooleanValue(mContext, appRated, false)
        dialog = AlertDialog.Builder(mContext)
            .setView(dialogView)
            .setCancelable(true)
            .show()
        dialog!!.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }




    fun showRate(mContext: Context?) {
        try {
            if (mContext != null) {
                mContext.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=" + mContext.getPackageName())
                    )
                )
            }
        } catch (e: ActivityNotFoundException) {
            if (mContext != null) {
                mContext.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=" + mContext.getPackageName())
                    )
                )
            }
        }
    }

    private fun hideDialog() {
        if (dialog != null) {
            if (dialog!!.isShowing()) {
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
        fun reportBug(
            context: Context?,
            subject: String,
            errorMessage: String,
            ratingValue: Float
        ) {
            var subject = subject
            var pInfo: PackageInfo? = null
            try {
                if (context != null) {
                    pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0)
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
                    countryCode = tm.getNetworkCountryIso()
                }
                val loc = Locale("", countryCode)
                country = loc.getDisplayCountry()
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
                   Device Information - ${context?.getResources()?.getString(R.string.app_name)}
                   Version : $version
                   Device Name : $deviceName
                   Android API : $osAPI
                   Android Version : $osVersion
                   Country : $country
                   """.trimIndent()
            if (context != null) {
                subject = subject + " " + context.getResources().getString(R.string.app_name)
            }
            try {
                if (context != null) {
                    context.startActivity(
                        Intent.createChooser(
                            Intent(
                                Intent.ACTION_SENDTO,
                                Uri.parse(
                                    "mailto:" + context.getResources().getString(R.string.feedback_email)
                                        .toString() + "?cc=&subject=" + Uri.encode(subject).toString()
                                        .toString() + "&body=" + Uri.encode(body)
                                )
                            ), context.getString(R.string.email_choose_from_client)
                        )
                    )
                }
            } catch (ex: ActivityNotFoundException) {
            }
        }
    }
}