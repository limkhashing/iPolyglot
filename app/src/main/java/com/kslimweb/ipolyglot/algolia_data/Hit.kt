package com.kslimweb.ipolyglot.algolia_data


import com.google.gson.annotations.SerializedName

data class Hit(
    @SerializedName("chapter_ara")
    val chapterAra: String,
    @SerializedName("chapter_eng")
    val chapterEng: String,
    @SerializedName("in_book_reference")
    val inBookReference: List<String>?,
    val objectID: String,
    val reference: List<String>?
)