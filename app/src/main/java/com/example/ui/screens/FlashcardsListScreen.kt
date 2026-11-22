package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Languages
import com.example.data.model.SavedFlashcard
import com.example.ui.components.AddCustomCardDialog
import com.example.ui.viewmodel.TranslatorViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FlashcardsListScreen(
    viewModel: TranslatorViewModel,
    onStartStudy: () -> Unit
) {
    val allCards by viewModel.allCards.collectAsState()
    val filteredCards by viewModel.filteredCards.collectAsState()
    val searchQuery by viewModel.cardSearchQuery.collectAsState()
    val statusFilter by viewModel.selectedCardStatusFilter.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }

    val masteredCount = remember(allCards) { allCards.count { it.masteryLevel >= 5 } }
    val learningCount = remember(allCards) { allCards.count { it.masteryLevel in 1..4 } }
    val newCount = remember(allCards) { allCards.count { it.masteryLevel == 0 } }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.testTag("add_custom_card_fab")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Добавить карточку")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(6.dp))
                // Screen Title
                Text(
                    text = "Мои карточки для изучения",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Сохранённые слова из переводчика для повторения",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            // Statistics Bar
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard(
                        title = "Всего",
                        value = "${allCards.size}",
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Изучаю",
                        value = "$learningCount",
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Выучено",
                        value = "$masteredCount",
                        color = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Big CTA: "Начать учить карточки"
            if (allCards.isNotEmpty()) {
                item {
                    ElevatedCard(
                        onClick = onStartStudy,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("start_study_banner_button"),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Начать тренировку карточек",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "3D перелистывание, викторины и аудио-тесты",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                )
                            }
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(48.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        Icons.Default.PlayArrow,
                                        contentDescription = "Учить",
                                        tint = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Search & Filter
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setCardSearchQuery(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("search_cards_input"),
                    placeholder = { Text("Поиск по словам и переводам...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    shape = RoundedCornerShape(14.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val filters = listOf("Все", "Новые", "Изучаю", "Выучено")
                    filters.forEach { filter ->
                        FilterChip(
                            selected = statusFilter == filter,
                            onClick = { viewModel.setCardStatusFilter(filter) },
                            label = { Text(filter) }
                        )
                    }
                }
            }

            // Cards List
            if (filteredCards.isEmpty()) {
                item {
                    EmptyCardsState(
                        hasAnyCards = allCards.isNotEmpty(),
                        onAddSamples = {
                            // Seed essential sample cards (Chinese, English, Spanish)
                            viewModel.saveCustomCard("Привет", "你好", "ru", "zh", "Nǐ hǎo", "Базовое приветствие")
                            viewModel.saveCustomCard("Спасибо", "谢谢", "ru", "zh", "Xièxiè", "Вежливость")
                            viewModel.saveCustomCard("Вода", "水", "ru", "zh", "Shuǐ", "Еда и напитки")
                            viewModel.saveCustomCard("Где туалет?", "洗手间在哪里？", "ru", "zh", "Xǐshǒujiān zài nǎlǐ?", "Путешествия")
                            viewModel.saveCustomCard("Сколько это стоит?", "这个多少钱？", "ru", "zh", "Zhège duōshao qián?", "Покупки")
                        }
                    )
                }
            } else {
                items(filteredCards, key = { it.id }) { card ->
                    FlashcardItemRow(
                        card = card,
                        onSpeak = { text, lang -> viewModel.speak(text, lang) },
                        onDelete = { viewModel.deleteCard(card) }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(72.dp))
            }
        }
    }

    if (showAddDialog) {
        AddCustomCardDialog(
            defaultSourceLang = "ru",
            defaultTargetLang = "zh",
            onDismiss = { showAddDialog = false },
            onSave = { src, trg, pho, note ->
                viewModel.saveCustomCard(src, trg, "ru", "zh", pho, note)
            }
        )
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    color: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        color = color.copy(alpha = 0.12f)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun FlashcardItemRow(
    card: SavedFlashcard,
    onSpeak: (String, String) -> Unit,
    onDelete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    val srcLang = Languages.findByCode(card.sourceLangCode)
    val trgLang = Languages.findByCode(card.targetLangCode)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded }
            .testTag("flashcard_item_${card.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Languages header
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "${srcLang.flagEmoji} → ${trgLang.flagEmoji}", fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${card.sourceLangName} → ${card.targetLangName}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }

                // Mastery Stars
                Row(verticalAlignment = Alignment.CenterVertically) {
                    repeat(5) { index ->
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = if (index < card.masteryLevel) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.outlineVariant,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Main texts
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = card.sourceText,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = card.translatedText,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    if (card.phonetic.isNotBlank()) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = card.phonetic,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Row {
                    IconButton(
                        onClick = { onSpeak(card.translatedText, card.targetLangCode) },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.VolumeUp,
                            contentDescription = "Озвучить",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Удалить",
                            tint = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }

            if (card.note.isNotBlank() || isExpanded) {
                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = card.note.ifEmpty { "Повторений: ${card.reviewCount} • Успешно: ${card.correctCount}" },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyCardsState(
    hasAnyCards: Boolean,
    onAddSamples: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
            modifier = Modifier.size(72.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    Icons.Default.AutoStories,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(36.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (hasAnyCards) "По вашему запросу ничего не найдено" else "Ваша колода карточек пока пуста",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = if (hasAnyCards) "Попробуйте изменить фильтр или поисковый запрос"
            else "Переведите любое слово на первой вкладке и нажмите кнопку «Сохранить в карточки»!",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.padding(horizontal = 24.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        if (!hasAnyCards) {
            Spacer(modifier = Modifier.height(18.dp))
            Button(
                onClick = onAddSamples,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("seed_sample_cards_button")
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Добавить 5 частых слов (Китайский)")
            }
        }
    }
}
