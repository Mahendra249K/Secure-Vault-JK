package com.hidefile.secure.folder.vault.dpss

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.hidefile.secure.folder.vault.R

class SpinAdp(
    private val ctx: Context,
    resource: Int,
    private val contentArray: Array<String>,
    private val imageArray: Array<Int>
) : ArrayAdapter<String?>(
    ctx, R.layout.layout_spin, R.id.tvSpin, contentArray
) { override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView, parent) }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView, parent) }
    fun getCustomView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val row = inflater.inflate(R.layout.layout_spin, parent, false)
        val textView = row.findViewById<TextView>(R.id.tvSpin)
        textView.text = contentArray[position]
        val imageView = row.findViewById<ImageView>(R.id.ivSpin)
        imageView.setImageResource(imageArray[position])
        return row
    }
}