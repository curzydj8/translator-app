package com.marsgeek.translator.translation

import java.util.UUID

/** Unified translation task model (Phase 3). */
data class TranslationTask(
    val id: String = UUID.randomUUID().toString(),
    val sourceLang: String,
    val targetLang: String,
    val inputText: String,
    val outputText: String? = null,
    val engineName: String? = null,
    val status: TranslationStatus = TranslationStatus.PENDING,
    val errorMessage: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
