package com.example.todo.data

import androidx.room.*


@Dao
interface TodoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTodo(todo: TodoEntity)

    @Query("SELECT * FROM todo_table ORDER BY id DESC")
    suspend fun readAllData():List<TodoEntity>

    @Update
    suspend fun updateEntity(todo: TodoEntity)
}