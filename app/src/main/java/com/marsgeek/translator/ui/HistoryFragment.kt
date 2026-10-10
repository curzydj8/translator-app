package com.marsgeek.translator.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.marsgeek.translator.R
import com.marsgeek.translator.TranslatorApp
import com.marsgeek.translator.data.TranslationRepository
import com.marsgeek.translator.translation.TranslationTask
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Phase 4: history list, detail dialog, single-item delete.
 * Data survives app restarts via Room.
 */
class HistoryFragment : Fragment() {

    private lateinit var repository: TranslationRepository
    private lateinit var adapter: HistoryAdapter
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_history, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val app = requireActivity().application as TranslatorApp
        repository = TranslationRepository(app.database.translationDao())

        val recycler = view.findViewById<RecyclerView>(R.id.recycler_history)
        val tvEmpty = view.findViewById<TextView>(R.id.tv_history_empty)
        adapter = HistoryAdapter { task -> showDetail(task) }
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            repository.observeAll().collect { tasks ->
                adapter.submitList(tasks)
                tvEmpty.visibility = if (tasks.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }

    private fun showDetail(task: TranslationTask) {
        val detail = buildString {
            append("源文本：").append(task.inputText).append("\n\n")
            append("目标文本：").append(task.outputText ?: "-").append("\n\n")
            append("语言方向：").append(task.sourceLang).append(" -> ").append(task.targetLang).append("\n")
            append("创建时间：").append(dateFormat.format(Date(task.createdAt))).append("\n")
            append("翻译状态：").append(task.status.name).append("\n")
            append("翻译引擎：").append(task.engineName ?: "-").append("\n")
            if (task.errorMessage != null) {
                append("错误信息：").append(task.errorMessage)
            }
        }
        AlertDialog.Builder(requireContext())
            .setTitle("历史详情")
            .setMessage(detail)
            .setPositiveButton("删除") { _, _ ->
                viewLifecycleOwner.lifecycleScope.launch {
                    repository.deleteById(task.id)
                }
            }
            .setNegativeButton("关闭", null)
            .show()
    }
}
