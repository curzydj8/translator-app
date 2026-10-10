package com.marsgeek.translator

import com.marsgeek.translator.translation.TranslationStatus
import com.marsgeek.translator.translation.TranslationTask
import com.marsgeek.translator.ui.shareableText
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

/** Phase 5: copy/share exposes only the displayed translation output. */
class ShareTextTest {

    private fun task(status: TranslationStatus, output: String? = "你好") = TranslationTask(
        id = "task-1",
        sourceLang = "en",
        targetLang = "zh-CN",
        inputText = "hello",
        outputText = output,
        engineName = "test-engine",
        status = status
    )

    @Test
    fun completedTaskSharesOutputText() {
        assertEquals("你好", shareableText(task(TranslationStatus.COMPLETED)))
    }

    @Test
    fun failedTaskSharesNothing() {
        assertNull(shareableText(task(TranslationStatus.FAILED)))
    }

    @Test
    fun pendingTaskSharesNothing() {
        assertNull(shareableText(task(TranslationStatus.PENDING)))
    }

    @Test
    fun translatingTaskSharesNothing() {
        assertNull(shareableText(task(TranslationStatus.TRANSLATING)))
    }

    @Test
    fun completedTaskWithoutOutputSharesNothing() {
        assertNull(shareableText(task(TranslationStatus.COMPLETED, output = null)))
    }

    @Test
    fun sharedTextIsExactlyTheOutput() {
        // Only the output may leave the app: no input text, id, or engine name.
        val shared = shareableText(task(TranslationStatus.COMPLETED, output = "Bonjour"))
        assertEquals("Bonjour", shared)
    }
}
