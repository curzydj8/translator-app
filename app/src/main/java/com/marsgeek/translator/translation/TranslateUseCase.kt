package com.marsgeek.translator.translation

/** Executes a translation through the engine interface (Phase 3). */
class TranslateUseCase(private val engine: TranslationEngine) {

    suspend fun execute(sourceLang: String, targetLang: String, text: String): TranslationTask {
        val task = TranslationTask(
            sourceLang = sourceLang,
            targetLang = targetLang,
            inputText = text,
            engineName = engine.name,
            status = TranslationStatus.TRANSLATING
        )
        return try {
            val output = engine.translate(TranslationInput(sourceLang, targetLang, text))
            task.copy(
                outputText = output.translatedText,
                engineName = output.engineName,
                status = TranslationStatus.COMPLETED
            )
        } catch (e: Exception) {
            task.copy(
                status = TranslationStatus.FAILED,
                errorMessage = e.message ?: "Unknown error"
            )
        }
    }
}
