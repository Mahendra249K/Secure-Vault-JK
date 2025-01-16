package com.hidefile.secure.folder.vault.cluecanva

import android.content.Context
import android.preference.PreferenceManager

object PreUpyogi {
    const val PREF_SELECTED_LANGUAGE = "PreUpyogi_Bhasha"


    @JvmStatic
    fun getLanguageCode(context: Context?): String? {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getString(PREF_SELECTED_LANGUAGE, "")
    }

    @JvmStatic
    fun setLanguageCode(context: Context?, selectedLanguageCode: String?) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putString(PREF_SELECTED_LANGUAGE, selectedLanguageCode)
        editor.apply()
    }
}