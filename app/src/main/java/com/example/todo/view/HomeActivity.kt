package com.example.todo

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.todo.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding:ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


    fun createTask(view: View){
        val intent= Intent(this@HomeActivity,CreateTaskActivity::class.java)
        startActivity(intent)
    }
}