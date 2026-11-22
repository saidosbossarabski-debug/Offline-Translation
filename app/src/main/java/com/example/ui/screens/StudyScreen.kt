package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FlipCameraAndroid
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Languages
import com.example.ui.viewmodel.StudyMode
import com.example.ui.viewmodel.TranslatorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyScreen(
    viewModel: TranslatorViewModel,
    onNavigateToTranslator: () -> Unit
) {
    val allCards by viewModel.allCards.collectAsState()
    val studyCards by viewModel.studyCards.collectAsState()
    val currentIndex by viewModel.currentStudyIndex.collectAsState()
    val isFlipped by viewModel.isCardFlipped.collectAsState()
    val studyMode by viewModel.studyMode.collectAsState()
    val quizOptions by viewModel.quizOptions.collectAsState()
    val quizSelectedOption by viewModel.quizSelectedOption.collectAsState()
    val isQuizCorrect by viewModel.isQuizCorrect.collectAsState()
    val isComplete by viewModel.studySessionComplete.collectAsState()
    val correctCount by viewModel.sessionCorrectCount.collectAsState()
    val wrongCount by viewModel.sessionWrongCount.collectAsState()

    LaunchedEffect(allCards) {
        if (studyCards.isEmpty() && allCards.isNotEmpty()) {
            viewModel.startStudySession(studyMode)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        // Title
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Тренажёр карточек",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Интервальное повторение и проверка знаний",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Mode selector
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            val modes = listOf(
                StudyMode.FLIP_CARD to "Карточки",
                StudyMode.QUIZ_4_OPTIONS to "Тест",
                StudyMode.AUDIO_QUIZ to "Аудио"
            )
            modes.forEachIndexed { index, (mode, label) ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = modes.size),
                    onClick = { viewModel.startStudySession(mode) },
                    selected = studyMode == mode
                ) {
                    Text(label, fontSize = 13.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (allCards.isEmpty()) {
            // Empty State
            EmptyStudyState(
                onSeedCards = {
                    viewModel.saveCustomCard("Привет", "你好", "ru", "zh", "Nǐ hǎo", "Базовое приветствие")
                    viewModel.saveCustomCard("Спасибо", "谢谢", "ru", "zh", "Xièxiè", "Вежливость")
                    viewModel.saveCustomCard("Вода", "水", "ru", "zh", "Shuǐ", "Напитки")
                    viewModel.saveCustomCard("Где отель?", "酒店在哪里？", "ru", "zh", "Jiǔdiàn zài nǎlǐ?", "Путешествия")
                    viewModel.saveCustomCard("Сколько это стоит?", "这个多少钱？", "ru", "zh", "Zhège duōshao qián?", "Покупки")
                    viewModel.startStudySession(studyMode)
                },
                onGoToTranslator = onNavigateToTranslator
            )
        } else if (isComplete) {
            // Celebration / Session Completed
            SessionCompletedCard(
                correctCount = correctCount,
                wrongCount = wrongCount,
                totalCount = studyCards.size,
                onRestart = { viewModel.startStudySession(studyMode) }
            )
        } else if (studyCards.isNotEmpty() && currentIndex < studyCards.size) {
            val currentCard = studyCards[currentIndex]
            val progress = (currentIndex + 1).toFloat() / studyCards.size.toFloat()
            val srcLang = Languages.findByCode(currentCard.sourceLangCode)
            val trgLang = Languages.findByCode(currentCard.targetLangCode)

            // Progress Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Карточка ${currentIndex + 1} из ${studyCards.size}",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "${(progress * 100).toInt()}%",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            Spacer(modifier = Modifier.height(20.dp))

            when (studyMode) {
                StudyMode.FLIP_CARD -> {
                    // 3D Flip Card View
                    FlipCardView(
                        isFlipped = isFlipped,
                        sourceText = currentCard.sourceText,
                        sourceLangName = "${srcLang.flagEmoji} ${currentCard.sourceLangName}",
                        translatedText = currentCard.translatedText,
                        targetLangName = "${trgLang.flagEmoji} ${currentCard.targetLangName}",
                        phonetic = currentCard.phonetic,
                        note = currentCard.note,
                        onFlip = { viewModel.flipCard() },
                        onSpeak = { viewModel.speak(currentCard.translatedText, currentCard.targetLangCode) }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Review Action Buttons (Need review vs Mastered)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Button(
                            onClick = { viewModel.submitFlipReview(isCorrect = false) },
                            modifier = Modifier
                                .weight(1f)
                                .height(54.dp)
                                .testTag("study_review_again_button"),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer,
                                contentColor = MaterialTheme.colorScheme.onErrorContainer
                            )
                        ) {
                            Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Повторить", fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { viewModel.submitFlipReview(isCorrect = true) },
                            modifier = Modifier
                                .weight(1f)
                                .height(54.dp)
                                .testTag("study_know_well_button"),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Знаю!", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                StudyMode.QUIZ_4_OPTIONS -> {
                    // Quiz Mode
                    QuizCardView(
                        question = currentCard.sourceText,
                        questionSub = "Выберите перевод для: \"${currentCard.sourceText}\" (${srcLang.flagEmoji} → ${trgLang.flagEmoji})",
                        options = quizOptions,
                        selectedOption = quizSelectedOption,
                        isCorrect = isQuizCorrect,
                        correctAnswer = currentCard.translatedText,
                        onSelectOption = { viewModel.selectQuizOption(it) },
                        onNext = { viewModel.moveToNextCard() }
                    )
                }

                StudyMode.AUDIO_QUIZ -> {
                    // Audio Listening Quiz Mode
                    AudioQuizCardView(
                        targetLangCode = currentCard.targetLangCode,
                        targetText = currentCard.translatedText,
                        options = quizOptions,
                        selectedOption = quizSelectedOption,
                        isCorrect = isQuizCorrect,
                        correctAnswer = currentCard.translatedText,
                        onPlayAudio = { viewModel.speak(currentCard.translatedText, currentCard.targetLangCode) },
                        onSelectOption = { viewModel.selectQuizOption(it) },
                        onNext = { viewModel.moveToNextCard() }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun FlipCardView(
    isFlipped: Boolean,
    sourceText: String,
    sourceLangName: String,
    translatedText: String,
    targetLangName: String,
    phonetic: String,
    note: String,
    onFlip: () -> Unit,
    onSpeak: () -> Unit
) {
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing),
        label = "cardFlip"
    )

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12f * density
            }
            .clickable { onFlip() }
            .testTag("flip_study_card"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = if (isFlipped) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)
            else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            if (rotation <= 90f) {
                // Front Side
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = sourceLangName,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = sourceText,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.FlipCameraAndroid,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.outline
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Нажмите, чтобы перевернуть",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                }
            } else {
                // Back Side (mirrored back so text is normal)
                Column(
                    modifier = Modifier.graphicsLayer { rotationY = 180f },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = targetLangName,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = translatedText,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )
                    if (phonetic.isNotBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = phonetic,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.secondary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    if (note.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = note,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    IconButton(
                        onClick = onSpeak,
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.primary, CircleShape)
                            .size(44.dp)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.VolumeUp,
                            contentDescription = "Озвучить",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuizCardView(
    question: String,
    questionSub: String,
    options: List<String>,
    selectedOption: String?,
    isCorrect: Boolean?,
    correctAnswer: String,
    onSelectOption: (String) -> Unit,
    onNext: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = questionSub,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = question,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

            options.forEach { option ->
                val isSelected = selectedOption == option
                val isThisTheCorrectOne = option == correctAnswer
                val cardColor = when {
                    selectedOption == null -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                    isThisTheCorrectOne -> Color(0xFF10B981).copy(alpha = 0.2f) // green
                    isSelected && isCorrect == false -> MaterialTheme.colorScheme.errorContainer // red
                    else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
                }

                val border = when {
                    selectedOption != null && isThisTheCorrectOne -> BorderStroke(2.dp, Color(0xFF10B981))
                    selectedOption != null && isSelected && isCorrect == false -> BorderStroke(2.dp, MaterialTheme.colorScheme.error)
                    else -> null
                }

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable(enabled = selectedOption == null) { onSelectOption(option) },
                    shape = RoundedCornerShape(14.dp),
                    color = cardColor,
                    border = border
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = option,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = if (isSelected || (selectedOption != null && isThisTheCorrectOne)) FontWeight.Bold else FontWeight.Medium
                        )
                        if (selectedOption != null) {
                            if (isThisTheCorrectOne) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF10B981))
                            } else if (isSelected) {
                                Icon(Icons.Default.Close, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }

            if (selectedOption != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onNext,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("quiz_next_button"),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Следующая карточка →", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun AudioQuizCardView(
    targetLangCode: String,
    targetText: String,
    options: List<String>,
    selectedOption: String?,
    isCorrect: Boolean?,
    correctAnswer: String,
    onPlayAudio: () -> Unit,
    onSelectOption: (String) -> Unit,
    onNext: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Аудио-тренажёр: прослушайте и выберите правильное слово",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            Surface(
                modifier = Modifier
                    .size(80.dp)
                    .clickable { onPlayAudio() },
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.Hearing,
                        contentDescription = "Прослушать звук",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(42.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Нажмите для воспроизведения",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(20.dp))

            options.forEach { option ->
                val isSelected = selectedOption == option
                val isThisTheCorrectOne = option == correctAnswer
                val cardColor = when {
                    selectedOption == null -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                    isThisTheCorrectOne -> Color(0xFF10B981).copy(alpha = 0.2f)
                    isSelected && isCorrect == false -> MaterialTheme.colorScheme.errorContainer
                    else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
                }

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable(enabled = selectedOption == null) { onSelectOption(option) },
                    shape = RoundedCornerShape(14.dp),
                    color = cardColor
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = option,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                        if (selectedOption != null) {
                            if (isThisTheCorrectOne) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF10B981))
                            } else if (isSelected) {
                                Icon(Icons.Default.Close, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }

            if (selectedOption != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onNext,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Далее →", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun SessionCompletedCard(
    correctCount: Int,
    wrongCount: Int,
    totalCount: Int,
    onRestart: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("session_completed_card"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
        )
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(72.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.EmojiEvents,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Отличная тренировка!",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            val percent = if (totalCount > 0) ((correctCount.toFloat() / totalCount.toFloat()) * 100).toInt() else 0
            Text(
                text = "Вы повторили $totalCount карточек с результатом $percent%",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$correctCount",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF10B981)
                    )
                    Text("Правильно", style = MaterialTheme.typography.labelSmall)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$wrongCount",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
                    )
                    Text("На повторение", style = MaterialTheme.typography.labelSmall)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onRestart,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("restart_study_button"),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Повторить снова", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun EmptyStudyState(
    onSeedCards: () -> Unit,
    onGoToTranslator: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
            modifier = Modifier.size(72.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    Icons.Default.Psychology,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(36.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Нет карточек для тренировки",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Сохраните новые слова в переводчике или загрузите стартовый набор, чтобы начать учить!",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onSeedCards,
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Загрузить 5 частых слов")
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = onGoToTranslator,
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Перейти к переводчику")
        }
    }
}
