package com.example.todo.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.versionedparcelable.ParcelField

@Entity(tableName = "todo_table")
data class TodoEntity(
    // we'll require title, importance(star), for later (completion_date, alarm_time)
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val important: Boolean
)
