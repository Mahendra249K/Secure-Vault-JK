package com.hidefile.secure.folder.vault.dpss

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hidefile.secure.folder.vault.cluecanva.RecentList
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.hidefile.secure.folder.vault.cluecanva.SupPref
import com.bumptech.glide.Glide
import com.hidefile.secure.folder.vault.R
import java.lang.Exception
import java.lang.RuntimeException
import java.util.ArrayList

class RecentAppListAdp(
    var act: Activity,
    var arrAppList: ArrayList<RecentList>,
    var onClick: View.OnClickListener,
    var longlistener: View.OnLongClickListener
) : RecyclerView.Adapter<RecentAppListAdp.ViewHolder>() {
    private val manager: RequestManager
    private val options: RequestOptions
    fun removeLast() {
        if (isLongPress) {
            arrAppList.removeAt(arrAppList.size - 1)
            notifyDataSetChanged()
        }
    }

    fun addLast() {
        if (!isLongPress) {
            arrAppList.add(arrAppList.size, RecentList("ic_plus", "", "Add"))
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(act).inflate(R.layout.app_layout, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = arrAppList[position]
        val webIcon = data.image
        var icon = -1
        if(webIcon !=null){
            when (webIcon) {
                "ic_google1" -> icon = R.drawable.ic_bro_google
                "ic_youtube" -> icon = R.drawable.ic_bro_youtube
                "ic_facebook" -> icon = R.drawable.ic_bro_fb
                "ic_twitter" -> icon = R.drawable.ic_bro_twitter
                "ic_instagram" -> icon = R.drawable.ic_bro_inst
                "ic_plus" -> icon = R.drawable.ic_bro_add
            }
        }

        if (icon == -1) {
            manager.load(data.url + "/google.com").apply(options).into(holder.ivAppIcon)
        } else {
            manager.load(icon).apply(options).into(holder.ivAppIcon)
        }
        holder.tvAppTitle.text = data.title
        holder.lin_approot.tag = position
        holder.lin_approot.setOnClickListener(onClick)
        holder.lin_approot.setOnLongClickListener(longlistener)
        if (isLongPress) {
            holder.iv_delete.visibility = View.VISIBLE
        } else {
            holder.iv_delete.visibility = View.GONE
        }
        holder.iv_delete.setOnClickListener { v: View? ->
            val dataMap = SupPref.getAppDataMap(act)
            if (dataMap.containsKey(arrAppList[position].url)) {
                dataMap.remove(arrAppList[position].url)
                SupPref.saveAppData(act, dataMap)
                arrAppList.remove(arrAppList[position])
                notifyDataSetChanged()
            } else {
            }
        }
    }

    override fun getItemCount(): Int {
        return arrAppList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivAppIcon: ImageView
        var iv_delete: ImageView
        var tvAppTitle: TextView
        var lin_approot: LinearLayout

        init {
            ivAppIcon = itemView.findViewById(R.id.ivIconApp)
            iv_delete = itemView.findViewById(R.id.ivIconDelete1)
            tvAppTitle = itemView.findViewById(R.id.AppTitleTv)
            lin_approot = itemView.findViewById(R.id.ll_app_main)
        }
    }

    companion object {
        private val TAG = RecentAppListAdp::class.java.simpleName
        @JvmField
        var isLongPress = false
        var ADD_CUSTOM_WEB = "-30"
        @Throws(RuntimeException::class)
        fun getResourseId(context: Context, pVariableName: String?): Int {
            return try {
                context.resources.getIdentifier(pVariableName, "drawable", context.packageName)
            } catch (e: Exception) {
                throw RuntimeException("Error getting Resource ID.", e)
            }
        }
    }

    init {
        isLongPress = false
        manager = Glide.with(act)
        options = RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.ic_bro_new)
    }
}