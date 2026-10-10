package com.marsgeek.translator.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.marsgeek.translator.R
import com.marsgeek.translator.translation.TranslationTask
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/** Phase 4: history list adapter. */
class HistoryAdapter(
    private val onItemClick: (TranslationTask) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    private var items: List<TranslationTask> = emptyList()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

    fun submitList(list: List<TranslationTask>) {
        items = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvInput: TextView = view.findViewById(R.id.tv_item_input)
        private val tvOutput: TextView = view.findViewById(R.id.tv_item_output)
        private val tvMeta: TextView = view.findViewById(R.id.tv_item_meta)

        fun bind(task: TranslationTask) {
            tvInput.text = task.inputText
            tvOutput.text = task.outputText ?: task.errorMessage ?: ""
            val direction = task.sourceLang + " -> " + task.targetLang
            val time = dateFormat.format(Date(task.createdAt))
            tvMeta.text = "$direction  |  $time  |  ${task.status.name}"
            itemView.setOnClickListener { onItemClick(task) }
        }
    }
}
