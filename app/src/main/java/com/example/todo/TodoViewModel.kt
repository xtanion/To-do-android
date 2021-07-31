package com.example.todo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.MutableLiveData
import com.example.todo.data.TodoDao
import com.example.todo.data.TodoDatabase
import com.example.todo.data.TodoEntity
import com.example.todo.data.TodoRepository
import kotlinx.coroutines.launch

class TodoViewModel(application: Application):AndroidViewModel(application) {
    private val repository: TodoRepository
    private lateinit var todoDao: TodoDao

    init {
        todoDao = TodoDatabase.getInstance(application).todoDao()
        repository = TodoRepository(todoDao)
        repository.readData()
        //readAllData = repository.readAllData
    }

    fun listData():LiveData<List<TodoEntity>>{
        return repository.readAllData
    }

    fun addToDo(todo:TodoEntity){
        viewModelScope.launch(){
            repository.addTodo(todo)
        }

    }

    fun updateTodo(todo:TodoEntity){
        viewModelScope.launch(){
            repository.updateTodo(todo)
        }
    }

    fun removeTodo(todo: TodoEntity){
        viewModelScope.launch {
            repository.deleteTodo(todo)
        }
    }

//    fun sizeof():String{
//        readToDo()
//        return readAllData.value?.size.toString()
//    }

}