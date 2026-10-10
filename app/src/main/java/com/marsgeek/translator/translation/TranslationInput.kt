package com.marsgeek.translator.translation

/** Input model for a translation request (Phase 3). */
data class TranslationInput(
    val sourceLang: String,
    val targetLang: String,
    val text: String
)
