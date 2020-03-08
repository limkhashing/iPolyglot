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
import javax.inject.Inject


class Searcher @Inject constructor(private val index: Index,
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

    suspend fun searchVersesInChapter(currentChapter: String) = mutableListOf<HitAlQuran>().apply {
        withContext(bgDispatcher) {
            val currentAlQuranChapterVerses = parseSearchResponse(querySearchChapter(currentChapter))
            addAll(currentAlQuranChapterVerses)
        }
    }

    private fun parseSearchResponse(speechTextSearchResponse: ResponseSearch) = listOf<HitAlQuran>().apply {
        return speechTextSearchResponse.hits.deserialize(HitAlQuran.serializer())
    }
}
