package com.hidefile.secure.folder.vault.dashex

import android.accounts.AccountManager
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.hidefile.secure.folder.vault.AdActivity.Call_Back_Ads
import com.hidefile.secure.folder.vault.AdActivity.Common_Adm
import com.hidefile.secure.folder.vault.AdActivity.SharedPref
import com.hidefile.secure.folder.vault.R
import com.hidefile.secure.folder.vault.cluecanva.*
import com.hidefile.secure.folder.vault.databinding.QueSeqBinding
import java.util.*
import java.util.regex.Pattern

class QuestionSecurity() : FoundationActivity() {
    private lateinit var binding: QueSeqBinding

    var QType: String? = ""
    var mContext: Context? = null
    var que_answer: EditText? = null
    var RDbhp: RDbhp? = null
    var questionList: Spinner? = null
    var txt_resetEmail: TextView? = null
    var iv_option: ImageView? = null
    var inputQuestionValue: String? = null
    var progress_bar :ProgressBar?=null
    var btn_submit: VTv? = null
    var tv_question: VTv? = null

    private var actionBar: ActionBar? = null

    var regex = Pattern.compile("[$&+.,:;=\\\\?@#|/'<>^*(){}%!-]")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            WindowManager.LayoutParams.FLAG_FULLSCREEN
//        )
        binding = QueSeqBinding.inflate(layoutInflater);



