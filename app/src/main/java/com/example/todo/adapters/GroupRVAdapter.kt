package com.example.todo.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.data.GroupEntity
import com.example.todo.databinding.GroupRowBinding

class GroupRVAdapter(val rvInterface: groupRVInterface):RecyclerView.Adapter<GroupRVAdapter.GroupViewHolder>() {

    private var DataList = emptyList<GroupEntity>()
    var _binding : GroupRowBinding? = null
    val binding get() =  _binding!!
    private lateinit var context: Context

    inner class GroupViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        init {
            itemView.setOnClickListener { 
                val position = adapterPosition
                val data = DataList[position]
                rvInterface.onGroupClick(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        _binding = GroupRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        context = parent.context
        return GroupViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val groupEntity: GroupEntity = DataList[position]

        binding.groupName.text = groupEntity.groupName
        binding.groupColor.setColorFilter(Color.parseColor(groupEntity.accent_colour))
    }

    override fun getItemCount(): Int {
        return DataList.size
    }

    fun NotifyGroupChanges(groupList:List<GroupEntity>){
        this.DataList = groupList
        notifyDataSetChanged()
    }

    fun NotifyGroupAdded(position: Int){
        notifyItemInserted(0)
    }
    
    interface groupRVInterface{
        fun onGroupClick(data:GroupEntity)
    }
}