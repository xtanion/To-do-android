package com.example.todo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.MutableLiveData
import com.example.todo.data.TodoDao
import com.example.todo.data.TodoDatabase
import com.example.todo.data.TodoEntity
import kotlinx.coroutines.launch

class TodoViewModel(application: Application):AndroidViewModel(application) {
    private var readAllData = MutableLiveData<List<TodoEntity>>()
    private lateinit var todoDao: TodoDao

    init {
        todoDao = TodoDatabase.getInstance(application).todoDao()
    }

    fun readToDo(){
        viewModelScope.launch() {
            val data:List<TodoEntity> = todoDao.readAllData()
            readAllData.value = data
        }
    }

    fun addToDo(todo:TodoEntity){
        viewModelScope.launch(){
            todoDao.addTodo(todo)
        }
    }

    fun updateUser(todo:TodoEntity){
        viewModelScope.launch(){
            todoDao.updateEntity(todo)
        }
    }

}