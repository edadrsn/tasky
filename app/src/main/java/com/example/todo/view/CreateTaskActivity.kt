package com.example.todo.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.todo.databinding.ActivityCreateTaskBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CreateTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateTaskBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCreateTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

    }

    //Verileri firestora kaydet
    fun createTask(view: View) {
        // Kullanıcı giriş yaptıysadevam et
        if (auth.currentUser != null) {
            val taskMap = mutableMapOf<String, Any>()
            taskMap.put("taskTitle", binding.taskText!!.text.toString())

            // Firestore'a "Tasks" koleksiyonuna bu yeni veriyi ekleme
            firestore.collection("Tasks").add(taskMap)
                .addOnSuccessListener {
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this@CreateTaskActivity, it.localizedMessage, Toast.LENGTH_SHORT)
                        .show()
                }
        }

        // Yeni task eklendi TaskList Activity'sine git
        val intent = Intent(this@CreateTaskActivity, TaskList::class.java)
        startActivity(intent)
    }
}