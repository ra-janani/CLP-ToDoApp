package com.example.todoapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.todoapp.data.model.SubTask
import com.example.todoapp.data.model.Task
import com.example.todoapp.di.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class TaskViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: TaskRepository

    private lateinit var viewModel: TaskViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = TaskViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `addTask should call repository insertTask`() = runTest {
        val title = "Test Task"
        val description = "Test Description"

        viewModel.addTask(title, description)
        testScheduler.advanceUntilIdle()

        verify(repository).insertTask(any())
    }

    @Test
    fun `updateTask should call repository updateTask`() = runTest {
        val task = Task(id = 1, title = "Test Task", description = "Test Description")

        viewModel.updateTask(task)
        testScheduler.advanceUntilIdle()

        verify(repository).updateTask(task)
    }

    @Test
    fun `deleteTask should call repository deleteTask`() = runTest {
        val task = Task(id = 1, title = "Test Task", description = "Test Description")

        viewModel.deleteTask(task)
        testScheduler.advanceUntilIdle()

        verify(repository).deleteTask(task)
    }

    @Test
    fun `getTaskById should return Flow from repository`() = runTest {
        val taskId = 1
        val task = Task(id = taskId, title = "Test Task", description = "Test Description")
        `when`(repository.getTaskById(taskId)).thenReturn(flowOf(task))

        val result = viewModel.getTaskById(taskId)

        assert(result == repository.getTaskById(taskId))
    }

    @Test
    fun `addSubTask should call repository insertSubTask`() = runTest {
        val taskId = 1
        val title = "Test SubTask"

        viewModel.addSubTask(taskId, title)
        testScheduler.advanceUntilIdle()

        verify(repository).insertSubTask(any())
    }

    @Test
    fun `updateSubTask should call repository updateSubTask`() = runTest {
        val subTask = SubTask(id = 1, taskId = 1, title = "Test SubTask")

        viewModel.updateSubTask(subTask)
        testScheduler.advanceUntilIdle()

        verify(repository).updateSubTask(subTask)
    }

    @Test
    fun `deleteSubTask should call repository deleteSubTask`() = runTest {
        val subTask = SubTask(id = 1, taskId = 1, title = "Test SubTask")

        viewModel.deleteSubTask(subTask)
        testScheduler.advanceUntilIdle()

        verify(repository).deleteSubTask(subTask)
    }

    @Test
    fun `getSubTasksForTask should return Flow from repository`() = runTest {
        val taskId = 1
        val subTasks = listOf(SubTask(id = 1, taskId = taskId, title = "Test SubTask"))
        `when`(repository.getSubTasksForTask(taskId)).thenReturn(flowOf(subTasks))

        val result = viewModel.getSubTasksForTask(taskId)

        assert(result == repository.getSubTasksForTask(taskId))
    }
}