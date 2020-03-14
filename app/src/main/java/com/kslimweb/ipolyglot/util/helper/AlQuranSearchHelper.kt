package com.kslimweb.ipolyglot.util.helper

import com.kslimweb.ipolyglot.model.alquran.HitAlQuran
import com.kslimweb.ipolyglot.network.algolia.AlgoliaSearcher
import kotlinx.serialization.json.Json

class AlQuranSearchHelper(private val json: Json, private val searcher: AlgoliaSearcher) {

    private var indexNextVerse = 0
    private val storedCurrentChapterVerses = mutableListOf<HitAlQuran>()
    private val bufferedAlQuran = mutableListOf<HitAlQuran>()

    fun clearAlQuran() {
        storedCurrentChapterVerses.clear()
        bufferedAlQuran.clear()
    }

    fun getBufferedAlQuran(): MutableList<HitAlQuran> {
        return bufferedAlQuran
    }

    fun setBufferAlQuran(matchedAlQuran: HitAlQuran) {

        getCurrentChapterVerses().let {
            val verseIndex = it.indexOfFirst { storedAlQuran -> storedAlQuran.verse == matchedAlQuran.verse }

            it.getOrNull(verseIndex - 2)?.let { verse -> bufferedAlQuran.add(verse) }
            it.getOrNull(verseIndex - 1)?.let { verse -> bufferedAlQuran.add(verse) }

            bufferedAlQuran.add(it[verseIndex])

            it.getOrNull(verseIndex + 1)?.let { verse ->
                bufferedAlQuran.add(verse)
                indexNextVerse = verseIndex + 1
            }
        }
    }

    fun addBufferedAlQuran() {
        getCurrentChapterVerses().let {
            bufferedAlQuran.add(it[++indexNextVerse])
        }
    }

    fun getCurrentChapterVerses(): MutableList<HitAlQuran> {
        return storedCurrentChapterVerses
    }

    suspend fun storeCurrentChapterVerses(chapter: String) {
        storedCurrentChapterVerses.addAll(searcher.searchVersesInChapter(chapter))
    }

    /** Store search results in local storage **/
//    private fun getStoredAlQuranObject(): String? {
//        return sharedPreferences.getString(STORED_SEARCH_RESULT, null)
//    }
//
//    private fun getBufferedAlQuranObject(): String? {
//        return sharedPreferences.getString(BUFFERED_SEARCH_RESULT, null)
//    }

//    fun getStoredAlQuranResult(): List<HitAlQuran>? {
//        getStoredAlQuranObject()?.let {
//            return json.parse(HitAlQuran.serializer().list, it)
//        }
//        return null
//    }

//    suspend fun storeCurrentChapterVerses(chapter: String) =
//        storeAlQuranObject(searcher.searchVersesInChapter(chapter))

//    fun getBufferedAlQuran(): List<HitAlQuran>? {
//        getBufferedAlQuranObject()?.let {
//            return json.parse(HitAlQuran.serializer().list, it)
//        }
//        return null
//    }

//    private fun storeAlQuranObject(currentAlQuranChapterVerses: MutableList<HitAlQuran>) {
//        val json = json.stringify(HitAlQuran.serializer().list, currentAlQuranChapterVerses)
//        val editor = sharedPreferences.edit()
//        editor.putString(STORED_SEARCH_RESULT, json)
//        editor.apply()
//    }

//    private fun storeBufferedAlQuranObject(bufferedAlQurans: MutableList<HitAlQuran>) {
//        val json = json.stringify(HitAlQuran.serializer().list, bufferedAlQurans)
//        val editor = sharedPreferences.edit()
//        editor.putString(BUFFERED_SEARCH_RESULT, json)
//        editor.apply()
//    }

//    fun clearAlQuran() {
//        val editor = sharedPreferences.edit()
//        editor.putString(STORED_SEARCH_RESULT, null)
//        editor.putString(BUFFERED_SEARCH_RESULT, null)
//        editor.apply()
//    }
}