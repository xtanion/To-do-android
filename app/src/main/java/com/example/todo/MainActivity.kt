package com.example.todo

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var mAuth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_ToDo)
        createNotificationChannel()
        setContentView(R.layout.activity_main)

        val preferences:SharedPreferences = getSharedPreferences("SkipAuth", MODE_PRIVATE)
        val clicked:Boolean = preferences.getBoolean("Skip",false)

        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser
        Log.d("Skip SignIn Main",clicked.toString())

        if (!clicked && user==null) {
            val logInIntent = Intent(this, SigninActivity::class.java)
            startActivity(logInIntent)
            finish()
        }

    }

    private fun createNotificationChannel() {
        val id = getString(R.string.CHANNEL_ONE)
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val channel = NotificationChannel(id,"Test Notification", NotificationManager.IMPORTANCE_HIGH)
            channel.description = "Testing Alarm Manager (service)"
            channel.enableLights(true)
            channel.enableVibration(true)
            val notificationManager = this.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }
}