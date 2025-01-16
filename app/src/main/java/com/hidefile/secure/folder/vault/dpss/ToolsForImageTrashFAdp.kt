package com.hidefile.secure.folder.vault.dpss

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.Joaquin.thiago.ListIdPic


import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.api.services.drive.Drive
import com.hidefile.secure.folder.vault.AdActivity.Common_Adm
import com.hidefile.secure.folder.vault.R
import com.hidefile.secure.folder.vault.cluecanva.RDbhp
import com.hidefile.secure.folder.vault.cluecanva.TillsPth
import com.hidefile.secure.folder.vault.cluecanva.VTv
import com.hidefile.secure.folder.vault.dashex.IvFullScreenView
import com.hidefile.secure.folder.vault.edptrs.InSafe
import java.io.File


class ToolsForImageTrashFAdp : Fragment(), OnImageItemClickListner {
    val imagesList = ArrayList<ListIdPic>()
    var service: Drive? = null
    var mContext: Context? = null
    var recycler_photolist: RecyclerView? = null
    var lin_nodata: LinearLayout? = null
    var relativeNativeLayout: RelativeLayout? = null
    var viewListImageAdp: ViewListImageAdp? = null
    var RDbhp: RDbhp? = null
    var isSelectedMode = false
    var gridLayoutManager: GridLayoutManager? = null
    private var actionBar: ActionBar? = null
    private var actionMode: ActionMode? = null

    private var countSelected = 0
    var isReload = false


    var idNative = ""

    private val callback: ActionMode.Callback = object : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            val menuInflater = mode.menuInflater
            menuInflater.inflate(R.menu.emen_trash, menu)
            actionMode = mode
            countSelected = 0
            layoutOptions!!.visibility = View.VISIBLE
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
            return false
        }

        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            val i = item.itemId
            if (i == R.id.action_delete) {
                DeletePotos(activity)
                return true
            } else if (i == R.id.action_recover) {
                BaclImageFromTrash(activity)
                return true
            }
            return false
        }

        override fun onDestroyActionMode(mode: ActionMode) {
            if (countSelected > 0) {
                deselectAll()
            }
            layoutOptions!!.visibility = View.GONE
            actionMode = null
        }
    }
    private var layoutOptions: LinearLayout? = null
    private var llDelete: LinearLayout? = null
    private var llRestore: LinearLayout? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.photo_trf, container, false)
        mContext = activity
        actionBar = (activity as AppCompatActivity?)!!.supportActionBar


        RDbhp = com.hidefile.secure.folder.vault.cluecanva.RDbhp.getInstance(mContext)



