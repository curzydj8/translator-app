package com.marsgeek.translator.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.marsgeek.translator.R
import com.marsgeek.translator.TranslatorApp
import com.marsgeek.translator.data.TranslationRepository
import com.marsgeek.translator.translation.TranslateUseCase
import com.marsgeek.translator.translation.TranslationStatus
import com.marsgeek.translator.translation.TranslationTask
import com.marsgeek.translator.translation.UnavailableTranslationEngine
import kotlinx.coroutines.launch

/**
 * Phase 5: translation page with copy/share of the displayed result.
 * Successful tasks are saved to history via TranslationRepository (Phase 4).
 * Copy/share buttons are enabled only while a completed result is shown;
 * only the displayed output text is ever copied or shared, nothing else.
 * No real engine is integrated, so tasks fail with a clear reason.
 */
class TranslateFragment : Fragment() {

    private val useCase = TranslateUseCase(UnavailableTranslationEngine())
    private lateinit var repository: TranslationRepository
    private var lastTask: TranslationTask? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_translate, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val app = requireActivity().application as TranslatorApp
        repository = TranslationRepository(app.database.translationDao())

        val input = view.findViewById<EditText>(R.id.et_input)
        val btnPaste = view.findViewById<Button>(R.id.btn_paste)
        val btnClear = view.findViewById<Button>(R.id.btn_clear)
        val btnTranslate = view.findViewById<Button>(R.id.btn_translate)
        val btnCopy = view.findViewById<Button>(R.id.btn_copy)
        val btnShare = view.findViewById<Button>(R.id.btn_share)
        val spinnerSource = view.findViewById<Spinner>(R.id.spinner_source)
        val spinnerTarget = view.findViewById<Spinner>(R.id.spinner_target)
        val progress = view.findViewById<ProgressBar>(R.id.progress)
        val tvStatus = view.findViewById<TextView>(R.id.tv_status)

        val adapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_item, Languages.displayNames()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSource.adapter = adapter
        spinnerTarget.adapter = adapter
        spinnerTarget.setSelection(1)

        fun updateActionButtons() {
            val shareable = lastTask?.let { shareableText(it) }
            btnCopy.isEnabled = shareable != null
            btnShare.isEnabled = shareable != null
        }
        updateActionButtons()

        btnPaste.setOnClickListener {
            val cm = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = cm.primaryClip
            if (clip != null && clip.itemCount > 0) {
                val text = clip.getItemAt(0).coerceToText(requireContext()).toString()
                input.setText(text)
                input.setSelection(text.length)
                Toast.makeText(requireContext(), R.string.pasted, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), R.string.clipboard_empty, Toast.LENGTH_SHORT).show()
            }
        }

        btnClear.setOnClickListener {
            input.text.clear()
            tvStatus.text = ""
            lastTask = null
            updateActionButtons()
        }

        btnCopy.setOnClickListener {
            val text = lastTask?.let { shareableText(it) } ?: return@setOnClickListener
            val cm = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            cm.setPrimaryClip(ClipData.newPlainText("translation", text))
            Toast.makeText(requireContext(), R.string.copied, Toast.LENGTH_SHORT).show()
        }

        btnShare.setOnClickListener {
            val text = lastTask?.let { shareableText(it) } ?: return@setOnClickListener
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, text)
            }
            startActivity(Intent.createChooser(intent, getString(R.string.share_title)))
        }

        btnTranslate.setOnClickListener {
            val text = input.text.toString()
            if (text.isBlank()) {
                tvStatus.setText(R.string.empty_input_hint)
                return@setOnClickListener
            }
            val sourceCode = Languages.codeFor(spinnerSource.selectedItem.toString()) ?: "en"
            val targetCode = Languages.codeFor(spinnerTarget.selectedItem.toString()) ?: "zh-CN"
            btnTranslate.isEnabled = false
            progress.visibility = View.VISIBLE
            tvStatus.setText(R.string.translating)
            lastTask = null
            updateActionButtons()
            viewLifecycleOwner.lifecycleScope.launch {
                val task = useCase.execute(sourceCode, targetCode, text)
                if (!isAdded) return@launch
                progress.visibility = View.GONE
                btnTranslate.isEnabled = true
                lastTask = task
                if (task.status == TranslationStatus.COMPLETED) {
                    repository.saveCompleted(task)
                    tvStatus.text = task.outputText
                } else if (task.status == TranslationStatus.FAILED) {
                    tvStatus.text = getString(R.string.translate_failed, task.errorMessage)
                } else {
                    tvStatus.text = task.status.name
                }
                updateActionButtons()
            }
        }
    }
}
