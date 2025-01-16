package com.hidefile.secure.folder.vault.dashex

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.hidefile.secure.folder.vault.R
import com.hidefile.secure.folder.vault.cluecanva.RDbhp
import com.hidefile.secure.folder.vault.cluecanva.TillsPth
import com.hidefile.secure.folder.vault.cluecanva.VTv
import com.hidefile.secure.folder.vault.dpss.MyBookMarkFtr
import com.hidefile.secure.folder.vault.dpss.PagerViewAdp
import com.hidefile.secure.folder.vault.dpss.RecentViewFtr


class MyBrowser : FoundationActivity(), View.OnClickListener {
    @JvmField
    var mainLlToolbar: LinearLayout? = null
    @JvmField
    var mainLlSearchView: LinearLayout? = null
    @JvmField
    var tabLayout: TabLayout? = null
    var mainLayout: LinearLayout? = null
    var bgview: LinearLayout? = null
    var mContext: Context? = null
    var againIcClose: ImageView? = null
    var ivSearch: ImageView? = null
    var ivClear: ImageView? = null
    var actvSearch: EditText? = null
    var prevMenuItem: MenuItem? = null
    private var idBanner= ""
    var isOpened = false
    var RDbhp: RDbhp? = null
    private var viewPager: ViewPager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        requestWindowFeature(Window.FEATURE_NO_TITLE)
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            WindowManager.LayoutParams.FLAG_FULLSCREEN
//        )
        setContentView(R.layout.browser_safe)
        mContext = this@MyBrowser

        createdBookMarks()

