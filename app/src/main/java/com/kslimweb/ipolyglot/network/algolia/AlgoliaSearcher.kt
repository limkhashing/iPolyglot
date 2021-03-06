package com.kslimweb.ipolyglot.network.algolia

import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.client.Index
import com.algolia.search.dsl.query
import com.algolia.search.dsl.restrictSearchableAttributes
import com.algolia.search.helper.deserialize
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.search.Query
import com.kslimweb.ipolyglot.model.alquran.HitAlQuran
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext


class AlgoliaSearcher(private val index: Index,
                      private val bgDispatcher: CoroutineDispatcher) {

    private suspend fun querySearch(queryText: String): ResponseSearch {
        return SearcherSingleIndex(index, Query(query = queryText)).search()
    }

    private suspend fun querySearchChapter(queryText: String): ResponseSearch {
        val query = query("\""+queryText+"\"") { restrictSearchableAttributes { +"chapter" } }
        return SearcherSingleIndex(index, query).search()
    }

    suspend fun search(speechText: String) = mutableListOf<HitAlQuran>().apply {
        withContext(bgDispatcher) {
            addAll(parseSearchResponse(querySearch(speechText)))
        }
    }

    suspend fun searchVersesInChapter(currentChapter: String): List<HitAlQuran> {
        return parseSearchResponse(querySearchChapter(currentChapter))
    }

    private fun parseSearchResponse(speechTextSearchResponse: ResponseSearch) = listOf<HitAlQuran>().apply {
        if (speechTextSearchResponse.hitsOrNull == null || speechTextSearchResponse.hitsOrNull?.isEmpty()!!) {
            return@apply
        }
        return speechTextSearchResponse.hits.deserialize(HitAlQuran.serializer())
    }
}
