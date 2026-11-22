package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AddCustomCardDialog(
    defaultSourceLang: String,
    defaultTargetLang: String,
    onDismiss: () -> Unit,
    onSave: (sourceText: String, translatedText: String, phonetic: String, note: String) -> Unit
) {
    var sourceWord by remember { mutableStateOf("") }
    var translatedWord by remember { mutableStateOf("") }
    var phonetic by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Добавить новую карточку",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = sourceWord,
                    onValueChange = { sourceWord = it },
                    label = { Text("Слово / фраза (на русском или другом)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("custom_card_source_input"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = translatedWord,
                    onValueChange = { translatedWord = it },
                    label = { Text("Перевод (например, на китайском)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("custom_card_target_input"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = phonetic,
                    onValueChange = { phonetic = it },
                    label = { Text("Транскрипция / Пиньинь (необязательно)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Заметка или пример (необязательно)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (sourceWord.isNotBlank() && translatedWord.isNotBlank()) {
                        onSave(sourceWord.trim(), translatedWord.trim(), phonetic.trim(), note.trim())
                        onDismiss()
                    }
                },
                enabled = sourceWord.isNotBlank() && translatedWord.isNotBlank(),
                modifier = Modifier.testTag("custom_card_save_button"),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Сохранить")
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Отмена")
            }
        }
    )
}
