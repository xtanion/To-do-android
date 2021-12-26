package com.example.todo.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.todo.R
import com.example.todo.viewmodels.TodoViewModel
import com.example.todo.databinding.FragmentUserBinding

class UserFragment : DialogFragment() {

    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!
    private val mViewModel: TodoViewModel by activityViewModels()
    private val rotateAnim: Animation by lazy { AnimationUtils.loadAnimation(context,
        R.anim.rotate_ninty
    ) }
    private val rotateAnimAnti: Animation by lazy { AnimationUtils.loadAnimation(context,
        R.anim.rotate_ninty_anti
    ) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUserBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        dialog?.window?.setBackgroundDrawableResource(R.drawable.dialogue_bg)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val currentUser = mViewModel.mAuthMethod().currentUser
        val name = currentUser?.displayName
        val email = currentUser?.email
        val dpUrl = currentUser?.photoUrl


        val profilePicView = binding.profilePic
        if (dpUrl != null) {
            Glide.with(view)
                .load(dpUrl)
                .placeholder(R.drawable.ic_user_placeholder)
                .fitCenter()
                .into(profilePicView)
        }else{
            Glide.with(view)
                .load("https://static.wikia.nocookie.net/0da1c340-f9d1-4805-8ed7-d91d7e257cce/scale-to-width/755")
                .placeholder(R.drawable.ic_user_placeholder)
                .fitCenter()
                .into(profilePicView)
        }
        if (currentUser!=null) {
            binding.apply {
                username.text = name
                userEmail.text = email
                signInOut.text = "Sign Out"
            }
        }else{
            binding.apply {
                signInOut.text = "Sign in with Google"
                signInOut.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_twotone_person,0,0,0)
                userEmail.visibility = View.GONE
                manageAcc.visibility = View.GONE
                addAccount.visibility = View.GONE
                username.text = "Not Logged In"
                syncData.text = "Sync Not Possible"
            }
        }
        binding.helpFeedback.setOnClickListener {
            openUrl("https://github.com/xtanion/To-do-android/blob/main/README.md")
        }
        binding.rateUs.setOnClickListener {
            Toast.makeText(context,"Enjoying the App? Star the repository on Github",Toast.LENGTH_LONG).show()
            openUrl("https://github.com/xtanion/To-do-android")
        }

        binding.changeTheme.setOnClickListener {
            changeTheme()
            var drawable = R.drawable.ic_sun
            drawable = if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                R.drawable.ic_sun
            } else{
                R.drawable.ic_night
            }
            binding.changeTheme.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable,0,0,0)
        }

    }

    private fun openUrl(url: String) {
        val uri:Uri = Uri.parse(url)
        val intent:Intent = Intent(Intent.ACTION_VIEW,uri)
        startActivity(intent)
    }

    private fun changeTheme(){

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

}