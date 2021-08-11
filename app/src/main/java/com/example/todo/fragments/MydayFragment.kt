package com.example.todo.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.GeneratedAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.todo.*
import com.example.todo.data.GroupEntity
import com.example.todo.data.TodoConverter
import com.example.todo.data.TodoEntity
import com.example.todo.data.nestedTodo
import com.example.todo.databinding.FragmentMydayBinding
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_details.*
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*


class MydayFragment : Fragment(),TodoRVAdapter.RVInterface {

    var _binding:FragmentMydayBinding? = null
    val binding get() = _binding!!
    private val mViewModel: TodoViewModel by activityViewModels()
    private val rotateAnim: Animation by lazy { AnimationUtils.loadAnimation(context,R.anim.rotate_ninty) }
    private val rotateAnimAnti: Animation by lazy { AnimationUtils.loadAnimation(context,R.anim.rotate_ninty_anti) }
    private val buttonPress: Animation by lazy { AnimationUtils.loadAnimation(context,R.anim.button_press) }

    // Getting Date-Time
    @RequiresApi(Build.VERSION_CODES.O)
    val dateToday = LocalDateTime.now()
    val date_time = dateToday.toString()
    @RequiresApi(Build.VERSION_CODES.O)
    val day = LocalDate.now().dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.US)

    @RequiresApi(Build.VERSION_CODES.O)
    val formatter = DateTimeFormatter.ofPattern("MMMM dd")
    @RequiresApi(Build.VERSION_CODES.O)
    val formatted = dateToday.format(formatter)

    val dayToShow: String = "${day},${formatted}"

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

        //Auth
        val user = mViewModel.mAuthMethod()

        // First RV
        val recyclerViewIncomp = binding.recyclerViewIncomplete
        val adapter_incomp = TodoRVAdapter(this)
        binding.dateTimeExpanded.text = dayToShow
        activity?.findViewById<BottomAppBar>(R.id.bottom_appbar)?.visibility = View.VISIBLE
        activity?.findViewById<FloatingActionButton>(R.id.floatingActionButtonMain)?.visibility = View.VISIBLE
        val listSwitcher:ImageView = activity?.findViewById(R.id.list_switcher)!!

        //SWIPE GESTURE for rv1
        val swipeGesture = object :SwipeGesture(){

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when(direction){
                    ItemTouchHelper.LEFT->{
                        val data = mViewModel.listData().value?.get(viewHolder.adapterPosition)
                        mViewModel.removeTodo(data!!)
                    }
                }
            }
        }

        //First RV
        recyclerViewIncomp.adapter = adapter_incomp
        recyclerViewIncomp.layoutManager = LinearLayoutManager(context)
        recyclerViewIncomp.setHasFixedSize(false)
        ItemTouchHelper(swipeGesture).attachToRecyclerView(recyclerViewIncomp)

        //SWIPE Gesture for rv2
        val swipeGestureTwo = object :SwipeGesture(){

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when(direction){
                    ItemTouchHelper.LEFT->{
                        val data = mViewModel.listCompleted().value?.get(viewHolder.adapterPosition)
                        mViewModel.removeTodo(data!!)
                    }
                }
            }
        }


        // Second RV
        val recyclerViewComp = binding.recyclerViewComplete
        val adapter_comp = TodoRVAdapter(this)
        recyclerViewComp.adapter = adapter_comp
        recyclerViewComp.layoutManager = LinearLayoutManager(context)
        recyclerViewComp.setHasFixedSize(false)
        ItemTouchHelper(swipeGestureTwo).attachToRecyclerView(recyclerViewComp)

        mViewModel.listData().observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = true
            adapter_incomp.NotifyChanges(it)
            binding.progressBar.visibility = View.GONE

            if(it.isNotEmpty()) {
                val newEntity: TodoEntity = it[0]
                mViewModel.fireBaseAdd(newEntity)
            }
            if (it.size==0){
                binding.noResultLottie.isVisible = true
            }else{
                binding.noResultLottie.visibility = View.GONE
            }

        })

        mViewModel.listCompleted().observe(viewLifecycleOwner, Observer {
            adapter_comp.NotifyChanges(it)
//            val size = mViewModel.listCompleted().value?.size
            val size = it.size
            binding.completedText.text = "Completed  ${size}"

            if(it.isNotEmpty()) {
                val newEntity: TodoEntity = it[0]
                mViewModel.fireBaseAdd(newEntity)
            }
            if (it.size==0){
                binding.completedIcon.isVisible = false
                binding.completedText.isVisible = false
            }else{
                binding.completedIcon.isVisible = true
                binding.completedText.isVisible = true
            }
        })

        binding.completedText.setOnClickListener{
            val completed_recycler = binding.recyclerViewComplete
            if (completed_recycler.isVisible){
                binding.completedIcon.animation = rotateAnimAnti
                completed_recycler.isVisible = false
            }
            else{
                binding.completedIcon.animation = rotateAnim
                completed_recycler.isVisible = true
            }
        }

