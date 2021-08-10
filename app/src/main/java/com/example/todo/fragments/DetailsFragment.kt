package com.example.todo.fragments

import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.todo.R
import com.example.todo.TodoViewModel
import com.example.todo.data.TodoEntity
import com.example.todo.databinding.FragmentDetailsBinding
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DetailsFragment : Fragment() {

    var _binding: FragmentDetailsBinding? = null
    val binding get() = _binding!!
    private val mViewModel: TodoViewModel by activityViewModels()
    val args:DetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailsBinding.inflate(layoutInflater,container,false)
        val view =  binding.root

        return view
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val entity = args.dataEntity!!

        activity?.findViewById<BottomAppBar>(R.id.bottom_appbar)?.visibility = View.GONE
        activity?.findViewById<FloatingActionButton>(R.id.floatingActionButtonMain)?.visibility = View.GONE

        binding.detailedItemTitle.setText( args.dataEntity?.title.toString())
        binding.detailedDescription.setText(args.dataEntity?.description)
        binding.datetimeDetails.setText("Last Updated: ${entity.dateTime}")

        if(args.dataEntity?.important == true){
            binding.importantSign.imageTintList = (ColorStateList.valueOf(ContextCompat.getColor(
                requireContext(), R.color.red
            )))
        }
        if(args.dataEntity?.completed==true){
            binding.checkbox.isChecked = true
        }

        binding.updateTickButton.setOnClickListener {
            val title:String = binding.detailedItemTitle.text.toString()
            val description:String = binding.detailedDescription.text.toString()
            val importance:Boolean = args.dataEntity!!.important
            val check: Boolean = binding.checkbox.isChecked
            //Add more later on (links,images,importance,etc)

            val updatedUnit:TodoEntity = TodoEntity(entity!!.id,title,description,importance,check,entity.groupName,entity.dateTime,entity.nestedTodo)
            mViewModel.updateTodo(updatedUnit)
            Toast.makeText(context,updatedUnit.toString(),Toast.LENGTH_SHORT).show()
            val action = DetailsFragmentDirections.actionDetailsFragmentToMydayFragment()
            Navigation.findNavController(view).navigate(action)
        }

        binding.navigateBack.setOnClickListener {
            Navigation.findNavController(it).navigateUp()
        }
    }

}