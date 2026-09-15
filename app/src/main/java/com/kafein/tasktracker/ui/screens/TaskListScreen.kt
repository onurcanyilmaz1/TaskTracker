package com.kafein.tasktracker.ui.screens
import androidx.compose.material3.Button
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kafein.tasktracker.data.local.TaskEntity
import com.kafein.tasktracker.viewmodel.TaskViewModel
import java.text.DateFormat
import java.util.Date

@Composable
fun TaskListScreen(
    viewModel: TaskViewModel,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tasks by viewModel.tasks.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Görevler"
        )
        Button(
            onClick = onAddClick,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Görev Ekle")
        }

        if (errorMessage != null) {
            Text(
                text = errorMessage ?: ""
            )
        }

        if (tasks.isEmpty()) {
            Text(
                text = "Henüz görev yok",
                modifier = Modifier.padding(top = 16.dp)
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = tasks,
                    key = { task -> task.id }
                ) { task ->

                    TaskItem(
                        task = task,
                        onCheckedChange = {
                            viewModel.toggleTaskCompletion(task)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun TaskItem(
    task: TaskEntity,
    onCheckedChange: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {

        Checkbox(
            checked = task.isCompleted,
            onCheckedChange = {
                onCheckedChange()
            }
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = task.title
            )

            Text(
                text = formatDate(task.createdAt)
            )
        }
    }

    HorizontalDivider()
}

private fun formatDate(time: Long): String {
    return DateFormat
        .getDateTimeInstance(
            DateFormat.SHORT,
            DateFormat.SHORT
        )
        .format(Date(time))
}