//        binding.floatingActionButton.setOnClickListener {
//            binding.floatingActionButton.startAnimation(buttonPress)
//            val action = MydayFragmentDirections.actionMydayFragmentToAddFragment()
//            Navigation.findNavController(view).navigate(action)
//        }

        val fab = activity?.findViewById<FloatingActionButton>(R.id.floatingActionButtonMain)
        fab?.setOnClickListener {
                it.startAnimation(buttonPress)
                val action = MydayFragmentDirections.actionMydayFragmentToAddFragment()
                Navigation.findNavController(view).navigate(action)
            }
        // Sync and Iterate through all items Asynchronously
        lifecycleScope.launchWhenResumed {
            binding.progressBar.visibility = View.VISIBLE
            val dataIncomplete = mViewModel.listData().value
            val dataComplete = mViewModel.listCompleted().value

            if (dataComplete != null) {
                for (items in dataComplete) {
                    mViewModel.fireBaseAdd(items)
                }
            }
            if (dataIncomplete != null) {
                for (items in dataIncomplete){
                    mViewModel.fireBaseAdd(items)
                }
            }
            mViewModel.getFirebaseData()
            binding.progressBar.visibility = View.GONE
        }

        binding.gearIcon.setOnClickListener {
            val data = mViewModel.fireDataReturn()
            Toast.makeText(context,data.toString(),Toast.LENGTH_LONG).show()
        }


        listSwitcher.setOnClickListener {
            it.startAnimation(buttonPress)
            val action = MydayFragmentDirections.actionMydayFragmentToAddGroupFragment()
            Navigation.findNavController(view).navigate(action)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCheckboxClick(data:TodoEntity) {
        //val columnData = mViewModel.listData().value?.get(position)
        if (!data.completed){
            val newData = TodoEntity(data.id,data.title,data.description,data.important,true,data.groupName,date_time,data.nestedTodo)
            mViewModel.updateTodo(newData)

        }
        else{
            val newData = TodoEntity(data.id,data.title,data.description,data.important,false,data.groupName,date_time,data.nestedTodo)
            mViewModel.updateTodo(newData)
        }
    }

    override fun onStarClick(data: TodoEntity) {
        if (data.important){
            val newData = TodoEntity(data.id,data.title,data.description,false,data.completed,data.groupName,date_time,data.nestedTodo)
            mViewModel.updateTodo(newData)

        }else{
            val newData = TodoEntity(data.id,data.title,data.description,true,data.completed,data.groupName,date_time,data.nestedTodo)
            mViewModel.updateTodo(newData)
        }
    }

    override fun onViewClick(data: TodoEntity) {
        val action = MydayFragmentDirections.actionMydayFragmentToDetailsFragment(data)
        Navigation.findNavController(requireView()).navigate(action)
    }

}