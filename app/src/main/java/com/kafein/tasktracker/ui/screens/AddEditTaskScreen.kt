package com.kafein.tasktracker.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AddEditTaskScreen(
    initialTitle: String = "",
    screenTitle: String = "Yeni Görev",
    onSave: (String) -> Unit
) {
    var title by remember(initialTitle) {
        mutableStateOf(initialTitle)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = screenTitle
        )

        OutlinedTextField(
            value = title,
            onValueChange = {
                title = it
            },
            label = {
                Text("Görev başlığı")
            },
            modifier = Modifier.padding(top = 16.dp)
        )

        Button(
            onClick = {
                onSave(title)
            },
            enabled = title.isNotBlank(),
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Kaydet")
        }
    }
}