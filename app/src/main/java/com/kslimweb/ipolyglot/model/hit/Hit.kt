package com.kslimweb.ipolyglot.model.hit


import com.google.gson.annotations.SerializedName

data class Hit(
    @SerializedName("chapter_ara")
    val chapterAraText: String,
    @SerializedName("chapter_eng")
    val chapterEngText: String,
    @SerializedName("in_book_reference")
    val inBookReference: List<String>?,
    val objectID: String,
    val reference: List<String>?,
    @SerializedName("_snippetResult")
    val snippetResult: SnippetResult?
)