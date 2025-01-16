package com.hidefile.secure.folder.vault.dpss

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.Glide
import com.hidefile.secure.folder.vault.R
import com.hidefile.secure.folder.vault.cluecanva.TreeRoot
import com.hidefile.secure.folder.vault.cluecanva.VTv
import xiomaradrawn.illemire.saoirse.ListCommand
import java.io.File
import java.text.DecimalFormat
import java.util.ArrayList

class ListFileAdp(var mContext: Context, private var mediaFiles: ArrayList<TreeRoot>) :
    RecyclerView.Adapter<ListFileAdp.ViewHolder>() {
    var onImageItemClickListner: OnImageItemClickListner? = null
    var isSelected = false
    fun setSelectionMode(selectionMode: Boolean) {
        isSelected = selectionMode
        notifyDataSetChanged()
    }

    fun setItemClickEvent(onImageItemClickListner: OnImageItemClickListner?) {
        this.onImageItemClickListner = onImageItemClickListner
    }

    fun setImageResize(imageSize: Int) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.row_file_adapter, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val mediaFile = File(mediaFiles[position].getNewPath())
        Log.e(
            "name and size",
            "1 ==> " + mediaFiles[position].getDisplayName() + "\n" + getStringSizeLengthFile(
                mediaFiles[position].size
            ) + "\n" + mediaFile.exists()
        )
        val fname = mediaFile.name.substring(0, mediaFile.name.length - 4)
        val extention2 = ListCommand.getFileExtension(mediaFile.name)
        val extention1 = ListCommand.getFileExtension(fname)
        val extention = extention1 + extention2
        val options = RequestOptions().placeholder(getFileIcon(extention))
        val parth = mediaFile.absolutePath
        val targetLocation = File(parth)
        holder.tv_filename.text = mediaFiles.get(position).getDisplayName()
        Log.e(
            "name and size",
            "==> " + mediaFiles[position].getDisplayName() + "\n" + getStringSizeLengthFile(
                mediaFiles[position].size
            )
        )
        holder.tv_size.text =
            getStringSizeLengthFile(
                mediaFiles.get(position).size
            )
        holder.media_thumbnail.visibility = View.VISIBLE
        Glide.with(mContext)
            .load(mediaFiles[holder.absoluteAdapterPosition].getNewPath())
            .apply(options)
            .into(holder.media_thumbnail)
        if (mediaFiles[position].checked) {
            holder.iv_chek.visibility = View.VISIBLE
            holder.iv_chek.setImageResource(R.drawable.check)
        } else {
            holder.iv_chek.setImageResource(R.drawable.uncheck)
            if (isSelected) holder.iv_chek.visibility =
                View.VISIBLE else holder.iv_chek.visibility = View.INVISIBLE
        }
        holder.iv_chek.setOnClickListener {
            if (onImageItemClickListner != null) {
                onImageItemClickListner!!.onImageItemClick(position)
            }
        }
        holder.frm_root.setOnClickListener {
            if (onImageItemClickListner != null) {
                onImageItemClickListner!!.onImageItemClick(position)
            }
        }
        holder.iv_chek.setOnLongClickListener {
            if (onImageItemClickListner != null) {
                onImageItemClickListner!!.onImageItemLongClick(position)
            }
            isSelected = true
            false
        }
        holder.frm_root.setOnLongClickListener {
            if (onImageItemClickListner != null) {
                onImageItemClickListner!!.onImageItemLongClick(position)
            }
            isSelected = true
            false
        }
    }

    override fun getItemCount(): Int {
        return mediaFiles.size
    }

    fun setData(videoList: ArrayList<TreeRoot>) {
        mediaFiles = videoList
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var frm_root: LinearLayout
        var iv_chek: ImageView
        var tv_filename: VTv
        var tv_size: VTv
        val media_thumbnail: ImageView

        init {
            media_thumbnail = view.findViewById(R.id.media_thumbnail)
            frm_root = view.findViewById(R.id.frm_root)
            iv_chek = view.findViewById(R.id.iv_chek)
            tv_filename = view.findViewById(R.id.tv_filename)
            tv_size = view.findViewById(R.id.tv_size)
        }
    }

    companion object {
        fun getFileIcon(extention: String?): Int {
            //todo
//            when (extention) {
//                "apkbin", "apk" -> return R.drawable.vi_apk
//                "pdf", "pdfbin" -> return R.drawable.vi_pdf
//                "doc", "docx", "docbin", "docxbin" -> return R.drawable.vi_doc
//                "ppt", "pptbin", "pptx", "pptxbin" -> return R.drawable.vi_ppt
//                "xls", "xlsbin", "xlsx", "xlsxbin" -> return R.drawable.vi_xle
//                "txt", "txtbin", "csv", "csvbin", "rtf", "rtfbin", "odt", "odtbin" -> return R.drawable.vi_txt
//                "png", "pngbin" -> return R.drawable.vi_png
//                "jpg", "jpgbin", "svg", "svgbin", "bmp", "bmpbin" -> return R.drawable.vi_jpg
//                "gif", "gifbin" -> return R.drawable.vi_gif
//                "mp4", "mp4bin", "3gp", "3gpbin", "3gpp", "3gppbin", "3gpp2", "3gpp2bin", "mpeg", "mpegpng", "mkv", "mkvpng", "mov", "movpng" -> return R.drawable.vi_video
//                "aac", "aacbin", "flac", "flacbin", "m4a", "m4abin", "mp3", "mp3bin", "oga", "ogabin", "wav", "wavbin", "wma", "wmabin" -> return R.drawable.vi_mp3
//                "html", "htmlbin", "html5", "html5bin", "htm", "htmbin", "css", "cssbin", "asp", "aspbin" -> return R.drawable.vi_html
//                "zip", "zipbin", "rar", "rarbin", "rar4", "rar4bin" -> return R.drawable.vi_zip
//                else -> return R.drawable.vi_other
//            }
            return R.drawable.vi_other
        }

        fun getStringSizeLengthFile(size: Long): String {
            val df = DecimalFormat("0.00")
            val sizeKb = 1024.0f
            val sizeMb = sizeKb * sizeKb
            val sizeGb = sizeMb * sizeKb
            val sizeTerra = sizeGb * sizeKb
            if (size < sizeMb) return df.format((size / sizeKb).toDouble()) + " Kb" else if (size < sizeGb) return df.format(
                (size / sizeMb).toDouble()
            ) + " Mb" else if (size < sizeTerra) return df.format((size / sizeGb).toDouble()) + " Gb"
            return ""
        }
    }
}