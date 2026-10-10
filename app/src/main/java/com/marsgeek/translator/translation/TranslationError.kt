package com.marsgeek.translator.translation

/** Structured translation failures (Phase 3). */
sealed class TranslationError(val message: String) {
    class EngineUnavailable(message: String) : TranslationError(message)
    class Unknown(message: String) : TranslationError(message)
}
