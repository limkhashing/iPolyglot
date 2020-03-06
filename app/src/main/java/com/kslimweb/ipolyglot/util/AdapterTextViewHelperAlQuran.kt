package com.kslimweb.ipolyglot.util

import com.algolia.instantsearch.core.highlighting.HighlightedString
import com.google.gson.Gson
import com.kslimweb.ipolyglot.adapter.AlQuranAdapter
import com.kslimweb.ipolyglot.model.alquran.test.HighlightResult
import com.kslimweb.ipolyglot.model.alquran.test.TestHitAlQuran
import com.kslimweb.ipolyglot.model.alquran.test.Verse
import com.kslimweb.ipolyglot.model.alquran.test.VerseData
import kotlin.reflect.full.declaredMemberProperties

class AdapterTextViewHelperAlQuran(private val gson: Gson) {

    fun setHighlightResultText(holder: AlQuranAdapter.ViewHolder,
                               position: Int,
                               hitsAlQuran: List<TestHitAlQuran>) {

        val verseData = mutableListOf<VerseData>()
        val listOfHighlightedStrings = mutableListOf<List<HighlightedString>>()

        val highlightResult = gson.fromJson(
            hitsAlQuran[position]._highlightResult.toString(),
            HighlightResult::class.java
        )

        TestHitAlQuran::class.declaredMemberProperties.forEach {
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
                verseData.add(
                    VerseData(
                        verseNumber,
                        verseList,
                        listOfHighlightedStrings
                    )
                )
            }
        }
        setHighlightMeaning(holder, verseData as List<VerseData>)
    }

    private fun setHighlightMeaning(holder: AlQuranAdapter.ViewHolder,
                                    verseData: List<VerseData>) {

        verseData.forEachIndexed { verseDataIndex, record ->
            record.verseList.forEachIndexed { index, verse ->
                if (verse.matchLevel != "none") {

                    val verseValue = verse.value.replace("<em>", "").replace("</em>", "")
                    holder.textMeaning.append( "(${record.verseNumber}) " + verseValue + "\n\n")
                    holder.textTranslation.append("(${record.verseNumber}) " + record.verseList[0].value + "\n\n")
                }
            }
        }
    }
}
