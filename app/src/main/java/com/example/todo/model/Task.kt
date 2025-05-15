package com.example.todo.model

import com.google.type.Date

data class Task(
    val taskId:String="",
    val taskTitle:String="",
    val taskDescription:String="",
    val taskDate: java.util.Date
)