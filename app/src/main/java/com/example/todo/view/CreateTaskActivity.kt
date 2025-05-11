package com.example.todo

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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

    fun addTask(view: View) {
        //Kullanıcı giriş yaptıysa bilgilerini al
        if (auth.currentUser != null) {
            val taskMap = mutableMapOf<String, Any>()
            taskMap.put(
                "taskTitle",
                binding.taskText!!.text.toString()
            )

            //Firestore'a gönderiyi ekle
            firestore.collection("Tasks").add(taskMap)
                .addOnSuccessListener {
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this@CreateTaskActivity, it.localizedMessage, Toast.LENGTH_SHORT)
                        .show()
                }
        }
    }


}