package com.kslimweb.ipolyglot.util

import com.google.gson.Gson
import com.kslimweb.ipolyglot.adapter.SearchResponseAlQuranAdapter
import com.kslimweb.ipolyglot.model.alquran.HighlightResult
import com.kslimweb.ipolyglot.model.alquran.HitAlQuran

class AdapterTextViewHelperAlQuran(private val hitAlQuran: List<HitAlQuran>) {

    fun setChapterText(holder: SearchResponseAlQuranAdapter.ViewHolder, position: Int) {

    }

    // TODO set for al quran only
    fun setHighlightResultText(holder: SearchResponseAlQuranAdapter.ViewHolder, position: Int) {
        val highlightResult = Gson().fromJson(hitAlQuran[position]._highlightResult.toString(), HighlightResult::class.java)
        setHighlightMeaning(position, holder)
    }

    private fun setHighlightMeaning(position: Int, holder: SearchResponseAlQuranAdapter.ViewHolder) {

    }

    private fun setHighlightTranslation(position: Int, holder: SearchResponseAlQuranAdapter.ViewHolder) {

    }
}