package com.example.todo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.todo.databinding.FragmentAddBinding
import com.example.todo.databinding.FragmentUserBinding

class UserFragment : DialogFragment() {

    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!
    private val mViewModel: TodoViewModel by activityViewModels()
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
        }

        binding.username.text = name
        binding.userEmail.text = email

    }

}