        setContentView(binding.root)
        SharedPref.AppOpenShow = false
        TilsCo.isStr = false
//         Common_Adm.getInstance().AmNativeLoad(this, findViewById<View>(R.id.llNativeLarge) as ViewGroup, true)
        mContext = this@QuestionSecurity
        QType = intent.getStringExtra("QType")
        Init()

    }



    private fun Init() {

        RDbhp = com.hidefile.secure.folder.vault.cluecanva.RDbhp.getInstance(mContext)
        btn_submit = findViewById(R.id.btn_submit)
        que_answer = findViewById(R.id.que_answer)
        iv_option=findViewById(R.id.iv_option)
        questionList = findViewById(R.id.question_spinner)
        tv_question = findViewById(R.id.question_list)
        val toolbar = findViewById<Toolbar>(R.id.main_toolbar)
        progress_bar=findViewById(R.id.progress_bar)
        setSupportActionBar(toolbar)
        actionBar = supportActionBar
        if (actionBar != null) {
            val tv_tital = findViewById<VTv>(R.id.tv_tital)
            tv_tital.setText(R.string.security_question)
            val iv_back = findViewById<ImageView>(R.id.iv_back)
            iv_back.setOnClickListener({ onBackPressed() })

        }
        if (QType.equals("Set", ignoreCase = true)) {
            questionList!!.setVisibility(View.VISIBLE)
            tv_question!!.setVisibility(View.GONE)
        } else {
            questionList!!.setVisibility(View.GONE)
            tv_question!!.setVisibility(View.VISIBLE)
            val squestion = RDbhp!!.getUser(1).getSquestion()
            tv_question!!.setText(squestion)
        }
        val stringQuestionList = resources.getStringArray(R.array.question_array_list)
        val stringQuestionValue = resources.getStringArray(R.array.question_array_list)
        val spinnerArrayAdapter = ArrayAdapter(
            (mContext)!!, R.layout.que_spin, stringQuestionList
        )
        spinnerArrayAdapter.setDropDownViewResource(R.layout.item_spin)
        questionList!!.setAdapter(spinnerArrayAdapter)
        questionList!!.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                inputQuestionValue = stringQuestionValue[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })
        btn_submit!!.setOnClickListener { v ->
            SupPref.setBooleanValue(this@QuestionSecurity, SupPref.isFromPin, true)
            val iMethod = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            iMethod.hideSoftInputFromWindow(v.windowToken, 0)
            if (!que_answer!!.getText().toString().isEmpty()) {
                if (!regex.matcher(
                        que_answer!!.getText().toString().trim { it <= ' ' }.lowercase(
                            Locale.getDefault()
                        )
                    ).find()
                ) {
                    if (QType.equals("Set", ignoreCase = true)) {
                        val RDbhp = com.hidefile.secure.folder.vault.cluecanva.RDbhp.getInstance(this@QuestionSecurity)
                        val existPin = intent.getStringExtra("existPin")
                        RDbhp.insertUser(
                            existPin,
                            inputQuestionValue,
                            que_answer!!.getText().toString().trim { it <= ' ' })
                        val userItem = RDbhp.userData


                        gotoFinalActivity()
                    } else {
                        val answer = RDbhp!!.getUser(1).getAnswer()
                        if (que_answer!!.getText().toString().trim { it <= ' ' }
                                .equals(answer, ignoreCase = true)) {

                            val intent = Intent(mContext, Pswd::class.java)
                            intent.putExtra("ElsePin", true)
                            startActivityForResult(intent, 204)


                        } else {
                            val userItem = RDbhp!!.getUser(1)
                            val email = userItem.getEmail()
                            if (email != null && !email.trim { it <= ' ' }.isEmpty()) {
                                txt_resetEmail!!.visibility = View.VISIBLE
                            }
                            que_answer!!.setText("")
                            EntryAux.showToast(mContext, R.string.msg_wrong_answer)
                        }
                    }
                } else {
                    EntryAux.showToast(mContext, R.string.msg_answer_valid)
                }
            } else {
                EntryAux.showToast(mContext, R.string.msg_answer)
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    override fun onStart() {
        super.onStart()
        Constant.isShowOpenAd = true
        SharedPref.AppOpenShow = false

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    override fun onResume() {
        super.onResume()
        SharedPref.AppOpenShow = false
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    override fun onPause() {
        super.onPause()
        SharedPref.AppOpenShow = false
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    override fun onStop() {
        super.onStop()
        SharedPref.AppOpenShow = false
        Log.d("cycle", "stop")
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 204) {
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
        if (requestCode == REQUEST_ACCOUNT_PICKER) {
            if ((resultCode == Activity.RESULT_OK) && (data != null) && (data.extras != null)) {
                val accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME)
                if (accountName != null && !accountName.trim { it <= ' ' }.isEmpty()) {
                    SupPref.putValue(mContext, SupPref.AccountName, accountName)
                    val existPin = intent.getStringExtra("existPin")
                    RDbhp!!.insertUser(
                        existPin,
                        inputQuestionValue,
                        que_answer!!.text.toString(),
                        accountName
                    )
                    gotoFinalActivity()
                }
            } else {
                gotoFinalActivity()
            }
        }
    }

    private fun gotoFinalActivity() {

        progress_bar?.setVisibility(View.VISIBLE)
        Handler().postDelayed({

            SupPref.setBooleanValue(mContext, SupPref.FirstTimeUser, false)
            startActivity(Intent(this@QuestionSecurity, BordMain::class.java))
            setResult(Activity.RESULT_OK)
            finish()
        }, 1000)

//
//        Common_Adm.getInstance()
//            .everytimeInterstialAdShow(true, this@QuestionSecurity, object : Call_Back_Ads {
//                override fun onAdsClose() {
//                    SupPref.setBooleanValue(mContext, SupPref.FirstTimeUser, false)
//                    startActivity(Intent(this@QuestionSecurity, BordMain::class.java))
//                    setResult(Activity.RESULT_OK)
//                    finish()
//                }
//
//                override fun onLoading() {
//                    SupPref.setBooleanValue(mContext, SupPref.FirstTimeUser, false)
//                    startActivity(Intent(this@QuestionSecurity, BordMain::class.java))
//                    setResult(Activity.RESULT_OK)
//                    finish()
//                }
//
//                override fun onAdsFail() {
//                    SupPref.setBooleanValue(mContext, SupPref.FirstTimeUser, false)
//                    startActivity(Intent(this@QuestionSecurity, BordMain::class.java))
//                    setResult(Activity.RESULT_OK)
//                    finish()
//                }
//            })
//
////
////        startActivity(Intent(this@QuestionSecurity, BordMain::class.java))
////        setResult(Activity.RESULT_OK)
////        finish()


    }

    @SuppressLint("LongLogTag")
    inner class CreateDirectoryTask() : AsyncTask<Void?, Void?, Void?>() {
        var progress: ProgressDialog
        override fun onPreExecute() {
            super.onPreExecute()
        }

        protected override fun doInBackground(vararg params: Void?): Void? {
            return null
        }

        override fun onPostExecute(aVoid: Void?) {
            super.onPostExecute(aVoid)
            progress.dismiss()
        }

        init {
            progress = ProgressDialog(this@QuestionSecurity)
            progress.setTitle(getString(R.string.connected_server_msg))
            progress.setMessage(getString(R.string.please_wait_msg))
            progress.setCancelable(false)
            progress.isIndeterminate = true
            progress.show()
        }
    }

    companion object {
        private val TAG = "QuestionSecurity"
        private val REQUEST_ACCOUNT_PICKER = 0
    }
}