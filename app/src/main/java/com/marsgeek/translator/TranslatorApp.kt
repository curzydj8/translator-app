package com.marsgeek.translator

import android.app.Application
import androidx.room.Room
import com.marsgeek.translator.data.AppDatabase

/** Application holding the Room database singleton (Phase 4). */
class TranslatorApp : Application() {
    val database: AppDatabase by lazy {
        Room.databaseBuilder(this, AppDatabase::class.java, "translator.db")
            .fallbackToDestructiveMigration()
            .build()
    }
}
