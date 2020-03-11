package com.kslimweb.ipolyglot.util.extension

import android.content.SharedPreferences
import com.kslimweb.ipolyglot.model.alquran.HitAlQuran
import com.kslimweb.ipolyglot.network.algolia.AlgoliaSearcher
import com.kslimweb.ipolyglot.util.AppConstants.BUFFERED_SEARCH_RESULT
import com.kslimweb.ipolyglot.util.AppConstants.STORED_SEARCH_RESULT
import kotlinx.serialization.json.Json
import kotlinx.serialization.list

class AlQuranSearchHelper(private val sharedPreferences: SharedPreferences,
                          private val json: Json,
                          private val searcher: AlgoliaSearcher) {

    private fun getStoredAlQuranObject(): String? {
        return sharedPreferences.getString(STORED_SEARCH_RESULT, null)
    }

    private fun getBufferedAlQuranObject(): String? {
        return sharedPreferences.getString(BUFFERED_SEARCH_RESULT, null)
    }

    fun getStoredAlQuranResult(): List<HitAlQuran>? {
        getStoredAlQuranObject()?.let {
            return json.parse(HitAlQuran.serializer().list, it)
        }
        return null
    }

    fun getBufferedAlQuran(): List<HitAlQuran>? {
        getBufferedAlQuranObject()?.let {
            return json.parse(HitAlQuran.serializer().list, it)
        }
        return null
    }

    private fun storeAlQuranObject(currentAlQuranChapterVerses: MutableList<HitAlQuran>) {
        val json = json.stringify(HitAlQuran.serializer().list, currentAlQuranChapterVerses)
        val editor = sharedPreferences.edit()
        editor.putString(STORED_SEARCH_RESULT, json)
        editor.apply()
    }

    private fun storeBufferedAlQuranObject(bufferedAlQurans: MutableList<HitAlQuran>) {
        val json = json.stringify(HitAlQuran.serializer().list, bufferedAlQurans)
        val editor = sharedPreferences.edit()
        editor.putString(BUFFERED_SEARCH_RESULT, json)
        editor.apply()
    }

    fun clearAlQuran() {
        val editor = sharedPreferences.edit()
        editor.putString(STORED_SEARCH_RESULT, null)
        editor.putString(BUFFERED_SEARCH_RESULT, null)
        editor.apply()
    }

    suspend fun storeCurrentChapterVerses(chapter: String) =
        storeAlQuranObject(searcher.searchVersesInChapter(chapter))

    fun bufferChapterVerses(alQuranVerse: HitAlQuran) = mutableListOf<HitAlQuran>().apply {
        getStoredAlQuranResult()?.let {
            this.add(it[it.indexOf(alQuranVerse) - 2])
            this.add(it[it.indexOf(alQuranVerse) - 1])
            this.add(it[it.indexOf(alQuranVerse)])
            this.add(it[it.indexOf(alQuranVerse) + 1])
        }.also {
            storeBufferedAlQuranObject(this@apply)
        }
    }
}