package com.kafein.tasktracker.repository

import com.kafein.tasktracker.data.local.TaskDao
import com.kafein.tasktracker.data.local.TaskEntity
import kotlinx.coroutines.flow.Flow
import com.kafein.tasktracker.data.remote.TodoApi
import com.kafein.tasktracker.data.remote.toTaskEntity

class TaskRepository(
    private val taskDao: TaskDao,
    private val todoApi: TodoApi
) {

    val allTasks: Flow<List<TaskEntity>> = taskDao.getAllTasks()

    suspend fun insertTask(task: TaskEntity) {
        taskDao.insertTask(task)
    }

    suspend fun insertTasks(tasks: List<TaskEntity>) {
        taskDao.insertTasks(tasks)
    }

    suspend fun updateTask(task: TaskEntity) {
        taskDao.updateTask(task)
    }

    suspend fun deleteTask(task: TaskEntity) {
        taskDao.deleteTask(task)
    }

    suspend fun getTaskCount(): Int {
        return taskDao.getTaskCount()
    }
    suspend fun loadInitialTasks() {
        if (taskDao.getTaskCount() > 0) {
            return
        }

        val remoteTasks = todoApi.getTodos()

        val tasks = remoteTasks
            .take(10)
            .map { todo ->
                todo.toTaskEntity()
            }

        taskDao.insertTasks(tasks)
    }
}