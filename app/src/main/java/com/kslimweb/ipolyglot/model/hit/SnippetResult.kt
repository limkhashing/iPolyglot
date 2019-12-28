package com.kslimweb.ipolyglot.model.hit


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class SnippetResult (
    @SerialName("chapter_ara")
    val chapterAra: ChapterAra,
    @SerialName("chapter_eng")
    val chapterEng: ChapterEng,
    @SerialName("content_ara")
    val contentsAra: List<ContentAra>,
    @SerialName("content_eng")
    val contentsEng: List<ContentEng>
)