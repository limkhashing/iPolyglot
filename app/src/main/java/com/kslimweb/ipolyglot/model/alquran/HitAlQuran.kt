package com.kslimweb.ipolyglot.model.alquran

import com.algolia.instantsearch.helper.highlighting.Highlightable
import com.algolia.search.model.ObjectID
import com.algolia.search.model.indexing.Indexable
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class HitAlQuran(
    val verse_1: List<String>? = null,
    val verse_2: List<String>? = null,
    val verse_3: List<String>? = null,
    val verse_4: List<String>? = null,
    val verse_5: List<String>? = null,
    val verse_6: List<String>? = null,
    val verse_7: List<String>? = null,
    val verse_8: List<String>? = null,
    val verse_9: List<String>? = null,
    val verse_10: List<String>? = null,
    val verse_11: List<String>? = null,
    val verse_12: List<String>? = null,
    val verse_13: List<String>? = null,
    val verse_14: List<String>? = null,
    val verse_15: List<String>? = null,
    val verse_16: List<String>? = null,
    val verse_17: List<String>? = null,
    val verse_18: List<String>? = null,
    val verse_19: List<String>? = null,
    val verse_20: List<String>? = null,
    val verse_21: List<String>? = null,
    val _snippetResult: JsonObject?,
    override val objectID: ObjectID,
    override val _highlightResult: JsonObject?
) : Indexable, Highlightable {

}