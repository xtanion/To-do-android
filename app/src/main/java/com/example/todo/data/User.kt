package com.example.todo.data

data class User(
    val username: String,
    val confirmPassword: String,
    val newPassword: String? = null
)
