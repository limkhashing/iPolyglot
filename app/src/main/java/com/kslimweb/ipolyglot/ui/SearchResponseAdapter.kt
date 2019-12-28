package com.kslimweb.ipolyglot.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kslimweb.ipolyglot.R
import com.kslimweb.ipolyglot.model.hit.Hit
import com.kslimweb.ipolyglot.util.SearchResultTextViewHelper


class SearchResponseAdapter(private val hits: List<Hit>) : RecyclerView.Adapter<SearchResponseAdapter.ViewHolder>() {

    private val searchResultTextViewHelper = SearchResultTextViewHelper(hits)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context), parent)
    }

    override fun getItemCount(): Int {
        return hits.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.chapterNumber.text = hits[position].objectID.toString()

        searchResultTextViewHelper.setChapterText(holder, position)
        searchResultTextViewHelper.setSnippetText(holder, position)
        searchResultTextViewHelper.setReference(holder, position)
        searchResultTextViewHelper.setInBookReference(holder, position)
        searchResultTextViewHelper.setHighlightResultText(holder, position)
    }

    class ViewHolder internal constructor(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.item_search, parent, false)) {
        val chapterNumber: TextView = itemView.findViewById(R.id.txt_chapter_number) as TextView
        val chapterArabic: TextView = itemView.findViewById(R.id.txt_chapter_arabic) as TextView
        val chapterTranslated: TextView = itemView.findViewById(R.id.txt_chapter_translated) as TextView
        val snippetsAra: TextView = itemView.findViewById(R.id.txt_snippet_ara) as TextView
        val snippetsEng: TextView = itemView.findViewById(R.id.txt_snippet_eng) as TextView
        val references: TextView = itemView.findViewById(R.id.txt_reference) as TextView
        val inBookReferences: TextView = itemView.findViewById(R.id.txt_in_book_reference) as TextView
        val highlightResults: TextView = itemView.findViewById(R.id.txt_highlight_result) as TextView
    }
}