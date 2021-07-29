package com.example.todo.fragments

import android.app.Application
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.todo.R
import com.example.todo.TodoViewModel
import com.example.todo.data.TodoEntity
import com.example.todo.databinding.FragmentAddBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddFragment : BottomSheetDialogFragment() {
    var _binding:FragmentAddBinding? = null
    val binding get() = _binding
    private lateinit var mViewModel: TodoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddBinding.inflate(layoutInflater,container,false)
        val view = binding?.root
        mViewModel = ViewModelProvider(this).get(TodoViewModel::class.java)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var starred:Boolean = false
        val star_button = binding?.starIcon
        binding?.addToDbButton?.setOnClickListener {
            val input_todo:String = binding!!.todoInput.text.toString()
            val todo_arg = TodoEntity(0,input_todo,starred)

            mViewModel.addToDo(todo_arg)

            //val action = AddFragmentDirections.actionAddFragmentToMydayFragment()
            //Navigation.findNavController(view).navigate(action)

        }

        star_button?.setOnClickListener {
            starred = true
            star_button.setTextColor(ContextCompat.getColor(requireContext(),R.color.peach))
        }

    }

}