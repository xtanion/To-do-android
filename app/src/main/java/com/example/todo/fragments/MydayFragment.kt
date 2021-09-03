package com.example.todo.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.*
import com.example.todo.adapters.TodoRVAdapter
import com.example.todo.data.TodoEntity
import com.example.todo.databinding.FragmentMydayBinding
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_details.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*


class MydayFragment : Fragment(), TodoRVAdapter.RVInterface {

    var _binding:FragmentMydayBinding? = null
    val binding get() = _binding!!
    private val mViewModel: TodoViewModel by activityViewModels()
    private val rotateAnim: Animation by lazy { AnimationUtils.loadAnimation(context,R.anim.rotate_ninty) }
    private val rotateAnimAnti: Animation by lazy { AnimationUtils.loadAnimation(context,R.anim.rotate_ninty_anti) }
    private val buttonPress: Animation by lazy { AnimationUtils.loadAnimation(context,R.anim.button_press) }
//    private var groupName:String = "all"
    private lateinit var groupName:String

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
        Log.d("STARTED","started MyDayFragment")
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //Identify Group?
        var incomplete_list = emptyList<TodoEntity>()
        var complete_list = emptyList<TodoEntity>()

        //Auth
        val user = mViewModel.mAuthMethod()

        // First RV
        val recyclerViewIncomplete = binding.recyclerViewIncomplete
        val adapterIncomp = TodoRVAdapter(this)
        binding.dateTimeExpanded.text = dayToShow
        activity?.findViewById<BottomAppBar>(R.id.bottom_appbar)?.visibility = View.VISIBLE
        val fab = activity?.findViewById<FloatingActionButton>(R.id.floatingActionButtonMain)
        fab?.visibility = View.VISIBLE
        val listSwitcher:ImageView = activity?.findViewById(R.id.list_switcher)!!
        val menuSwitcher:ImageView = activity?.findViewById(R.id.menu_switcher)!!

        //SWIPE GESTURE for rv1
        val swipeGesture = object :SwipeGesture(requireContext()){

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when(direction){
                    ItemTouchHelper.LEFT->{
                        val data = incomplete_list[viewHolder.adapterPosition]
                        mViewModel.removeTodo(data)
                    }
                }
            }
        }

        //First RV
        recyclerViewIncomplete.apply {
            adapter = adapterIncomp
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(false)
        }
        ItemTouchHelper(swipeGesture).attachToRecyclerView(recyclerViewIncomplete)

        //SWIPE Gesture for rv2
        val swipeGestureTwo = object :SwipeGesture(requireContext()){

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when(direction){
                    ItemTouchHelper.LEFT->{
                        val data = complete_list[viewHolder.adapterPosition]
                        mViewModel.removeTodo(data)
                    }
                }
            }
        }

        // Second RV
        val recyclerViewComp = binding.recyclerViewComplete
        val adapterComp = TodoRVAdapter(this)
        recyclerViewComp.adapter = adapterComp
        recyclerViewComp.layoutManager = LinearLayoutManager(context)
        recyclerViewComp.setHasFixedSize(false)
        ItemTouchHelper(swipeGestureTwo).attachToRecyclerView(recyclerViewComp)


        mViewModel.listData().observe(viewLifecycleOwner,{
            //Debug
            Log.d("LiveData",it.toString())
            binding.progressBar.visibility = View.VISIBLE
            incomplete_list = it
            adapterIncomp.NotifyChanges(it)
            binding.progressBar.visibility = View.GONE
            if (it.isEmpty()) {
                binding.noResultLottie.isVisible = true
            } else {
                binding.noResultLottie.visibility = View.GONE
            }

        })
        mViewModel.listCompleted().observe(viewLifecycleOwner,{

            Log.d("LiveData Completed",it.toString())
            binding.progressBar.visibility = View.VISIBLE
            complete_list = it
            adapterComp.NotifyChanges(it)
            binding.progressBar.visibility = View.GONE

            if (it.isEmpty()) {
                binding.apply {
                    completedIcon.isVisible = false
                    completedText.isVisible = false
                }
            } else {
                binding.apply {
                    completedIcon.isVisible = true
                    completedText.isVisible = true
                    //completedText.text = "Completed  ${it.size}"
                }
            }
        })
        mViewModel.returnGroupName().observe(viewLifecycleOwner,{
            groupName= it
            Log.d("groupName",groupName)
        })

//        lifecycleScope.launchWhenResumed {
////                for (items in incomplete_list) {
////                    mViewModel.fireBaseAdd(items)
////                    //Log.d("FIREBASE_ADD",items.toString())
////                }
////                for (items in complete_list){
////                    mViewModel.fireBaseAdd(items)
////                }
////                mViewModel.getFirebaseData()
////            }


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

        fab?.setOnClickListener {
                it.startAnimation(buttonPress)
                val action = MydayFragmentDirections.actionMydayFragmentToAddFragment(groupName)
                Navigation.findNavController(view).navigate(action)
        }

        binding.searchIcon.setOnClickListener {
            //binding.gearIcon.playAnimation()
            it.startAnimation(buttonPress)
            val action = MydayFragmentDirections.actionMydayFragmentToSearchFragment()
            Navigation.findNavController(view).navigate(action)
            //val data = mViewModel.fireDataReturn()
            //Toast.makeText(context,data.toString(),Toast.LENGTH_LONG).show()

        }


        listSwitcher.setOnClickListener {
            it.startAnimation(buttonPress)
            val action = MydayFragmentDirections.actionMydayFragmentToAddGroupFragment()
            Navigation.findNavController(view).navigate(action)
        }

        menuSwitcher.setOnClickListener {
            it.startAnimation(buttonPress)
            val action = MydayFragmentDirections.actionMydayFragmentToUserFragment()
            Navigation.findNavController(view).navigate(action)
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

    override fun onCheckboxClick(data:TodoEntity) {
        //val columnData = mViewModel.listData().value?.get(position)
        if (!data.completed){
            val newData = TodoEntity(data.id,data.title,data.description,data.important,true,data.groupName,date_time,data.nestedTodo,data.bitmap,data.requestCode,data.alarmTime)
            mViewModel.updateTodo(newData)

        }
        else{
            val newData = TodoEntity(data.id,data.title,data.description,data.important,false,data.groupName,date_time,data.nestedTodo,data.bitmap,data.requestCode,data.alarmTime)
            mViewModel.updateTodo(newData)
        }
    }

    override fun onStarClick(data: TodoEntity) {
        if (data.important){
            val newData = TodoEntity(data.id,data.title,data.description,false,data.completed,data.groupName,date_time,data.nestedTodo,data.bitmap,data.requestCode,data.alarmTime)
            mViewModel.updateTodo(newData)

        }else{
            val newData = TodoEntity(data.id,data.title,data.description,true,data.completed,data.groupName,date_time,data.nestedTodo,data.bitmap,data.requestCode,data.alarmTime)
            mViewModel.updateTodo(newData)
        }
    }

    override fun onViewClick(data: TodoEntity) {
        val action = MydayFragmentDirections.actionMydayFragmentToDetailsFragment(data)
        Navigation.findNavController(requireView()).navigate(action)
    }


}