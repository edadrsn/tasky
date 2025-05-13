package com.example.todo.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo.R
import com.example.todo.adapter.TaskRecyclerAdapter
import com.example.todo.databinding.ActivityHomeBinding
import com.example.todo.model.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var taskArrayList: ArrayList<Task>
    private lateinit var taskAdapter: TaskRecyclerAdapter
    private var listenerRegistration: ListenerRegistration? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Initialize işlemlerini yaptık
        auth = Firebase.auth
        db = Firebase.firestore
        taskArrayList = ArrayList<Task>()



        // RecyclerView ayarlarını yaptık
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this@HomeActivity) // Layout manager ile her bir item'ın düzenini belirledik
        taskAdapter = TaskRecyclerAdapter(taskArrayList) { task ->
            deleteTask(task)
        } // Adapter'ı oluşturuyoruz ve postArrayList ile bağladık
        binding.recyclerView.adapter = taskAdapter // Adapter'ı RecyclerView'a bağladık



        if (Firebase.auth.currentUser != null) {
            getData()
        } else {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        
        binding.menuIcon.setOnClickListener {
            val popupMenu=PopupMenu(this,it)
            popupMenu.menuInflater.inflate(R.menu.task_menu,popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { item->
                when(item.itemId){
                    R.id.signOut -> {
                        listenerRegistration?.remove()
                        Firebase.auth.signOut()
                        startActivity(Intent(this@HomeActivity,MainActivity::class.java))
                        finishAffinity()
                        true
                    }
                    else -> false

                }
            }

            popupMenu.show()

        }

    }


    override fun onStop() {
        super.onStop()
        // Dinleyiciyi kaldırıcazki gereksiz dinleme işlemlerinden kaçınalım
        listenerRegistration?.remove()
    }

    //Görev oluştur butonu tanımlandı
    fun createTask(view: View) {
        val intent = Intent(this@HomeActivity, CreateTaskActivity::class.java)
        startActivity(intent)
    }

    //Firestore verileri alma
    fun getData() {
        val currentUserId=Firebase.auth.currentUser?.uid
        listenerRegistration=db.collection("Tasks")
            .whereEqualTo("userId",currentUserId)
            .addSnapshotListener { value, error ->
                // Eğer bir hata oluşursa kullanıcıya göster
                if (error != null) {
                    Toast.makeText(this, error.localizedMessage, Toast.LENGTH_SHORT).show()
                } else {
                    // Hata yoksa ve veri null değilse devam et
                    if (value != null) {
                        if (!value.isEmpty) {

                            // Yeni veri geldiğinde listeyi temizle
                            taskArrayList.clear()

                            // Firestore'daki her belgeyi döngüyle gez
                            for (document in value.documents) {
                                // taskTitle ve id alanını alıyoruz, taskTitle yoksa boş string veriyoruz
                                val taskId = document.id
                                val taskTitle = document["taskTitle"] as? String ?: ""

                                // Task modelinden bir nesne oluşturuyoruz
                                val task = Task(taskId, taskTitle)

                                // Listeye ekliyoruz
                                taskArrayList.add(task)
                            }

                            // RecyclerView'a bağlı adapter'a, verilerin güncellendiğini bildiriyoruz
                            taskAdapter.notifyDataSetChanged()


                            // Eğer taskList'te en az bir task varsa, ImageView ve TextView'ı gizle
                            if (taskArrayList.isNotEmpty()) {
                                binding.imageView3.visibility = View.GONE  // Resmi gizle
                                binding.textView.visibility = View.GONE   // Yazıyı gizle
                                binding.recyclerView.visibility = View.VISIBLE
                            } else {
                                binding.imageView3.visibility = View.VISIBLE  // Resmi göster
                                binding.textView.visibility = View.VISIBLE   // Yazıyı göster
                                binding.recyclerView.visibility = View.GONE
                            }
                        }
                    }
                }
            }
    }


    fun deleteTask(task:Task){
        db.collection("Tasks")
            .document(task.taskId) // taskId'yi kullanarak silme işlemi yapıyoruz
            .delete()
            .addOnSuccessListener {
                taskArrayList.remove(task) // Listeyi güncelliyoruz
                taskAdapter.notifyDataSetChanged() // Adapter'ı güncelliyoruz
                Toast.makeText(this, "Task deleted successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to delete task", Toast.LENGTH_SHORT).show()
            }
    }
}


