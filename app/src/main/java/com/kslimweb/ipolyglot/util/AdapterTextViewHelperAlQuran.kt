package com.kslimweb.ipolyglot.util

import android.util.Log
import com.google.gson.Gson
import com.kslimweb.ipolyglot.adapter.SearchResponseAlQuranAdapter
import com.kslimweb.ipolyglot.model.alquran.HighlightResult
import com.kslimweb.ipolyglot.model.alquran.HitAlQuran
import com.kslimweb.ipolyglot.model.alquran.Verse
import com.kslimweb.ipolyglot.model.alquran.VerseData
import kotlin.reflect.full.declaredMemberProperties

class AdapterTextViewHelperAlQuran {

    // TODO set bold for highlighted text
    private val verseData = mutableListOf<VerseData>()

    fun setHighlightResultText(holder: SearchResponseAlQuranAdapter.ViewHolder,
                               position: Int,
                               hitsAlQuran: List<HitAlQuran>) {

        val highlightResult = Gson().fromJson(
            hitsAlQuran[position]._highlightResult.toString(),
            HighlightResult::class.java
        )

        HighlightResult::class.declaredMemberProperties.forEach {
            val verseField = it.get(highlightResult)
            if (verseField != null) {
                val verseNumber = it.name
                val verseList = verseField as List<Verse>
                verseData.add(VerseData(verseNumber, verseList))
            }
        }
        setHighlightMeaning(holder, verseData as List<VerseData>)
    }

    private fun setHighlightMeaning(holder: SearchResponseAlQuranAdapter.ViewHolder,
                                    verseData: List<VerseData>) {

        verseData.forEachIndexed { verseDataIndex, record ->
            record.verseList.forEachIndexed { index, verse ->
                if (verse.matchLevel != "none") {
                    holder.textMeaning.text = "(${record.verseNumber}) " + verse.value
                    holder.textTranslation.text = "(${record.verseNumber}) " + verseData[verseDataIndex].verseList[0].value
                }
            }
        }

//        verseList.forEachIndexed { index, content ->
//            if (content.matchLevel != "none") {
//                holder.textMeaning.text = "($verseNumber) " + content.value
//                holder.textTranslation.text = "($verseNumber) " + content.value
//
//    //                hitAlQuran[position].verse_1?.get(index)
//    //                    ?.tokens?.forEachIndexed token@{ tokenIndex, it ->
//    //                    if (it.highlighted) {
//    //                        val highlightedContent = SpannableString(it.content)
//    //                        highlightedContent.setSpan(
//    //                            StyleSpan(Typeface.BOLD),
//    //                            it.content.indexOf(it.content),
//    //                            it.content.length,
//    //                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
//    //                        )
//    //                        holder.textMeaning.append(highlightedContent)
//    //                    } else
//    //                        holder.textMeaning.append(" " + it.content + " ")
//    //                }
//            }
//        }
    }
}
