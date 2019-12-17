package com.kslimweb.ipolygot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kslimweb.ipolygot.algolia_data.Hit

class AlgoliaSearchAdapter(private val hitsJson: List<Hit>) : RecyclerView.Adapter<AlgoliaSearchAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context), parent)
    }

    override fun getItemCount(): Int {
        return hitsJson.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chapterArabic = hitsJson[position].chapterAra
        val chapterTranslated = hitsJson[position].chapterEng
        val reference = hitsJson[position].reference
        val inBookReference = hitsJson[position].inBookReference

        holder.chapterArabic.text = "Arabic Chapter: " + chapterArabic
        holder.chapterTranslated.text = "English Chapter: " + chapterTranslated

        if (reference != null) {
            val sb = StringBuilder()
            reference.forEachIndexed { index, it ->
                sb.append((index+1).toString() + ". " + it + "\n\t")
            }
            holder.reference.text = "Reference: \n\t" + sb.toString()
            holder.reference.visibility = View.VISIBLE
        }

        if (inBookReference != null) {
            val sb = StringBuilder()
            inBookReference.forEachIndexed { index, it ->
                sb.append((index+1).toString() + ". " + it + "\n\t")
            }
            holder.inBookReference.text = "In-book Reference: \n\t" + sb.toString()
            holder.inBookReference.visibility = View.VISIBLE
        }
    }

    class ViewHolder internal constructor(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.item_search, parent, false)) {
        val chapterArabic: TextView = itemView.findViewById(R.id.chapter_arabic) as TextView
        val chapterTranslated: TextView = itemView.findViewById(R.id.chapter_translated) as TextView
        val reference: TextView = itemView.findViewById(R.id.reference) as TextView
        val inBookReference: TextView = itemView.findViewById(R.id.in_book_reference) as TextView
    }
}