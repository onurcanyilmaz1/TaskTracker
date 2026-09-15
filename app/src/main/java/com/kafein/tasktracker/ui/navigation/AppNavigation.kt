package com.kafein.tasktracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kafein.tasktracker.ui.screens.AddEditTaskScreen
import com.kafein.tasktracker.ui.screens.TaskListScreen
import com.kafein.tasktracker.viewmodel.TaskViewModel

@Composable
fun AppNavigation(
    viewModel: TaskViewModel,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

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
                }
            )
        }

        composable("add_task") {
            AddEditTaskScreen(
                onSave = { title ->
                    viewModel.addTask(title)
                    navController.popBackStack()
                }
            )
        }
    }
}