package com.example.todolist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.todolist.databinding.ActivityMainBinding
import com.example.todolist.datasource.TaskDataSource
import com.example.todolist.ui.AddTaskActivity
import com.example.todolist.ui.adapter.TaskListAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter: TaskListAdapter by lazy { TaskListAdapter() }
    private val launchSomeActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                //val data: Intent? = result.data
                updateList()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupAdapter()
        insertListeners()
    }

    private fun insertListeners() {
        configFAB()
        editTask()
        deleteTask()
    }

    private fun deleteTask() {
        adapter.listenerDelete = {
            TaskDataSource.deleteTask(it)
            updateList()
        }
    }

    private fun editTask() {
        adapter.listenerEdit = {
            val intent = Intent(this, AddTaskActivity::class.java)
            intent.putExtra(AddTaskActivity.TASK_ID, it.id)
            launchSomeActivity.launch(intent)
        }
    }

    private fun configFAB() {
        binding.FABAdd.setOnClickListener {
            openActivityForResult()
        }
    }

    private fun openActivityForResult() {
        val intent = Intent(this, AddTaskActivity::class.java)
        launchSomeActivity.launch(intent)
    }

    private fun setupAdapter() {
        binding.RVTasks.adapter = adapter
    }

    private fun updateList() {
        adapter.submitList(TaskDataSource.getList())
    }
}
