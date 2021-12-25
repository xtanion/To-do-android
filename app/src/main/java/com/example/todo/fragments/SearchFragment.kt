package com.example.todo.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.todo.R
import com.example.todo.databinding.FragmentSearchBinding
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton


class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        //this.dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.findViewById<BottomAppBar>(R.id.bottom_appbar)?.visibility = View.GONE
        activity?.findViewById<FloatingActionButton>(R.id.floatingActionButtonMain)?.visibility = View.GONE

        binding.searchLayout.setStartIconOnClickListener {
            val action = SearchFragmentDirections.actionSearchFragmentToMydayFragment2()
            Navigation.findNavController(view).navigate(action)
        }
    }
}