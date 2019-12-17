package com.kslimweb.ipolygot.algolia_data


import com.google.gson.annotations.SerializedName

data class ContentEng(
    val fullyHighlighted: Boolean,
    val matchLevel: String,
    val matchedWords: List<String>,
    val value: String
)