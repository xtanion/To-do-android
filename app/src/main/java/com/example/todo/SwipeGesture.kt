package com.example.todo

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

abstract class SwipeGesture: ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
    //for swipe up-down
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }


}