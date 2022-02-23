package com.example.todolist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.todolist.databinding.ActivityMainBinding
import com.example.todolist.datasource.room.TaskDataBase
import com.example.todolist.ui.AddTaskActivity
import com.example.todolist.ui.AddTaskActivity.Companion.TASK_ID
import com.example.todolist.ui.adapter.TaskListAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter: TaskListAdapter = TaskListAdapter()
    private val db by lazy { TaskDataBase.intance(this).taskDao() }
    private val launchSomeActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
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

    override fun onResume() {
        super.onResume()
        updateList()
    }

    private fun insertListeners() {
        configFAB()
        editTask()
        deleteTask()
    }

    private fun deleteTask() {
        adapter.listenerDelete = {
            db.delete(it)
            updateList()
        }
    }

    private fun editTask() {
        adapter.listenerEdit = {
            Intent(this, AddTaskActivity::class.java).apply {
                putExtra(TASK_ID, it)
                startActivity(this)
            }
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
        binding.IncludeEmpty.emptyStateLayout.visibility = if (db.searchAll().isEmpty()) View.VISIBLE
        else View.GONE
        adapter.submitList(db.searchAll())
    }
}
