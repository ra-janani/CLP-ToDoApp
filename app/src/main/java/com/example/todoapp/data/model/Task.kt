package com.example.todoapp.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="task_table")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    @ColumnInfo(name = "task_title") // table's column name is task_title
    val title:String,
    @ColumnInfo(name = "task_description") // table's column name is task_description
    val description:String,
    val dueDate:Long,
    val isCompleted:Boolean=false
)
