package com.marsgeek.translator.data

import com.marsgeek.translator.translation.TranslationStatus
import com.marsgeek.translator.translation.TranslationTask
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Phase 4: single Kotlin entry point for all SQLite access.
 * Only COMPLETED tasks are saved (failed tasks are not persisted,
 * consistent with the Phase 3 documented policy).
 */
class TranslationRepository(private val dao: TranslationDao) {

    fun observeAll(): Flow<List<TranslationTask>> =
        dao.observeAll().map { records -> records.map { it.toTask() } }

    suspend fun getById(id: String): TranslationTask? =
        dao.getById(id)?.toTask()

    suspend fun saveCompleted(task: TranslationTask) {
        if (task.status == TranslationStatus.COMPLETED) {
            dao.insert(task.toRecord())
        }
    }

    suspend fun deleteById(id: String) {
        dao.deleteById(id)
    }
}
