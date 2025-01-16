package com.hidefile.secure.folder.vault.dashex

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.hidefile.secure.folder.vault.R
import com.hidefile.secure.folder.vault.cluecanva.TillsPth

class PolicyShow : AppCompatActivity() {
    var web_browser: WebView? = null
    var ic_back: ImageView? = null
    var header_txt: TextView? = null
    var tv_try_again: TextView? = null
    var pbar: ProgressBar? = null
    var ll_noInternet: RelativeLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.policy_show)
        findViewById<View>(R.id.iv_option).visibility = View.GONE
        pbar = findViewById(R.id.pbar)
        tv_try_again = findViewById(R.id.tv_try_again)
        ll_noInternet = findViewById(R.id.lay_noInternet)
        web_browser = findViewById(R.id.browser)
        header_txt = findViewById<View>(R.id.tv_tital) as TextView
        header_txt!!.text = "Privacy Policy"
        ic_back = findViewById(R.id.iv_back)
        if (TillsPth.isNetworkAvailable(this@PolicyShow)) {
            ll_noInternet?.setVisibility(View.GONE)
        } else {
            ll_noInternet?.setVisibility(View.VISIBLE)
        }
        ic_back?.setOnClickListener(View.OnClickListener { view: View? -> onBackPressed() })
        tv_try_again?.setOnClickListener(View.OnClickListener { view: View? ->
            if (TillsPth.isNetworkAvailable(this@PolicyShow)) {
                onResume()
            } else {
                ll_noInternet?.setVisibility(View.GONE)
                Handler().postDelayed({ ll_noInternet?.setVisibility(View.VISIBLE) }, 200)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        if (TillsPth.isNetworkAvailable(this@PolicyShow)) {
            web_browser!!.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                    view.loadUrl(url)
                    return false
                }

                override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    pbar!!.visibility = View.VISIBLE
                    ll_noInternet!!.visibility = View.GONE
                }

                override fun onLoadResource(view: WebView, url: String) {}
                override fun onPageFinished(view: WebView, url: String) {
                    pbar!!.visibility = View.GONE
                }

                override fun onReceivedError(
                    view: WebView,
                    request: WebResourceRequest,
                    error: WebResourceError
                ) {
                    super.onReceivedError(view, request, error)
                    ll_noInternet!!.visibility = View.VISIBLE
                }
            }
            web_browser!!.settings.javaScriptEnabled = true
            web_browser!!.settings.loadsImagesAutomatically = true
            web_browser!!.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
            web_browser!!.scrollTo(0, 0)
            web_browser!!.loadUrl(getString(R.string.privacy_policy))
        } else ll_noInternet!!.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}