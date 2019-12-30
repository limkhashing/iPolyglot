package com.kslimweb.ipolyglot.util

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import android.util.Log
import android.widget.TextView
import com.google.gson.Gson
import com.kslimweb.ipolyglot.model.hit.*
import com.kslimweb.ipolyglot.ui.SearchResponseAdapter

class SearchResultTextViewHelper(private val hits: List<Hit>) {

    private var highlightNumbering = 0

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
        val snippetResult = Gson().fromJson(hits[position]._snippetResult.toString(), SnippetResult::class.java)
        setSnippetContentsAra(holder, snippetResult.contentsAra)
        setSnippetContentsEng(holder, snippetResult.contentsEng)
    }

    private fun setSnippetContentsAra(holder: SearchResponseAdapter.ViewHolder, contentsAra: List<ContentAra>) {
        val sb = StringBuilder()
        contentsAra.forEachIndexed { index, it ->
            if (it.matchLevel != "none") {
                if (index != contentsAra.lastIndex)
                    sb.append((index+1).toString() + ". " +
                            it.value.replace("<em>", "")
                                .replace("</em>", "") + "...\n\t")
                else
                    sb.append((index+1).toString() + ". " +
                            it.value.replace("<em>", "")
                                .replace("</em>", "") + "...")
            }
        }
        holder.snippetsAra.text = "Arabic Contents: \n\t" + sb.toString()
        setTextWithSpan(holder.snippetsAra,
            holder.snippetsAra.text.toString(),
            "Arabic Contents: ",
            StyleSpan(Typeface.BOLD))
    }

    private fun setSnippetContentsEng(holder: SearchResponseAdapter.ViewHolder, contentsEng: List<ContentEng>) {
        val sb = StringBuilder()
        contentsEng.forEachIndexed { index, it ->
            if (it.matchLevel != "none") {
                if (index != contentsEng.lastIndex)
                    sb.append((index+1).toString() + ". " +
                            it.value.replace("<em>", "")
                                .replace("</em>", "") + "...\n\t")
                else
                    sb.append((index+1).toString() + ". " +
                            it.value.replace("<em>", "")
                                .replace("</em>", "") + "...")
            }
        }
        holder.snippetsEng.text = "English Contents: \n\t" + sb.toString()
        setTextWithSpan(holder.snippetsEng,
            holder.snippetsEng.text.toString(),
            "English Contents: ",
            StyleSpan(Typeface.BOLD))
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
            holder.inBookReferences.text = "In-book Reference: \n\t" + sb.toString()
            setTextWithSpan(holder.inBookReferences,
                holder.inBookReferences.text.toString(),
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
            holder.references.text = "Reference: \n\t" + sb.toString()
            setTextWithSpan(holder.references,
                holder.references.text.toString(),
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
        setHighlightChapterAra(position, holder, highlightResult.chapterAra)
        setHighlightContentsAra(position, holder, highlightResult.contentsAra)
        setHighlightChapterEng(position, holder, highlightResult.chapterEng)
        setHighlightContentsEng(position, holder, highlightResult.contentsEng)
        highlightNumbering = 0 // reset numbering
    }

    private fun setHighlightChapterAra(position: Int, holder: SearchResponseAdapter.ViewHolder, chapterAra: ChapterAra) {
        if (chapterAra.matchLevel != "none")  {
            hits[position].highlightedChapterAra?.tokens?.let { tokens ->
                highlightNumbering += 1
                var isHighlighted = false
                holder.highlightResults.append(highlightNumbering.toString() + ". ")
                tokens.forEachIndexed { tokenIndex, it ->
                    if (tokenIndex > 4 && isHighlighted)
                        return@forEachIndexed

                    if (it.highlighted) {
                        val highlightedContent = SpannableString(it.content)
                        highlightedContent.setSpan(StyleSpan(Typeface.BOLD),
                            it.content.indexOf(it.content),
                            it.content.length,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                        holder.highlightResults.append(highlightedContent)
                        isHighlighted = true
                    }
                    else
                        holder.highlightResults.append(" " + it.content + " ")
                }
                holder.highlightResults.append("...\n\t")
            }
        }
    }

    private fun setHighlightContentsAra(position: Int, holder: SearchResponseAdapter.ViewHolder, contentsAra: List<ContentAra>) {
        contentsAra.forEachIndexed { index, content ->
            if (content.matchLevel != "none") {
                highlightNumbering += 1
                var isHighlighted = false
                holder.highlightResults.append(highlightNumbering.toString() + ". ")
                hits[position].highlightedContentsAra?.get(index)?.tokens?.forEachIndexed token@{ tokenIndex, it ->
                    if (tokenIndex > 4 && isHighlighted)
                        return@token

                    if (it.highlighted) {
                        val highlightedContent = SpannableString(it.content)
                        highlightedContent.setSpan(
                            StyleSpan(Typeface.BOLD),
                            it.content.indexOf(it.content),
                            it.content.length,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        holder.highlightResults.append(highlightedContent)
                        isHighlighted = true
                    } else
                        holder.highlightResults.append(" " + it.content + " ")
                }
                holder.highlightResults.append("...\n\t")
            }
        }
    }

    private fun setHighlightChapterEng(position: Int, holder: SearchResponseAdapter.ViewHolder, chapterEng: ChapterEng) {
        if (chapterEng.matchLevel != "none") {
           hits[position].highlightedChapterEng?.tokens?.let { tokens ->
               highlightNumbering += 1
               var isHighlighted = false
               holder.highlightResults.append(highlightNumbering.toString() + ". ")
               tokens.forEachIndexed { tokenIndex, it ->
                   if (tokenIndex > 4 && isHighlighted)
                       return@forEachIndexed

                   if (it.highlighted) {
                       val highlightedContent = SpannableString(it.content)
                       highlightedContent.setSpan(StyleSpan(Typeface.BOLD),
                           it.content.indexOf(it.content),
                           it.content.length,
                           Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                       holder.highlightResults.append(highlightedContent)
                       isHighlighted = true
                   }
                   else
                       holder.highlightResults.append(" " + it.content + " ")
               }
               holder.highlightResults.append("...\n\t")
           }
       }
    }

    private fun setHighlightContentsEng(position: Int, holder: SearchResponseAdapter.ViewHolder, contentsEng: List<ContentEng>) {
        contentsEng.forEachIndexed { index, content ->
            if (content.matchLevel != "none") {
                highlightNumbering += 1
                var isHighlighted = false
                holder.highlightResults.append(highlightNumbering.toString() + ". ")
                hits[position].highlightedContentsEng?.get(index)?.tokens?.forEachIndexed token@{ tokenIndex, it ->
                    if (tokenIndex > 4 && isHighlighted)
                        return@token

                    if (it.highlighted) {
                        val highlightedContent = SpannableString(it.content)
                        highlightedContent.setSpan(
                            StyleSpan(Typeface.BOLD),
                            it.content.indexOf(it.content),
                            it.content.length,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                        isHighlighted = true
                        holder.highlightResults.append(highlightedContent)
                    } else
                        holder.highlightResults.append(" " + it.content + " ")
                }
                holder.highlightResults.append("...\n\t")
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