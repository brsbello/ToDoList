package com.example.todolist.datasource.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.todolist.model.Task

@Database(entities = [Task::class], version = 1)
abstract class TaskDataBase : RoomDatabase(){

    abstract fun taskDao(): TaskRoom

    companion object {
        fun intance(context : Context) : TaskDataBase{
           return Room.databaseBuilder(
                context,
                TaskDataBase::class.java,
                "todolist.db"
            ).allowMainThreadQueries().build()
        }
    }
}