package com.kslimweb.ipolyglot.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kslimweb.ipolyglot.R
import com.kslimweb.ipolyglot.model.alquran.HitAlQuran
import com.kslimweb.ipolyglot.util.AdapterTextViewHelperAlQuran


class SearchResponseAlQuranAdapter(private val hitsAlQuran: List<HitAlQuran>)
    : RecyclerView.Adapter<SearchResponseAlQuranAdapter.ViewHolder>() {

    private val adapterTextViewHelperAlQuran = AdapterTextViewHelperAlQuran(hitsAlQuran)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context), parent)
    }

    override fun getItemCount(): Int {
        return hitsAlQuran.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.alQuranChapter.text = "Chapter " + hitsAlQuran[position].objectID.raw
        adapterTextViewHelperAlQuran.setHighlightResultText(holder, position)
    }

    class ViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.item_search_alquran, parent, false)) {
        val alQuranChapter = itemView.findViewById(R.id.txt_al_quran_chapter) as TextView
    }
}

//val STICKY_ITEM_TYPE = ItemType.STICKY.ordinal
//val TEXT_ITEM_TYPE = ItemType.SIMPLE_TEXT.ordinal
//
//class SearchResponseAlQuranAdapter(val hitsAlQuran: List<HitAlQuran>) :
//    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
//
//    private val adapterTextViewHelperAlQuran = AdapterTextViewHelperAlQuran(hitsAlQuran)
//
//    override fun getItemViewType(position: Int) =
//        if (hitsAlQuran[position].objectID.raw != "") {
//            STICKY_ITEM_TYPE
//        } else {
//            TEXT_ITEM_TYPE
//        }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        val isStickyItem = viewType == STICKY_ITEM_TYPE
//        val layoutInflater = LayoutInflater.from(parent.context)
//
//        return if (isStickyItem) {
//            StickyViewHolder(layoutInflater.inflate(R.layout.item_sticky_header, parent, false))
//        } else {
//            TextViewHolder(layoutInflater.inflate(R.layout.item_search, parent, false))
//        }
//    }
//
//    override fun getItemCount(): Int = hitsAlQuran.size
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        if (holder is TextViewHolder) {
//            holder.textMeaning?.text = "Meaning"
//            holder.textTranslation?.text = "Translation"
//        }
////        holder.alQuranChapter.text = "Chapter " + hitsAlQuran[position].objectID.toString()
//    }
//
//    open inner class TextViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val textMeaning = itemView.findViewById(R.id.txt_meaning) as? TextView
//        val textTranslation = itemView.findViewById(R.id.txt_translation) as? TextView
//    }
//
//    inner class StickyViewHolder(itemView: View) : TextViewHolder(itemView), StickyHeader {
//        override val stickyId= hitsAlQuran[adapterPosition].objectID.raw
//    }
//}