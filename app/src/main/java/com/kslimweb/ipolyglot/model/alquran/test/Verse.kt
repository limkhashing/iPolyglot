package com.kslimweb.ipolyglot.model.alquran.test

import com.algolia.instantsearch.core.highlighting.HighlightedString

data class Verse(
    val matchLevel: String,
    val value: String
)

data class VerseData(
    val verseNumber: String,
    val verseList: List<Verse>,
    val listOfHighlightedStrings: List<List<HighlightedString>>
)