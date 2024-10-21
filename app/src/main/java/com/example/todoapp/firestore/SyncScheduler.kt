package com.example.todoapp.firestore

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

// ensures that the sync process occurs regularly in the background
fun scheduleSyncWork(context: Context) {
    val constraints = Constraints.Builder()
        .setRequiredNetworkType(androidx.work.NetworkType.CONNECTED)
        .build()

    val syncWorkRequest = PeriodicWorkRequestBuilder<TaskSyncWorker>(15, TimeUnit.MINUTES)
        .setConstraints(constraints)
        .build()

    WorkManager.getInstance(context)
        .enqueueUniquePeriodicWork(
            "TaskSyncWork",
            ExistingPeriodicWorkPolicy.KEEP,
            syncWorkRequest
        )
}
