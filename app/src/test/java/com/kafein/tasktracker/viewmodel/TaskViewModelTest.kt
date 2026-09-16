package com.kafein.tasktracker.viewmodel

import com.kafein.tasktracker.data.local.TaskEntity
import com.kafein.tasktracker.repository.TaskRepositoryContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TaskViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var repository: FakeTaskRepository
    private lateinit var viewModel: TaskViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        repository = FakeTaskRepository()
        viewModel = TaskViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `addTask trims title before inserting`() {
        viewModel.addTask("   Spora git   ")

        assertEquals(
            "Spora git",
            repository.insertedTask?.title
        )
    }

    @Test
    fun `addTask does not insert blank title`() {
        viewModel.addTask("     ")

        assertNull(repository.insertedTask)
    }

    @Test
    fun `toggleTaskCompletion changes false to true`() {
        val task = TaskEntity(
            id = 1,
            title = "Rapor hazırla",
            isCompleted = false
        )

        viewModel.toggleTaskCompletion(task)

        assertEquals(
            true,
            repository.updatedTask?.isCompleted
        )

        assertFalse(task.isCompleted)
    }
}

private class FakeTaskRepository : TaskRepositoryContract {

    private val tasks =
        MutableStateFlow<List<TaskEntity>>(emptyList())

    override val allTasks: Flow<List<TaskEntity>> = tasks

    var insertedTask: TaskEntity? = null
    var updatedTask: TaskEntity? = null
    var deletedTask: TaskEntity? = null

    override suspend fun insertTask(task: TaskEntity) {
        insertedTask = task
    }

    override suspend fun updateTask(task: TaskEntity) {
        updatedTask = task
    }

    override suspend fun deleteTask(task: TaskEntity) {
        deletedTask = task
    }

    override suspend fun loadInitialTasks() {
        // Test sırasında gerçek API çağrısı yapılmaz.
    }
}