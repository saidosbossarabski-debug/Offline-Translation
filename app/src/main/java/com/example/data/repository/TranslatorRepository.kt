package com.example.data.repository

import com.example.data.engine.OfflineTranslationEngine
import com.example.data.engine.PhraseItem
import com.example.data.engine.PhrasebookData
import com.example.data.engine.TranslationResult
import com.example.data.local.FlashcardDao
import com.example.data.model.SavedFlashcard
import com.example.data.model.TranslationHistoryItem
import kotlinx.coroutines.flow.Flow

class TranslatorRepository(
    private val flashcardDao: FlashcardDao
) {
    val allFlashcards: Flow<List<SavedFlashcard>> = flashcardDao.getAllFlashcards()
    val translationHistory: Flow<List<TranslationHistoryItem>> = flashcardDao.getHistory()

    fun searchFlashcards(query: String): Flow<List<SavedFlashcard>> {
        return flashcardDao.searchFlashcards(query)
    }

    suspend fun translate(
        text: String,
        sourceLang: String,
        targetLang: String,
        saveToHistory: Boolean = true
    ): TranslationResult {
        val result = OfflineTranslationEngine.translate(text, sourceLang, targetLang)
        if (saveToHistory && text.isNotBlank() && result.translatedText.isNotBlank()) {
            flashcardDao.insertHistory(
                TranslationHistoryItem(
                    sourceText = text.trim(),
                    translatedText = result.translatedText,
                    sourceLangCode = sourceLang,
                    targetLangCode = targetLang,
                    phonetic = result.phonetic
                )
            )
        }
        return result
    }

    suspend fun saveFlashcard(card: SavedFlashcard): Long {
        return flashcardDao.insertFlashcard(card)
    }

    suspend fun isWordAlreadySaved(sourceText: String, targetLangCode: String): Boolean {
        return flashcardDao.findCard(sourceText.trim(), targetLangCode) != null
    }

    suspend fun deleteFlashcard(card: SavedFlashcard) {
        flashcardDao.deleteFlashcard(card)
    }

    suspend fun deleteFlashcardById(id: Long) {
        flashcardDao.deleteFlashcardById(id)
    }

    suspend fun updateFlashcard(card: SavedFlashcard) {
        flashcardDao.updateFlashcard(card)
    }

    suspend fun recordReview(id: Long, currentLevel: Int, isCorrect: Boolean) {
        val newLevel = if (isCorrect) {
            (currentLevel + 1).coerceAtMost(5)
        } else {
            (currentLevel - 1).coerceAtLeast(0)
        }
        flashcardDao.recordReviewResult(
            id = id,
            level = newLevel,
            isCorrect = if (isCorrect) 1 else 0,
            timestamp = System.currentTimeMillis()
        )
    }

    suspend fun clearHistory() {
        flashcardDao.clearHistory()
    }

    suspend fun deleteHistoryItem(id: Long) {
        flashcardDao.deleteHistoryById(id)
    }

    fun getPhrasebookCategories(): List<String> = PhrasebookData.CATEGORIES

    fun getPhrases(category: String): List<PhraseItem> {
        return if (category == "Все") {
            PhrasebookData.PHRASES
        } else {
            PhrasebookData.PHRASES.filter { it.category == category }
        }
    }
}
