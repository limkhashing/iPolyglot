package com.kslimweb.ipolygot.algolia_data


import com.google.gson.annotations.SerializedName

data class HighlightResult(
    @SerializedName("content_ara")
    val contentAra: ContentAra,
    @SerializedName("content_eng")
    val contentEng: ContentEng
)