package com.kslimweb.ipolyglot.network.algolia

import com.algolia.search.saas.Index
import com.algolia.search.saas.Query
import com.google.gson.Gson
import com.kslimweb.ipolyglot.model.Hit
import com.kslimweb.ipolyglot.model.HitsJson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.json.JSONObject

class Algolia {

    private fun queryIndexSearch(queryText: String, index: Index): JSONObject? {
        return index.search(Query(queryText), null)
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

    private fun parseSearchList(speechTextSearchJson: JSONObject?, translatedTextSearchJson: JSONObject?): MutableList<Hit> {
        val speechSearchList = Gson().fromJson(speechTextSearchJson.toString(), HitsJson::class.java).hits
        val translatedTextSearchList = Gson().fromJson(translatedTextSearchJson.toString(), HitsJson::class.java).hits
        return getSearchList(speechSearchList, translatedTextSearchList)
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
