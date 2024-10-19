package com.example.todoapp.di

import android.app.Application
import androidx.room.Room
import com.example.todoapp.data.dao.SubTaskDao
import com.example.todoapp.data.dao.TaskDao
import com.example.todoapp.data.dataBase.TaskDatabase
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(app: Application): TaskDatabase {
        return Room.databaseBuilder(app, TaskDatabase::class.java, "task_database")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideTaskDao(database: TaskDatabase): TaskDao {
        return database.taskDao()
    }

    @Provides
    fun provideSubTaskDao(database: TaskDatabase): SubTaskDao {
        return database.subtaskDao()
    }

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance() // Provide Firestore instance
    }


    @Provides
    @Singleton
    fun provideRepository(
        taskDao: TaskDao,
        subtaskDao: SubTaskDao,
        firestore: FirebaseFirestore // Pass Firestore instance here
    ): TaskRepository {
        return TaskRepository(taskDao, subtaskDao, firestore) // Provide the implementation here
    }
}