package com.kafein.tasktracker.viewmodel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafein.tasktracker.data.local.TaskEntity
import com.kafein.tasktracker.repository.TaskRepositoryContract
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class TaskViewModel(
    private val repository: TaskRepositoryContract
) : ViewModel() {

    val tasks: StateFlow<List<TaskEntity>> =
        repository.allTasks.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()
    init {
        loadInitialTasks()
    }
    private fun loadInitialTasks() {
        viewModelScope.launch {
            try {
                repository.loadInitialTasks()
            } catch (e: Exception) {
                _errorMessage.value =
                    "Görevler internetten alınamadı. İnternet bağlantınızı kontrol edin."
            }
        }
    }
    fun clearError() {
        _errorMessage.value = null
    }

    fun addTask(title: String) {
        val trimmedTitle = title.trim()

        if (trimmedTitle.isBlank()) {
            return
        }

        viewModelScope.launch {
            repository.insertTask(
                TaskEntity(
                    title = trimmedTitle
                )
            )
        }
    }

    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    fun toggleTaskCompletion(task: TaskEntity) {
        viewModelScope.launch {
            repository.updateTask(
                task.copy(
                    isCompleted = !task.isCompleted
                )
            )
        }
    }

    fun updateTask(task: TaskEntity, newTitle: String) {
        val trimmedTitle = newTitle.trim()

        if (trimmedTitle.isBlank()) {
            return
        }

        viewModelScope.launch {
            repository.updateTask(
                task.copy(
                    title = trimmedTitle
                )
            )
        }
    }
}