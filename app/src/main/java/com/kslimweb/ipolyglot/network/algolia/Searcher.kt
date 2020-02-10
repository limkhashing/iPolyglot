package com.kslimweb.ipolyglot.network.algolia

import android.util.Log
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.client.Index
import com.algolia.search.helper.deserialize
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.search.Query
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

    suspend fun search(speechText: String, translatedText: String): List<HitHadith> {
        var searchHits = emptyList<HitHadith>()
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

    private fun parseSearchResponse(speechTextSearchResponse: ResponseSearch, translatedTextSearchResponse: ResponseSearch): List<HitHadith> {
        Log.d("Searcher", speechTextSearchResponse.hits.size.toString())
        Log.d("Searcher", speechTextSearchResponse.hits.toString())

        val speechTextSearchHits = speechTextSearchResponse.hits.deserialize(HitAlQuran.serializer())
//        val translatedTextSearchHits = translatedTextSearchResponse.hits.deserialize(HitAlQuran.serializer())
//        return combineSearchList(speechTextSearchHits, translatedTextSearchHits)
        return emptyList()
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
}
