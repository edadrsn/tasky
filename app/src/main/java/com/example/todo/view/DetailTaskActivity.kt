package com.example.todo.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.todo.R
import com.example.todo.databinding.ActivityDetailTaskBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailTaskActivity : AppCompatActivity() {

    private lateinit var binding :ActivityDetailTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivityDetailTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Verileri al
        val taskTitle=intent.getStringExtra("taskTitle")
        val taskDescription=intent.getStringExtra("taskDescription")
        val taskDateMillis=intent.getLongExtra("taskDate",0L)
        val taskDate= Date(taskDateMillis)

        //Aldığın verileri yazdır
        binding.detailTaskTitle.text=taskTitle
        binding.detailTaskDescription.text=taskDescription
        binding.detailTaskDate.text= SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(taskDate)

    }
}