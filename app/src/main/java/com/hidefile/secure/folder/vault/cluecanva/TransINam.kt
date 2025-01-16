package com.hidefile.secure.folder.vault.cluecanva

import android.app.Activity
import android.content.ComponentName
import android.content.pm.PackageManager
import java.lang.Exception

class TransINam(builder: Builder) {
    var unActiveNames: List<String>?
    var activeName: String?
    var packageName: String?
    private val activity: Activity
    fun setNow() {
        activity.packageManager.setComponentEnabledSetting(
            ComponentName(activity, activeName!!),
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP
        )
        for (i in unActiveNames!!.indices) {
            try {
                activity.packageManager.setComponentEnabledSetting(
                    ComponentName(activity, unActiveNames!![i]),
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    class Builder(val activity: Activity) {
        var disableNames: List<String>? = null
        var activeName: String? = null
        var packageName: String? = null
        fun disableNames(disableNamesl: List<String>?): Builder {
            disableNames = disableNamesl
            return this
        }

        fun activeName(activeName: String?): Builder {
            this.activeName = activeName
            return this
        }

        fun packageName(packageName: String?): Builder {
            this.packageName = packageName
            return this
        }

        fun build(): TransINam {
            return TransINam(this)
        }
    }

    init {
        unActiveNames = builder.disableNames
        activity = builder.activity
        activeName = builder.activeName
        packageName = builder.packageName
    }
}