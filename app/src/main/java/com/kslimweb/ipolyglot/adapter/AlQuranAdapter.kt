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
import com.kslimweb.ipolyglot.databinding.ItemSearchAlquranBinding
import com.kslimweb.ipolyglot.model.alquran.HitAlQuran

class AlQuranAdapter : RecyclerView.Adapter<AlQuranAdapter.AlQuranViewHolder>() {

    private var hitsAlQuran: List<HitAlQuran> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlQuranViewHolder {
        return AlQuranViewHolder(ItemSearchAlquranBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return hitsAlQuran.size
    }

    override fun onBindViewHolder(holder: AlQuranViewHolder, position: Int) {
        holder.bind(hitsAlQuran[position])
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

    inner class AlQuranViewHolder(private val binding: ItemSearchAlquranBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HitAlQuran) = with(binding) {
            binding.item = item
            executePendingBindings()
            if (binding.txtMeaning.text != "") binding.txtMeaning.text = ""
            if (binding.txtTranslation.text != "") binding.txtTranslation.text = ""
            item.highlightedMeanings?.let { setHighlightMeanings(it, binding.txtMeaning) }
            item.highlightedTranslations?.let { setHighlightTranslations(it, binding.txtTranslation) }
        }
    }
}
