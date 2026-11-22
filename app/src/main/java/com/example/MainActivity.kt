package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.Style
import androidx.compose.material.icons.outlined.Translate
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.FlashcardsListScreen
import com.example.ui.screens.PhrasebookScreen
import com.example.ui.screens.StudyScreen
import com.example.ui.screens.TranslatorScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.StudyMode
import com.example.ui.viewmodel.TranslatorViewModel

enum class MainTab(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
) {
    TRANSLATOR("Перевод", Icons.Filled.Translate, Icons.Outlined.Translate, "tab_translator"),
    FLASHCARDS("Карточки", Icons.Filled.Style, Icons.Outlined.Style, "tab_flashcards"),
    STUDY("Тренажёр", Icons.Filled.Psychology, Icons.Outlined.Psychology, "tab_study"),
    PHRASEBOOK("Разговорник", Icons.Filled.MenuBook, Icons.Outlined.MenuBook, "tab_phrasebook")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                PolyglotApp()
            }
        }
    }
}

@Composable
fun PolyglotApp(
    viewModel: TranslatorViewModel = viewModel()
) {
    var selectedTab by remember { mutableStateOf(MainTab.TRANSLATOR) }
    val allCards by viewModel.allCards.collectAsState()

    // Handle back button if on a secondary tab
    BackHandler(enabled = selectedTab != MainTab.TRANSLATOR) {
        selectedTab = MainTab.TRANSLATOR
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing,
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier.testTag("main_navigation_bar")
            ) {
                MainTab.values().forEach { tab ->
                    val isSelected = selectedTab == tab
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { selectedTab = tab },
                        icon = {
                            if (tab == MainTab.FLASHCARDS && allCards.isNotEmpty()) {
                                BadgedBox(
                                    badge = {
                                        Badge {
                                            Text("${allCards.size}")
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                                        contentDescription = tab.title
                                    )
                                }
                            } else {
                                Icon(
                                    imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                                    contentDescription = tab.title
                                )
                            }
                        },
                        label = {
                            Text(
                                text = tab.title,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        modifier = Modifier.testTag(tab.testTag)
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                MainTab.TRANSLATOR -> {
                    TranslatorScreen(
                        viewModel = viewModel,
                        onNavigateToFlashcards = { selectedTab = MainTab.FLASHCARDS }
                    )
                }

                MainTab.FLASHCARDS -> {
                    FlashcardsListScreen(
                        viewModel = viewModel,
                        onStartStudy = {
                            viewModel.startStudySession(StudyMode.FLIP_CARD)
                            selectedTab = MainTab.STUDY
                        }
                    )
                }

                MainTab.STUDY -> {
                    StudyScreen(
                        viewModel = viewModel,
                        onNavigateToTranslator = { selectedTab = MainTab.TRANSLATOR }
                    )
                }

                MainTab.PHRASEBOOK -> {
                    PhrasebookScreen(
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}
