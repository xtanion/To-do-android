package com.example.todo

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Paint
import android.opengl.Visibility
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.data.TodoEntity
import com.example.todo.databinding.FragmentMydayBinding
import com.example.todo.databinding.SingleColumnBinding
import com.example.todo.fragments.MydayFragment
import kotlinx.android.synthetic.main.single_column.view.*

class TodoRVAdapter(val rvInterface: RVInterface): RecyclerView.Adapter<TodoRVAdapter.TodoViewHolder>(){

    private var DataList = emptyList<TodoEntity>()
    var _binding : SingleColumnBinding? = null
    val binding get() =  _binding!!
    private lateinit var context:Context
    private var lastPosition: Int = -1

    inner class TodoViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        init {
            itemView.checkbox_column.setOnClickListener {
                val position: Int = adapterPosition
                rvInterface.onCheckboxClick(position)
            }
            itemView.star_icon_column.setOnClickListener{
                val position: Int = adapterPosition
                rvInterface.onStarClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        _binding = SingleColumnBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        context = parent.context
        return TodoViewHolder(binding.root)
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val currentItem = DataList[position]
        val star_icon: ImageView = binding.starIconColumn

        if (holder.adapterPosition>lastPosition) {
            val animation: Animation =
                AnimationUtils.loadAnimation(holder.itemView.context, R.anim.fall_down)
            holder.itemView.startAnimation(animation)}

            binding.checkboxTextColumn.text = currentItem.title

            if (currentItem.completed) {
                binding.checkboxColumn.isChecked = true
                binding.checkboxTextColumn.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                binding.checkboxColumn.isChecked = false
            }

            if (currentItem.important == true) {
                binding.starIconColumn.isVisible = true
                //binding.checkboxColumn.buttonTintList = ColorStateList.valueOf(ContextCompat.getColor(context,R.color.red))
                binding.starIconColumn.imageTintList =
                    (ColorStateList.valueOf(ContextCompat.getColor(context, R.color.red)))
            }




    }

    override fun getItemCount(): Int {
        return DataList.size
    }

    class TodoDiffCallback(var oldList:List<TodoEntity>,var newList: List<TodoEntity>):DiffUtil.Callback(){
        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return (oldList.get(oldItemPosition).id == newList.get(newItemPosition).id)
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return (oldList.get(oldItemPosition).equals(newList.get(newItemPosition)))
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun NotifyChanges(DataList:List<TodoEntity>){
        val oldList = this.DataList
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(
            TodoDiffCallback(oldList,DataList)
        )
        this.DataList = DataList
        //notifyDataSetChanged()
        diffResult.dispatchUpdatesTo(this)
    }
    fun ColumnAdded(Data: TodoEntity){
        val mutableDataList:MutableList<TodoEntity> = DataList.toMutableList()
        mutableDataList.add(0,Data)
        this.DataList = mutableDataList.toList()
//        DataList.toMutableList().add(0,Data)
        notifyItemInserted(0)
    }

    interface RVInterface{
        fun onCheckboxClick(position: Int)
        fun onStarClick(position: Int)
    }


}