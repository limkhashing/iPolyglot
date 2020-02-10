package com.kslimweb.ipolyglot.util

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.widget.TextView

object TextUtils {
    fun setTextWithSpan(textView: TextView, text: String, spanText: String, style: StyleSpan?) {
        val sb = SpannableStringBuilder(text)
        val start = text.indexOf(spanText)
        val end = start + spanText.length
        sb.setSpan(style, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        textView.text = sb
    }
}