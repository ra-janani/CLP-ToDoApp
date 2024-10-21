package com.example.todoapp.di

import com.example.todoapp.data.dao.SubTaskDao
import com.example.todoapp.data.dao.TaskDao
import com.example.todoapp.data.model.SubTask
import com.example.todoapp.data.model.Task
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val taskDao: TaskDao,
    private val subTaskDao: SubTaskDao,
    private val firestore: FirebaseFirestore
) {
    //Task dB operations
    val allTasks: Flow<List<Task>> = taskDao.getAllTasks()
    fun getTaskById(taskId: Int): Flow<Task?> {
        return taskDao.getTaskById(taskId)
    }
    suspend fun insertTask(task: Task) {
        taskDao.insertTask(task)
        // Sync with Firebase Firestore
        firestore.collection("tasks")
            .document(task.id.toString())
            .set(task)
            .await() // Make sure it's completed
    }

    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task)
        firestore.collection("tasks")
            .document(task.id.toString())
            .set(task)
            .await()
    }

    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
        firestore.collection("tasks")
            .document(task.id.toString())
            .delete()
            .await()
    }

    //SubTasks dB operations
    suspend fun insertSubTask(subTask: SubTask){
        subTaskDao.insertSubTask(subTask)
        firestore.collection("tasks")
            .document(subTask.taskId.toString())
            .collection("subtasks")
            .document(subTask.id.toString())
            .set(subTask)
            .await()
    }

    suspend fun deleteSubTask(subTask: SubTask){
        subTaskDao.deleteSubTask(subTask)
        firestore.collection("tasks")
            .document(subTask.taskId.toString())
            .collection("subtasks")
            .document(subTask.id.toString())
            .delete()
            .await()

    }

    suspend fun updateSubTask(subTask: SubTask){
        subTaskDao.updateSubTask(subTask)
        firestore.collection("tasks")
            .document(subTask.taskId.toString())
            .collection("subtasks")
            .document(subTask.id.toString())
            .set(subTask)
            .await()
    }
    // This function does not need to be suspend because it returns Flow, which is asynchronous by nature
    fun getSubTasksForTask(taskId: Int): Flow<List<SubTask>> {
        return subTaskDao.getSubTasksForTask(taskId)
    }
}