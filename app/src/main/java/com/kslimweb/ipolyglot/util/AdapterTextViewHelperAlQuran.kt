package com.kslimweb.ipolyglot.util

import com.algolia.instantsearch.core.highlighting.HighlightedString
import com.google.gson.Gson
import com.kslimweb.ipolyglot.adapter.SearchResponseAlQuranAdapter
import com.kslimweb.ipolyglot.model.alquran.HighlightResult
import com.kslimweb.ipolyglot.model.alquran.HitAlQuran
import com.kslimweb.ipolyglot.model.alquran.Verse
import com.kslimweb.ipolyglot.model.alquran.VerseData
import kotlin.reflect.full.declaredMemberProperties

class AdapterTextViewHelperAlQuran(private val gson: Gson) {

    // TODO set bold for highlighted text and verse number
    //  show progress dialog
    fun setHighlightResultText(holder: SearchResponseAlQuranAdapter.ViewHolder,
                               position: Int,
                               hitsAlQuran: List<HitAlQuran>) {

        val verseData = mutableListOf<VerseData>()
        val listOfHighlightedStrings = mutableListOf<List<HighlightedString>>()

        val highlightResult = gson.fromJson(
            hitsAlQuran[position]._highlightResult.toString(),
            HighlightResult::class.java
        )

        HitAlQuran::class.declaredMemberProperties.forEach {
            val hitAlQuran = it.get(hitsAlQuran[position])
            if (hitAlQuran != null) {
                try {
                    val highlightedStrings = hitAlQuran as List<HighlightedString>
                    listOfHighlightedStrings.add(highlightedStrings)
                } catch (e: ClassCastException) { }
            }
        }

        HighlightResult::class.declaredMemberProperties.forEach {
            val verseField = it.get(highlightResult)
            if (verseField != null) {
                val verseNumber = it.name
                val verseList = verseField as List<Verse>
                verseData.add(VerseData(verseNumber, verseList, listOfHighlightedStrings))
            }
        }
        setHighlightMeaning(holder, verseData as List<VerseData>)
    }

    private fun setHighlightMeaning(holder: SearchResponseAlQuranAdapter.ViewHolder,
                                    verseData: List<VerseData>) {

        verseData.forEachIndexed { verseDataIndex, record ->
            record.verseList.forEachIndexed { index, verse ->
                if (verse.matchLevel != "none") {

                    val verseValue = verse.value.replace("<em>", "").replace("</em>", "")
                    holder.textMeaning.append( "(${record.verseNumber}) " + verseValue + "\n\n")
                    holder.textTranslation.append("(${record.verseNumber}) " + record.verseList[0].value + "\n\n")

                    //                    val highlightedStrings = record.listOfHighlightedStrings[verseDataIndex]
//                    highlightedStrings.forEach {
//                        it.tokens.forEach { token ->
//                            if (token.highlighted) {
//                                Log.d("Adapter", token.content)
//
//                                val highlightedContent = SpannableString(token.content)
//                                highlightedContent.setSpan(token.content,
//                                    token.content.indexOf(token.content),
//                                    token.content.length,
//                                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//
//                                holder.textMeaning.append(highlightedContent)
//                            } else {
////                                holder.textMeaning.append(" " + token.content + " ")
//                            }
//                        }
//                    }
                }
            }
        }
    }
}
