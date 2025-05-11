package com.example.todo.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo.R
import com.example.todo.adapter.TaskRecyclerAdapter
import com.example.todo.databinding.ActivityHomeBinding
import com.example.todo.databinding.ActivityTaskListBinding
import com.example.todo.model.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class TaskList : AppCompatActivity() {

    private lateinit var binding: ActivityTaskListBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var taskArrayList: ArrayList<Task>
    private lateinit var taskAdapter: TaskRecyclerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivityTaskListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Initialize işlemlerini yaptık
        auth = Firebase.auth
        db = Firebase.firestore
        taskArrayList = ArrayList<Task>()
        getData()

        // RecyclerView ayarlarını yaptık
        binding.recyclerView.layoutManager = LinearLayoutManager(this@TaskList) // Layout manager ile her bir item'ın düzenini belirledik
        taskAdapter = TaskRecyclerAdapter(taskArrayList) // Adapter'ı oluşturuyoruz ve postArrayList ile bağladık
        binding.recyclerView.adapter = taskAdapter // Adapter'ı RecyclerView'a bağladık

    }

    //Firestore verileri alma
    fun getData() {
        db.collection("Tasks")
            .addSnapshotListener { value, error ->
                // Eğer bir hata oluşursa kullanıcıya göster
                if (error != null) {
                    Toast.makeText(this, error.localizedMessage, Toast.LENGTH_SHORT).show()
                } else {
                    // Hata yoksa ve veri null değilse devam et
                    if (value != null) {
                        if (!value.isEmpty) {
                            val documents = value.documents

                            // Yeni veri geldiğinde listeyi temizle
                            taskArrayList.clear()

                            // Firestore'daki her belgeyi döngüyle gez
                            for (document in documents) {
                                // taskTitle alanını alıyoruz, yoksa boş string veriyoruz 
                                val taskTitle = document["taskTitle"] as? String ?: ""

                                // Task modelinden bir nesne oluşturuyoruz
                                val task = Task(taskTitle)

                                // Listeye ekliyoruz
                                taskArrayList.add(task)
                            }

                            // RecyclerView'a bağlı adapter'a, verilerin güncellendiğini bildiriyoruz
                            taskAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        }
    }