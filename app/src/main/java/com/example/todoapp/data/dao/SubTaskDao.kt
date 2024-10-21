package com.example.todoapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.todoapp.data.model.SubTask
import kotlinx.coroutines.flow.Flow

@Dao
interface SubTaskDao {

    @Query("Select * from subtask_table where task_id=:taskId")
    fun getSubTasksForTask(taskId: Int): Flow<List<SubTask>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubTask(subTask: SubTask)

    @Delete
    suspend fun deleteSubTask(subTask: SubTask)

    @Update
    suspend fun updateSubTask(subTask: SubTask)

}