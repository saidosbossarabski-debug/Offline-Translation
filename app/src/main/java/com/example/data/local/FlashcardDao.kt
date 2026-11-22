package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.SavedFlashcard
import com.example.data.model.TranslationHistoryItem
import kotlinx.coroutines.flow.Flow

@Dao
interface FlashcardDao {

    @Query("SELECT * FROM flashcards ORDER BY createdAt DESC")
    fun getAllFlashcards(): Flow<List<SavedFlashcard>>

    @Query("SELECT * FROM flashcards WHERE sourceLangCode = :src AND targetLangCode = :trg ORDER BY createdAt DESC")
    fun getFlashcardsByLanguages(src: String, trg: String): Flow<List<SavedFlashcard>>

    @Query("SELECT * FROM flashcards WHERE sourceText LIKE '%' || :query || '%' OR translatedText LIKE '%' || :query || '%' ORDER BY createdAt DESC")
    fun searchFlashcards(query: String): Flow<List<SavedFlashcard>>

    @Query("SELECT * FROM flashcards WHERE LOWER(sourceText) = LOWER(:sourceText) AND targetLangCode = :targetLang LIMIT 1")
    suspend fun findCard(sourceText: String, targetLang: String): SavedFlashcard?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFlashcard(card: SavedFlashcard): Long

    @Update
    suspend fun updateFlashcard(card: SavedFlashcard)

    @Delete
    suspend fun deleteFlashcard(card: SavedFlashcard)

    @Query("DELETE FROM flashcards WHERE id = :id")
    suspend fun deleteFlashcardById(id: Long)

    @Query("UPDATE flashcards SET masteryLevel = :level, reviewCount = reviewCount + 1, correctCount = correctCount + :isCorrect, lastReviewedAt = :timestamp WHERE id = :id")
    suspend fun recordReviewResult(id: Long, level: Int, isCorrect: Int, timestamp: Long)

    // History queries
    @Query("SELECT * FROM translation_history ORDER BY timestamp DESC LIMIT 100")
    fun getHistory(): Flow<List<TranslationHistoryItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(item: TranslationHistoryItem): Long

    @Query("DELETE FROM translation_history WHERE id = :id")
    suspend fun deleteHistoryById(id: Long)

    @Query("DELETE FROM translation_history")
    suspend fun clearHistory()
}
