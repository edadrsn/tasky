package com.example.todo.model

import java.util.Date


data class Task(
    val taskId:String="",
    val taskTitle:String="",
    val taskDescription:String="",
    val taskDate: Date= Date()
)