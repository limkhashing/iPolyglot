package com.kslimweb.ipolyglot.algolia_data


data class ContentEng(
    val fullyHighlighted: Boolean,
    val matchLevel: String,
    val matchedWords: List<String>,
    val value: String
)