package com.example.todo.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.ByteArrayOutputStream
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

    @TypeConverter
    fun BitmapToByteArray(bitmap: Bitmap?):ByteArray?{
        val outputStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.PNG,250,outputStream)
        val storedAs = outputStream.toByteArray()
        Log.d("Compress",storedAs.toString())
        return storedAs
    }

    @TypeConverter
    fun ByteArrayToBitmap(bArray:ByteArray?):Bitmap?{
        Log.d("Compress",bArray.toString())
        return BitmapFactory.decodeByteArray(bArray, 0, bArray?.size!!)
    }
}