package com.kslimweb.ipolyglot.model.alquran


import com.google.gson.annotations.SerializedName
import oliviazoe0.processor.AutoUnpack

@AutoUnpack
data class HighlightResult(
    @SerializedName("verse_1")
    val verse1: List<Verse>?,
    @SerializedName("verse_2")
    val verse2: List<Verse>?,
    @SerializedName("verse_3")
    val verse3: List<Verse>?,
    @SerializedName("verse_4")
    val verse4: List<Verse>?,
    @SerializedName("verse_5")
    val verse5: List<Verse>?,
    @SerializedName("verse_6")
    val verse6: List<Verse>?,
    @SerializedName("verse_7")
    val verse7: List<Verse>?,
    @SerializedName("verse_8")
    val verse8: List<Verse>?,
    @SerializedName("verse_9")
    val verse9: List<Verse>?,
    @SerializedName("verse_10")
    val verse10: List<Verse>?,
    @SerializedName("verse_11")
    val verse11: List<Verse>?,
    @SerializedName("verse_12")
    val verse12: List<Verse>?,
    @SerializedName("verse_13")
    val verse13: List<Verse>?,
    @SerializedName("verse_14")
    val verse14: List<Verse>?,
    @SerializedName("verse_15")
    val verse15: List<Verse>?,
    @SerializedName("verse_16")
    val verse16: List<Verse>?,
    @SerializedName("verse_17")
    val verse17: List<Verse>?,
    @SerializedName("verse_18")
    val verse18: List<Verse>?,
    @SerializedName("verse_19")
    val verse19: List<Verse>?,
    @SerializedName("verse_20")
    val verse20: List<Verse>?,
    @SerializedName("verse_21")
    val verse21: List<Verse>?
)
