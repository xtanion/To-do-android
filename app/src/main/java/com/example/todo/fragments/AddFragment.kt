package com.example.todo.fragments

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat.getColor
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import com.example.todo.TodoViewModel
import com.example.todo.data.TodoEntity
import com.example.todo.databinding.FragmentAddBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_details.*
import java.time.LocalDateTime

class AddFragment : BottomSheetDialogFragment() {
    var _binding:FragmentAddBinding? = null
    val binding get() = _binding
    private lateinit var mViewModel: TodoViewModel
    @RequiresApi(Build.VERSION_CODES.O)
    val dateToday = LocalDateTime.now()
    val args:AddFragmentArgs by navArgs()

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

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var starred:Boolean = false
        val star_button = binding?.starIcon
        val group_name = args.groupName
        //view.showKeyboard()

        binding?.addToDbLottie?.setOnClickListener {
            val input_todo:String = binding!!.todoInput.text.toString()
            val todo_arg = TodoEntity(0,input_todo,null,starred,false,group_name,dateToday.toString(),null)

            if (input_todo!=""){
                mViewModel.addToDo(todo_arg)
                Toast.makeText(context,todo_arg.toString(),Toast.LENGTH_SHORT).show()
                view.hideKeyboard()
                dismiss()
            }
        }

        star_button?.setOnCheckedChangeListener { button, b ->
            starred = button.isChecked()
            if (button.isChecked){
                button.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                button.buttonTintList = ContextCompat.getColorStateList(requireContext(),R.color.red)
            }else{
                button.setTextColor(ContextCompat.getColor(requireContext(), R.color.snow_white))
                button.buttonTintList = ContextCompat.getColorStateList(requireContext(),R.color.snow_white)
            }

        }

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

    @SuppressLint("ServiceCast")
    fun View.showKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0)
    }


}