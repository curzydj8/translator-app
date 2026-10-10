package com.marsgeek.translator.translation

/** Output model produced by a real translation engine (Phase 3). */
data class TranslationOutput(
    val translatedText: String,
    val engineName: String
)
