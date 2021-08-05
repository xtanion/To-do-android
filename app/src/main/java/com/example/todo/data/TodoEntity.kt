package com.example.todo.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.time.LocalDateTime


@Parcelize
@Entity(tableName = "todo_table")
data class TodoEntity(
    // we'll require title, importance(star), for later (completion_date, alarm_time)
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val description: String?,
    val important: Boolean,
    val completed: Boolean,
    val groupName: String?,
    val dateTime: String
):Parcelable
{
    fun equals(other: TodoEntity?): Boolean {
        if(id != other?.id){
            return false
        }
        if (title != other.title){
            return false
        }
        if (description!= other.description){
            return false
        }
        if (important != other.important){
            return false
        }
        if (completed != other.completed){
            return false
        }
        if (groupName != other.groupName){
            return false
        }
        return true
    }
}
