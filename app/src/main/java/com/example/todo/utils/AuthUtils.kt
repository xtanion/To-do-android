package com.example.todo.utils

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.todo.activities.MainActivity
import com.example.todo.activities.SigninActivity
import com.example.todo.data.User
import com.example.todo.viewmodels.TodoViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase

class AuthUtils {

    fun firebaseAuthWithGoogle(context:Context, activity: AppCompatActivity, mAuth:FirebaseAuth, idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(Constants.LOGIN_TAG, "signInWithCredential:success")
                    //val user = mAuth.currentUser
                    val mainActivityIntent = Intent(context, MainActivity::class.java)
                    context.startActivity(mainActivityIntent)
                    activity.finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(context,"Failed To Login", Toast.LENGTH_LONG).show()
                    Log.w(Constants.LOGIN_TAG , "signInWithCredential:failure", task.exception)
                }
            }
    }

    fun localSignInGoogle(user: User){
        val rootNode:FirebaseDatabase = FirebaseDatabase.getInstance()
        val uid = Utility().crateFirebaseUid(user)
        val ref = rootNode.getReference("users/${uid}/")
        ref.setValue(user)
    }

    fun loginTypeGoogle():Boolean{
        return false
    }
}