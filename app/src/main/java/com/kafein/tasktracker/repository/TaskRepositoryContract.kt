package com.kafein.tasktracker.repository

import com.kafein.tasktracker.data.local.TaskEntity
import kotlinx.coroutines.flow.Flow

interface TaskRepositoryContract {

    val allTasks: Flow<List<TaskEntity>>

    suspend fun insertTask(task: TaskEntity)

    suspend fun updateTask(task: TaskEntity)

    suspend fun deleteTask(task: TaskEntity)

    suspend fun loadInitialTasks()
}