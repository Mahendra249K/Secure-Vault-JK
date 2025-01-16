package com.hidefile.secure.folder.vault.dashex

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.hidefile.secure.folder.vault.R
import com.hidefile.secure.folder.vault.cluecanva.EntryAux
import com.hidefile.secure.folder.vault.cluecanva.RecentList
import com.hidefile.secure.folder.vault.cluecanva.SupPref
import com.hidefile.secure.folder.vault.cluecanva.VTv
import java.util.*

class NewAppAddition : FoundationActivity(), View.OnClickListener {
//    var btn_add: Button? = null
    var btn_addUrl: ImageView? = null
    var mContext: Context? = null
    var et_url: TextInputEditText? = null
    var et_tital: TextInputEditText? = null

//

//


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_app_addition)
        mContext = this@NewAppAddition

//        Common_Adm.getInstance().AmNativeLoad(this@NewAppAddition,
//            findViewById<View>(R.id.llNativeLarge) as ViewGroup?, true)


        val tv_tital = findViewById<VTv>(R.id.tv_tital)
        tv_tital.text = "Add Bookmarks"
        val iv_back = findViewById<ImageView>(R.id.iv_back)
        iv_back.setOnClickListener { v: View? -> onBackPressed() }
        findViewById<View>(R.id.iv_option).visibility = View.GONE
        findViewById<View>(R.id.Save_photovideo).visibility = View.VISIBLE

        Init()

    }




    private fun Init() {
        et_tital = findViewById(R.id.new_app_et_tital)
        et_url = findViewById(R.id.new_app_et_url)
//        btn_add = findViewById(R.id.new_app_add_btn)
        btn_addUrl = findViewById(R.id.Save_photovideo)
//        btn_add!!.setOnClickListener(this)
        btn_addUrl!!.setOnClickListener(this)
        findViewById<View>(R.id.iv_back).setOnClickListener { v: View? -> finish() }
    }
    override fun onClick(v: View) {
        when (v.id) {
            R.id.Save_photovideo -> {
                val url = et_url!!.text.toString().lowercase(Locale.getDefault()).trim { it <= ' ' }
                if (et_tital!!.text.toString().trim { it <= ' ' }
                        .isEmpty() || et_tital!!.length() <= 0 || et_tital == null) {
                    Toast.makeText(applicationContext, "Enter website title", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    if (isValidUrl(url)) {
                        hideSoftInput(btn_addUrl)
                        val dataMap = SupPref.getAppDataMap(mContext)
                        if (dataMap.containsKey(url)) {
                            EntryAux.showToast(mContext, "Repeated URL")
                        } else {
                            if (url.contains("https://") || url.contains("http://")) dataMap[url] =
                                RecentList(
                                    "ic_web",
                                    url,
                                    et_tital!!.text.toString()
                                        .trim { it <= ' ' }) else dataMap["https://$url"] =
                                RecentList(
                                    "ic_web",
                                    "https://$url",
                                    et_tital!!.text.toString().trim { it <= ' ' })
                            SupPref.saveAppData(mContext, dataMap)
                            EntryAux.showToast(mContext, "Add new url successfully.")
                            setResult(Activity.RESULT_OK)
                            finish()
                        }
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Enter valid website url",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            else -> {}
        }
    }
    private fun hideSoftInput(view: ImageView?) {
        view!!.clearFocus()
        val imm = (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
    private fun isValidUrl(url: String): Boolean {
        val p = Patterns.WEB_URL
        val m = p.matcher(url.lowercase(Locale.getDefault()))
        return m.matches()
    }
}