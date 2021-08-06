package com.example.todo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "list_table")
data class ListEntity(
    @PrimaryKey(autoGenerate = true)
    val groupId: Int,
    val group_name: String,
    val accent_colour: String
)
