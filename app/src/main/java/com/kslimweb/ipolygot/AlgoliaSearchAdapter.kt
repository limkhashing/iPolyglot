package com.kslimweb.ipolygot

import android.util.Log
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

        holder.chapterNumber.text = hitsJson[position].objectID
        holder.chapterArabic.text = "Arabic Chapter: " + hitsJson[position].chapterAra
        holder.chapterTranslated.text = "English Chapter: " + hitsJson[position].chapterEng

        val reference = hitsJson[position].reference
        if (reference != null) {
            val sb = StringBuilder()
            reference.forEachIndexed { index, it ->
                if (index != reference.lastIndex)
                    sb.append((index+1).toString() + ". " + it + "\n\t")
                else
                    sb.append((index+1).toString() + ". " + it)
            }
            holder.reference.text = "Reference: \n\t" + sb.toString()
            holder.reference.visibility = View.VISIBLE
        }

        val inBookReference = hitsJson[position].inBookReference
        if (inBookReference != null) {
            val sb = StringBuilder()
            inBookReference.forEachIndexed { index, it ->
                if (index != inBookReference.lastIndex)
                    sb.append((index+1).toString() + ". " + it + "\n\t")
                else
                    sb.append((index+1).toString() + ". " + it)
            }
            holder.inBookReference.text = "In-book Reference: \n\t" + sb.toString()
            holder.inBookReference.visibility = View.VISIBLE
        }
    }

    class ViewHolder internal constructor(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.item_search, parent, false)) {
        val chapterNumber: TextView = itemView.findViewById(R.id.txt_chapter_number) as TextView
        val chapterArabic: TextView = itemView.findViewById(R.id.txt_chapter_arabic) as TextView
        val chapterTranslated: TextView = itemView.findViewById(R.id.txt_chapter_translated) as TextView
        val reference: TextView = itemView.findViewById(R.id.txt_reference) as TextView
        val inBookReference: TextView = itemView.findViewById(R.id.txt_in_book_reference) as TextView
    }
}