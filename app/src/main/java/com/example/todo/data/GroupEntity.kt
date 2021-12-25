package com.example.todo.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class GroupEntity(
    @PrimaryKey(autoGenerate = true)
    val groupId:Int,
    val groupName: String,
    val accent_colour: String
):Parcelable
