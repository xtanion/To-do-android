package com.example.todo.utils

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R

abstract class SwipeGesture(context:Context): ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
    //for swipe up-down
    val cntxt:Context = context
    private val deleteIcon = ContextCompat.getDrawable(context, R.drawable.ic_delete_icon)
    private val whiteColor = ContextCompat.getColor(context, R.color.muted_white)
    private val intrinsicWidth = deleteIcon?.intrinsicWidth!!
    private val intrinsicHeight = deleteIcon?.intrinsicHeight!!
    private val background= ColorDrawable()
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        val v: Vibrator? = getSystemService(cntxt, Vibrator::class.java)
        v?.let {
            if (Build.VERSION.SDK_INT>=26){
                it.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
            }else{
                it.vibrate(100)
            }
        }
        return false
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom - itemView.top

        background.apply {
            color = Color.parseColor("#E03737")
            setBounds(
                itemView.right + dX.toInt(),
                itemView.top,
                itemView.right,
                itemView.bottom
            )
            draw(c)
        }
        val deleteIconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
        val deleteIconMargin = (itemHeight - intrinsicHeight) / 2
        val deleteIconLeft = itemView.right - deleteIconMargin - intrinsicWidth
        val deleteIconRight = itemView.right - deleteIconMargin
        val deleteIconBottom = deleteIconTop + intrinsicHeight

        deleteIcon?.setTintList(ColorStateList.valueOf(whiteColor))
        deleteIcon?.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
        deleteIcon?.draw(c)
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

}