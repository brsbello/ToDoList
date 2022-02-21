package com.example.todolist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.todolist.databinding.ActivityMainBinding
import com.example.todolist.datasource.TaskDAO
import com.example.todolist.ui.AddTaskActivity
import com.example.todolist.ui.adapter.TaskListAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter: TaskListAdapter = TaskListAdapter()
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
    }

    override fun onResume() {
        super.onResume()
        insertListeners()
        updateList()
    }

    private fun insertListeners() {
        configFAB()
        editTask()
        deleteTask()
    }

    private fun deleteTask() {
        adapter.listenerDelete = {
            TaskDAO.deleteTask(it)
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
        val list = TaskDAO.getList()
        binding.IncludeEmpty.emptyStateLayout.visibility = if (list.isEmpty()) View.VISIBLE
        else View.GONE
        adapter.submitList(list)
    }
}
