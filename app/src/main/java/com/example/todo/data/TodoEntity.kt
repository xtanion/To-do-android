package com.example.todo.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "todo_table")
data class TodoEntity(
    // we'll require title, importance(star), for later (completion_date, alarm_time)
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val important: Boolean
)
