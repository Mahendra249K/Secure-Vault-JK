package com.hidefile.secure.folder.vault.dashex

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.EditorInfo
import android.webkit.*
import android.widget.*
import androidx.cardview.widget.CardView
import com.hidefile.secure.folder.vault.R
import com.hidefile.secure.folder.vault.cluecanva.RDbhp
import com.hidefile.secure.folder.vault.cluecanva.TillsPth
import com.hidefile.secure.folder.vault.cluecanva.WvNest

class MyWebView :FoundationActivity(), View.OnClickListener {
    var web_tv_tital: TextView? = null
    var webActSearch: EditText? = null
    var webViews: WvNest? = null
    var progressBar: ProgressBar? = null
    var lin_tital: LinearLayout? = null
    var lin_etital: LinearLayout? = null
    var RDbhp: RDbhp? = null
    var ic_next: ImageView? = null
    var my_bookmark: ImageView? = null
    var ic_previous: ImageView? = null
    var iv_home: ImageView? = null
    var againIcClose: ImageView? = null
    var ivSearch: ImageView? = null
    var ivClear: ImageView? = null
    var card_tital: CardView? = null
    var isOpened = false
    private var idBanner= ""
    var mContext: Context? = null
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        requestWindowFeature(Window.FEATURE_NO_TITLE)
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            WindowManager.LayoutParams.FLAG_FULLSCREEN
//        )
        setContentView(R.layout.my_web)





