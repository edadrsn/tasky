package com.example.todo.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.todo.databinding.ActivityCreateTaskBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

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
            val taskMap = hashMapOf<String,Any>()
            taskMap.put("taskTitle", binding.taskText.text.toString())
            taskMap.put("userId",auth.currentUser!!.uid)

            // Firestore'a "Tasks" koleksiyonuna bu yeni veriyi ekleme
            firestore.collection("Tasks").add(taskMap)
                .addOnSuccessListener {
                    Toast.makeText(this@CreateTaskActivity,"Task added successfully",Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this@CreateTaskActivity,"Couldn't add task", Toast.LENGTH_SHORT)
                        .show()
                    Log.e("FirestoreError", "Error: ${it.localizedMessage}")
                }
        }

        // Yeni task eklendi TaskList Activity'sine git
        val intent = Intent(this@CreateTaskActivity, HomeActivity::class.java)
        startActivity(intent)
    }
}