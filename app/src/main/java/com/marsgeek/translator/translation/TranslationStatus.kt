package com.marsgeek.translator.translation

/** Translation task lifecycle states (Phase 3). */
enum class TranslationStatus {
    PENDING,
    TRANSLATING,
    COMPLETED,
    FAILED
}
