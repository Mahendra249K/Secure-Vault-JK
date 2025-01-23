package com.hidefile.secure.folder.vault.dashex


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION
import android.os.Bundle
import android.os.Environment
import android.os.storage.StorageManager
import android.provider.Settings
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.annotation.RequiresApi
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.calldorado.Calldorado
import com.calldorado.Calldorado.ColorElement
import com.hidefile.secure.folder.vault.AdActivity.SharedPref
import com.hidefile.secure.folder.vault.R
import com.hidefile.secure.folder.vault.calldorado.OverlayPermissionManager
import com.hidefile.secure.folder.vault.cluecanva.HandPrmt
import com.hidefile.secure.folder.vault.cluecanva.HandPrmt.OnListener
import com.hidefile.secure.folder.vault.cluecanva.SupPref
import java.io.File


class PermitAccess : FoundationActivity(), View.OnClickListener {
    private val REQUEST_HIDE_PHOTOS = 1996
    var resultLauncher: ActivityResultLauncher<Intent>? = null
    var btnGrant: Button? = null
    var tvDontAllow: TextView? = null
    var mTvPolicy: TextView? = null
    var idNative = ""


    @SuppressLint("MissingInflatedId")
    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.permit)
        SharedPref.AppOpenShow = true
        resultLauncher = registerForActivityResult(
            StartActivityForResult()
        ) { result: ActivityResult ->
            onRequestPermit()
        }
        init()
        setPrivacyPolicy()

        val cdoConditions = Calldorado.getAcceptedConditions(this)

        if (cdoConditions.containsKey(Calldorado.Condition.EULA)) {
            if (java.lang.Boolean.TRUE == cdoConditions[Calldorado.Condition.EULA]) {
                onBoardingSuccess()
            } else {
                val conditionsMap = HashMap<Calldorado.Condition, Boolean>()
                conditionsMap[Calldorado.Condition.EULA] = true
                conditionsMap[Calldorado.Condition.PRIVACY_POLICY] = true
                Calldorado.acceptConditions(this, conditionsMap)
                onBoardingSuccess()
            }
        }

        val colorMap = HashMap<ColorElement?, Int?>()
        colorMap[ColorElement.NavigationColor] = getColor(R.color.colorAccent)
        Calldorado.setCustomColors(this@PermitAccess, colorMap)
    }

    private fun onBoardingSuccess() {
        Calldorado.start(this)
    }


    private fun setPrivacyPolicy() {
        val spannableString = SpannableString("Privacy Policy")
        val foregroundSpan = ForegroundColorSpan(resources.getColor(R.color.colorPrimary))
        spannableString.setSpan(
            foregroundSpan, 0, spannableString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        mTvPolicy?.text = Html.fromHtml(
            "By continuing, you accept and approve the <a href='" + getString(
                R.string.privacy_policy_uri
            ) + "'>" + spannableString + "</a>"
        )
        mTvPolicy?.movementMethod = LinkMovementMethod.getInstance()

    }

    fun init() {
        mTvPolicy = findViewById(R.id.mTvPolicy)
        btnGrant = findViewById(R.id.allowPermissionBtn)
        btnGrant?.setOnClickListener(this)
        tvDontAllow?.setOnClickListener(this)
    }

    private fun onRequestPermit() {
        HandPrmt.getInstance().requestPermissions(this, object : OnListener {
            override fun onPermissionGranted() {
                moveDrawOverlays()
            }

            override fun onPermissionDenied() {}
            override fun onOpenSettings() {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                resultLauncher!!.launch(intent)
            }
        })
    }

    private fun movePswd() {
        val mainIntent = Intent(this@PermitAccess, Pswd::class.java)
        startActivity(mainIntent)
        finish()
    }

    private fun moveDrawOverlays() {
        val overlayPermissionManager = OverlayPermissionManager(this)
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse(
                "package:$packageName"
            )
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        if (!overlayPermissionManager.isGranted) {
            overlayPermissionManager.requestOverlay { overlayActivityResultLauncher.launch(intent) }
        } else {
            movePswd()
        }
    }


    var overlayActivityResultLauncher = registerForActivityResult(
        StartActivityForResult()
    ) { result: ActivityResult? ->
        if (Settings.canDrawOverlays(this@PermitAccess)) {
            OverlayPermissionManager.shouldContinueThread = false
            movePswd()
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_HIDE_PHOTOS && resultCode == RESULT_OK) {
            if (data != null && data.data != null) {
                val treeUri = data.data
                contentResolver.takePersistableUriPermission(
                    treeUri!!, Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
                SupPref.putHideUri(this, treeUri.toString())
                btnGrant!!.performClick()
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private fun onRequest() {
        val storeFile = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .toString() + "/.StayHide"
        )
        if (storeFile.exists() && storeFile.isDirectory) {
            if (SupPref.getburyUri(this) == null || SupPref.getburyUri(this)
                    .trim { it <= ' ' } == "" || !SupPref.getburyUri(this).endsWith(".StayHide")
            ) {
                askPermit(this@PermitAccess, Environment.DIRECTORY_PICTURES + "/.StayHide/")
            }
        } else {
            val success = storeFile.mkdir()
            if (success) {
                if (storeFile.exists() && storeFile.isDirectory) {
                    if (SupPref.getburyUri(this) == null || SupPref.getburyUri(this) == "" || !SupPref.getburyUri(
                            this
                        ).endsWith(".StayHide")
                    ) {
                        askPermit(
                            this@PermitAccess, Environment.DIRECTORY_PICTURES + "/.StayHide/"
                        )
                    }
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    fun askPermit(context: Activity, targetDirectory: String) {
        var targetDirectory = targetDirectory
        val storageManager = context.getSystemService(STORAGE_SERVICE) as StorageManager
        val intent = storageManager.primaryStorageVolume.createOpenDocumentTreeIntent()
        var uri = intent.getParcelableExtra<Uri>("android.provider.extra.INITIAL_URI")
        val hideUri = SupPref.getburyUri(this)
        if (hideUri.trim { it <= ' ' }.isEmpty()) {
            var scheme = uri.toString()
            scheme = scheme.replace("/root/", "/document/")
            targetDirectory.replace("/", "%2F").also { targetDirectory = it }
            scheme += "%3A$targetDirectory"
            uri = Uri.parse(scheme)
        } else {
            uri = Uri.parse(hideUri)
        }
        intent.putExtra("android.provider.extra.INITIAL_URI", uri)
        context.startActivityForResult(intent, REQUEST_HIDE_PHOTOS)
    }



    override fun onClick(v: View) {
        when (v.id) {
            R.id.allowPermissionBtn -> if (VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val hidden_URI = SupPref.getburyUri(this@PermitAccess)
                if (hidden_URI != null && hidden_URI.trim { it <= ' ' } != "") {
                    try {
                        contentResolver.takePersistableUriPermission(
                            Uri.parse(hidden_URI),
                            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                        )
                        onRequestPermit()
                    } catch (e: SecurityException) {
                        e.printStackTrace()
                        onRequest()
                    }
                } else {
                    onRequest()
                }
            } else {
                onRequestPermit()
            }
        }
    }
}