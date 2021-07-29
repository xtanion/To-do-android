package com.example.todo.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo.R
import com.example.todo.TodoRVAdapter
import com.example.todo.TodoViewModel
import com.example.todo.databinding.FragmentMydayBinding


class MydayFragment : Fragment() {

    var _binding:FragmentMydayBinding? = null
    val binding get() = _binding!!
    private lateinit var mViewModel: TodoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMydayBinding.inflate(layoutInflater,container,false)
        val view =  binding.root
        mViewModel = ViewModelProvider(this).get(TodoViewModel::class.java)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val recyclerView = binding.recyclerView
        val adapter = TodoRVAdapter()

        mViewModel.readToDo()

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}