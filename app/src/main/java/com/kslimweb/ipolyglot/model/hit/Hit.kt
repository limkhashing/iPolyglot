package com.kslimweb.ipolyglot.model.hit


import com.algolia.instantsearch.core.highlighting.HighlightedString
import com.algolia.instantsearch.helper.highlighting.Highlightable
import com.algolia.search.model.Attribute
import com.algolia.search.model.ObjectID
import com.algolia.search.model.indexing.Indexable
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.ContextualSerialization
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JsonObject

@Serializable
data class Hit(
//    @SerializedName("chapter_ara")
    @SerialName("chapter_ara")
    val chapterAraText: String,

//    @SerializedName("chapter_eng")
    @SerialName("chapter_eng")
    val chapterEngText: String,

//    @SerializedName("in_book_reference")
    @SerialName("in_book_reference")
    val inBookReference: List<String>?,

    val reference: List<String>?,

//    @SerializedName("_snippetResult")
//    val highlightResult: SnippetResult,

    override val objectID: ObjectID,
    override val _highlightResult: JsonObject?
) : Indexable, Highlightable {

    @Transient
    val highlightedChapterAra = getHighlight(Attribute("chapter_ara"))

    @Transient
    val highlightedChapterEng = getHighlight(Attribute("chapter_eng"))

    @Transient
    val highlightedContentsAra = getHighlights(Attribute("content_ara"))

    @Transient
    val highlightedContentsEng = getHighlights(Attribute("content_eng"))
}