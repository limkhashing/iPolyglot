package com.kslimweb.ipolyglot.model.hit


import com.algolia.instantsearch.helper.highlighting.Highlightable
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class Hit(
    @SerializedName("chapter_ara")
    val chapterAraText: String,
    @SerializedName("chapter_eng")
    val chapterEngText: String,
    @SerializedName("in_book_reference")
    val inBookReference: List<String>?,
    val objectID: String,
    val reference: List<String>?,
    override val _highlightResult: JsonObject?
) : Highlightable