package com.kslimweb.ipolyglot.util.extension

import android.content.SharedPreferences
import com.kslimweb.ipolyglot.di.module.DataModule.ALGOLIA_SEARCH_PREF
import com.kslimweb.ipolyglot.model.alquran.HitAlQuran
import kotlinx.serialization.json.Json
import kotlinx.serialization.list

class SearchResultHelper(private val sharedPreferences: SharedPreferences, private val json: Json) {

    private fun getStoredSearchResultObject(): String? {
        return sharedPreferences.getString(ALGOLIA_SEARCH_PREF, null)
    }

    fun getStoredSearchResult(): List<HitAlQuran>? {
        getStoredSearchResultObject()?.let {
            return json.parse(HitAlQuran.serializer().list, it)
        }
        return null
    }

    fun storeSearchResultObject(currentAlQuranChapterVerses: MutableList<HitAlQuran>) {
        val json = json.stringify(HitAlQuran.serializer().list, currentAlQuranChapterVerses)
        val editor = sharedPreferences.edit()
        editor.putString(ALGOLIA_SEARCH_PREF, json)
        editor.apply()
    }

    fun clearStoredResult() {
        val editor = sharedPreferences.edit()
        editor.putStringSet(ALGOLIA_SEARCH_PREF, null)
        editor.apply()
    }
}