package com.example.todo.data

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface TodoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTodo(todo: TodoEntity)

    @Query("SELECT * FROM todo_table ORDER BY completed ASC,important DESC,id DESC")
    fun readAllData():LiveData<List<TodoEntity>> //change back to List<TodoEntity> if doesn't work

    @Update
    suspend fun updateEntity(todo: TodoEntity)

    @Delete
    suspend fun deleteEntity(todo: TodoEntity)
}