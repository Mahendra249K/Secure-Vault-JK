package com.hidefile.secure.folder.vault.dpss

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.Joaquin.thiago.ListIdPic
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.Glide
import com.hidefile.secure.folder.vault.R
import com.hidefile.secure.folder.vault.edptrs.InSafe
import com.hidefile.secure.folder.vault.cluecanva.TillStr
import com.hidefile.secure.folder.vault.cluecanva.VTv
import com.makeramen.roundedimageview.RoundedImageView
import java.io.File
import java.util.ArrayList

class ViewListImageAdp(var mContext: Activity, private var mediaFiles: ArrayList<ListIdPic>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var onImageItemClickListner: OnImageItemClickListner? = null
    var spanCount = 1
    var mImageResize = 0
    var options: RequestOptions? = null
    var isSelected = false
    fun setData(mediaFiles: ArrayList<ListIdPic>) {
        this.mediaFiles = mediaFiles
    }

    @JvmName("setSpanCount1")
    fun setSpanCount(spanCount: Int) {
        this.spanCount = spanCount
    }

    fun setItemClickEvent(onImageItemClickListner: OnImageItemClickListner?) {
        this.onImageItemClickListner = onImageItemClickListner
    }

    fun setImageResize(imageSize: Int) {
        mImageResize = imageSize
        options = RequestOptions()
            .centerCrop()
            .override(mImageResize, mImageResize)
            .placeholder(R.drawable.ic_placeholder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (!InSafe.isGridlayout) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_file_adapter, parent, false)
            ViewHolderList(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_image_adapter, parent, false)
            ViewHolderGrid(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (InSafe.isGridlayout) 0 else 1
    }

    fun setSelectionMode(selectionMode: Boolean) {
        isSelected = selectionMode
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        if (getItemViewType(holder.absoluteAdapterPosition) == 0) {
            val viewHolderGrid = holder as ViewHolderGrid
            val mediaFile = File(mediaFiles[position].getNewPath())
            val path = mediaFile.absolutePath
            val targetLocation = File(path)
            Glide.with(mContext)
                .load(mediaFiles[position].getNewPath())
                .apply(options!!)
                .into(viewHolderGrid.media_thumbnail)
            viewHolderGrid.tv_filename.text = mediaFiles[position].getDisplayName()
            viewHolderGrid.tv_size.text = TillStr.convertStorage(targetLocation.length())
            viewHolderGrid.tv_filename.visibility = View.GONE
            viewHolderGrid.tv_size.visibility = View.GONE
            if (mediaFiles[position].checked) {
                viewHolderGrid.iv_chek.visibility = View.VISIBLE
            } else {
                viewHolderGrid.iv_chek.visibility = View.INVISIBLE
            }
            viewHolderGrid.iv_chek.setOnClickListener {
                if (onImageItemClickListner != null) {
                    onImageItemClickListner!!.onImageItemClick(position)
                }
            }
            viewHolderGrid.frm_root.setOnClickListener {
                if (onImageItemClickListner != null) {
                    onImageItemClickListner!!.onImageItemClick(position)
                }
            }
            viewHolderGrid.iv_chek.setOnLongClickListener {
                if (onImageItemClickListner != null) {
                    onImageItemClickListner!!.onImageItemLongClick(position)
                }
                false
            }
            viewHolderGrid.frm_root.setOnLongClickListener {
                if (onImageItemClickListner != null) {
                    onImageItemClickListner!!.onImageItemLongClick(position)
                }
                false
            }
        } else if (getItemViewType(position) == 1) {
            val viewHolderList = holder as ViewHolderList
            viewHolderList.media_thumbnail.visibility = View.VISIBLE
            Glide.with(mContext)
                .load(mediaFiles[position].getNewPath())
                .apply(options!!)
                .into(viewHolderList.media_thumbnail)
            viewHolderList.tv_filename.text = mediaFiles[position].getDisplayName()
            viewHolderList.tv_size.text = TillStr.convertStorage(mediaFiles[position].getSize())
            viewHolderList.tv_filename.visibility = View.VISIBLE
            viewHolderList.tv_size.visibility = View.VISIBLE
            if (mediaFiles[position].checked) {
                viewHolderList.iv_chek.visibility = View.VISIBLE
                viewHolderList.iv_chek.setImageResource(R.drawable.check)
            } else {
                viewHolderList.iv_chek.visibility = View.GONE
                viewHolderList.iv_chek.setImageResource(R.drawable.uncheck)
            }
            viewHolderList.iv_chek.setOnClickListener {
                if (onImageItemClickListner != null) {
                    onImageItemClickListner!!.onImageItemClick(position)
                }
            }
            viewHolderList.frm_root.setOnClickListener {
                if (onImageItemClickListner != null) {
                    onImageItemClickListner!!.onImageItemClick(position)
                }
            }
            viewHolderList.iv_chek.setOnLongClickListener {
                if (onImageItemClickListner != null) {
                    onImageItemClickListner!!.onImageItemLongClick(position)
                    isSelected = true
                }
                false
            }
            viewHolderList.frm_root.setOnLongClickListener {
                if (onImageItemClickListner != null) {
                    onImageItemClickListner!!.onImageItemLongClick(position)
                    isSelected = true
                }
                false
            }
        }
    }

    override fun getItemCount(): Int {
        return mediaFiles.size
    }

    inner class ViewHolderGrid internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        var frm_root: LinearLayout
        var iv_chek: ImageView
        var tv_filename: VTv
        var tv_size: VTv
        var media_thumbnail: RoundedImageView

        init {
            media_thumbnail = view.findViewById(R.id.media_thumbnail)
            tv_filename = view.findViewById(R.id.tv_filename)
            tv_size = view.findViewById(R.id.tv_size)
            frm_root = view.findViewById(R.id.frm_root)
            iv_chek = view.findViewById(R.id.iv_chek)
        }
    }

    inner class ViewHolderList internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        var frm_root: LinearLayout
        var media_thumbnail: ImageView
        var iv_chek: ImageView
        var tv_filename: VTv
        var tv_size: VTv

        init {
            media_thumbnail = view.findViewById(R.id.media_thumbnail)
            tv_filename = view.findViewById(R.id.tv_filename)
            tv_size = view.findViewById(R.id.tv_size)
            frm_root = view.findViewById(R.id.frm_root)
            iv_chek = view.findViewById(R.id.iv_chek)
        }
    }
}