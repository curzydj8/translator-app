package com.marsgeek.translator

import com.marsgeek.translator.data.TranslationRecord
import com.marsgeek.translator.data.toRecord
import com.marsgeek.translator.data.toTask
import com.marsgeek.translator.translation.TranslationStatus
import com.marsgeek.translator.translation.TranslationTask
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

/** Phase 4: verifies Task <-> Record mapping round-trip. */
class TranslationMappersTest {

    private fun sampleTask() = TranslationTask(
        id = "task-1",
        sourceLang = "en",
        targetLang = "zh-CN",
        inputText = "hello",
        outputText = "你好",
        engineName = "test",
        status = TranslationStatus.COMPLETED,
        createdAt = 1234567890L
    )

    @Test
    fun taskToRecordPreservesFields() {
        val record = sampleTask().toRecord()
        assertEquals("task-1", record.id)
        assertEquals("en", record.sourceLang)
        assertEquals("zh-CN", record.targetLang)
        assertEquals("hello", record.inputText)
        assertEquals("你好", record.outputText)
        assertEquals("COMPLETED", record.status)
        assertEquals(1234567890L, record.createdAt)
    }

    @Test
    fun recordToTaskRoundTrip() {
        val task = sampleTask().toRecord().toTask()
        assertEquals(sampleTask(), task)
    }

    @Test
    fun unknownStatusFallsBackToFailed() {
        val record = TranslationRecord(
            id = "x", sourceLang = "en", targetLang = "zh-CN",
            inputText = "a", outputText = null, engineName = null,
            status = "BOGUS", errorMessage = "e", createdAt = 0L
        )
        assertEquals(TranslationStatus.FAILED, record.toTask().status)
    }

    @Test
    fun failedTaskKeepsErrorMessage() {
        val task = TranslationTask(
            sourceLang = "en", targetLang = "zh-CN", inputText = "hi",
            status = TranslationStatus.FAILED, errorMessage = "no engine"
        )
        val restored = task.toRecord().toTask()
        assertEquals(TranslationStatus.FAILED, restored.status)
        assertEquals("no engine", restored.errorMessage)
        assertNull(restored.outputText)
    }
}
