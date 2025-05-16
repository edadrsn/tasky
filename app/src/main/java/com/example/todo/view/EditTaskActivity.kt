package com.example.todo.view

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.todo.R
import com.example.todo.databinding.ActivityEditTaskBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class EditTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditTaskBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var taskId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEditTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()

        taskId = intent.getStringExtra("taskId") ?: ""
        val taskTitle = intent.getStringExtra("taskTitle") ?: ""
        val taskDescription = intent.getStringExtra("taskDescription") ?: ""
        val taskDateMillis = intent.getLongExtra("taskDate", 0L)

        //Verileri ekranda gösterme
        binding.editTask.setText(taskTitle)
        binding.editTask.setText(taskTitle)
        binding.editDescription.setText(taskDescription)
        if (taskDateMillis != 0L) {
            val date = Date(taskDateMillis)
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(date)
            binding.editDatePicker.setText(formattedDate)
        }

        // Tarih alanı tıklanınca DatePicker açılması için
        binding.editDatePicker.setOnClickListener {
            val calendar = Calendar.getInstance()

            // Mevcut tarihe göre picker'ı başlat
            val currentDateString = binding.editDatePicker.text.toString()
            if (currentDateString.isNotBlank()) {
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val parsedDate = sdf.parse(currentDateString)
                if (parsedDate != null) {
                    calendar.time = parsedDate
                }
            }

            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog =
                DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                    val selectedDate = String.format(
                        "%02d/%02d/%04d",
                        selectedDay,
                        selectedMonth + 1,
                        selectedYear
                    )
                    binding.editDatePicker.setText(selectedDate)
                }, year, month, day)

            datePickerDialog.show()
        }
}

    fun updateTask(view: View) {

        val updateTask = binding.editTask.text.toString()
        val updateDescription = binding.editDescription.text.toString()
        val updateDate = binding.editDatePicker.text.toString()


        // Tarihi Date tipine dönüştür
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val updatedDate: Date
        try {
            updatedDate = dateFormat.parse(updateDate) ?: Date()
        } catch (e: Exception) {
            Toast.makeText(this, "Geçerli bir tarih girin", Toast.LENGTH_SHORT).show()
            return
        }

        //Firebase kaydet
        val updateTaskMap = hashMapOf<String, Any>()
        updateTaskMap["taskTitle"] = updateTask
        updateTaskMap["taskDescription"] = updateDescription
        updateTaskMap["taskDate"] = updatedDate



        // Firestore'da belirli belgeyi güncelle (örneğin taskId ile)
        firestore.collection("Tasks").document(taskId)
            .update(updateTaskMap)
            .addOnSuccessListener {
                Toast.makeText(this@EditTaskActivity, "Görev güncellendi", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this@EditTaskActivity, "Güncelleme başarısız: ${it.localizedMessage}", Toast.LENGTH_SHORT).show()
            }

        }


    fun previousPage(view:View){
        val intent=Intent(this@EditTaskActivity,HomeActivity::class.java)
        startActivity(intent)
    }
}