        val toolbar = findViewById<Toolbar>(R.id.main_toolbar)


        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            val tv_tital = findViewById<VTv>(R.id.tv_tital)
            tv_tital.setText(R.string.my_browser)
            val iv_back = findViewById<ImageView>(R.id.iv_back)
            iv_back.setOnClickListener { v: View? ->
                        onBackPressed()
            }
            findViewById<View>(R.id.iv_option).visibility = View.GONE
        }
        Init()

    }

    private fun createdBookMarks() {
        var name :String? =null
        var name1 :String? =null
        name ="Yahoo"
        name1 ="www.yahoo.com"

        RDbhp = com.hidefile.secure.folder.vault.cluecanva.RDbhp.getInstance(mContext)
        RDbhp?.addBookmark(
            name,
            name1.trim { it <= ' ' },
            "encrypted_userName",
            "encrypted_userPW",
            "01"
        )
        RDbhp?.close()

        RDbhp?.addBookmark(
            "reddit",
            "https://www.reddit.com/".trim { it <= ' ' },
            "encrypted_userName",
            "encrypted_userPW",
            "01"
        )
        RDbhp?.close()

        RDbhp?.addBookmark(
            "asura",
            "https://asura.gg/".trim { it <= ' ' },
            "encrypted_userName",
            "encrypted_userPW",
            "01"
        )
        RDbhp?.close()

        RDbhp?.addBookmark(
            "yandex",
            "https://dzen.ru/?yredirect=true".trim { it <= ' ' },
            "encrypted_userName",
            "encrypted_userPW",
            "01"
        )
        RDbhp?.close()
    }


    override fun onBackPressed() {
        super.onBackPressed()

    }

    private fun Init() {
        mainLlSearchView = findViewById(R.id.mainLlSearchView)
        bgview = findViewById(R.id.bgview)
        againIcClose = findViewById(R.id.againIcClose)
        ivSearch = findViewById(R.id.ivSearch)
        ivClear = findViewById(R.id.ivClear)
        actvSearch = findViewById(R.id.webActSearch)
        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewpager)
        tabLayout?.newTab()?.let { tabLayout?.addTab(it.setText("Recently Viewed")) }
        tabLayout?.newTab()?.let { tabLayout?.addTab(it.setText("My Bookmarks")) }
        mainLayout = findViewById(R.id.mainLayout)
        mainLlToolbar = findViewById(R.id.mainLlToolbar)
        tabLayout?.setTabGravity(TabLayout.GRAVITY_FILL)
        setupViewPager(viewPager)
        actvSearch?.setOnClickListener(View.OnClickListener { v: View? ->
            againIcClose?.setVisibility(View.VISIBLE)
            ivSearch?.setVisibility(View.GONE)
            ivClear?.setVisibility(View.GONE)
        })
        actvSearch?.setOnEditorActionListener(TextView.OnEditorActionListener { v: TextView?, actionId: Int, event: KeyEvent? ->
            val query = actvSearch?.getText().toString().trim { it <= ' ' }
            if (actionId == EditorInfo.IME_ACTION_GO) {
                goSearch()
            }
            true
        })
        actvSearch?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (actvSearch?.getText()!!.length == 0) {
                    againIcClose?.setVisibility(View.VISIBLE)
                    ivClear?.setVisibility(View.GONE)
                    ivSearch?.setVisibility(View.GONE)
                } else if (actvSearch?.getText()!!.length > 0) {
                    ivClear?.setVisibility(View.VISIBLE)
                    ivSearch?.setVisibility(View.VISIBLE)
                    againIcClose?.setVisibility(View.GONE)
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        againIcClose?.setOnClickListener(this)
        ivSearch?.setOnClickListener(this)
        ivClear?.setOnClickListener(this)
        tabLayout?.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager?.setCurrentItem(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        viewPager?.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {}

            override fun onPageSelected(position: Int) {
                if (prevMenuItem != null) {
                    prevMenuItem!!.isChecked = false
                } else {
                }
                val tab = tabLayout?.getTabAt(position)
                tab!!.select()
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    private fun setupViewPager(viewPager: ViewPager?) {
        val adapter = PagerViewAdp(supportFragmentManager)
        adapter.addFragment(RecentViewFtr())
        adapter.addFragment(MyBookMarkFtr())
        viewPager!!.adapter = adapter
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint() }
    override fun onUserInteraction() {
        super.onUserInteraction() }
    override fun onResume() {
        super.onResume() }
    override fun onPause() {
        super.onPause() }
    override fun onStop() {
        super.onStop() }
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_HOME) {
        }
        return super.onKeyDown(keyCode, event) }
    override fun onClick(v: View) {
        when (v.id) {
            R.id.ivClear -> actvSearch!!.setText("")
            R.id.againIcClose -> {
                actvSearch!!.setText("")
                hideSoftKeyboard()
            }
            R.id.ivSearch -> goSearch() }
    }
    fun goSearch() {
        if (actvSearch!!.text.length > 0) {
            if (TillsPth.isNetworkAvailable(mContext)) {
                bgview!!.visibility = View.GONE
                mainLlToolbar!!.visibility = View.VISIBLE
                val intent = Intent(mContext, MyWebView::class.java)
                intent.putExtra(
                    "Query",
                    "https://www.google.com/search?q=" + actvSearch!!.text.toString())
                startActivity(intent)
                actvSearch!!.setText("")
            }
        }
    }

    fun goSearch2(query: String?) {
        bgview!!.visibility = View.GONE
        mainLlToolbar!!.visibility = View.VISIBLE
        val intent = Intent(mContext, MyWebView::class.java)
        intent.putExtra("Query", query)
        startActivity(intent)
        actvSearch!!.setText("")
    }

    fun setListnerToRootView() {
        val activityRootView = window.decorView.findViewById<View>(android.R.id.content)
        activityRootView.viewTreeObserver.addOnGlobalLayoutListener {
            val heightDiff = activityRootView.rootView.height - activityRootView.height
            if (heightDiff > 100) {
                bgview!!.visibility = View.VISIBLE
                mainLlToolbar!!.visibility = View.GONE
                if (isOpened == false) {
                }
                isOpened = true
            } else if (isOpened == true) {
                againIcClose!!.visibility = View.GONE
                ivClear!!.visibility = View.GONE
                ivSearch!!.visibility = View.GONE
                actvSearch!!.setText("")
                bgview!!.visibility = View.GONE
                mainLlToolbar!!.visibility = View.VISIBLE
                isOpened = false
            }
        }
    }

    fun hideSoftKeyboard() {
        if (currentFocus != null) {
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
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
}