package com.kslimweb.ipolyglot.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.kslimweb.ipolyglot.R
import com.kslimweb.ipolyglot.model.alquran.HitAlQuran
import com.kslimweb.ipolyglot.util.AdapterTextViewHelperAlQuran

// TODO set a better UI - Sticky Header
class SearchResponseAlQuranAdapter(private var hitsAlQuran: List<HitAlQuran>, gson: Gson)
    : RecyclerView.Adapter<SearchResponseAlQuranAdapter.ViewHolder>() {

    private val adapterTextViewHelperAlQuran = AdapterTextViewHelperAlQuran(gson)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context), parent)
    }

    override fun getItemCount(): Int {
        return hitsAlQuran.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.alQuranChapter.text = "Chapter " + hitsAlQuran[position].objectID.raw
        holder.textMeaning.text = ""
        holder.textTranslation.text = ""
        adapterTextViewHelperAlQuran.setHighlightResultText(holder, position, hitsAlQuran)
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
