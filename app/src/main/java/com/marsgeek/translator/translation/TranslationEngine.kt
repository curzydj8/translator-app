package com.marsgeek.translator.translation

/** Translation engine interface (Phase 3). */
interface TranslationEngine {
    val name: String
    suspend fun translate(input: TranslationInput): TranslationOutput
}
