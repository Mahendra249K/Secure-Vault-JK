package com.hidefile.secure.folder.vault.AdActivity


import android.app.Application
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ProcessLifecycleOwner
import com.facebook.ads.AudienceNetworkAds
import com.hidefile.secure.folder.vault.R
import com.hidefile.secure.folder.vault.cluecanva.HandleLyf
import com.hidefile.secure.folder.vault.cluecanva.RDbhp
import com.hidefile.secure.folder.vault.cluecanva.SupPref
import com.hidefile.secure.folder.vault.newmarketing.NetworkUtil
import com.socialbrowser.socialmedianetwork.allinoneplace.ads.appOpenLifeCycleChange
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig
import java.io.File

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        application = this
        AudienceNetworkAds.initialize(this)


//        val testDeviceIds = Arrays.asList("DD2E35E506D1C99B6F4D4146B7B7F0E4")
//        val configuration = RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build()
//        MobileAds.setRequestConfiguration(configuration)

        val openAppManager = Open_App_Manager(this)

        val appLifecycleObserver = AppLifecycleObserver(object : appOpenLifeCycleChange {
            override fun onForeground() {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {

                    openAppManager.appInForeground()


                }
            }

            override fun onBackground() {}
        })

        ProcessLifecycleOwner.get().lifecycle.addObserver(appLifecycleObserver)



        TEMP_COPY_FOLDER =
            filesDir.toString() + File.separator + "Share" + File.separator + "Content"
        registerActivityLifecycleCallbacks(HandleLyf())
        RDbhp.getInstance(applicationContext)

        sharedPreferences = getSharedPreferences(packageName, 0)

        NetworkUtil.initNetwork(this)

        val config: YandexMetricaConfig =
            YandexMetricaConfig.newConfigBuilder(resources.getString(R.string.AppMetrica_id))
                .build()
        YandexMetrica.activate(applicationContext, config)
        YandexMetrica.enableActivityAutoTracking(this)

        if (SupPref.getBooleanValue(this, SupPref.isDarkModeOn, false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            SupPref.setBooleanValue(this, SupPref.isDarkModeOn, false)
        }
    }

    companion object {
        var sharedPreferences: SharedPreferences? = null
        var TEMP_COPY_FOLDER: String? = null
        var APPLICATION_ID: String = "com.hidefile.secure.folder.vault"

        var application: MyApplication? = null
    }
}