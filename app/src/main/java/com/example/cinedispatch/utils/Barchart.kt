package com.example.cinedispatch.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.random.Random

class Barchart @JvmOverloads constructor(context: Context,
                                         attrs: AttributeSet? = null,
                                         defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr){

    private val backgroundPaint = Paint()
    private val barPaint = Paint()
    private var bins: IntArray = IntArray(10) // Array to hold bin counts
    private var maxBinHeight: Float = 0f
    private var start=0
    private var end=0

    init {
        backgroundPaint.color = Color.parseColor("#2E4A59") // Background color for each bar
        backgroundPaint.style = Paint.Style.FILL

        barPaint.color = Color.parseColor("#FFB703") // Bar color
        barPaint.style = Paint.Style.FILL
    }

    fun setData(voteAverage:Float){
        val binCount=10
        val random=Random(System.currentTimeMillis())

        bins= IntArray(binCount)
        val index=(voteAverage-1).toInt().coerceIn(0, binCount - 1)
        bins[index]=voteAverage.toInt()

        for (i in bins.indices){
            if(i==index){
                bins[i]=voteAverage.toInt()

            }else{
                if(i==index-1 || i==index+1 ){
                     start=(voteAverage - 2).toInt().coerceAtLeast(1)
                    end=(voteAverage-1).toInt().coerceAtLeast(start+1)
                    bins[i]=random.nextInt(start,end)
                }else{
                    start=1
                    end = voteAverage.toInt().coerceAtLeast(start + 1)
                    bins[i]=random.nextInt(start,end)
                }
                bins[i] = if (end > start) random.nextInt(start, end) else start

            }
        }
        maxBinHeight = bins.maxOrNull()?.toFloat() ?: 0f
        invalidate() // Request a redraw
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width
        val height = height
        val barCount = bins.size
        val barWidth = width / (barCount * 1.8).toInt() // Increase the width of the bars
        val maxBarHeight = height * 0.8f // Maximum height for filled bars (data)
        val barSpacing = barWidth / 1.5f// Reduce the space between bars
        val cornerRadius = 30f // Radius for rounded corners

        for (i in bins.indices) {
            val left = (barSpacing * i * 2)
            val right = left + barWidth
            val top = 0f // Top position of the background bar (start from the top)
            val filledTop = height - (bins[i].toFloat() / maxBinHeight * maxBarHeight)

            // Draw the background (unfilled) part of the bar (same for all bars)
            canvas.drawRoundRect(left, top, right, height.toFloat(), cornerRadius, cornerRadius, backgroundPaint)

            // Draw the filled part of the bar (varies based on data)
            canvas.drawRoundRect(left, filledTop, right, height.toFloat(), cornerRadius, cornerRadius, barPaint)
        }
    }

}