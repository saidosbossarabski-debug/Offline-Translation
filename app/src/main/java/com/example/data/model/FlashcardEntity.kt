package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flashcards")
data class SavedFlashcard(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val sourceText: String,
    val translatedText: String,
    val sourceLangCode: String,
    val targetLangCode: String,
    val sourceLangName: String,
    val targetLangName: String,
    val phonetic: String = "",
    val note: String = "",
    val category: String = "Общее",
    val masteryLevel: Int = 0,    // 0 = Новый, 1..4 = В процессе, 5 = Выучено
    val reviewCount: Int = 0,
    val correctCount: Int = 0,
    val lastReviewedAt: Long = 0L,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "translation_history")
data class TranslationHistoryItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val sourceText: String,
    val translatedText: String,
    val sourceLangCode: String,
    val targetLangCode: String,
    val phonetic: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
