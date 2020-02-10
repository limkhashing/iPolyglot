package com.kslimweb.ipolyglot.model.hadith


import com.google.gson.annotations.SerializedName

data class HighlightResult (
    @SerializedName("chapter_ara")
    val chapterAra: ChapterAra,
    @SerializedName("chapter_eng")
    val chapterEng: ChapterEng,
    @SerializedName("content_ara")
    val contentsAra: List<ContentAra>?,
    @SerializedName("content_eng")
    val contentsEng: List<ContentEng>?
)