package com.kslimweb.ipolyglot.model


data class ContentEng(
    val fullyHighlighted: Boolean,
    val matchLevel: String,
    val matchedWords: List<String>,
    val value: String
)