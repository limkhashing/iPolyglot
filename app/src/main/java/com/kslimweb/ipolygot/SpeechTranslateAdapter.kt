package com.kslimweb.ipolygot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kslimweb.ipolygot.algolia_data.Hit
import java.util.ArrayList

class SpeechTranslateAdapter(private val results: ArrayList<String> = ArrayList(),
                             private var hits: List<Hit> = emptyList())
    : RecyclerView.Adapter<SpeechTranslateAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return results.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context), parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val speechTranslateSearchResult = results[position]
        val resultPart = speechTranslateSearchResult.split("-")
        holder.speechText.text = resultPart[0]
        holder.translateText.text = resultPart[1]

        if (hits.isNotEmpty()) {
            holder.searchAppearLabel.visibility = View.VISIBLE
            holder.searchRecyclerView.adapter = AlgoliaSearchAdapter(hits)
            holder.searchRecyclerView.visibility = View.VISIBLE
        }

    }

    internal fun addResult(speechResult: String, translateText: String, hits: List<Hit>) {
        val result = speechResult + "-" + translateText
        results.add(0, result)
        this.hits = hits
        notifyItemInserted(0)
    }

    fun getResults(): ArrayList<String> {
        return results
    }

    class ViewHolder internal constructor(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.cardview_speech_translate, parent, false)) {
        val speechText: TextView = itemView.findViewById(R.id.speech_text) as TextView
        val translateText: TextView = itemView.findViewById(R.id.translate_text) as TextView
        val searchAppearLabel: TextView = itemView.findViewById(R.id.txt_label_search) as TextView
        val searchRecyclerView: RecyclerView = itemView.findViewById(R.id.rv_search) as RecyclerView
    }
}