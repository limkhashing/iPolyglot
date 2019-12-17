package com.kslimweb.ipolygot.algolia_data


import com.google.gson.annotations.SerializedName

data class ContentAra(
    val matchLevel: String,
    val matchedWords: List<Any>,
    val value: String
)