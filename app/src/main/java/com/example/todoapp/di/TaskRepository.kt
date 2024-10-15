package com.example.todoapp.di

import androidx.lifecycle.LiveData
import com.example.todoapp.data.dao.TaskDao
import com.example.todoapp.data.model.Task
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskRepository @Inject constructor(private val taskDao:TaskDao){
    // Inside your repository
    val allTasks: Flow<List<Task>> = taskDao.getAllTasks()
    suspend fun insert(task: Task) {
        taskDao.insertTask(task)
    }

    suspend fun update(task: Task) {
        taskDao.updateTask(task)
    }

    suspend fun delete(task: Task) {
        taskDao.deleteTask(task)
    }
}