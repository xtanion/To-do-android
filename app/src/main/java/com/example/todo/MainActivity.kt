package com.example.todo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var mAuth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_ToDo)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser

        if (user==null){
            Toast.makeText(applicationContext,"Not signed in",Toast.LENGTH_SHORT).show()
            val LogInIntent = Intent(this,SigninActivity::class.java)
            startActivity(LogInIntent)
            finish()
        }else{
            Toast.makeText(applicationContext,"Signed in already",Toast.LENGTH_SHORT).show()
        }

    }
}