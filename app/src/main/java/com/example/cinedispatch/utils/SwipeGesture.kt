package com.example.cinedispatch.utils

import android.R
import android.content.Context
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator


abstract class SwipeGesture(context:Context):ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
    val deleteColor=ContextCompat.getColor(context,R.color.holo_red_dark)
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder,
    ): Boolean {
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
        RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            .addSwipeLeftBackgroundColor(deleteColor)
            .addSwipeLeftActionIcon(com.example.cinedispatch.R.drawable.binicon)
            .addSwipeRightActionIcon(com.example.cinedispatch.R.drawable.binicon)
            .addSwipeRightBackgroundColor(deleteColor)
            .create()
            .decorate()
        super.onChildDraw(
            c!!, recyclerView!!,
            viewHolder!!, dX, dY, actionState, isCurrentlyActive
        )
    }
}