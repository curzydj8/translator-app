package com.marsgeek.translator.data

import com.marsgeek.translator.translation.TranslationStatus
import com.marsgeek.translator.translation.TranslationTask

/** Pure mapping between domain model and Room entity (Phase 4). */
fun TranslationTask.toRecord(): TranslationRecord = TranslationRecord(
    id = id,
    sourceLang = sourceLang,
    targetLang = targetLang,
    inputText = inputText,
    outputText = outputText,
    engineName = engineName,
    status = status.name,
    errorMessage = errorMessage,
    createdAt = createdAt
)

fun TranslationRecord.toTask(): TranslationTask = TranslationTask(
    id = id,
    sourceLang = sourceLang,
    targetLang = targetLang,
    inputText = inputText,
    outputText = outputText,
    engineName = engineName,
    status = runCatching { TranslationStatus.valueOf(status) }
        .getOrDefault(TranslationStatus.FAILED),
    errorMessage = errorMessage,
    createdAt = createdAt
)
