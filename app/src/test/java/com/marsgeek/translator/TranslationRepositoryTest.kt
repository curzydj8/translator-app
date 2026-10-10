package com.marsgeek.translator

import com.marsgeek.translator.data.TranslationDao
import com.marsgeek.translator.data.TranslationRecord
import com.marsgeek.translator.data.TranslationRepository
import com.marsgeek.translator.translation.TranslationStatus
import com.marsgeek.translator.translation.TranslationTask
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

/** In-memory fake DAO: verifies repository logic without Android framework. */
private class FakeDao : TranslationDao {
    private val store = mutableMapOf<String, TranslationRecord>()
    override suspend fun insert(record: TranslationRecord) { store[record.id] = record }
    override fun observeAll(): Flow<List<TranslationRecord>> = flowOf(store.values.toList())
    override suspend fun getById(id: String): TranslationRecord? = store[id]
    override suspend fun deleteById(id: String) { store.remove(id) }
}

class TranslationRepositoryTest {

    private fun completedTask() = TranslationTask(
        id = "c1", sourceLang = "en", targetLang = "zh-CN",
        inputText = "hello", outputText = "你好",
        engineName = "test", status = TranslationStatus.COMPLETED
    )

    private fun failedTask() = TranslationTask(
        id = "f1", sourceLang = "en", targetLang = "zh-CN",
        inputText = "hi", status = TranslationStatus.FAILED,
        errorMessage = "no engine"
    )

    @Test
    fun saveCompletedPersistsCompletedTask() = runBlocking {
        val repo = TranslationRepository(FakeDao())
        repo.saveCompleted(completedTask())
        val loaded = repo.getById("c1")
        assertEquals("hello", loaded?.inputText)
        assertEquals("你好", loaded?.outputText)
        assertEquals(TranslationStatus.COMPLETED, loaded?.status)
    }

    @Test
    fun saveCompletedSkipsFailedTask() = runBlocking {
        val repo = TranslationRepository(FakeDao())
        repo.saveCompleted(failedTask())
        assertNull(repo.getById("f1"))
    }

    @Test
    fun deleteByIdRemovesRecord() = runBlocking {
        val repo = TranslationRepository(FakeDao())
        repo.saveCompleted(completedTask())
        repo.deleteById("c1")
        assertNull(repo.getById("c1"))
        assertEquals(0, repo.observeAll().first().size)
    }

    @Test
    fun observeAllReturnsSavedTasks() = runBlocking {
        val repo = TranslationRepository(FakeDao())
        repo.saveCompleted(completedTask())
        val list = repo.observeAll().first()
        assertEquals(1, list.size)
        assertEquals("c1", list[0].id)
    }
}
