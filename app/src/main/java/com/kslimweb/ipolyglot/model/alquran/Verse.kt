package com.kslimweb.ipolyglot.model.alquran

data class Verse(
    val matchLevel: String,
    val value: String
)

data class VerseData(
    val verseNumber: String,
    val verseList: List<Verse>
)