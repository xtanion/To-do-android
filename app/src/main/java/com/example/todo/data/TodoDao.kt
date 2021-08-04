package com.example.todo.data

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface TodoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTodo(todo: TodoEntity)

    @Query("SELECT * FROM todo_table WHERE completed = 0 ORDER BY important DESC,id DESC")
    fun readAllData():LiveData<List<TodoEntity>> //change back to List<TodoEntity> if doesn't work

    @Query("SELECT * FROM todo_table WHERE completed = 1 ORDER BY important DESC,id DESC")
    fun readCompleted():LiveData<List<TodoEntity>>

    @Update
    suspend fun updateEntity(todo: TodoEntity)

    @Delete
    suspend fun deleteEntity(todo: TodoEntity)

    @Query("SELECT * FROM todo_table WHERE important = 1 ORDER BY completed ASC,id DESC")
    fun readImportant():LiveData<List<TodoEntity>>

    @Query("SELECT * FROM todo_table WHERE title LIKE :searchQuery")
    fun searchDatabase(searchQuery: String):LiveData<List<TodoEntity>>
}