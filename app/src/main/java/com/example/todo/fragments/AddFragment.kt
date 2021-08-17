package com.example.todo.fragments

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
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
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat.getColor
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.AlarmReceiver
import com.example.todo.R
import com.example.todo.TodoViewModel
import com.example.todo.data.TodoEntity
import com.example.todo.databinding.FragmentAddBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.android.synthetic.main.fragment_details.*
import kotlinx.coroutines.processNextEventInCurrentThread
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
        val star_button = binding.starIcon
        val group_name = args.groupName
        //view.showKeyboard()

        binding.addToDbLottie.setOnClickListener {
            val input_todo:String = binding.todoInput.text.toString()
            val todo_arg = TodoEntity(0,input_todo,null,starred,false,group_name,dateToday.toString(),null,null)

            if (input_todo!=""){
                mViewModel.addToDo(todo_arg)
                //Toast.makeText(context,todo_arg.toString(),Toast.LENGTH_SHORT).show()
                view.hideKeyboard()
                dismiss()
            }
        }

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
        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(4)
            .setMinute(20)
            .setTitleText("Select Time")
            .setTheme(R.style.Theme_App_Timepicker)
            .build()

        timePicker.show(parentFragmentManager,"TimePicker")

        timePicker.addOnPositiveButtonClickListener{
            var hrs = timePicker.hour
            val min = timePicker.minute
            if (timePicker.hour>12){
                hrs -= 12
                binding.repeatIcon.text = "${hrs}:${min}AM"
            }else{
                binding.repeatIcon.text = "${timePicker.hour}:${timePicker.minute}PM"
            }
            calendar = Calendar.getInstance()
            calendar[Calendar.HOUR_OF_DAY] = hrs
            calendar[Calendar.MINUTE] = min
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MILLISECOND] = 0

            setAlarm()
        }
    }

    private fun setAlarm(){

        alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context,AlarmReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)
        Log.d("Alarm","Set At: ${Date().toString()}")
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.timeInMillis,AlarmManager.INTERVAL_DAY,pendingIntent)


    }

    private fun showDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Date")
            .setTheme(R.style.ThemeOverlay_App_DatePicker)
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        datePicker.show(parentFragmentManager,"DatePicker")
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