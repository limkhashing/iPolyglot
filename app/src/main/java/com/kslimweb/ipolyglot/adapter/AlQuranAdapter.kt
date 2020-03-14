package com.kslimweb.ipolyglot.adapter

import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.core.highlighting.HighlightedString
import com.kslimweb.ipolyglot.R
import com.kslimweb.ipolyglot.model.alquran.HitAlQuran

class AlQuranAdapter : RecyclerView.Adapter<AlQuranAdapter.ViewHolder>() {

    private var hitsAlQuran: List<HitAlQuran> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context), parent)
    }

    override fun getItemCount(): Int {
        return hitsAlQuran.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chapter =  hitsAlQuran[position].chapter
        val verseNumber =  hitsAlQuran[position].verse
        holder.alQuranChapter.text = "Chapter " + chapter + ", Verse " + verseNumber
        holder.textMeaning.text = ""
        holder.textTranslation.text = ""

        hitsAlQuran[position].highlightedMeanings?.let {
            setHighlightMeanings(it, holder.textMeaning)
        }

        hitsAlQuran[position].highlightedTranslations?.let {
            setHighlightTranslations(it, holder.textTranslation)
        }
    }

    private fun setHighlightMeanings(highlightedMeanings: HighlightedString, textMeaning: TextView) {
        highlightedMeanings.tokens.forEach { wordToken ->
            if (wordToken.highlighted) {
                val highlightedWord = SpannableString(wordToken.content)

                highlightedWord.setSpan(
                    StyleSpan(Typeface.BOLD),
                    wordToken.content.indexOf(wordToken.content),
                    wordToken.content.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                textMeaning.append(highlightedWord)

            } else {
                textMeaning.append(wordToken.content)
            }
        }
    }

    private fun setHighlightTranslations(highlightedTranslations: HighlightedString, textTranslation: TextView) {
        highlightedTranslations.tokens.forEach { wordToken ->
            if (wordToken.highlighted) {
                val highlightedWord = SpannableString(wordToken.content)

                highlightedWord.setSpan(
                    StyleSpan(Typeface.BOLD),
                    wordToken.content.indexOf(wordToken.content),
                    wordToken.content.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                textTranslation.append(highlightedWord)

            } else {
                textTranslation.append(wordToken.content)
            }
        }
    }

    fun setData(hitsAlQuran: List<HitAlQuran>) {
        this.hitsAlQuran = hitsAlQuran
        notifyDataSetChanged()
    }

    class ViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.item_search_alquran, parent, false)) {
        val alQuranChapter = itemView.findViewById(R.id.txt_al_quran_chapter) as TextView
        val textMeaning = itemView.findViewById(R.id.txt_meaning) as TextView
        val textTranslation = itemView.findViewById(R.id.txt_translation) as TextView
    }
}