        mContext = this@MyWebView
        RDbhp = com.hidefile.secure.folder.vault.cluecanva.RDbhp.getInstance(mContext)
        Init()


    }




    private fun Init() {
        againIcClose = findViewById(R.id.againIcClose)
        ivSearch = findViewById(R.id.ivSearch)
        ivClear = findViewById(R.id.ivClear)
        my_bookmark = findViewById(R.id.iv_my_bookmark)
        ic_next = findViewById(R.id.ic_next)
        ic_previous = findViewById(R.id.ic_previous)
        lin_tital = findViewById(R.id.lin_tital)
        card_tital = findViewById(R.id.card_tital)
        webViews = findViewById(R.id.webview)
        progressBar = findViewById(R.id.pbLoader)
        lin_tital?.setOnClickListener(this)
        lin_etital = findViewById(R.id.lin_etital)
        web_tv_tital = findViewById(R.id.tv_tital)
        webActSearch = findViewById(R.id.webActSearch)
        iv_home = findViewById(R.id.iv_hhd)
        webActSearch?.setOnClickListener(View.OnClickListener { v: View? ->
            againIcClose?.setVisibility(
                View.VISIBLE
            )
        })
        webActSearch?.setOnEditorActionListener(TextView.OnEditorActionListener { v: TextView?, actionId: Int, event: KeyEvent? ->
            val query = webActSearch?.getText().toString().trim { it <= ' ' }
            if (actionId == EditorInfo.IME_ACTION_GO) {
                goSearch()
            }
            true
        })
        webActSearch?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (webActSearch?.getText()?.length == 0) {
                    againIcClose?.setVisibility(View.VISIBLE)
                    ivClear?.setVisibility(View.GONE)
                    ivSearch?.setVisibility(View.GONE)
                } else if (webActSearch?.getText()?.length!! > 0) {
                    ivClear?.setVisibility(View.VISIBLE)
                    ivSearch?.setVisibility(View.VISIBLE)
                    againIcClose?.setVisibility(View.GONE)
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        lin_tital?.setOnClickListener(this)
        card_tital?.setOnClickListener(this)
        web_tv_tital?.setOnClickListener(this)
        againIcClose?.setOnClickListener(this)
        ivSearch?.setOnClickListener(this)
        ivClear?.setOnClickListener(this)
        my_bookmark?.setOnClickListener(this)
        ic_previous?.setOnClickListener(this)
        iv_home?.setOnClickListener(this)
        ic_next?.setOnClickListener(this)
        setListnerToRootView()
        initWeb()
    }





    override fun onStop() {
        super.onStop()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_HOME) {
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun initWeb() {
        webViews!!.webViewClient = MkWebViewClient()
        webViews!!.webChromeClient = MkWebChromeClient()
        val settings = webViews!!.settings
        settings.javaScriptEnabled = true
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        settings.setSupportZoom(true)
        settings.builtInZoomControls = true
        settings.displayZoomControls = false
        settings.cacheMode = WebSettings.LOAD_DEFAULT
        settings.allowFileAccess = true
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.loadsImagesAutomatically = true
        settings.defaultTextEncodingName = "utf-8"
        settings.domStorageEnabled = true
        settings.userAgentString = MOBILE_USER_AGENT
        settings.pluginState = WebSettings.PluginState.ON
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        val Query = intent.getStringExtra("Query")

        if (TillsPth.isNetworkAvailable(mContext)) webViews!!.loadUrl(
            Query!!
        )
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.card_tital, R.id.lin_tital, R.id.tv_tital -> lin_etital!!.visibility =
                View.VISIBLE
            R.id.ivClear -> webActSearch!!.setText("")
            R.id.againIcClose -> {
                //                lin_bgview.setVisibility(View.GONE);
                lin_tital!!.visibility = View.VISIBLE
                lin_etital!!.visibility = View.GONE
            }
            R.id.ivSearch -> goSearch()
            R.id.iv_my_bookmark -> {
                RDbhp = com.hidefile.secure.folder.vault.cluecanva.RDbhp.getInstance(mContext)
                if (RDbhp!!.isExist(webViews!!.url!!.trim { it <= ' ' })) {
                    Toast.makeText(this, "Entry already exist in bookmarks.", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    RDbhp?.addBookmark(
                        webViews!!.title,
                        webViews!!.url!!.trim { it <= ' ' },
                        "encrypted_userName",
                        "encrypted_userPW",
                        "01"
                    )
                    Toast.makeText(this, R.string.lable_bookmark_saved, Toast.LENGTH_SHORT).show()
                    my_bookmark!!.setImageResource(R.drawable.ic_bookmark_black_24dp)
                }
                RDbhp?.close()
            }
            R.id.ic_previous -> webViews!!.goBack()
            R.id.ic_next -> webViews!!.goForward()
            R.id.iv_hhd -> finish()
        }
    }

    fun goSearch() {
        if (webActSearch!!.text.length > 0) {
            if (TillsPth.isNetworkAvailable(mContext)) {
                webViews!!.loadUrl("https://www.google.com/search?q=" + webActSearch!!.text.toString())
                lin_tital!!.visibility = View.VISIBLE
                lin_etital!!.visibility = View.GONE
            }
        }
    }

    fun setListnerToRootView() {
        val activityRootView = window.decorView.findViewById<View>(android.R.id.content)
        activityRootView.viewTreeObserver.addOnGlobalLayoutListener {
            val heightDiff = activityRootView.rootView.height - activityRootView.height
            if (heightDiff > 100) {
                lin_etital!!.visibility = View.VISIBLE
                if (isOpened == false) {
                }
                isOpened = true
            } else if (isOpened == true) {
                lin_etital!!.visibility = View.GONE
                isOpened = false
            }
        }
    }

    override fun onBackPressed() {
        if (webViews!!.canGoBack()) {
            webViews!!.goBack()
        } else {
            super.onBackPressed()
        }
    }

    override fun onPause() {
        super.onPause()
        try {
            webViews!!.javaClass.getMethod("onPause").invoke(webViews)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        try {
            webViews!!.javaClass.getMethod("onResume").invoke(webViews)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private inner class MkWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            if (url == null) {
                return true
            }
            if (url.startsWith(HTTP) || url.startsWith(HTTPS)) {
                if (TillsPth.isNetworkAvailable(mContext)) {
                    view.loadUrl(url)
                }
                return true
            }
            return try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
                true
            } catch (e: Exception) {
                true
            }
        }

        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            progressBar!!.progress = 0
            progressBar!!.visibility = View.VISIBLE
            web_tv_tital!!.text = "Loading..."
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            progressBar!!.visibility = View.GONE
            title = webViews!!.title
            web_tv_tital!!.text = webViews!!.title
            lin_etital!!.visibility = View.GONE
            lin_tital!!.visibility = View.VISIBLE
            webActSearch!!.setText(url)
            RDbhp = com.hidefile.secure.folder.vault.cluecanva.RDbhp.getInstance(mContext)
            if (RDbhp!!.isExist(webViews!!.url!!.trim { it <= ' ' })) {
                my_bookmark!!.setImageResource(R.drawable.ic_bookmark_black_24dp)
            } else {
                my_bookmark!!.setImageResource(R.drawable.ic_bookmark_border_black_24dp)
            }
            RDbhp?.close()
        }

        override fun onReceivedError(
            view: WebView,
            request: WebResourceRequest,
            error: WebResourceError
        ) {
            super.onReceivedError(view, request, error)
        }
    }

    private inner class MkWebChromeClient : WebChromeClient() {
        override fun onProgressChanged(view: WebView, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            progressBar!!.progress = newProgress
            if (newProgress > 0) {
                if (newProgress == Companion.WEB_PROGRESS_MAX) {
                    progressBar!!.visibility = View.GONE
                } else {
                    progressBar!!.visibility = View.VISIBLE
                }
            }
        }

        override fun onReceivedIcon(view: WebView, icon: Bitmap) {
            super.onReceivedIcon(view, icon)
        }

        override fun onReceivedTitle(view: WebView, title: String) {
            super.onReceivedTitle(view, title)
            setTitle(title)
            web_tv_tital!!.text = title
        }

    }

    companion object {
        private const val WEB_PROGRESS_MAX = 100
        private const val MOBILE_USER_AGENT =
            "Mozilla/8.0 (Linux; U; Android 4.4; en-us; Nexus 4 Build/JOP24G) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30"
        private const val HTTP = "http://"
        private const val HTTPS = "https://"
    }
}