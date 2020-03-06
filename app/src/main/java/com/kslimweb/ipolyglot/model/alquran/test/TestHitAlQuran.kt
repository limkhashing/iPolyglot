package com.kslimweb.ipolyglot.model.alquran.test

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
data class TestHitAlQuran(
    override val objectID: ObjectID,
    override var _highlightResult: JsonObject?
) : Indexable, Highlightable {

    @Transient
    val verse1 = getHighlights(Attribute("verse_1"))

    @Transient
    val verse2= getHighlights(Attribute("verse_2"))

    @Transient
    val verse3 = getHighlights(Attribute("verse_3"))

    @Transient
    val verse4 = getHighlights(Attribute("verse_4"))

    @Transient
    val verse5 = getHighlights(Attribute("verse_5"))

    @Transient
    val verse6 = getHighlights(Attribute("verse_6"))

    @Transient
    val verse7 = getHighlights(Attribute("verse_7"))

    @Transient
    val verse8 = getHighlights(Attribute("verse_8"))

    @Transient
    val verse9 = getHighlights(Attribute("verse_9"))

    @Transient
    val verse10 = getHighlights(Attribute("verse_10"))

    @Transient
    val verse11 = getHighlights(Attribute("verse_11"))

    @Transient
    val verse12 = getHighlights(Attribute("verse_12"))

    @Transient
    val verse13 = getHighlights(Attribute("verse_13"))

    @Transient
    val verse14 = getHighlights(Attribute("verse_14"))

    @Transient
    val verse15 = getHighlights(Attribute("verse_15"))

    @Transient
    val verse16 = getHighlights(Attribute("verse_16"))

    @Transient
    val verse17 = getHighlights(Attribute("verse_17"))

    @Transient
    val verse18 = getHighlights(Attribute("verse_18"))

    @Transient
    val verse19 = getHighlights(Attribute("verse_19"))

    @Transient
    val verse20 = getHighlights(Attribute("verse_20"))

    @Transient
    val verse21 = getHighlights(Attribute("verse_21"))
}
