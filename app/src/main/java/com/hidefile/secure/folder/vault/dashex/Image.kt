package com.hidefile.secure.folder.vault.dashex


import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.text.Layout
import android.text.SpannableString
import android.text.style.AlignmentSpan
import android.view.*
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.hidefile.secure.folder.vault.AdActivity.Common_Adm
import com.hidefile.secure.folder.vault.R
import com.hidefile.secure.folder.vault.cluecanva.*
import com.hidefile.secure.folder.vault.dpss.OnImageItemClickListner
import org.apache.commons.io.comparator.LastModifiedFileComparator
import java.io.File
import java.util.*


class Image : FoundationActivity(),
    OnImageItemClickListner {
    val arrayList = ArrayList<SelfieIEm?>()
    var actionBar: ActionBar? = null
    var recyclerview: RecyclerView? = null
    var gridLayoutManager: GridLayoutManager? = null
    var imageGalleryAdapter: HiddenSelfieAdp? = null
    var noItemFound: LinearLayout? = null
    var relativeNativeLayout: RelativeLayout? = null


    var isSelectedMode = false
    var mContext: Context? = null
    private var actionMode: ActionMode? = null
    private var imageCountSelected = 0
    private val actionBarTitle: TextView? = null
    private var layoutOptions: LinearLayout? = null




    var idNative = ""

    private var llUnHide: LinearLayout? = null
    private var llDelete: LinearLayout? = null
    private val callback: ActionMode.Callback = object : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            val menuInflater = mode.menuInflater
            menuInflater.inflate(R.menu.emen_unet, menu)
            val action_unhide = menu.findItem(R.id.action_unhide)
            action_unhide.isVisible = false
            actionMode = mode
            imageCountSelected = 0
            layoutOptions!!.visibility = View.VISIBLE
            return true }
        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
            return false }
        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            val i = item.itemId
            if (i == R.id.action_unhide) {
                return true
            } else if (i == R.id.action_delete) {
                DeletePhoto(this@Image)
                return true }
            return false }
        override fun onDestroyActionMode(mode: ActionMode) {
            if (imageCountSelected > 0) {
                deselectAll() }
            layoutOptions!!.visibility = View.GONE
            actionMode = null
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hidden_selfie)
        mContext = this@Image

        val toolbar = findViewById<Toolbar>(R.id.main_toolbar)
        setSupportActionBar(toolbar)
        actionBar = supportActionBar

        if (actionBar != null) {
            val tv_tital = findViewById<VTv>(R.id.tv_tital)
            tv_tital.setSpannableText(
                getString(R.string.tital_intruder),
                getString(R.string.tital_selfie))
            val iv_back = findViewById<ImageView>(R.id.iv_back)
            iv_back.setOnClickListener { v: View? -> onBackPressed() }
            findViewById<View>(R.id.iv_option).visibility = View.GONE }

        Init()


    }


    override fun onBackPressed() {
        super.onBackPressed()



    }
    fun Init() {
        relativeNativeLayout = findViewById(R.id.relativeSmall)
        noItemFound = findViewById(R.id.lin_noItemFound)
        recyclerview = findViewById(R.id.recyclerview_mywork)
        gridLayoutManager = GridLayoutManager(this, 4)
        recyclerview?.setLayoutManager(gridLayoutManager)
        val itemDecoration =
            ConfigureSetWise(
                mContext!!,
                R.dimen.item_space
            )
        recyclerview?.addItemDecoration(itemDecoration)
        arrayList.clear()
        layoutOptions = findViewById(R.id.optionsLayout)
        llUnHide = findViewById(R.id.linUnHide)
        llDelete = findViewById(R.id.linDelete)
        llUnHide?.setVisibility(View.GONE)
        llDelete?.setVisibility(View.VISIBLE)
        llDelete?.setOnClickListener { view: View? -> DeletePhoto(this@Image) }
        val listFile: Array<File>
        val file = File(TillsPth.worngPwd)
        if (file.isDirectory) {
            listFile = file.listFiles()
            Arrays.sort(listFile, LastModifiedFileComparator.LASTMODIFIED_COMPARATOR)
            for (i in listFile.indices) {
                arrayList.add(SelfieIEm(false, listFile[i].absolutePath, listFile[i].name))
            }
        }
        if (arrayList.size == 0) {
            noItemFound?.setVisibility(View.VISIBLE)
            relativeNativeLayout!!.visibility = View.GONE
        } else {
            Collections.reverse(arrayList)
            imageGalleryAdapter = HiddenSelfieAdp(this@Image, arrayList)
            imageGalleryAdapter!!.hiddenSelfieItemClick(this)
            imageGalleryAdapter!!.setImageResize(TillsPth.getImageResize(mContext, recyclerview))
            noItemFound?.setVisibility(View.GONE)
            Common_Adm.getInstance().loadBanner(this@Image, findViewById<ViewGroup?>(R.id.llBanner),findViewById<ViewGroup?>(R.id.adSimmer1))
            relativeNativeLayout!!.visibility = View.VISIBLE
            recyclerview?.setAdapter(imageGalleryAdapter)
        }
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
        if (keyCode == KeyEvent.KEYCODE_HOME) { }
        return super.onKeyDown(keyCode, event) }
    override fun onImageItemClick(position: Int) {
        if (isSelectedMode) {
            if (actionMode == null) {
//                actionMode = startActionMode(callback)
                layoutOptions!!.visibility = View.VISIBLE
            }
            toggleSelection(position)
            val actionModeText = imageCountSelected.toString() + " " + getString(R.string.selected_name)
            val finalResult = SpannableString(actionModeText)
            finalResult.setSpan(
                AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                0,
                actionModeText.length,
                0)
//            actionMode!!.title = finalResult
            if (imageCountSelected == 0) {
//                actionMode!!.finish()
                isSelectedMode = false
                layoutOptions!!.visibility = View.GONE

            }
        } else {
            val intent = Intent(mContext, SelfieImageView::class.java)
            intent.putExtra("postion", position)
            intent.putExtra("listIdPics", arrayList)
            startActivity(intent)
        }
    }
    override fun onImageItemLongClick(position: Int) {
        isSelectedMode = true
        if (actionMode == null) {
//            actionMode = startActionMode(callback)
            layoutOptions!!.visibility = View.VISIBLE
        }
        toggleSelection(position)
//        actionMode!!.title =
//            imageCountSelected.toString() + " " + getString(R.string.selected)
        if (imageCountSelected == 0) {
//            actionMode!!.finish()
            layoutOptions!!.visibility = View.GONE
            isSelectedMode = false
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true }
            else -> {
                false }
        }
    }
    private fun toggleSelection(position: Int) {
        arrayList[position]!!.isChecked = !arrayList[position]!!.isChecked
        if (arrayList[position]!!.isChecked) {
            imageCountSelected++
        } else {
            imageCountSelected-- }
        imageGalleryAdapter!!.notifyDataSetChanged() }
    @SuppressLint("SetTextI18n")
    fun DeletePhoto(act: Activity?) {
        val bottomSheetDialog = BottomSheetDialog(
            act!!, R.style.BottomSheetDialogTheme)
        val dialogView = View.inflate(act, R.layout.dig_delete, null)
        val animationView = dialogView.findViewById<LottieAnimationView>(R.id.animation_view)
        val button_negative = dialogView.findViewById<AppCompatButton>(R.id.btn_negative)
        val button_positive = dialogView.findViewById<AppCompatButton>(R.id.btn_positive)
        val title = dialogView.findViewById<TextView>(R.id.tv_sort_title)
        val textView_message = dialogView.findViewById<TextView>(R.id.textView_message)
        title.text = "Delete Intruder image?"
        title.visibility = View.VISIBLE
        textView_message.text = "Do You Really Want To Delete This Image?"
        button_negative.text = "Cancel"
        button_positive.text = "Delete"
        animationView.setAnimation(R.raw.animation_delete)
        button_negative.setOnClickListener { v: View? -> bottomSheetDialog.dismiss() }
        button_positive.setOnClickListener { v: View? ->
            DeleteImageTask().execute()
            bottomSheetDialog.dismiss()
            layoutOptions!!.visibility = View.GONE

        }

        bottomSheetDialog.setContentView(dialogView)
        bottomSheetDialog.show() }
    private fun deselectAll() {
        var i = 0
        val l = arrayList.size
        while (i < l) {
            arrayList[i]!!.isChecked = false
            i++ }
        isSelectedMode = false
        imageCountSelected = 0
        imageGalleryAdapter!!.notifyDataSetChanged() }
    private val selected: ArrayList<SelfieIEm?>
        private get() {
            val selectedImages = ArrayList<SelfieIEm?>()
            var i = 0
            val l = arrayList.size
            while (i < l) {
                if (arrayList[i]!!.isChecked) {
                    selectedImages.add(arrayList[i]) }
                i++ }
            return selectedImages }
    inner class DeleteImageTask : AsyncTask<String?, Int?, String>() {
        var pd: ProgressDialog? = null
        override fun onPreExecute() {
            super.onPreExecute()
            pd = ProgressDialog(this@Image)
            pd!!.setTitle("Delete to intruder Image")
            pd!!.setMessage("Please wait....")
            pd!!.setCancelable(false)
            pd!!.isIndeterminate = true
            pd!!.show()
        }
        protected override fun doInBackground(vararg params: String?): String? {
            val selected = selected
            for (i in selected.indices) {
                val file = File(selected[i]!!.hiddenSelfiePath)
                if (file.exists()) file.delete() }
            return "null"
        }
        override fun onPostExecute(s: String) {
            super.onPostExecute(s)
            pd!!.dismiss()
            imageGalleryAdapter!!.notifyDataSetChanged()
            Toast.makeText(mContext, "Delete Intruder Image successfully", Toast.LENGTH_SHORT)
                .show()
            if (actionMode != null)
//                actionMode!!.finish()
            imageCountSelected = 0
            Init()
        }
    }
    inner class HiddenSelfieAdp(
        var mContext: Context,
        mediaFiles: ArrayList<SelfieIEm?>?
    ) : RecyclerView.Adapter<HiddenSelfieAdp.ViewHolder>() {
        var onImageItemClickListner: OnImageItemClickListner? = null
        var mImageResize = 0
        fun hiddenSelfieItemClick(onImageItemClickListner: OnImageItemClickListner?) {
            this.onImageItemClickListner = onImageItemClickListner }
        fun setImageResize(imageSize: Int) {
            mImageResize = imageSize }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.galry_img, parent, false)
            return ViewHolder(v) }
        override fun onBindViewHolder(
            holder: ViewHolder,
            @SuppressLint("RecyclerView") position: Int) {
            val mediaFile = File(arrayList[position]!!.hiddenSelfiePath)
            if (mediaFile.exists()) {
                val options =
                    RequestOptions().centerCrop().override(mImageResize, mImageResize).placeholder(
                        R.drawable.ic_placeholder)
                val parth = mediaFile.absolutePath
                val targetLocation = File(parth)
                holder.gallery_image.visibility = View.VISIBLE
                Glide.with(mContext)
                    .load(Uri.fromFile(targetLocation))
                    .apply(options)
                    .into(holder.gallery_image)
                holder.gallery_image.layoutParams.height = mImageResize
                holder.gallery_image.layoutParams.width = mImageResize
                if (arrayList[position]!!.isChecked) {
                    holder.iv_chek.visibility = View.VISIBLE
                } else {
                    holder.iv_chek.visibility = View.GONE
                }
                holder.iv_chek.setOnClickListener { v: View? ->
                    if (onImageItemClickListner != null) {
                        onImageItemClickListner!!.onImageItemClick(position)
                    }
                }
                holder.frm_root.setOnClickListener { v: View? ->
                    if (onImageItemClickListner != null) {
                        onImageItemClickListner!!.onImageItemClick(position)
                    }
                }
                holder.iv_chek.setOnLongClickListener { v: View? ->
                    if (onImageItemClickListner != null) {
                        onImageItemClickListner!!.onImageItemLongClick(position)
                    }
                    false
                }
                holder.frm_root.setOnLongClickListener { v: View? ->
                    if (onImageItemClickListner != null) {
                        onImageItemClickListner!!.onImageItemLongClick(position)
                    }
                    false
                }
            }
        }

        override fun getItemCount(): Int {
            return arrayList.size
        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var frm_root: FrameLayout
            var frm_chek: FrameLayout
            val gallery_image: ImageView
            val iv_chek: ImageView

            init {
                gallery_image = view.findViewById(R.id.iv_gallery)
                frm_root = view.findViewById(R.id.frm_root)
                frm_chek = view.findViewById(R.id.frm_chek)
                iv_chek = view.findViewById(R.id.iv_chek)
            }
        }
    }
}