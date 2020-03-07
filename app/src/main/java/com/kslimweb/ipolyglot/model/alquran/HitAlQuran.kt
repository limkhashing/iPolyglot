package com.kslimweb.ipolyglot.model.alquran

import com.algolia.instantsearch.helper.highlighting.Highlightable
import com.algolia.search.model.Attribute
import com.algolia.search.model.ObjectID
import com.algolia.search.model.indexing.Indexable
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JsonObject
import oliviazoe0.processor.AutoUnpack

@AutoUnpack
@Serializable
data class HitAlQuran(
    override val objectID: ObjectID,
    override var _highlightResult: JsonObject?,
    val meaning: String,
    val translation: String
) : Indexable, Highlightable {

    @Transient
    val highlightedMeanings = getHighlight(Attribute("meaning"))

    @Transient
    val highlightedTranslations = getHighlight(Attribute("translation"))
}
