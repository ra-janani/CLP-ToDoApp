package com.example.todoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.model.SubTask
import com.example.todoapp.data.model.Task
import com.example.todoapp.di.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(private val repository: TaskRepository) : ViewModel() {
    val allTasks = repository.allTasks

    // Function to add a task using title and description
    fun addTask(title: String, description: String) {
        val newTask = Task(title = title, description = description)
        viewModelScope.launch {
            repository.insertTask(newTask) // Insert new task into the repository (Room database)
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    // Fetch a specific task by ID
    fun getTaskById(taskId: Int): Flow<Task?> {
        return repository.getTaskById(taskId)
    }

    fun addSubTask(taskId: Int, title: String) {
        val newSubTask = SubTask(taskId = taskId, title = title, isCompleted = false)
        viewModelScope.launch {
            repository.insertSubTask(newSubTask)
        }
    }

    fun updateSubTask(subTask: SubTask) {
        viewModelScope.launch {
            repository.updateSubTask(subTask)
        }
    }

    fun deleteSubTask(subTask: SubTask) {
        viewModelScope.launch {
            repository.deleteSubTask(subTask)
        }
    }

    // Function to fetch all sub-tasks for a specific task
    fun getSubTasksForTask(taskId: Int) = repository.getSubTasksForTask(taskId)
}