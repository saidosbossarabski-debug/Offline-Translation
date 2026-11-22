package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.engine.OfflineTranslationEngine
import com.example.data.engine.PhraseItem
import com.example.data.engine.PhrasebookData
import com.example.data.engine.TranslationResult
import com.example.data.local.AppDatabase
import com.example.data.model.Language
import com.example.data.model.Languages
import com.example.data.model.SavedFlashcard
import com.example.data.model.TranslationHistoryItem
import com.example.data.repository.TranslatorRepository
import com.example.util.TtsManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class StudyMode {
    FLIP_CARD,
    QUIZ_4_OPTIONS,
    AUDIO_QUIZ
}

class TranslatorViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TranslatorRepository
    private val ttsManager: TtsManager = TtsManager(application)

    init {
        val db = AppDatabase.getInstance(application)
        repository = TranslatorRepository(db.flashcardDao())
    }

    // Translation screen states
    private val _sourceText = MutableStateFlow("")
    val sourceText: StateFlow<String> = _sourceText.asStateFlow()

    private val _sourceLang = MutableStateFlow(Languages.findByCode("ru"))
    val sourceLang: StateFlow<Language> = _sourceLang.asStateFlow()

    private val _targetLang = MutableStateFlow(Languages.findByCode("zh")) // Chinese by default as in prompt!
    val targetLang: StateFlow<Language> = _targetLang.asStateFlow()

    private val _translationResult = MutableStateFlow<TranslationResult?>(null)
    val translationResult: StateFlow<TranslationResult?> = _translationResult.asStateFlow()

    private val _isSavedInCards = MutableStateFlow(false)
    val isSavedInCards: StateFlow<Boolean> = _isSavedInCards.asStateFlow()

    private val _saveFeedbackMessage = MutableStateFlow<String?>(null)
    val saveFeedbackMessage: StateFlow<String?> = _saveFeedbackMessage.asStateFlow()

    // Flashcards list states
    val allCards: StateFlow<List<SavedFlashcard>> = repository.allFlashcards
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val history: StateFlow<List<TranslationHistoryItem>> = repository.translationHistory
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _cardSearchQuery = MutableStateFlow("")
    val cardSearchQuery: StateFlow<String> = _cardSearchQuery.asStateFlow()

    private val _selectedCardStatusFilter = MutableStateFlow("Все") // Все, Новые, Изучаю, Выучено
    val selectedCardStatusFilter: StateFlow<String> = _selectedCardStatusFilter.asStateFlow()

    val filteredCards: StateFlow<List<SavedFlashcard>> = combine(
        allCards,
        _cardSearchQuery,
        _selectedCardStatusFilter
    ) { cards, query, status ->
        cards.filter { card ->
            val matchesQuery = query.isBlank() ||
                    card.sourceText.contains(query, ignoreCase = true) ||
                    card.translatedText.contains(query, ignoreCase = true) ||
                    card.note.contains(query, ignoreCase = true)

            val matchesStatus = when (status) {
                "Новые" -> card.masteryLevel == 0
                "Изучаю" -> card.masteryLevel in 1..4
                "Выучено" -> card.masteryLevel >= 5
                else -> true
            }

            matchesQuery && matchesStatus
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Study session states
    private val _studyMode = MutableStateFlow(StudyMode.FLIP_CARD)
    val studyMode: StateFlow<StudyMode> = _studyMode.asStateFlow()

    private val _studyCards = MutableStateFlow<List<SavedFlashcard>>(emptyList())
    val studyCards: StateFlow<List<SavedFlashcard>> = _studyCards.asStateFlow()

    private val _currentStudyIndex = MutableStateFlow(0)
    val currentStudyIndex: StateFlow<Int> = _currentStudyIndex.asStateFlow()

    private val _isCardFlipped = MutableStateFlow(false)
    val isCardFlipped: StateFlow<Boolean> = _isCardFlipped.asStateFlow()

    private val _quizOptions = MutableStateFlow<List<String>>(emptyList())
    val quizOptions: StateFlow<List<String>> = _quizOptions.asStateFlow()

    private val _quizSelectedOption = MutableStateFlow<String?>(null)
    val quizSelectedOption: StateFlow<String?> = _quizSelectedOption.asStateFlow()

    private val _isQuizCorrect = MutableStateFlow<Boolean?>(null)
    val isQuizCorrect: StateFlow<Boolean?> = _isQuizCorrect.asStateFlow()

    private val _studySessionComplete = MutableStateFlow(false)
    val studySessionComplete: StateFlow<Boolean> = _studySessionComplete.asStateFlow()

    private val _sessionCorrectCount = MutableStateFlow(0)
    val sessionCorrectCount: StateFlow<Int> = _sessionCorrectCount.asStateFlow()

    private val _sessionWrongCount = MutableStateFlow(0)
    val sessionWrongCount: StateFlow<Int> = _sessionWrongCount.asStateFlow()

    // Phrasebook states
    private val _selectedPhraseCategory = MutableStateFlow("Все")
    val selectedPhraseCategory: StateFlow<String> = _selectedPhraseCategory.asStateFlow()

    init {
        // Pre-populate with a sample translation on first start so screen is immediately interactive
        updateSourceText("Привет")
    }

    fun updateSourceText(text: String) {
        _sourceText.value = text
        if (text.isBlank()) {
            _translationResult.value = null
            _isSavedInCards.value = false
            return
        }
        executeTranslation(text, _sourceLang.value.code, _targetLang.value.code)
    }

    fun setSourceLanguage(lang: Language) {
        if (lang.code == _targetLang.value.code) {
            swapLanguages()
        } else {
            _sourceLang.value = lang
            if (_sourceText.value.isNotBlank()) {
                executeTranslation(_sourceText.value, lang.code, _targetLang.value.code)
            }
        }
    }

    fun setTargetLanguage(lang: Language) {
        if (lang.code == _sourceLang.value.code) {
            swapLanguages()
        } else {
            _targetLang.value = lang
            if (_sourceText.value.isNotBlank()) {
                executeTranslation(_sourceText.value, _sourceLang.value.code, lang.code)
            }
        }
    }

    fun swapLanguages() {
        val temp = _sourceLang.value
        _sourceLang.value = _targetLang.value
        _targetLang.value = temp

        val currentResult = _translationResult.value
        if (currentResult != null && currentResult.translatedText.isNotBlank()) {
            _sourceText.value = currentResult.translatedText
            executeTranslation(currentResult.translatedText, _sourceLang.value.code, _targetLang.value.code)
        } else if (_sourceText.value.isNotBlank()) {
            executeTranslation(_sourceText.value, _sourceLang.value.code, _targetLang.value.code)
        }
    }

    private fun executeTranslation(text: String, srcCode: String, trgCode: String) {
        viewModelScope.launch {
            val result = repository.translate(text, srcCode, trgCode, saveToHistory = true)
            _translationResult.value = result
            _isSavedInCards.value = repository.isWordAlreadySaved(text, trgCode)
        }
    }

    fun saveCurrentTranslationToCards(customNote: String = "") {
        val result = _translationResult.value ?: return
        val srcText = _sourceText.value.trim()
        if (srcText.isEmpty() || result.translatedText.isEmpty()) return

        viewModelScope.launch {
            val card = SavedFlashcard(
                sourceText = srcText,
                translatedText = result.translatedText,
                sourceLangCode = _sourceLang.value.code,
                targetLangCode = _targetLang.value.code,
                sourceLangName = _sourceLang.value.nameRu,
                targetLangName = _targetLang.value.nameRu,
                phonetic = result.phonetic,
                note = customNote.ifEmpty { result.notes },
                category = "Словарь"
            )
            repository.saveFlashcard(card)
            _isSavedInCards.value = true
            _saveFeedbackMessage.value = "Слово \"${result.translatedText}\" сохранено в карточки!"
        }
    }

    fun clearSaveFeedbackMessage() {
        _saveFeedbackMessage.value = null
    }

    fun saveCustomCard(
        sourceText: String,
        translatedText: String,
        sourceLangCode: String,
        targetLangCode: String,
        phonetic: String,
        note: String
    ) {
        viewModelScope.launch {
            val srcLang = Languages.findByCode(sourceLangCode)
            val trgLang = Languages.findByCode(targetLangCode)
            val finalPhonetic = phonetic.ifBlank {
                OfflineTranslationEngine.getPhonetic(translatedText, targetLangCode)
            }
            val card = SavedFlashcard(
                sourceText = sourceText.trim(),
                translatedText = translatedText.trim(),
                sourceLangCode = sourceLangCode,
                targetLangCode = targetLangCode,
                sourceLangName = srcLang.nameRu,
                targetLangName = trgLang.nameRu,
                phonetic = finalPhonetic,
                note = note
            )
            repository.saveFlashcard(card)
            _saveFeedbackMessage.value = "Карточка добавлена!"
        }
    }

    fun deleteCard(card: SavedFlashcard) {
        viewModelScope.launch {
            repository.deleteFlashcard(card)
            // check if current active translation was this word
            if (_sourceText.value.equals(card.sourceText, ignoreCase = true) &&
                _targetLang.value.code == card.targetLangCode
            ) {
                _isSavedInCards.value = false
            }
        }
    }

    fun savePhraseToCards(phrase: PhraseItem, targetLang: Language) {
        viewModelScope.launch {
            // Translate the phrase to target language
            val transResult = OfflineTranslationEngine.translate(phrase.textRu, "ru", targetLang.code)
            val card = SavedFlashcard(
                sourceText = phrase.textRu,
                translatedText = transResult.translatedText,
                sourceLangCode = "ru",
                targetLangCode = targetLang.code,
                sourceLangName = "Русский",
                targetLangName = targetLang.nameRu,
                phonetic = transResult.phonetic,
                note = "Разговорник: ${phrase.category}",
                category = phrase.category
            )
            repository.saveFlashcard(card)
            _saveFeedbackMessage.value = "\"${transResult.translatedText}\" добавлено в карточки!"
        }
    }

    fun setCardSearchQuery(query: String) {
        _cardSearchQuery.value = query
    }

    fun setCardStatusFilter(status: String) {
        _selectedCardStatusFilter.value = status
    }

    fun setPhraseCategory(category: String) {
        _selectedPhraseCategory.value = category
    }

    fun clearHistory() {
        viewModelScope.launch {
            repository.clearHistory()
        }
    }

    fun deleteHistoryItem(id: Long) {
        viewModelScope.launch {
            repository.deleteHistoryItem(id)
        }
    }

    fun speak(text: String, langCode: String) {
        ttsManager.speak(text, langCode)
    }

    // Study & Learning Session Logic
    fun startStudySession(mode: StudyMode = StudyMode.FLIP_CARD) {
        _studyMode.value = mode
        val cards = allCards.value.shuffled()
        if (cards.isEmpty()) {
            _studyCards.value = emptyList()
            _studySessionComplete.value = false
            return
        }
        _studyCards.value = cards
        _currentStudyIndex.value = 0
        _isCardFlipped.value = false
        _quizSelectedOption.value = null
        _isQuizCorrect.value = null
        _studySessionComplete.value = false
        _sessionCorrectCount.value = 0
        _sessionWrongCount.value = 0

        prepareQuizOptionsForCurrentCard()
    }

    fun flipCard() {
        _isCardFlipped.value = !_isCardFlipped.value
    }

    private fun prepareQuizOptionsForCurrentCard() {
        val cards = _studyCards.value
        val index = _currentStudyIndex.value
        if (cards.isEmpty() || index >= cards.size) return

        val currentCard = cards[index]
        val correctAnswer = currentCard.translatedText

        // Gather distractors
        val otherCards = allCards.value.filter { it.id != currentCard.id && it.translatedText != correctAnswer }
        val distractors = otherCards.shuffled().take(3).map { it.translatedText }.toMutableList()

        // If not enough cards in DB, take standard vocabulary words
        val backupOptions = listOf("Спасибо", "Вода", "Здравствуйте", "Хорошо", "Друг", "Любовь", "Дом", "Книга")
        var fallbackIdx = 0
        while (distractors.size < 3 && fallbackIdx < backupOptions.size) {
            val opt = backupOptions[fallbackIdx++]
            if (opt != correctAnswer && !distractors.contains(opt)) {
                distractors.add(opt)
            }
        }

        val allOptions = (distractors + correctAnswer).shuffled()
        _quizOptions.value = allOptions
        _quizSelectedOption.value = null
        _isQuizCorrect.value = null
    }

    fun selectQuizOption(option: String) {
        if (_quizSelectedOption.value != null) return // Already answered
        val cards = _studyCards.value
        val index = _currentStudyIndex.value
        if (cards.isEmpty() || index >= cards.size) return

        val currentCard = cards[index]
        val isCorrect = (option == currentCard.translatedText)

        _quizSelectedOption.value = option
        _isQuizCorrect.value = isCorrect

        if (isCorrect) {
            _sessionCorrectCount.value++
        } else {
            _sessionWrongCount.value++
        }

        // Record progress in Room DB
        viewModelScope.launch {
            repository.recordReview(currentCard.id, currentCard.masteryLevel, isCorrect)
        }
    }

    fun submitFlipReview(isCorrect: Boolean) {
        val cards = _studyCards.value
        val index = _currentStudyIndex.value
        if (cards.isEmpty() || index >= cards.size) return

        val currentCard = cards[index]
        if (isCorrect) {
            _sessionCorrectCount.value++
        } else {
            _sessionWrongCount.value++
        }

        viewModelScope.launch {
            repository.recordReview(currentCard.id, currentCard.masteryLevel, isCorrect)
        }

        moveToNextCard()
    }

    fun moveToNextCard() {
        val cards = _studyCards.value
        val nextIndex = _currentStudyIndex.value + 1
        if (nextIndex >= cards.size) {
            _studySessionComplete.value = true
        } else {
            _currentStudyIndex.value = nextIndex
            _isCardFlipped.value = false
            prepareQuizOptionsForCurrentCard()
        }
    }

    override fun onCleared() {
        super.onCleared()
        ttsManager.shutdown()
    }
}
