package com.kslimweb.ipolyglot.util.helper

import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import com.google.gson.Gson
import com.kslimweb.ipolyglot.model.hadith.*
import com.kslimweb.ipolyglot.adapter.HadithAdapter
import com.kslimweb.ipolyglot.util.TextUtils.setTextWithSpan

class AdapterTextViewHelperHadith(private val hitHadiths: List<HitHadith>, private val gson: Gson) {

    private var highlightNumbering = 0

    fun setChapterText(holder: HadithAdapter.ViewHolder, position: Int) {
        holder.chapterArabic.text = "Arabic Chapter: " + hitHadiths[position].chapterAraText
        setTextWithSpan(holder.chapterArabic,
            holder.chapterArabic.text.toString(),
            "Arabic Chapter: ",
            StyleSpan(Typeface.BOLD))

        holder.chapterTranslated.text = "English Chapter: " + hitHadiths[position].chapterEngText
        setTextWithSpan(holder.chapterTranslated,
            holder.chapterTranslated.text.toString(),
            "English Chapter: ",
            StyleSpan(Typeface.BOLD))
    }

    fun setSnippetText(holder: HadithAdapter.ViewHolder, position: Int) {
        val snippetResult = gson.fromJson(hitHadiths[position]._snippetResult.toString(), SnippetResult::class.java)
        if (!snippetResult.contentsAra.isNullOrEmpty())
            setSnippetContentsAra(holder, snippetResult.contentsAra)
        if (!snippetResult.contentsEng.isNullOrEmpty())
            setSnippetContentsEng(holder, snippetResult.contentsEng)
    }

    private fun setSnippetContentsAra(holder: HadithAdapter.ViewHolder, contentsAra: List<ContentAra>) {
        holder.snippetsAra.typeface = Typeface.DEFAULT
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

    private fun setSnippetContentsEng(holder: HadithAdapter.ViewHolder, contentsEng: List<ContentEng>) {
        holder.snippetsEng.typeface = Typeface.DEFAULT
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

    fun setInBookReference(holder: HadithAdapter.ViewHolder, position: Int) {
        val inBookReference = hitHadiths[position].inBookReference
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

    fun setReference(holder: HadithAdapter.ViewHolder, position: Int) {
        val reference = hitHadiths[position].reference
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

    fun setHighlightResultText(holder: HadithAdapter.ViewHolder, position: Int) {
        holder.highlightResults.text = ""
        holder.highlightResults.text = "Found in: \n\t"
        setTextWithSpan(holder.highlightResults,
            holder.highlightResults.text.toString(),
            "Found in: ",
            StyleSpan(Typeface.BOLD))
        val highlightResult = gson.fromJson(hitHadiths[position]._highlightResult.toString(), HighlightResult::class.java)
        setHighlightChapterAra(position, holder, highlightResult.chapterAra)
        setHighlightChapterEng(position, holder, highlightResult.chapterEng)

        if (!highlightResult.contentsAra.isNullOrEmpty())
            setHighlightContentsAra(position, holder, highlightResult.contentsAra)
        if (!highlightResult.contentsEng.isNullOrEmpty())
            setHighlightContentsEng(position, holder, highlightResult.contentsEng)

        highlightNumbering = 0 // reset numbering
    }

    private fun setHighlightChapterAra(position: Int, holder: HadithAdapter.ViewHolder, chapterAra: ChapterAra) {
        if (chapterAra.matchLevel != "none")  {
            hitHadiths[position].highlightedChapterAra?.tokens?.let { tokens ->
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

    private fun setHighlightContentsAra(position: Int, holder: HadithAdapter.ViewHolder, contentsAra: List<ContentAra>) {
        contentsAra.forEachIndexed { index, content ->
            if (content.matchLevel != "none") {
                highlightNumbering += 1
                var isHighlighted = false
                holder.highlightResults.append(highlightNumbering.toString() + ". ")
                hitHadiths[position].highlightedContentsAra?.get(index)?.tokens?.forEachIndexed token@{ tokenIndex, it ->
                    if (tokenIndex > 1 && isHighlighted)
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

    private fun setHighlightChapterEng(position: Int, holder: HadithAdapter.ViewHolder, chapterEng: ChapterEng) {
        if (chapterEng.matchLevel != "none") {
           hitHadiths[position].highlightedChapterEng?.tokens?.let { tokens ->
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

    private fun setHighlightContentsEng(position: Int, holder: HadithAdapter.ViewHolder, contentsEng: List<ContentEng>) {
        contentsEng.forEachIndexed { index, content ->
            if (content.matchLevel != "none") {
                highlightNumbering += 1
                var isHighlighted = false
                holder.highlightResults.append(highlightNumbering.toString() + ". ")
                hitHadiths[position].highlightedContentsEng?.get(index)?.tokens?.forEachIndexed token@{ tokenIndex, it ->
                    if (tokenIndex > 1 && isHighlighted)
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
}