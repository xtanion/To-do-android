package com.example.todo.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.GeneratedAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import com.example.todo.TodoRVAdapter
import com.example.todo.TodoViewModel
import com.example.todo.data.TodoEntity
import com.example.todo.databinding.FragmentMydayBinding


class MydayFragment : Fragment(),TodoRVAdapter.RVInterface {

    var _binding:FragmentMydayBinding? = null
    val binding get() = _binding!!
    private val mViewModel: TodoViewModel by activityViewModels()
    val args: MydayFragmentArgs by navArgs()

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

        // FIrst RV
        val recyclerViewIncomp = binding.recyclerViewIncomplete
        val adapter_incomp = TodoRVAdapter(this)

        recyclerViewIncomp.adapter = adapter_incomp
        recyclerViewIncomp.layoutManager = LinearLayoutManager(context)
        recyclerViewIncomp.setHasFixedSize(false)

        // Second RV
        val recyclerViewComp = binding.recyclerViewComplete
        val adapter_comp = TodoRVAdapter(this)
        recyclerViewComp.adapter = adapter_comp
        recyclerViewComp.layoutManager = LinearLayoutManager(context)
        recyclerViewComp.setHasFixedSize(false)

        mViewModel.listData().observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = true
            adapter_incomp.NotifyChanges(it)
            binding.progressBar.isVisible = false

        })

        mViewModel.listCompleted().observe(viewLifecycleOwner, Observer {
            adapter_comp.NotifyChanges(it)
        })

        binding.completedText.setOnClickListener{
            val completed_recycler = binding.recyclerViewComplete
            completed_recycler.isVisible = !completed_recycler.isVisible
        }

        binding.floatingActionButton.setOnClickListener {
            val action = MydayFragmentDirections.actionMydayFragmentToAddFragment()
            Navigation.findNavController(view).navigate(action)
        }
        binding.navigateBack.setOnClickListener {
            val action = MydayFragmentDirections.actionMydayFragmentToFrontpage()
            Navigation.findNavController(view).navigate(action)
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCheckboxClick(position: Int) {
        //Toast.makeText(context,"${position.toString()}",Toast.LENGTH_SHORT).show()
        val columnData = mViewModel.listData().value?.get(position)
        if (columnData?.completed == true){
            val newData = TodoEntity(columnData.id,columnData.title,columnData.important,false)
            mViewModel.updateTodo(newData)

        }else{
            val newData = TodoEntity(columnData?.id!!,columnData.title,columnData.important,true)
            mViewModel.updateTodo(newData)
        }
    }

    override fun onStarClick(position: Int) {
        val columnData = mViewModel.listData().value?.get(position)
        if (columnData?.important == true){
            val newData = TodoEntity(columnData.id,columnData.title,false,columnData.completed)
            mViewModel.updateTodo(newData)

        }else{
            val newData = TodoEntity(columnData?.id!!,columnData.title,true,columnData.completed)
            mViewModel.updateTodo(newData)
        }
    }


}