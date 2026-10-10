package com.marsgeek.translator.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Room entity for translation history (Phase 4). */
@Entity(tableName = "translation_history")
data class TranslationRecord(
    @PrimaryKey val id: String,
    val sourceLang: String,
    val targetLang: String,
    val inputText: String,
    val outputText: String?,
    val engineName: String?,
    val status: String,
    val errorMessage: String?,
    val createdAt: Long
)
