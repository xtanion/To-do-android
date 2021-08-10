package com.example.todo

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.todo.data.*
import com.example.todo.data.relations.GroupWithTodos
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import java.lang.reflect.Type

class TodoViewModel(application: Application):AndroidViewModel(application) {
    private val repository: TodoRepository
    private var todoDao: TodoDao
    private lateinit var mAuth: FirebaseAuth
    private lateinit var fireData:List<TodoEntity>
    //private var groupName:String? = null
    init {
        todoDao = TodoDatabase.getInstance(application).todoDao()
        repository = TodoRepository(todoDao)
        initializer()
    }
    private fun initializer(){
        repository.readAllGroup()
        repository.readData()
        repository.readCompletedData()
        repository.readGroupWithTodos()
    }

    fun listAllGroup():LiveData<List<GroupEntity>>{
        repository.readAllGroup()
        Log.d("GROUPDATA",repository.readGroup.value.toString())
        return repository.readGroup
    }
    //TODO
    fun listGroupWithTodos():LiveData<List<GroupWithTodos>>{
        return repository.readGroupWithTodo
    }

    fun listData():LiveData<List<TodoEntity>>{
        return repository.readAllData
    }

    fun listCompleted():LiveData<List<TodoEntity>>{
        return repository.readCompleted
    }

    fun addToDo(todo:TodoEntity){
        viewModelScope.launch(){
            repository.addTodo(todo)
        }
    }
    fun addGroup(groupEntity: GroupEntity){
        viewModelScope.launch {
            repository.addGroup(groupEntity)
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

    fun mAuthMethod(): FirebaseAuth {
        mAuth = FirebaseAuth.getInstance()
        return mAuth
    }

    fun fireBaseAdd(entity:TodoEntity){
        val rootNode: FirebaseDatabase = FirebaseDatabase.getInstance()
        val uid = mAuthMethod().uid
        val reference = rootNode.getReference("posts/${uid}/")
        reference.child("${entity.id}").setValue(entity)
    }

    fun getFirebaseData(){
        viewModelScope.launch {
            val rootNode: FirebaseDatabase = FirebaseDatabase.getInstance()
            val uid = mAuthMethod().uid
            val gson: Gson = Gson()
            val reference = rootNode.getReference("posts/${uid}/")

            reference.get().addOnSuccessListener {
                if (it.exists()) {
                    val list = mutableListOf<TodoEntity>()
                    for (ds: DataSnapshot in it.children) {
                        val id = ds.child("id").getValue(Int::class.java)
                        val title = ds.child("title").getValue(String::class.java)
                        val description = ds.child("description").getValue(String::class.java)
                        val important: Boolean? =
                            ds.child("important").getValue(Boolean::class.java)
                        val completed: Boolean? =
                            ds.child("completed").getValue(Boolean::class.java)
                        val groupName: String? = ds.child("groupName").getValue(String::class.java)
                        val dateTime: String? = ds.child("dateTime").getValue(String::class.java)
                        val stringExtra: String? =
                            ds.child("nestedTodo").getValue(String::class.java)

                        val listType: Type = object : TypeToken<List<nestedTodo>?>() {}.type
                        val nestedTodo: List<nestedTodo>? = gson.fromJson(stringExtra, listType)
                        val entity = TodoEntity(id!!, title!!, description, important!!, completed!!, groupName!!, dateTime!!, nestedTodo)
                        list.add(entity)
                    }

                    fireData = list
                }
            }
        }
    }

    fun fireDataReturn():List<TodoEntity>{
        return fireData
    }

}