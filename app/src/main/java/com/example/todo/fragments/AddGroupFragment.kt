package com.example.todo.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo.adapters.GroupRVAdapter
import com.example.todo.viewmodels.TodoViewModel
import com.example.todo.data.GroupEntity
import com.example.todo.databinding.FragmentAddGroupBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddGroupFragment : BottomSheetDialogFragment(), GroupRVAdapter.groupRVInterface {

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
        val groupAdapter = GroupRVAdapter(this)

        group_rv.adapter = groupAdapter
        group_rv.layoutManager = LinearLayoutManager(context)
        group_rv.setHasFixedSize(false)

        mViewModel.listAllGroup().observe(viewLifecycleOwner, Observer {
            groupAdapter.NotifyGroupChanges(it)
        })

        binding.groupInput.setOnFocusChangeListener{view,hasFocus->
            if (hasFocus){
                binding.addGroupLottie.visibility = View.VISIBLE
            }else{
                binding.addGroupLottie.visibility = View.GONE
            }
        }

        binding.addGroupLottie.setOnClickListener {
            val groupName = binding.groupInput.text.toString()
            val color = "#FFFFFF"
            val gEntity = GroupEntity(0,groupName,color)
            mViewModel.addGroup(gEntity)
            dismiss()
            val action = AddGroupFragmentDirections.actionAddGroupFragmentToAddFragment()
            Navigation.findNavController(view).navigate(action)
            view.hideKeyboard()
        }

    }

    override fun onGroupClick(data: GroupEntity) {
        val name = data.groupName
        Log.d("SORTED_GROUP_NAME",name)
        //TODO : use VIEWMODEL for this
        mViewModel.groupLiveName(name)
        view?.hideKeyboard()
        dismiss()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    @SuppressLint("ServiceCast")
    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

}