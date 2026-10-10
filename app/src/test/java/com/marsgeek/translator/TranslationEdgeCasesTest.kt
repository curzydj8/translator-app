package com.marsgeek.translator

import com.marsgeek.translator.translation.TranslateUseCase
import com.marsgeek.translator.translation.TranslationEngine
import com.marsgeek.translator.translation.TranslationInput
import com.marsgeek.translator.translation.TranslationOutput
import com.marsgeek.translator.translation.TranslationStatus
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

/** Phase 5: input edge cases travel through the use-case untouched. */
private class EchoTestEngine : TranslationEngine {
    override val name: String = "echo"
    override suspend fun translate(input: TranslationInput): TranslationOutput =
        TranslationOutput(translatedText = input.text, engineName = name)
}

class TranslationEdgeCasesTest {

    @Test
    fun multilineInputPreserved() = runBlocking {
        val text = "line one\nline two\nline three"
        val task = TranslateUseCase(EchoTestEngine()).execute("en", "zh-CN", text)
        assertEquals(TranslationStatus.COMPLETED, task.status)
        assertEquals(text, task.inputText)
        assertEquals(text, task.outputText)
    }

    @Test
    fun specialCharactersPreserved() = runBlocking {
        val text = "Hello, 世界! @#\$%^&*()_+-=[]{}|;:'\",./<>? s92"
        val task = TranslateUseCase(EchoTestEngine()).execute("en", "zh-CN", text)
        assertEquals(text, task.inputText)
        assertEquals(text, task.outputText)
    }

    @Test
    fun emojiAndUnicodePreserved() = runBlocking {
        val text = "Mars"
        val task = TranslateUseCase(EchoTestEngine()).execute("en", "zh-CN", text)
        assertEquals(text, task.inputText)
        assertEquals(text, task.outputText)
    }

    @Test
    fun blankInputDoesNotCrashUseCase() = runBlocking {
        // Blank input is rejected at the UI layer; the use-case itself
        // must not crash if it ever receives one.
        val task = TranslateUseCase(EchoTestEngine()).execute("en", "zh-CN", "   ")
        assertEquals("   ", task.inputText)
    }
}
