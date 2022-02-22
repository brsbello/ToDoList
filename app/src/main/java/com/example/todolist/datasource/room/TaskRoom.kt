package com.example.todolist.datasource.room

import androidx.room.*
import com.example.todolist.model.Task

@Dao
interface TaskRoom {

    @Query("SELECT * FROM Task")
    fun searchAll() : List<Task>

    @Insert
    fun save(task : Task)

    @Delete
    fun delete(task : Task)

   @Update
   fun update(task : Task)
}