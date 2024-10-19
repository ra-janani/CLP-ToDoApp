package com.example.todoapp.data.dataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.todoapp.data.dao.SubTaskDao
import com.example.todoapp.data.dao.TaskDao
import com.example.todoapp.data.model.SubTask
import com.example.todoapp.data.model.Task

@Database(entities = [Task::class, SubTask::class], version = 2, exportSchema = false)
abstract class TaskDatabase:RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun subtaskDao(): SubTaskDao

    companion object { // which will return single instance for the database with name task_database
        @Volatile // ensures thread safety-change will be visible to all threads-prevent creation of multiple instances from several threads
        private var INSTANCE: TaskDatabase? = null

        fun getDatabase(context: Context): TaskDatabase {
            return INSTANCE ?: synchronized(this) {// only one thread can access this block of code at a time
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskDatabase::class.java,
                    "task_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}