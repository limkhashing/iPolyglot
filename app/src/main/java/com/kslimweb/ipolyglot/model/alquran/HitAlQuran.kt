package com.kslimweb.ipolyglot.model.alquran

import com.algolia.instantsearch.helper.highlighting.Highlightable
import com.algolia.search.model.Attribute
import com.algolia.search.model.ObjectID
import com.algolia.search.model.indexing.Indexable
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JsonObject

@Serializable
data class HitAlQuran(
    override val objectID: ObjectID,
    override var _highlightResult: JsonObject?
) : Indexable, Highlightable {

    @Transient
    val verse_1 = getHighlights(Attribute("verse_1"))

    @Transient
    val verse_2= getHighlights(Attribute("verse_2"))

    @Transient
    val verse_3 = getHighlights(Attribute("verse_3"))

    @Transient
    val verse_4 = getHighlights(Attribute("verse_4"))

    @Transient
    val verse_5 = getHighlights(Attribute("verse_5"))

    @Transient
    val verse_6 = getHighlights(Attribute("verse_6"))

    @Transient
    val verse_7 = getHighlights(Attribute("verse_7"))

    @Transient
    val verse_8 = getHighlights(Attribute("verse_8"))

    @Transient
    val verse_9 = getHighlights(Attribute("verse_9"))

    @Transient
    val verse_10 = getHighlights(Attribute("verse_10"))

    @Transient
    val verse_11 = getHighlights(Attribute("verse_11"))

    @Transient
    val verse_12 = getHighlights(Attribute("verse_12"))

    @Transient
    val verse_13 = getHighlights(Attribute("verse_13"))

    @Transient
    val verse_14 = getHighlights(Attribute("verse_14"))

    @Transient
    val verse_15 = getHighlights(Attribute("verse_15"))

    @Transient
    val verse_16 = getHighlights(Attribute("verse_16"))

    @Transient
    val verse_17 = getHighlights(Attribute("verse_17"))

    @Transient
    val verse_18 = getHighlights(Attribute("verse_18"))

    @Transient
    val verse_19 = getHighlights(Attribute("verse_19"))

    @Transient
    val verse_20 = getHighlights(Attribute("verse_20"))

    @Transient
    val verse_21 = getHighlights(Attribute("verse_21"))
}
