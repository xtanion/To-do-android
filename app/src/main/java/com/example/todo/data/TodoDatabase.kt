package com.example.todo.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import java.security.AccessControlContext

@Database(entities = [TodoEntity::class],version = 1)
abstract class TodoDatabase: RoomDatabase() {

    abstract fun todoDao(): TodoDao

    companion object{

        @Volatile
        private var INSTANCE : TodoDatabase? = null

        fun getInstance(context: Context): TodoDatabase {
            synchronized(this){
                var instance = INSTANCE

                //check if db is null, then create the database
                if(instance==null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TodoDatabase::class.java,
                        "todo_database"
                    ).fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}