package com.hidefile.secure.folder.vault.dpss

import android.content.Context
import android.view.LayoutInflater
import android.widget.BaseAdapter
import java.util.ArrayList

abstract class GenrCustAdp<T>(context: Context?, arrayList: ArrayList<T>?) :
    BaseAdapter() {
    protected var arrayList: List<T>?
    protected var context: Context?
    protected var layoutInflater: LayoutInflater
    protected var size = 0
    override fun getCount(): Int {
        return arrayList!!.size
    }

    override fun getItem(position: Int): T {
        return arrayList!![position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun setLayoutParams(size: Int) {
        this.size = size
    }

    fun releaseResources() {
        arrayList = null
        context = null
    }

    init {
        this.arrayList = arrayList
        this.context = context
        layoutInflater = LayoutInflater.from(this.context)
    }
}