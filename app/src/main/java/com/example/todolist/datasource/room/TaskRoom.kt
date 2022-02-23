package com.example.todolist.datasource.room

import androidx.room.*
import com.example.todolist.model.Task

@Dao
interface TaskRoom {

    @Query("SELECT * FROM Task")
    fun searchAll(): List<Task>

    @Insert
    fun save(task: Task)

    @Delete
    fun delete(task: Task)

    @Update
    fun update(task: Task)

    //@Query("SELECT * FROM Task WHERE id = :id")
    //fun searchForId(id: Long): Task?

    //@Query("SELECT * FROM Task ORDER BY date ASC")
    //fun searchAllOrderByDescAsc(): List<Produto>
}