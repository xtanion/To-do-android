package com.example.todo.data

import androidx.lifecycle.LiveData

class TodoRepository(private val todoDao: TodoDao) {
    lateinit var readAllData:LiveData<List<TodoEntity>>
    lateinit var readImportant: LiveData<List<TodoEntity>>
    lateinit var readCompleted: LiveData<List<TodoEntity>>

    suspend fun addTodo(todo: TodoEntity){
        todoDao.addTodo(todo)
    }

    fun readData(){
         readAllData = todoDao.readAllData()
    }

    fun readCompletedData(){
        readCompleted = todoDao.readCompleted()
    }

    suspend fun updateTodo(todo: TodoEntity){
        todoDao.updateEntity(todo)
    }

    suspend fun deleteTodo(todo: TodoEntity){
        todoDao.deleteEntity(todo)
    }

    fun readImportantData(){
        readImportant = todoDao.readImportant()
    }
}