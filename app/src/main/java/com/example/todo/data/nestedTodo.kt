package com.example.todo.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
data class nestedTodo(
    val id: Int,
    val title: String,
    val completed:Boolean
):Parcelable
