package com.example.todo.activities

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.todo.R
import com.example.todo.data.User
import com.example.todo.databinding.ActivitySigninBinding
import com.example.todo.utils.AuthUtils
import com.example.todo.utils.Constants
import com.example.todo.utils.Utility
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_signin.*

class SigninActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var binding: ActivitySigninBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private var signUp: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        mAuth = FirebaseAuth.getInstance()
        binding.signInGoogle.setOnClickListener {
            progressBar3.visibility = View.VISIBLE
            signIn()
            progressBar3.visibility = View.GONE
        }

//        binding.knowMore.setOnClickListener {
//            //Using Shared preferences
//            val preferences: SharedPreferences = getSharedPreferences("SkipAuth", MODE_PRIVATE)
//            val editor:SharedPreferences.Editor = preferences.edit()
//            editor.apply{
//                putBoolean("Skip",true)
//                apply()
//            }
//
//            Log.d("Skip SignIn","file created")
//
//            val mainActivityIntent = Intent(this, MainActivity::class.java)
//            startActivity(mainActivityIntent)
//            finish()
//        }

        binding.knowMore.setOnClickListener {
            val utility = Utility()
            utility.openUrl(this, Constants.GITHUB_URL)
        }

        binding.signUpText.setOnClickListener {
            if (!signUp){
                new_password_tin.visibility = View.VISIBLE
                binding.apply {
                    signUpText.text = SIGN_IN_DIALOGUE
                    confirmPass.hint = CONFIRM_PASS
                    signInUpButton.text = SIGN_UP
                    signUp = true
                }
            }else {
                new_password_tin.visibility = View.GONE
                binding.apply {
                    signUpText.text = SIGN_UP_DIALOGUE
                    confirmPass.hint = "Password"
                    signInUpButton.text = SIGN_IN
                    signUp = false
                }
            }
        }

        binding.signInUpButton.setOnClickListener {
            binding.apply {
                val username = user.text.toString()
                val confirmPassword = confirmPass.text.toString()
                if (signUp){
                    val newPassword = newPass.text.toString()
                    if (newPassword!=confirmPassword){
                        confirmPasswordTin.isErrorEnabled = true
                        confirmPass.error = Constants.PASS_MATCH_ERROR
                        Toast.makeText(this@SigninActivity, "Password Do not Match", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    val user = User(username,confirmPassword)
                    AuthUtils().localSignInGoogle(user)
                    Toast.makeText(this@SigninActivity, "Added acc", Toast.LENGTH_SHORT).show()

                }
            }
        }


    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(Login_TAG, "firebaseAuthWithGoogle:" + account.id)
//                firebaseAuthWithGoogle(account.idToken!!)
                val authUtils = AuthUtils()
                authUtils.firebaseAuthWithGoogle(this, this, mAuth,account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(Login_TAG, "Google sign in failed", e)
            }
        }
    }

    companion object {
        private const val RC_SIGN_IN = 120
        private const val Login_TAG = "GOOGLE_LOGIN"
        private const val SIGN_IN_DIALOGUE = "Already an User? Sign In"
        private const val SIGN_UP_DIALOGUE = "New User? Sign Up"
        private const val CONFIRM_PASS = "Confirm Password"
        private const val SIGN_UP = "Sign Up"
        private const val SIGN_IN = "Sign In"
    }
}