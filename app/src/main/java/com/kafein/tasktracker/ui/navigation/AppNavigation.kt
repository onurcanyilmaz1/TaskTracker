package com.kafein.tasktracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kafein.tasktracker.ui.screens.AddEditTaskScreen
import com.kafein.tasktracker.ui.screens.TaskListScreen
import com.kafein.tasktracker.viewmodel.TaskViewModel



@Composable
fun AppNavigation(
    viewModel: TaskViewModel,
    darkTheme: Boolean,
    onThemeToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val tasks by viewModel.tasks.collectAsState()

    NavHost(
        navController = navController,
        startDestination = "task_list",
        modifier = modifier
    ) {

        composable("task_list") {
            TaskListScreen(
                viewModel = viewModel,
                onAddClick = {
                    navController.navigate("add_task")
                },
                onEditClick = { task ->
                    navController.navigate("edit_task/${task.id}")
                },
                onDeleteClick = { task ->
                    viewModel.deleteTask(task)
                },
                darkTheme = darkTheme,
                onThemeToggle = onThemeToggle
            )
        }

        composable("add_task") {
            AddEditTaskScreen(
                initialTitle = "",
                screenTitle = "Yeni Görev",
                onSave = { title ->
                    viewModel.addTask(title)
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = "edit_task/{taskId}",
            arguments = listOf(
                navArgument("taskId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            val taskId = backStackEntry.arguments?.getInt("taskId")

            val task = tasks.firstOrNull { currentTask ->
                currentTask.id == taskId
            }

            if (task != null) {
                AddEditTaskScreen(
                    initialTitle = task.title,
                    screenTitle = "Görevi Düzenle",
                    onSave = { newTitle ->
                        viewModel.updateTask(
                            task = task,
                            newTitle = newTitle
                        )

                        navController.popBackStack()
                    }
                )
            }
        }
    }
}