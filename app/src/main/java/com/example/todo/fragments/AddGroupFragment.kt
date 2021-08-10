package com.example.todo.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo.GroupRVAdapter
import com.example.todo.TodoViewModel
import com.example.todo.databinding.FragmentAddGroupBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_add_group.*

class AddGroupFragment : BottomSheetDialogFragment() {

    var _binding: FragmentAddGroupBinding? = null
    val binding get() = _binding!!
    private val mViewModel : TodoViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddGroupBinding.inflate(layoutInflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val group_rv = binding.groupRecyclerview
        val groupAdapter = GroupRVAdapter()

        group_rv.adapter = groupAdapter
        group_rv.layoutManager = LinearLayoutManager(context)
        group_rv.setHasFixedSize(false)

        mViewModel.listAllGroup().observe(viewLifecycleOwner, Observer {
            groupAdapter.NotifyGroupChanges(it)
        })
    }

}