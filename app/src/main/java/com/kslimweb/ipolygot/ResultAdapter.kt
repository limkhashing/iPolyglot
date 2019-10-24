package com.kslimweb.ipolygot

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class ViewHolder internal constructor(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_result, parent, false)) {
    internal var text: TextView = itemView.findViewById(R.id.speech_text) as TextView
}

class ResultAdapter internal constructor(results: ArrayList<String>?) :
    RecyclerView.Adapter<ViewHolder>() {

    private val results = ArrayList<String>()

    init {
        if (results != null) {
            this.results.addAll(results)
        }
    }

    override fun getItemCount(): Int {
        return results.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context), parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.text.text = results[position]
    }

    internal fun addResult(result: String) {
        results.add(0, result)
        notifyItemInserted(0)
    }

    fun getResults(): ArrayList<String> {
        return results
    }
}