package com.hidefile.secure.folder.vault.dpss

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.Joaquin.thiago.ListItVid
import com.bumptech.glide.Glide
import com.hidefile.secure.folder.vault.R
import java.util.ArrayList

class VideoSelectAdp(context: Context?, images: ArrayList<ListItVid?>?) :
    GenrCustAdp<ListItVid?>(context, images) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var convertView = convertView
        val viewHolder: ViewHolder
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.view_engle_for_iv, null)
            viewHolder = ViewHolder()
            viewHolder.imageView = convertView.findViewById(R.id.iv_image_select)
            viewHolder.frm_chek = convertView.findViewById(R.id.frm_chek)
            viewHolder.ivCheck = convertView.findViewById(R.id.ivCheck)
            viewHolder.ivUncheck = convertView.findViewById(R.id.ivUncheck)
            convertView.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
        }
        viewHolder.imageView!!.layoutParams.width = size
        viewHolder.imageView!!.layoutParams.height = size
        viewHolder.frm_chek!!.layoutParams.width = size
        viewHolder.frm_chek!!.layoutParams.height = size
        if (arrayList?.get(position)!!.isChecked) {
            viewHolder.ivCheck!!.visibility = View.VISIBLE
            viewHolder.ivUncheck!!.visibility = View.GONE
        } else {
            viewHolder.ivCheck!!.visibility = View.GONE
            viewHolder.ivUncheck!!.visibility = View.VISIBLE
        }
        context?.let {
            Glide.with(it)
                .load(arrayList!![position]!!.getPath())
                .placeholder(R.drawable.image_placeholder).into(viewHolder.imageView!!)
        }
        return convertView
    }

    private class ViewHolder {
        var imageView: ImageView? = null
        var ivCheck: ImageView? = null
        var ivUncheck: ImageView? = null
        var frm_chek: FrameLayout? = null
    }
}