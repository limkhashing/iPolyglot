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
    val verse_1 = getHighlight(Attribute("verse_1"))

    @Transient
    val verse_2= getHighlight(Attribute("verse_2"))

    @Transient
    val verse_3 = getHighlight(Attribute("verse_3"))

    @Transient
    val verse_4 = getHighlight(Attribute("verse_4"))

    @Transient
    val verse_5 = getHighlight(Attribute("verse_5"))

    @Transient
    val verse_6 = getHighlight(Attribute("verse_6"))

    @Transient
    val verse_7 = getHighlight(Attribute("verse_7"))

    @Transient
    val verse_8 = getHighlight(Attribute("verse_8"))

    @Transient
    val verse_9 = getHighlight(Attribute("verse_9"))

    @Transient
    val verse_10 = getHighlight(Attribute("verse_10"))

    @Transient
    val verse_11 = getHighlight(Attribute("verse_11"))

    @Transient
    val verse_12 = getHighlight(Attribute("verse_12"))

    @Transient
    val verse_13 = getHighlight(Attribute("verse_13"))

    @Transient
    val verse_14 = getHighlight(Attribute("verse_14"))

    @Transient
    val verse_15 = getHighlight(Attribute("verse_15"))

    @Transient
    val verse_16 = getHighlight(Attribute("verse_16"))

    @Transient
    val verse_17 = getHighlight(Attribute("verse_17"))

    @Transient
    val verse_18 = getHighlight(Attribute("verse_18"))

    @Transient
    val verse_19 = getHighlight(Attribute("verse_19"))

    @Transient
    val verse_20 = getHighlight(Attribute("verse_20"))

    @Transient
    val verse_21 = getHighlight(Attribute("verse_21"))
}
