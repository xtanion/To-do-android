package com.example.todo.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import java.security.AccessControlContext

@Database(
    entities = [TodoEntity::class,GroupEntity::class],version = 1)
@TypeConverters(TodoConverter::class)
abstract class TodoDatabase: RoomDatabase() {

    abstract fun todoDao(): TodoDao

    companion object {

        @Volatile
        private var INSTANCE: TodoDatabase? = null

        fun getInstance(context: Context): TodoDatabase {
            synchronized(this) {
                val tempInstance = INSTANCE

                //check if db is null, then create the database
                if (tempInstance == null) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        TodoDatabase::class.java,
                        "todo_database"
                    ).fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                    return instance
                }
                else {
                    return tempInstance
                }
            }
        }
    }
}