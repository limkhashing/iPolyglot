package com.kslimweb.ipolyglot.algolia_data


data class ContentAra(
    val matchLevel: String,
    val matchedWords: List<Any>,
    val value: String
)