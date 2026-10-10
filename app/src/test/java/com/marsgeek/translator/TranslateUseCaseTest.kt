package com.marsgeek.translator

import com.marsgeek.translator.translation.TranslateUseCase
import com.marsgeek.translator.translation.TranslationEngine
import com.marsgeek.translator.translation.TranslationInput
import com.marsgeek.translator.translation.TranslationOutput
import com.marsgeek.translator.translation.TranslationStatus
import com.marsgeek.translator.translation.UnavailableTranslationEngine
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

/**
 * Test-only fake engine: a unit-test double, never shipped as real translation.
 */
private class FakeTestEngine : TranslationEngine {
    override val name: String = "test-fake"
    override suspend fun translate(input: TranslationInput): TranslationOutput =
        TranslationOutput(translatedText = "[TEST]" + input.text, engineName = name)
}

class TranslateUseCaseTest {

    @Test
    fun unavailableEngineProducesFailedTask() = runBlocking {
        val task = TranslateUseCase(UnavailableTranslationEngine())
            .execute("en", "zh-CN", "hello")
        assertEquals(TranslationStatus.FAILED, task.status)
        assertEquals("当前未安装可用翻译引擎。", task.errorMessage)
        assertNull(task.outputText)
        assertEquals("unavailable", task.engineName)
        assertNotNull(task.id)
    }

    @Test
    fun engineSuccessProducesCompletedTask() = runBlocking {
        val task = TranslateUseCase(FakeTestEngine()).execute("en", "zh-CN", "hello")
        assertEquals(TranslationStatus.COMPLETED, task.status)
        assertEquals("[TEST]hello", task.outputText)
        assertEquals("test-fake", task.engineName)
        assertNull(task.errorMessage)
    }

    @Test
    fun taskCarriesRequestFields() = runBlocking {
        val task = TranslateUseCase(FakeTestEngine()).execute("en", "zh-CN", "hi")
        assertEquals("en", task.sourceLang)
        assertEquals("zh-CN", task.targetLang)
        assertEquals("hi", task.inputText)
    }
}
