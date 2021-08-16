package com.example.todo.data

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.todo.data.relations.GroupWithTodos

class TodoRepository(private val todoDao: TodoDao) {
    lateinit var readAllData:LiveData<List<TodoEntity>>
    lateinit var readImportant: LiveData<List<TodoEntity>>
    lateinit var readCompleted: LiveData<List<TodoEntity>>
    lateinit var readGroup: LiveData<List<GroupEntity>>
    //lateinit var readGroupWithTodo: LiveData<List<GroupWithTodos>>

    // Add DATA
    suspend fun addGroup(group:GroupEntity){
        todoDao.addGroup(group)
    }
    suspend fun addTodo(todo: TodoEntity){
        todoDao.addTodo(todo)
    }

    //Read DATA
    fun readData(name: String):LiveData<List<TodoEntity>>{
         //readAllData = todoDao.readAllData()
        return todoDao.readAllData(name)
    }

    fun readCompletedData(name:String):LiveData<List<TodoEntity>>{
        return todoDao.readCompleted(name)
    }

    fun readAllGroup(){
        readGroup = todoDao.readAllGroups()
    }

    fun readGroupWithTodos(name: String): LiveData<List<GroupWithTodos>> {
        return todoDao.getGroupWithTodos(name)
    }

    // Update & Delete DATA
    suspend fun updateTodo(todo: TodoEntity){
        todoDao.updateEntity(todo)
    }

    suspend fun deleteTodo(todo: TodoEntity){
        todoDao.deleteEntity(todo)
    }

}