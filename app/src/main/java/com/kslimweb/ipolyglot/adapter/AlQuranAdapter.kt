package com.kslimweb.ipolyglot.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.kslimweb.ipolyglot.R
import com.kslimweb.ipolyglot.model.alquran.HitAlQuran

class AlQuranAdapter(gson: Gson) : RecyclerView.Adapter<AlQuranAdapter.ViewHolder>() {

    private var hitsAlQuran: List<HitAlQuran> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context), parent)
    }

    override fun getItemCount(): Int {
        return hitsAlQuran.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chapter =  hitsAlQuran[position].objectID.raw.split("_")[0]
        val verseNumber =  hitsAlQuran[position].objectID.raw.split("_")[1]
        holder.alQuranChapter.text = "Chapter " + chapter + ", Verse " + verseNumber

        // TODO put highlighted words
        holder.textMeaning.text = hitsAlQuran[position].meaning
        holder.textTranslation.text = hitsAlQuran[position].translation


    }

    fun setData(hitsAlQuran: List<HitAlQuran>) {
        this.hitsAlQuran = hitsAlQuran
        notifyDataSetChanged()
    }

    class ViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.item_search_alquran, parent, false)) {
        val alQuranChapter = itemView.findViewById(R.id.txt_al_quran_chapter) as TextView
        val textMeaning = itemView.findViewById(R.id.txt_meaning) as TextView
        val textTranslation = itemView.findViewById(R.id.txt_translation) as TextView
    }
}
