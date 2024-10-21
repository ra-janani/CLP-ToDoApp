package com.example.todoapp.firestore

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.firestore.FirebaseFirestore
import com.example.todoapp.data.dao.TaskDao
import com.example.todoapp.data.dao.SubTaskDao
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

//handle the actual synchronization,
// sending local changes to Firestore and pulling down any updates from Firestore.
class TaskSyncWorker @Inject constructor(
    private val taskDao: TaskDao,
    private val subTaskDao: SubTaskDao,
    private val firestore: FirebaseFirestore,
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = coroutineScope {
        try {
            // Fetch unsynced tasks from Room
            val tasks = taskDao.getAllTasks().firstOrNull() ?: emptyList()
            tasks.forEach { task ->
                firestore.collection("tasks").document(task.id.toString()).set(task).await()
                val subTasks = subTaskDao.getSubTasksForTask(task.id).firstOrNull() ?: emptyList()
                subTasks.forEach { subTask ->
                    firestore.collection("tasks")
                        .document(task.id.toString())
                        .collection("subtasks")
                        .document(subTask.id.toString())
                        .set(subTask)
                        .await()
                }
            }
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry() // Retry if failed
        }
    }
}
