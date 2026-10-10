package com.marsgeek.translator.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/** Data access for translation history (Phase 4). */
@Dao
interface TranslationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: TranslationRecord)

    @Query("SELECT * FROM translation_history ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<TranslationRecord>>

    @Query("SELECT * FROM translation_history WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): TranslationRecord?

    @Query("DELETE FROM translation_history WHERE id = :id")
    suspend fun deleteById(id: String)
}
