package com.example.todo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.todo.databinding.FragmentAddBinding
import com.example.todo.databinding.FragmentUserBinding

class UserFragment : DialogFragment() {

    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUserBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        return view
    }

}