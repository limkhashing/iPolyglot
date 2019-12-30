package com.kslimweb.ipolyglot.network.algolia

import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.client.Index
import com.algolia.search.helper.deserialize
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.search.Query
import com.kslimweb.ipolyglot.model.hit.Hit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class Searcher(private val speechText: String,
               private val translatedText: String,
               private val index: Index
) {

    private suspend fun querySearch(queryText: String): ResponseSearch {
        return SearcherSingleIndex(index, Query(query = queryText)).search()
    }

    suspend fun search(): List<Hit> {
        var finalList = emptyList<Hit>()
        withContext(Dispatchers.IO) {
            val speechTextSearchResponse = async {
                querySearch(speechText)
            }
            val translatedTextSearchResponse = async {
                querySearch(translatedText)
            }
            finalList = parseSearchResponse(speechTextSearchResponse.await(), translatedTextSearchResponse.await())
        }
        return finalList
    }

    private fun parseSearchResponse(speechTextSearchResponse: ResponseSearch, translatedTextSearchResponse: ResponseSearch): List<Hit> {
        val speechTextSearchHits = speechTextSearchResponse.hits.deserialize(Hit.serializer())
        val translatedTextSearchHits = translatedTextSearchResponse.hits.deserialize(Hit.serializer())
        return combineSearchList(speechTextSearchHits, translatedTextSearchHits)
    }

    private fun combineSearchList(speechSearchList: List<Hit>, translatedTextSearchList: List<Hit>): List<Hit> {
        val combinedHits = mutableListOf<Hit>()
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
