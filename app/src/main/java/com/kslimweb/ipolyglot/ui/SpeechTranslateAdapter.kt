package com.kslimweb.ipolyglot.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kslimweb.ipolyglot.R
import com.kslimweb.ipolyglot.model.hadith.HitHadith

class SpeechTranslateAdapter(private var speechResult: String,
                             private var translatedText: String,
                             private var hitHadiths: List<HitHadith>)
    : RecyclerView.Adapter<SpeechTranslateAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context),
            parent
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.speechText.text = speechResult
        holder.translateText.text = translatedText
        holder.searchAppearLabel.visibility = View.VISIBLE
        holder.searchAppearLabel.text = "Appeared in: "
        if (hitHadiths.isNotEmpty()) {
            holder.searchRecyclerView.adapter = SearchResponseAdapter(hitHadiths)
            holder.searchRecyclerView.visibility = View.VISIBLE
        } else {
            holder.searchAppearLabel.append(" None")
            holder.searchRecyclerView.visibility = View.GONE
        }
    }

    fun setResult(speechResult: String, translateText: String, hitHadiths: List<HitHadith>) {
        this.speechResult = speechResult
        this.translatedText = translateText
        this.hitHadiths = hitHadiths
        notifyDataSetChanged()
    }

    class ViewHolder internal constructor(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.cardview_speech_translate, parent, false)) {
        val speechText: TextView = itemView.findViewById(R.id.speech_text) as TextView
        val translateText: TextView = itemView.findViewById(R.id.translate_text) as TextView
        val searchAppearLabel: TextView = itemView.findViewById(R.id.txt_label_search) as TextView
        val searchRecyclerView: RecyclerView = itemView.findViewById(R.id.rv_search) as RecyclerView
    }
}