package com.example.todo.data

import android.graphics.Bitmap
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
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
    val groupName: String,
    val dateTime: String,
    val nestedTodo: List<nestedTodo?>?,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    val bitmap: Bitmap?,
    val requestCode:Int?,
    val alarmTime:Int?,
    val dueDate:String?
):Parcelable
{
    fun equals(other: TodoEntity?): Boolean {
        if(id != other?.id){
            return false
        }
        if (title != other.title){
            return false
        }
        //REMOVED FROM HERE
        //if (description!= other.description){
        //    return false
        //}
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
