package com.kslimweb.ipolyglot.model.hit


import com.algolia.instantsearch.core.highlighting.HighlightedString
import com.algolia.instantsearch.helper.highlighting.Highlightable
import com.algolia.search.model.Attribute
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.Transient

data class HighlightResult (
    @SerializedName("chapter_ara")
    val chapterAra: ChapterAra,
    @SerializedName("chapter_eng")
    val chapterEng: ChapterEng,
    @SerializedName("content_ara")
    val contentAra: List<ContentAra>,
    @SerializedName("content_eng")
    val contentEng: List<ContentEng>,
    override val _highlightResult: JsonObject?
) : Highlightable {

    @Transient
    val highlightedChapterAra: HighlightedString? = getHighlight(Attribute("chapter_ara"))

    @Transient
    val highlightedChapterEng: HighlightedString? = getHighlight(Attribute("chapter_eng"))

    @Transient
    val highlightedContentAra: HighlightedString?
        get() = getHighlight(Attribute("content_ara"))

    @Transient
    val highlightedContentEng: HighlightedString?
        get() = getHighlight(Attribute("content_eng"))
}