package com.example.todo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.data.TodoEntity
import com.example.todo.databinding.FragmentMydayBinding
import com.example.todo.databinding.SingleColumnBinding
import com.example.todo.fragments.MydayFragment

class TodoRVAdapter: RecyclerView.Adapter<TodoRVAdapter.TodoViewHolder>() {

    private var DataList = emptyList<TodoEntity>()
    var _binding : SingleColumnBinding? = null
    val binding get() =  _binding!!

    class TodoViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        _binding = SingleColumnBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return TodoViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val currentItem = DataList[position]

        binding.checkboxColumn.text = currentItem.title

        //set onclick listener on text and checkbox
        binding.checkboxColumn.setOnCheckedChangeListener { compoundButton, b ->

        }
    }

    override fun getItemCount(): Int {
        return DataList.size
    }

    fun NotifyChanges(DataList:List<TodoEntity>){
        this.DataList = DataList
        notifyDataSetChanged()
    }
}