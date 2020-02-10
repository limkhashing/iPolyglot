package com.kslimweb.ipolyglot.model.hadith


import com.google.gson.annotations.SerializedName

data class SnippetResult (
    @SerializedName("content_ara")
    val contentsAra: List<ContentAra>?,
    @SerializedName("content_eng")
    val contentsEng: List<ContentEng>?
)