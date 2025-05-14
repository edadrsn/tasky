package com.example.todo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.todo.databinding.RecyclerRowBinding
import com.example.todo.model.Task
import java.text.SimpleDateFormat
import java.util.Locale

// RecyclerView için özel bir adapter sınıfı tanımladım
class TaskRecyclerAdapter(
    private val taskList: ArrayList<Task>,
    private val onDeleteClick:(task:Task)->Unit) :
    RecyclerView.Adapter<TaskRecyclerAdapter.TaskHolder>() {

    // Her bir satırı tutan ViewHolder sınıfı. RecyclerRowBinding üzerinden erişim sağlanıyor
    class TaskHolder(val binding: RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root)

    // XML layout'u oluşturmak için kullanılır. Her bir satır için RecyclerRowBinding inflate ediyor
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskHolder(binding)
    }

    // Toplam kaç eleman gösterilecek
    override fun getItemCount(): Int {
        return taskList.size
    }

    // Her bir satıra verileri bağladım
    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        val task=taskList[position]
        // taskList'teki ilgili Task nesnesinin 'task' başlığını TextView'e atadım
        holder.binding.recyclerRowTask.text = task.task
        holder.binding.recyclerRowTaskDescription.text=task.taskDescription

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        holder.binding.recyclerRowTaskDate.text = dateFormat.format(task.taskDate)

        //Silme butonuna tıklama olayı
        holder.binding.deleteImage.setOnClickListener{
            onDeleteClick(task)
        }
    }
}
