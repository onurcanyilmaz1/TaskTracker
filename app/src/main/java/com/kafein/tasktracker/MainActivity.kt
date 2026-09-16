package com.kafein.tasktracker
import com.kafein.tasktracker.ui.navigation.AppNavigation
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
import com.kafein.tasktracker.ui.theme.TaskTrackerTheme
import com.kafein.tasktracker.viewmodel.TaskViewModel
import com.kafein.tasktracker.viewmodel.TaskViewModelFactory
import com.kafein.tasktracker.data.local.InitialDataPreferences
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val database = TaskDatabase.getDatabase(applicationContext)

        val initialDataPreferences =
            InitialDataPreferences(applicationContext)

        val repository = TaskRepository(
            taskDao = database.taskDao(),
            todoApi = RetrofitClient.todoApi,
            initialDataPreferences = initialDataPreferences
        )

        val viewModelFactory = TaskViewModelFactory(repository)

        setContent {

            val systemDarkTheme = isSystemInDarkTheme()

            var darkTheme by rememberSaveable {
                mutableStateOf(systemDarkTheme)
            }

            TaskTrackerTheme(
                darkTheme = darkTheme
            ) {

                val taskViewModel: TaskViewModel = viewModel(
                    factory = viewModelFactory
                )

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    AppNavigation(
                        viewModel = taskViewModel,
                        darkTheme = darkTheme,
                        onThemeToggle = {
                            darkTheme = !darkTheme
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}