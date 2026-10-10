package com.marsgeek.translator.data

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Phase 4: Room database, version 1.
 * Migration strategy: this is the first version, so fallbackToDestructiveMigration()
 * is used. When version 2 is introduced, add an explicit Migration object.
 */
@Database(entities = [TranslationRecord::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun translationDao(): TranslationDao
}
