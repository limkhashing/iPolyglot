package com.kslimweb.ipolygot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kslimweb.ipolygot.algolia_data.Hit

class SpeechTranslateAdapter(private var speechResult: String = "",
                             private var translateText: String = "",
                             private var hits: List<Hit> = emptyList())
    : RecyclerView.Adapter<SpeechTranslateAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context), parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.speechText.text = speechResult
        holder.translateText.text = translateText

        if (hits.isNotEmpty()) {
            holder.searchAppearLabel.visibility = View.VISIBLE
            holder.searchRecyclerView.adapter = AlgoliaSearchAdapter(hits)
            holder.searchRecyclerView.visibility = View.VISIBLE
        } else {
            holder.searchAppearLabel.visibility = View.GONE
            holder.searchRecyclerView.visibility = View.GONE
        }
    }

    internal fun setResult(speechResult: String, translateText: String, hits: List<Hit>) {
        this.speechResult = speechResult
        this.translateText = translateText
        notifyDataSetChanged()
        this.hits = hits
    }

    class ViewHolder internal constructor(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.cardview_speech_translate, parent, false)) {
        val speechText: TextView = itemView.findViewById(R.id.speech_text) as TextView
        val translateText: TextView = itemView.findViewById(R.id.translate_text) as TextView
        val searchAppearLabel: TextView = itemView.findViewById(R.id.txt_label_search) as TextView
        val searchRecyclerView: RecyclerView = itemView.findViewById(R.id.rv_search) as RecyclerView
    }
}