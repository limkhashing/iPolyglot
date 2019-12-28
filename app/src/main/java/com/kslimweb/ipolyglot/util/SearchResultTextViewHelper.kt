package com.kslimweb.ipolyglot.util

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import android.widget.TextView
import com.google.gson.Gson
import com.kslimweb.ipolyglot.model.hit.*
import com.kslimweb.ipolyglot.ui.SearchResponseAdapter

class SearchResultTextViewHelper(private val hits: List<Hit>) {

    private var highlighNumbering = 0

    fun setChapterText(holder: SearchResponseAdapter.ViewHolder, position: Int) {
        holder.chapterArabic.text = "Arabic Chapter: " + hits[position].chapterAraText
        setTextWithSpan(holder.chapterArabic,
            holder.chapterArabic.text.toString(),
            "Arabic Chapter: ",
            StyleSpan(Typeface.BOLD))

        holder.chapterTranslated.text = "English Chapter: " + hits[position].chapterEngText
        setTextWithSpan(holder.chapterTranslated,
            holder.chapterTranslated.text.toString(),
            "English Chapter: ",
            StyleSpan(Typeface.BOLD))
    }

    fun setSnippetText(holder: SearchResponseAdapter.ViewHolder, position: Int) {
        holder.chapterTranslated.text = "Contents: \n\t"
        setTextWithSpan(holder.snippets,
            holder.snippets.text.toString(),
            "Contents: ",
            StyleSpan(Typeface.BOLD))

//        holder.chapterTranslated.append(hits[position].snippetResult.value + "\n")
    }

    fun setInBookReference(holder: SearchResponseAdapter.ViewHolder, position: Int) {
        val inBookReference = hits[position].inBookReference
        if (inBookReference != null) {
            val sb = StringBuilder()
            inBookReference.forEachIndexed { index, it ->
                if (index != inBookReference.lastIndex)
                    sb.append((index+1).toString() + ". " + it + "\n\t")
                else
                    sb.append((index+1).toString() + ". " + it)
            }
            holder.inBookReference.text = "In-book Reference: \n\t" + sb.toString()
            setTextWithSpan(holder.inBookReference,
                holder.inBookReference.text.toString(),
                "In-book Reference: ",
                StyleSpan(Typeface.BOLD))
        }
    }

    fun setReference(holder: SearchResponseAdapter.ViewHolder, position: Int) {
        val reference = hits[position].reference
        if (reference != null) {
            val sb = StringBuilder()
            reference.forEachIndexed { index, it ->
                if (index != reference.lastIndex)
                    sb.append((index+1).toString() + ". " + it + "\n\t")
                else
                    sb.append((index+1).toString() + ". " + it)
            }
            holder.reference.text = "Reference: \n\t" + sb.toString()
            setTextWithSpan(holder.reference,
                holder.reference.text.toString(),
                "Reference: ",
                StyleSpan(Typeface.BOLD))
        }
    }

    fun setHighlightResultText(holder: SearchResponseAdapter.ViewHolder, position: Int) {
        holder.highlightResults.text = ""
        holder.highlightResults.text = "Found in: \n\t"
        setTextWithSpan(holder.highlightResults,
            holder.highlightResults.text.toString(),
            "Found in: ",
            StyleSpan(Typeface.BOLD))

        val highlightResult = Gson().fromJson(hits[position]._highlightResult.toString(), HighlightResult::class.java)

        setChapterAraHighlightText(position, holder, highlightResult.chapterAra)
        setContentsAraHighlightText(position, holder, highlightResult.contentsAra)
        setChapterEngHighlightText(position, holder, highlightResult.chapterEng)
        setContentsEngHighlightText(position, holder, highlightResult.contentsEng)
    }

    private fun setChapterAraHighlightText(position: Int, holder: SearchResponseAdapter.ViewHolder, chapterAra: ChapterAra) {
        if (chapterAra.matchLevel != "none")  {
            hits[position].highlightedChapterAra?.tokens?.let { tokens ->
                highlighNumbering += 1
                holder.highlightResults.append(highlighNumbering.toString() + ". ")
                tokens.forEach {
                    if (it.highlighted) {
                        val highlightedContent = SpannableString(it.content)
                        highlightedContent.setSpan(StyleSpan(Typeface.BOLD),
                            it.content.indexOf(it.content),
                            it.content.length,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                        holder.highlightResults.append(highlightedContent)
                    }
                    else {
                        holder.highlightResults.append(" " + it.content + " ")
                    }
                }
                holder.highlightResults.append("\n\t")
            }
        }
    }

    private fun setContentsAraHighlightText(position: Int, holder: SearchResponseAdapter.ViewHolder, contentsAra: List<ContentAra>) {
        contentsAra.forEachIndexed { index, content ->
            if (content.matchLevel != "none") {
                highlighNumbering += 1
                holder.highlightResults.append(highlighNumbering.toString() + ". ")
                hits[position].highlightedContentsAra?.get(index)?.tokens?.forEach {
                    if (it.highlighted) {
                        val highlightedContent = SpannableString(it.content)
                        highlightedContent.setSpan(
                            StyleSpan(Typeface.BOLD),
                            it.content.indexOf(it.content),
                            it.content.length,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        holder.highlightResults.append(highlightedContent)
                    } else {
                        holder.highlightResults.append(" " + it.content + " ")
                    }
                }
                holder.highlightResults.append("\n\t")
            }
        }
    }

    private fun setChapterEngHighlightText(position: Int, holder: SearchResponseAdapter.ViewHolder, chapterEng: ChapterEng) {
       if (chapterEng.matchLevel != "none") {
           hits[position].highlightedChapterEng?.tokens?.let { tokens ->
               highlighNumbering += 1
               holder.highlightResults.append(highlighNumbering.toString() + ". ")
               tokens.forEach {
                   if (it.highlighted) {
                       val highlightedContent = SpannableString(it.content)
                       highlightedContent.setSpan(StyleSpan(Typeface.BOLD),
                           it.content.indexOf(it.content),
                           it.content.length,
                           Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                       holder.highlightResults.append(highlightedContent)
                   }
                   else {
                       holder.highlightResults.append(" " + it.content + " ")
                   }
               }
               holder.highlightResults.append("\n\t")
           }
       }

    }

    private fun setContentsEngHighlightText(position: Int, holder: SearchResponseAdapter.ViewHolder, contentsEng: List<ContentEng>) {
        contentsEng.forEachIndexed { index, content ->
            if (content.matchLevel != "none") {
                highlighNumbering += 1
                holder.highlightResults.append(highlighNumbering.toString() + ". ")
                hits[position].highlightedContentsEng?.get(index)?.tokens?.forEach {
                    if (it.highlighted) {
                        val highlightedContent = SpannableString(it.content)
                        highlightedContent.setSpan(
                            StyleSpan(Typeface.BOLD),
                            it.content.indexOf(it.content),
                            it.content.length,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        holder.highlightResults.append(highlightedContent)
                    } else {
                        holder.highlightResults.append(" " + it.content + " ")
                    }
                }
                holder.highlightResults.append("\n\t")
            }
        }
    }

    private fun setTextWithSpan(textView: TextView, text: String, spanText: String, style: StyleSpan?) {
        val sb = SpannableStringBuilder(text)
        val start = text.indexOf(spanText)
        val end = start + spanText.length
        sb.setSpan(style, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        textView.text = sb
    }
}