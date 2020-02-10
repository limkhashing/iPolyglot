package com.kslimweb.ipolyglot.model.alquran


import com.google.gson.annotations.SerializedName

data class HighlightResult (
    @SerializedName("verse_1")
    val verse1: List<Verse1>
)
