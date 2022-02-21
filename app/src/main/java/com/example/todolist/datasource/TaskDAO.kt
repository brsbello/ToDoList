package com.example.todolist.datasource

import com.example.todolist.model.Task

class TaskDAO {

    companion object {
        private val list = mutableListOf<Task>()

        fun getList() = list.toList()

        fun insertTask(task: Task) {
            if (task.id == 0) {
                list.add(task.copy(id = list.size + 1))
            } else {
                list.remove(task)
                list.add(task)
            }
        }

        fun deleteTask(task: Task) {
            list.remove(task)
        }

        fun findById(taskId: Int) = list.find { it.id == taskId }
    }








}
