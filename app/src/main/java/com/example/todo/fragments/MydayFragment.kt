package com.example.todo.fragments

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.GeneratedAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import com.example.todo.TodoRVAdapter
import com.example.todo.TodoViewModel
import com.example.todo.data.TodoEntity
import com.example.todo.databinding.FragmentMydayBinding


class MydayFragment : Fragment() {

    var _binding:FragmentMydayBinding? = null
    val binding get() = _binding!!
    private val mViewModel: TodoViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMydayBinding.inflate(layoutInflater,container,false)
        val view =  binding.root
        //mViewModel = ViewModelProvider(this).get(TodoViewModel::class.java)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val recyclerView = binding.recyclerView
        val adapter = TodoRVAdapter()

        //mViewModel.readToDo()
        //adapter.NotifyChanges(mViewModel.listData().value!!)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(false)


        mViewModel.listData().observe(viewLifecycleOwner, Observer {
            Toast.makeText(context,"Dataset changed ${it.toString()}",Toast.LENGTH_SHORT).show()
            binding.progressBar.isVisible = true
            adapter.NotifyChanges(it)
            binding.progressBar.isVisible = false

        })

        binding.floatingActionButton.setOnClickListener {
            val action = MydayFragmentDirections.actionMydayFragmentToAddFragment()
            Navigation.findNavController(view).navigate(action)
        }

        // TEST CODE
        binding.gearIcon.setOnClickListener {
            val temp = TodoEntity(0,"Test Case",false,false)
            adapter.ColumnAdded(temp)
        }
        //TEST CODE

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}