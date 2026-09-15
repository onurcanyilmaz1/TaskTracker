package com.kafein.tasktracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kafein.tasktracker.data.local.TaskDatabase
import com.kafein.tasktracker.data.remote.RetrofitClient
import com.kafein.tasktracker.repository.TaskRepository
import com.kafein.tasktracker.ui.screens.TaskListScreen
import com.kafein.tasktracker.ui.theme.TaskTrackerTheme
import com.kafein.tasktracker.viewmodel.TaskViewModel
import com.kafein.tasktracker.viewmodel.TaskViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val database = TaskDatabase.getDatabase(applicationContext)

        val repository = TaskRepository(
            taskDao = database.taskDao(),
            todoApi = RetrofitClient.todoApi
        )

        val viewModelFactory = TaskViewModelFactory(repository)

        setContent {
            TaskTrackerTheme {

                val taskViewModel: TaskViewModel = viewModel(
                    factory = viewModelFactory
                )

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    TaskListScreen(
                        viewModel = taskViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}