//        Common_Adm.getInstance().AmNativeLoad(activity, view.findViewById<View>(R.id.llNativeSmall) as ViewGroup, false)



        if (actionBar != null) {
            val iv_back = requireActivity().findViewById<ImageView>(R.id.iv_back)
            iv_back.setOnClickListener { v: View? -> requireActivity().onBackPressed() }
            requireActivity().findViewById<View>(R.id.iv_option).visibility = View.GONE
            val tv_tital = requireActivity().findViewById<VTv>(R.id.tv_tital)
            tv_tital.setText("Trash Photo")
        }
        lin_nodata = view.findViewById(R.id.emptyLin)
        relativeNativeLayout = view.findViewById(R.id.relativeSmall)
        recycler_photolist = view.findViewById(R.id.rvPhotoList)
        layoutOptions = view.findViewById(R.id.optionsLayout)
        llDelete = view.findViewById(R.id.linDelete)
        llDelete!!.setVisibility(View.VISIBLE)
        llRestore = view.findViewById(R.id.llRestore)
        llRestore!!.setVisibility(View.VISIBLE)
        llDelete!!.setOnClickListener { v: View? ->
            DeletePotos(
                activity
            )
        }
        llRestore!!.setOnClickListener { v: View? ->
            BaclImageFromTrash(
                activity
            )
        }
        val no = if (InSafe.isGridlayout) 3 else 1
        gridLayoutManager = GridLayoutManager(mContext, no)
        recycler_photolist!!.setHasFixedSize(true)
        recycler_photolist!!.setLayoutManager(gridLayoutManager)
        viewListImageAdp = ViewListImageAdp(requireActivity(), imagesList)
        viewListImageAdp!!.setItemClickEvent(this)
        viewListImageAdp!!.setImageResize(TillsPth.getImageResize(mContext, recycler_photolist))
        recycler_photolist!!.setAdapter(viewListImageAdp)

        Init()
        return view
    }



    fun Init() {
        val no = if (InSafe.isGridlayout) 3 else 1
        gridLayoutManager!!.spanCount = no
        imagesList.clear()
        imagesList.addAll(RDbhp!!.trashImages)
        if (viewListImageAdp != null) {
            viewListImageAdp!!.notifyDataSetChanged()
        }
        if (imagesList.size > 0) {

            recycler_photolist!!.visibility = View.VISIBLE
            lin_nodata!!.visibility = View.GONE
            relativeNativeLayout!!.visibility = View.VISIBLE
        } else {
            recycler_photolist!!.visibility = View.GONE
            lin_nodata!!.visibility = View.VISIBLE
            relativeNativeLayout!!.visibility = View.GONE
        }
    }

    override fun onImageItemClick(position: Int) {
        if (isSelectedMode) {
            if (actionMode == null) {
                layoutOptions!!.visibility = View.VISIBLE
            }
            toggleSelection(position)
            if (countSelected == 0) {
                isSelectedMode = false
                viewListImageAdp!!.setSelectionMode(isSelectedMode)
                viewListImageAdp!!.notifyDataSetChanged()
                layoutOptions!!.visibility = View.GONE
            }
        } else {
            val intent = Intent(mContext, IvFullScreenView::class.java)
            intent.putExtra("postion", position)
            intent.putExtra("isFromTrash", true)
            intent.putExtra("listIdPics", imagesList)
            startActivityForResult(intent, 120)
        }
    }

    override fun onImageItemLongClick(position: Int) {
        isSelectedMode = true
        viewListImageAdp!!.setSelectionMode(isSelectedMode)
        if (actionMode == null) {
            layoutOptions!!.visibility = View.VISIBLE
        }
        toggleSelection(position)
        if (countSelected == 0) {

            isSelectedMode = false
            viewListImageAdp!!.setSelectionMode(isSelectedMode)
            layoutOptions!!.visibility = View.GONE
        }
        viewListImageAdp!!.notifyDataSetChanged()
    }

    private fun toggleSelection(position: Int) {
        imagesList[position].checked = !imagesList[position].isChecked
        if (imagesList[position].checked) {
            countSelected++
        } else {
            countSelected--
        }
        viewListImageAdp!!.notifyDataSetChanged()
    }

    private fun deselectAll() {
        var i = 0
        val l = imagesList.size
        while (i < l) {
            imagesList[i].checked = false
            i++
        }
        isSelectedMode = false
        viewListImageAdp!!.setSelectionMode(isSelectedMode)
        countSelected = 0
        viewListImageAdp!!.notifyDataSetChanged()
    }

    private val selected: ArrayList<ListIdPic>
        private get() {
            val selectedImages = ArrayList<ListIdPic>()
            var i = 0
            val l = imagesList.size
            while (i < l) {
                if (imagesList[i].isChecked) {
                    selectedImages.add(imagesList[i])
                }
                i++
            }
            return selectedImages
        }

    @SuppressLint("SetTextI18n")
    fun DeletePotos(act: Activity?) {
        val bottomSheetDialog = BottomSheetDialog(act!!, R.style.BottomSheetDialogTheme)
        val dialogView = View.inflate(act, R.layout.dig_delete, null)
        val button_negative = dialogView.findViewById<AppCompatButton>(R.id.btn_negative)
        val button_positive = dialogView.findViewById<AppCompatButton>(R.id.btn_positive)
        val title = dialogView.findViewById<TextView>(R.id.tv_sort_title)
        val textView_message = dialogView.findViewById<TextView>(R.id.textView_message)
        title.text = "Delete Forever?"
        title.visibility = View.VISIBLE
        textView_message.text = "Do you Really sure to delete selected file permanently?\n\n WARNING :-  It will Not Recovered after Delete\n"
        button_negative.text = "Cancel"
        button_positive.text = "Delete"
        button_negative.setOnClickListener { v: View? -> bottomSheetDialog.dismiss() }
        button_positive.setOnClickListener { v: View? ->
            DeleteImageTask().execute()
            bottomSheetDialog.dismiss()
            layoutOptions!!.visibility = View.GONE

        }
        bottomSheetDialog.setContentView(dialogView)
        bottomSheetDialog.show()
    }

    fun BaclImageFromTrash(act: Activity?) {
        val bottomSheetDialog = BottomSheetDialog(
            act!!, R.style.BottomSheetDialogTheme
        )
        val dialogView = View.inflate(act, R.layout.dig_delete, null)
        val button_negative = dialogView.findViewById<AppCompatButton>(R.id.btn_negative)
        val button_positive = dialogView.findViewById<AppCompatButton>(R.id.btn_positive)
        val title = dialogView.findViewById<TextView>(R.id.tv_sort_title)
        val textView_message = dialogView.findViewById<TextView>(R.id.textView_message)
        title.text = "Restore Pictures?"
        title.visibility = View.VISIBLE
        textView_message.text = "Do you Really want Restore selected $countSelected images From Trash to App ?"
        button_negative.text = "Cancel"
        button_positive.text = "Restore"
        button_negative.setOnClickListener { v: View? -> bottomSheetDialog.dismiss() }
        button_positive.setOnClickListener { v: View? ->
            RecoverImageTask().execute()
            bottomSheetDialog.dismiss()
            layoutOptions!!.visibility = View.GONE
        }
        bottomSheetDialog.setContentView(dialogView)
        bottomSheetDialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 120) {
            Init()
        }
    }
    inner class DeleteImageTask : AsyncTask<String?, Int?, String>() {
        var pd: ProgressDialog? = null
        override fun onPreExecute() {
            super.onPreExecute()
            pd = ProgressDialog(activity)
            pd!!.setTitle("Delete to Album")
            pd!!.setMessage("Please wait....")
            pd!!.setCancelable(false)
            pd!!.isIndeterminate = true
            pd!!.show()
        }
        protected override fun doInBackground(vararg params: String?): String? {
            val selected = selected
            for (i in selected.indices) {
                if (!selected[i].getCouldId().equals("null1", ignoreCase = true)) {
                    RDbhp!!.deletePhotoTrash(selected[i].getId())
                } else {
                    val file = File(selected[i].getNewPath())
                    if (file.exists()) file.delete()
                    RDbhp!!.deletePhotoItem(selected[i].getId())
                }
                imagesList.remove(selected[i])
            }
            return "null"
        }

        override fun onPostExecute(s: String) {
            super.onPostExecute(s)
            pd!!.dismiss()
            viewListImageAdp!!.notifyDataSetChanged()
            Toast.makeText(mContext, "Deleted Successfully From Album", Toast.LENGTH_SHORT).show()
            if (actionMode != null)
//                actionMode!!.finish()
            countSelected = 0
            Init()
        }
    }

    inner class RecoverImageTask : AsyncTask<String?, Int?, String>() {
        var pd: ProgressDialog? = null
        override fun onPreExecute() {
            super.onPreExecute()
            pd = ProgressDialog(activity)
            pd!!.setTitle("Recover to Album")
            pd!!.setMessage("Please wait....")
            pd!!.setCancelable(false)
            pd!!.isIndeterminate = true
            pd!!.show()
        }

        protected override fun doInBackground(vararg params: String?): String? {
            val selected = selected
            for (i in selected.indices) {
                RDbhp!!.reCoverPhoto(selected[i].getId())
                imagesList.remove(selected[i])
            }
            return "null"
        }

        override fun onPostExecute(s: String) {
            super.onPostExecute(s)
            pd!!.dismiss()
            viewListImageAdp!!.notifyDataSetChanged()
            Toast.makeText(mContext, "Data Recovered to Album successfully", Toast.LENGTH_SHORT).show()
            if (actionMode != null)
//                actionMode!!.finish()
            countSelected = 0
            Init()
        }
    }
}