package com.kslimweb.ipolyglot.ui

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.toSpanned
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.helper.android.highlighting.toSpannedString
import com.kslimweb.ipolyglot.R
import com.kslimweb.ipolyglot.model.hit.Hit


class AlgoliaSearchAdapter(private val hitsJson: List<Hit>) : RecyclerView.Adapter<AlgoliaSearchAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context),
            parent
        )
    }

    override fun getItemCount(): Int {
        return hitsJson.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.chapterNumber.text = hitsJson[position].objectID

        setChapterText(holder, position)
        setReference(holder, position)
        setInBookReference(holder, position)
        val highlightedResults= getHighlightResult(position)
        setHighlightedResultText(holder, highlightedResults)
    }

    private fun setHighlightedResultText(holder: ViewHolder, highlightedResults: MutableList<String>) {
        val sb = StringBuilder()
        highlightedResults.forEachIndexed { index, it ->
            if (index != highlightedResults.lastIndex)
                sb.append((index+1).toString() + ". " + it + "\n\t")
            else
                sb.append((index+1).toString() + ". " + it.toSpanned())
        }
        holder.highlightedResults.text = "Highlighted Result: \n\t" + sb.toString()
        holder.highlightedResults.visibility = View.VISIBLE
        setTextWithSpan(holder.highlightedResults,
            holder.highlightedResults.text.toString(),
            "Highlighted Result: ",
            StyleSpan(Typeface.BOLD))
    }

    private fun setInBookReference(holder: ViewHolder, position: Int) {
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
            setTextWithSpan(holder.inBookReference,
                holder.inBookReference.text.toString(),
                "In-book Reference: ",
                StyleSpan(Typeface.BOLD))
        }
    }

    private fun setReference(holder: ViewHolder, position: Int) {
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
            setTextWithSpan(holder.reference,
                holder.reference.text.toString(),
                "Reference: ",
                StyleSpan(Typeface.BOLD))
        }
    }

    private fun setChapterText(
        holder: ViewHolder,
        position: Int
    ) {
        holder.chapterArabic.text = "Arabic Chapter: " + hitsJson[position].chapterAraText
        setTextWithSpan(holder.chapterArabic,
            holder.chapterArabic.text.toString(),
            "Arabic Chapter: ",
            StyleSpan(Typeface.BOLD))

        holder.chapterTranslated.text = "English Chapter: " + hitsJson[position].chapterEngText
        setTextWithSpan(holder.chapterTranslated,
            holder.chapterTranslated.text.toString(),
            "English Chapter: ",
            StyleSpan(Typeface.BOLD))
    }

    private fun getHighlightResult(position: Int): MutableList<String> {
        val highlightResultList = mutableListOf<String>()
//        hitsJson[position].highlightResult?.let{ snippetResult ->
//
//            val snippetResultChapterAra = snippetResult.highlightedChapterAra
//            val snippetResultChapterEng = snippetResult.highlightedChapterEng
//            val snippetResultContentAraList = snippetResult.highlightedContentAra
//            val snippetResultContentEngList = snippetResult.highlightedContentEng
//
//            Log.d("Adapter", snippetResultChapterAra?.toSpannedString().toString() + " abc")
//            Log.d("Adapter", snippetResultChapterEng?.toSpannedString().toString() + " abc")
//
//            snippetResultChapterAra?.let {
//                highlightResultList.add(it.toSpannedString().toString())
//            }
//
//            snippetResultChapterEng?.let {
//                highlightResultList.add(it.toSpannedString().toString())
//            }
//
//        }
        return highlightResultList
    }

    private fun setTextWithSpan(textView: TextView, text: String, spanText: String, style: StyleSpan?) {
        val sb = SpannableStringBuilder(text)
        val start = text.indexOf(spanText)
        val end = start + spanText.length
        sb.setSpan(style, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        textView.text = sb
    }

    class ViewHolder internal constructor(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.item_search, parent, false)) {
        val chapterNumber: TextView = itemView.findViewById(R.id.txt_chapter_number) as TextView
        val chapterArabic: TextView = itemView.findViewById(R.id.txt_chapter_arabic) as TextView
        val chapterTranslated: TextView = itemView.findViewById(R.id.txt_chapter_translated) as TextView
        val reference: TextView = itemView.findViewById(R.id.txt_reference) as TextView
        val inBookReference: TextView = itemView.findViewById(R.id.txt_in_book_reference) as TextView
        val highlightedResults: TextView = itemView.findViewById(R.id.txt_snippet_result) as TextView
    }
}