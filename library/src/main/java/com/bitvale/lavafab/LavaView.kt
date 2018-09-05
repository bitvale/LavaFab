package com.bitvale.lavafab

import android.animation.FloatEvaluator
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PointF

/**
 * Created by Alexander Kolpakov on 27.08.2018
 */
interface LavaView {

    val center: PointF
    val radius: Float
    var currentRadius: Float
    var icon: Bitmap?
    val evaluator: FloatEvaluator
    var listener: LavaOnClickListener?

    fun draw(canvas: Canvas?)
    fun calculatePoints(animatedFraction: Float)
    fun handleOnClick(x: Float, y: Float)
    fun setOnClickListener(listener: LavaOnClickListener?)
    fun setOnClickListener(listener: () -> Unit)

    interface LavaOnClickListener {
        fun onClick()
    }
}