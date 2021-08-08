package com.example.todo.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class TodoConverter {

    val gson: Gson = Gson()
    @TypeConverter
    fun TodoToString(entity:List<nestedTodo?>?):String?{
        return gson.toJson(entity)
    }

    @TypeConverter
    fun StringToTodo(stringEntity:String?): List<nestedTodo>? {
        val listType: Type = object : TypeToken<List<nestedTodo>?>() {}.type
        return gson.fromJson(stringEntity, listType)
    }
}