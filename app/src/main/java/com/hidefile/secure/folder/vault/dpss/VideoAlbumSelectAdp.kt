package com.hidefile.secure.folder.vault.dpss
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.Joaquin.thiago.ListVedFl
import androidx.appcompat.widget.AppCompatTextView
import com.bumptech.glide.Glide
import com.hidefile.secure.folder.vault.R
import java.util.ArrayList

class VideoAlbumSelectAdp(context: Context?, albums: ArrayList<ListVedFl?>?) :
    GenrCustAdp<ListVedFl?>(context, albums) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var convertView = convertView
        val viewHolder: ViewHolder
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.grd_view_img, null)
            viewHolder = ViewHolder()
            viewHolder.imageView = convertView.findViewById(R.id.iv_album_image)
            viewHolder.textView = convertView.findViewById(R.id.tv_album_name)
            viewHolder.tv_item_count = convertView.findViewById(R.id.tv_item_count)
            convertView.tag = viewHolder } else {
            viewHolder = convertView.tag as ViewHolder
        }
        viewHolder.imageView!!.layoutParams.width = size
        viewHolder.imageView!!.layoutParams.height = size
        viewHolder.textView!!.text = arrayList?.get(position)!!.name
        viewHolder.tv_item_count!!.text = arrayList!![position]!!.items.size.toString() + " item"
        context?.let {
            Glide.with(it)
                .load(arrayList!![position]!!.items[0].getPath())
                .placeholder(R.drawable.image_placeholder).centerCrop()
                .into(viewHolder.imageView!!)
        }
        return convertView }
    private class ViewHolder {
        var imageView: ImageView? = null
        var textView: AppCompatTextView? = null
        var tv_item_count: AppCompatTextView? = null
    }
}