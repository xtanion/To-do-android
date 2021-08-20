package com.example.todo.fragments

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.todo.MainActivity
import com.example.todo.alarms.AlarmReceiver
import com.example.todo.R
import com.example.todo.TodoViewModel
import com.example.todo.alarms.AlarmService
import com.example.todo.data.TodoEntity
import com.example.todo.databinding.FragmentAddBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.time.LocalDateTime
import java.util.*

class AddFragment : BottomSheetDialogFragment() {
    var _binding:FragmentAddBinding? = null
    val binding get() = _binding!!
    private lateinit var mViewModel: TodoViewModel
    @RequiresApi(Build.VERSION_CODES.O)
    val dateToday = LocalDateTime.now()
    val args:AddFragmentArgs by navArgs()
    private val buttonPress: Animation by lazy { AnimationUtils.loadAnimation(context,R.anim.button_press) }
    private lateinit var alarmManager:AlarmManager
    private lateinit var calendar:Calendar
    private var dateToSet:String? = null
    private var timeRepeatSelected: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddBinding.inflate(layoutInflater,container,false)
        val view = binding.root
        mViewModel = ViewModelProvider(this).get(TodoViewModel::class.java)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var starred:Boolean = false
        val star_button = binding.starIcon
        val group_name = args.groupName
        var requestCode:Int = 0
        //view.showKeyboard()

        binding.addToDbLottie.setOnClickListener {
            val input_todo:String = binding.todoInput.text.toString()
            val todo_arg = TodoEntity(0,input_todo,null,starred,false,group_name,dateToday.toString(),null,null)

            if (input_todo!=""){
                mViewModel.addToDo(todo_arg)
                view.hideKeyboard()
                dismiss()
                //New Method->
                val intent:Intent = Intent(context,AlarmReceiver::class.java)
                intent.putExtra("title",input_todo)
                Log.d("Alarm REQUEST GOT",requestCode.toString())

                if (timeRepeatSelected) {
                    AlarmService(requireContext()).setExactAlarm(
                        calendar.timeInMillis,
                        requestCode,
                        intent
                    )
                }
            }
        }

        mViewModel.listData().observe(viewLifecycleOwner,{
            //TODO change to get the real data that has been updated
            requestCode = it.reversed()[0].id
            Log.d("Alarm REQUEST CODE",requestCode.toString())
        })

        star_button.setOnCheckedChangeListener { button, b ->
            button.startAnimation(buttonPress)
            starred = button.isChecked()
            if (button.isChecked){
                button.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                button.buttonTintList = ContextCompat.getColorStateList(requireContext(),R.color.red)
            }else{
                button.setTextColor(ContextCompat.getColor(requireContext(), R.color.snow_white))
                button.buttonTintList = ContextCompat.getColorStateList(requireContext(),R.color.snow_white)
            }

        }
        binding.setAlarmIcon.setOnClickListener {
                it.startAnimation(buttonPress)
                showDatePicker()
            }

        binding.repeatIcon.setOnClickListener {
            it.startAnimation(buttonPress)
            showTimePicker()
        }

    }


    private fun showTimePicker() {
        val currentTime = Calendar.getInstance()
        val hours = currentTime.get(Calendar.HOUR_OF_DAY)
        val minutes = currentTime.get(Calendar.MINUTE)

        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(hours)
            .setMinute(minutes)
            .setTitleText("Select Time")
            .setTheme(R.style.Theme_App_Timepicker)
            .build()

        timePicker.show(parentFragmentManager,"TimePicker")

        timePicker.addOnPositiveButtonClickListener{
            var hrs = timePicker.hour
            val min = timePicker.minute

            calendar = Calendar.getInstance()
            calendar[Calendar.HOUR_OF_DAY] = hrs
            calendar[Calendar.MINUTE] = min
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MILLISECOND] = 0

            if (timePicker.hour>12){
                hrs -= 12
                binding.repeatIcon.text = String.format("%02d:%02d PM",hrs,min)
            }else{
                binding.repeatIcon.text = String.format("%02d:%02d AM",hrs,min)
            }

            binding.repeatIcon.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.deep_blue)))
            binding.repeatIcon.compoundDrawables[0].setTint(resources.getColor(R.color.deep_blue))

            timeRepeatSelected = true
        }
    }


    private fun showDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Date")
            .setTheme(R.style.ThemeOverlay_App_DatePicker)
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        datePicker.show(parentFragmentManager,"DatePicker")

        datePicker.addOnPositiveButtonClickListener {
            val privateCalendar = Calendar.getInstance()
            privateCalendar.time = Date(it)
            val day = "${privateCalendar.get(Calendar.DAY_OF_MONTH)}-${privateCalendar.get(Calendar.MONTH)}-${privateCalendar.get(Calendar.YEAR)}"
            dateToSet = day
            binding.setAlarmIcon.text = dateToSet
            binding.setAlarmIcon.apply {
                setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.deep_blue)))
                compoundDrawables[0].setTint(resources.getColor(R.color.deep_blue))
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