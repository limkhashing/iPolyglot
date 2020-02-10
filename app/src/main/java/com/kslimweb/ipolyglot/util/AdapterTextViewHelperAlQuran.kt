package com.kslimweb.ipolyglot.util

import com.kslimweb.ipolyglot.model.alquran.HitAlQuran
import com.kslimweb.ipolyglot.model.hadith.*
import com.kslimweb.ipolyglot.adapter.SearchResponseHadithAdapter

class AdapterTextViewHelperAlQuran(private val hitAlQuran: List<HitAlQuran>) {

    fun setChapterText(holder: SearchResponseHadithAdapter.ViewHolder, position: Int) {

    }

    // TODO set for al quran only
    fun setHighlightResultText(holder: SearchResponseHadithAdapter.ViewHolder, position: Int) {

    }

    private fun setHighlightArabicMeaning(position: Int, holder: SearchResponseHadithAdapter.ViewHolder, chapterAra: ChapterAra) {

    }

    private fun setHighlightTranslation(position: Int, holder: SearchResponseHadithAdapter.ViewHolder, chapterEng: ChapterEng) {

    }
}