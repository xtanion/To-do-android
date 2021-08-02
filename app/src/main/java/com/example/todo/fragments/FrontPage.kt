package com.example.todo.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.todo.R
import com.example.todo.TodoViewModel
import com.example.todo.databinding.FragmentFrontpageBinding


class FrontPage : Fragment() {

    var _binding:FragmentFrontpageBinding? = null
    val binding get() = _binding!!
    private lateinit var mViewModel:TodoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFrontpageBinding.inflate(layoutInflater,container,false)
        val view = binding.root
        mViewModel = ViewModelProvider(this).get(TodoViewModel::class.java)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.mydayCard.setOnClickListener {
            val action = FrontPageDirections.actionFrontpageToMydayFragment("All tasks")
            Navigation.findNavController(it).navigate(action)
        }

        binding.importantsCard.setOnClickListener {
            val action = FrontPageDirections.actionFrontpageToMydayFragment("Important")
            Navigation.findNavController(it).navigate(action)
        }

        binding.mydayCount.text = mViewModel.listData().value?.size.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}