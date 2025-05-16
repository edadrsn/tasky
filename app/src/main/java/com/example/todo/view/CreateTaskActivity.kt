package com.example.todo.view

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.todo.databinding.ActivityCreateTaskBinding
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class CreateTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateTaskBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private val calendar = Calendar.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCreateTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)



        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        binding.taskDate.setOnClickListener {
            showDatePicker()
        }

    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                binding.taskDate.setText(sdf.format(selectedDate.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()
    }

    fun createTask(view: View) {
        // Kullanıcı giriş yaptıysa devam et
        if (auth.currentUser != null) {
            val taskMap = hashMapOf<String, Any>()
            taskMap["userId"] = auth.currentUser!!.uid
            taskMap["taskTitle"] = binding.taskText.text.toString()
            taskMap["taskDescription"] = binding.descriptionText.text.toString()


            val dateString = binding.taskDate.text.toString()
            if (dateString.isBlank()) {
                Toast.makeText(this, "Please select date", Toast.LENGTH_SHORT).show()
                return
            }

            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = dateFormat.parse(dateString) ?: Date()
            val timestamp = Timestamp(date)
            taskMap["taskDate"] = timestamp

            // Firestore'a veriyi ekle
            firestore.collection("Tasks").add(taskMap)
                .addOnSuccessListener {
                    Toast.makeText(
                        this@CreateTaskActivity,
                        "Task added successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this@CreateTaskActivity, "Couldn't add task", Toast.LENGTH_SHORT)
                        .show()
                    Log.e("FirestoreError", "Error: ${it.localizedMessage}")
                }
        }


        // Yeni task eklendi TaskList Activity'sine git
        val intent = Intent(this@CreateTaskActivity, HomeActivity::class.java)
        startActivity(intent)
    }

    fun previousPage(view: View) {
        val intent = Intent(this@CreateTaskActivity, HomeActivity::class.java)
        startActivity(intent)
    }
}