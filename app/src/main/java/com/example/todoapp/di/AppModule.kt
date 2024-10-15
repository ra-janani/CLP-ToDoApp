package com.example.todoapp.di

import android.app.Application
import androidx.room.Room
import com.example.todoapp.data.dao.TaskDao
import com.example.todoapp.data.dataBase.TaskDatabase
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
    fun provideDao(database: TaskDatabase): TaskDao {
        return database.taskDao()
    }
}