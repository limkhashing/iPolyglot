package com.kslimweb.ipolyglot.network.algolia

import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.client.Index
import com.algolia.search.helper.deserialize
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.search.Query
import com.kslimweb.ipolyglot.model.alquran.HitAlQuran
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject


class Searcher @Inject constructor(private val index: Index,
                                   private val bgDispatcher: CoroutineDispatcher) {

    private suspend fun querySearch(queryText: String): ResponseSearch {
        return SearcherSingleIndex(index, Query(query = queryText)).search()
    }

    suspend fun search(speechText: String) = mutableListOf<HitAlQuran>().apply {
        withContext(bgDispatcher) {
            val speechTextSearchResponse = async {
                querySearch(speechText)
            }
            addAll(parseSearchResponse(speechTextSearchResponse.await()))
        }
    }

    private fun parseSearchResponse(speechTextSearchResponse: ResponseSearch) = listOf<HitAlQuran>().apply {
        val speechTextSearchHits = speechTextSearchResponse.hits.deserialize(HitAlQuran.serializer())
        speechTextSearchHits.sortedWith(compareBy({
            it.objectID.raw.split("_")[0] // sort chapters
        }, {
            it.objectID.raw.split("_")[1] // sort verse
        }))
        return speechTextSearchHits
    }
}
