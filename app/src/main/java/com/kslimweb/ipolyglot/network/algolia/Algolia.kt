package com.kslimweb.ipolyglot.network.algolia

import android.util.Log
import com.algolia.search.client.Index
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.search.Query
import com.google.gson.Gson
import com.kslimweb.ipolyglot.model.hit.Hit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class Algolia {

    private suspend fun queryIndexSearch(queryText: String, index: Index): ResponseSearch {
        return index.search(Query(queryText))
    }

    suspend fun algoliaSearch(speechText: String, translatedText: String, index: Index): List<Hit> {
        var finalList = emptyList<Hit>()
        withContext(Dispatchers.IO) {
            val speechTextSearchJson = async {
                queryIndexSearch(speechText, index)
            }
            val translatedTextSearchJson = async {
                queryIndexSearch(translatedText, index)
            }
            finalList = parseSearchList(speechTextSearchJson.await(), translatedTextSearchJson.await())
        }
        return finalList
    }

    private fun parseSearchList(speechTextSearchJson: ResponseSearch, translatedTextSearchJson: ResponseSearch): MutableList<Hit> {

        val speechTextSearchHits = mutableListOf<Hit>()
        speechTextSearchJson.hits.forEach {
            Log.d("JSON", it.json.toString())
            speechTextSearchHits.add(Gson().fromJson(it.json.toString(), Hit::class.java))
        }

        val translatedTextSearchHits = mutableListOf<Hit>()
        translatedTextSearchJson.hits.forEach {
            Log.d("JSON", it.json.toString())
            translatedTextSearchHits.add(Gson().fromJson(it.json.toString(), Hit::class.java))
        }
        return getSearchList(speechTextSearchHits, translatedTextSearchHits)
    }

    private fun getSearchList(speechSearchList: List<Hit>, translatedTextSearchList: List<Hit>): MutableList<Hit> {
        val finalList = mutableListOf<Hit>()
        speechSearchList.forEach {
            if (!finalList.contains(it))
                finalList.add(it)
        }
        translatedTextSearchList.forEach {
            if (!finalList.contains(it))
                finalList.add(it)
        }
        return finalList
    }
}
