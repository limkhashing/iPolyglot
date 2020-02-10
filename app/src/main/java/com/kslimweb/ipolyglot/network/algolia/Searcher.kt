package com.kslimweb.ipolyglot.network.algolia

import android.util.Log
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.client.Index
import com.algolia.search.helper.deserialize
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.search.Query
import com.google.gson.Gson
import com.kslimweb.ipolyglot.model.alquran.HighlightResult
import com.kslimweb.ipolyglot.model.alquran.HitAlQuran
import com.kslimweb.ipolyglot.model.hadith.HitHadith
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject


class Searcher @Inject constructor(private val index: Index) {

    private suspend fun querySearch(queryText: String): ResponseSearch {
        return SearcherSingleIndex(index, Query(query = queryText)).search()
    }

    suspend fun search(speechText: String, translatedText: String): List<HitAlQuran> {
        var searchHits = emptyList<HitAlQuran>()
        withContext(Dispatchers.IO) {
            val speechTextSearchResponse = async {
                querySearch(speechText)
            }
            val translatedTextSearchResponse = async {
                querySearch(translatedText)
            }
            searchHits = parseSearchResponse(speechTextSearchResponse.await(), translatedTextSearchResponse.await())
        }
        return searchHits
    }

    private fun parseSearchResponse(speechTextSearchResponse: ResponseSearch, translatedTextSearchResponse: ResponseSearch)
            : List<HitAlQuran> {
        val speechTextSearchHits = speechTextSearchResponse.hits.deserialize(HitAlQuran.serializer())
        Log.d("Searcher", speechTextSearchHits.toString())
        val translatedTextSearchHits = translatedTextSearchResponse.hits.deserialize(HitAlQuran.serializer())
        Log.d("Searcher", translatedTextSearchHits.toString())
        return combineSearchAlQuranList(speechTextSearchHits, translatedTextSearchHits)
//        return emptyList()
    }

    private fun combineSearchList(speechSearchList: List<HitHadith>, translatedTextSearchList: List<HitHadith>): List<HitHadith> {
        val combinedHits = mutableListOf<HitHadith>()
        speechSearchList.forEach {
            if (!combinedHits.contains(it))
                combinedHits.add(it)
        }
        translatedTextSearchList.forEach {
            if (!combinedHits.contains(it))
                combinedHits.add(it)
        }
        return combinedHits
    }

    private fun combineSearchAlQuranList(speechSearchList: List<HitAlQuran>,
                                         translatedTextSearchList: List<HitAlQuran>): List<HitAlQuran> {

        val highlightResult = Gson().fromJson(speechSearchList[0]._highlightResult.toString(),
            HighlightResult::class.java)
        
        // TODO update json objects
        // check Arabic Meaning Match Level first before replacing
        if (highlightResult.verse1[0].matchLevel == "none") {
            // replace arabic

            speechSearchList.forEachIndexed { index, hit ->
                val speechSearchJson = hit._highlightResult?.get("verse_" + index+1)?.jsonArray?.get(0)?.jsonObject
                val translateSearchJson = translatedTextSearchList[index]._highlightResult?.get("verse_" + index+1)?.jsonArray?.get(0)?.jsonObject

            }
        }
        return speechSearchList
    }
}
