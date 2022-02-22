package com.example.todolist.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.todolist.databinding.ActivityAddTaskBinding
import com.example.todolist.datasource.room.TaskDataBase
import com.example.todolist.extensions.format
import com.example.todolist.extensions.text
import com.example.todolist.model.Task
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*

class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding
    private var idTask : Long = 0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(TASK_ID)) {
            intent.getParcelableExtra<Task>(TASK_ID)?.let {
                binding.BTNewTask.text = "Alterar Tarefa"
                idTask = it.id
                binding.ETTitle.text = it.title
                binding.ETDate.text = it.date
                binding.ETHour.text = it.hour
                binding.ETDescription.text = it.description
            }
        }
        insertListeners()
    }

    private fun insertListeners() {
        configDate()
        configHour()
        configCancelButton()
        configCreateButton()
    }

    private fun configCreateButton() {
        val db = TaskDataBase.intance(this)
        binding.BTNewTask.setOnClickListener {
            val task = newTask()
            if(idTask > 0 ) {
                db.taskDao().update(task)
            } else {
                db.taskDao().save(task)
            }
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    private fun newTask(): Task {
        return Task(
            id = idTask,
            title = binding.ETTitle.text,
            description = binding.ETDescription.text,
            date = binding.ETDate.text,
            hour = binding.ETHour.text

        )
    }

    private fun configCancelButton() {
        binding.BTCancel.setOnClickListener {
            finish()
        }
    }

    private fun configHour() {
        binding.ETHour.editText?.setOnClickListener {
            val timePicker =
                MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).build()
            timePicker.addOnPositiveButtonClickListener {
                val minute =
                    if (timePicker.minute in 0..9) "0${timePicker.minute}" else timePicker.minute
                val hour = if (timePicker.hour in 0..9) "0${timePicker.hour}" else timePicker.hour
                binding.ETHour.text = "${hour}:${minute}"
            }
            timePicker.show(supportFragmentManager, "TIME_PICKER_TAG")
        }
    }

    private fun configDate() {
        binding.ETDate.editText?.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.addOnPositiveButtonClickListener {
                val timeZone = TimeZone.getDefault()
                val offset = timeZone.getOffset(Date().time) * -1
                binding.ETDate.text = Date(it + offset).format()
            }
            datePicker.show(supportFragmentManager, "DATE_PICKER_TAG")
        }
    }

    companion object {
        const val TASK_ID = "task_id"
    }
}