package com.hidefile.secure.folder.vault.dpss

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hidefile.secure.folder.vault.R
import com.hidefile.secure.folder.vault.cluecanva.Bhasha

class MangamtiBhasaPasandKarvaniAdapter(var context: Context, var bhashaList: List<Bhasha>?) :
    RecyclerView.Adapter<MangamtiBhasaPasandKarvaniAdapter.ImageViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val v1 = LayoutInflater.from(parent.context)
            .inflate(R.layout.mangamti_bhasa_pasand_karvani_adpater, parent, false)
        return ImageViewHolder(v1)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val bhasha = bhashaList!![position]
        holder.imgLanguage.setImageResource(bhasha.bhashaidee)
        holder.txtLanguageName.text = bhasha.bhashaName
        holder.radioSelected.isChecked = bhasha.ShuBhashaPasandKari_lidhi_che
    }

    override fun getItemCount(): Int {
        return if (bhashaList != null) bhashaList!!.size else 0
    }

    val selectedItem: Bhasha?
        get() {
            if (bhashaList != null && bhashaList!!.size > 0) {
                for (bhasha in bhashaList!!) {
                    if (bhasha.ShuBhashaPasandKari_lidhi_che) {
                        return bhasha
                    }
                }
            }
            return null
        }

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val imgLanguage: ImageView
        val txtLanguageName: TextView
        val radioSelected: RadioButton

        init {
            imgLanguage = itemView.findViewById(R.id.imgLanguage)
            txtLanguageName = itemView.findViewById(R.id.txtLanguageName)
            radioSelected = itemView.findViewById(R.id.radioSelected)
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            for (bhasha in bhashaList!!) {
                bhasha.ShuBhashaPasandKari_lidhi_che = false
            }
            bhashaList!![bindingAdapterPosition].ShuBhashaPasandKari_lidhi_che = true
            notifyDataSetChanged()
        }
    }
}