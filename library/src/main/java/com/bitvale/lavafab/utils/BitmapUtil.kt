package com.bitvale.lavafab.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat


object BitmapUtil {
    fun getBitmapFromDrawable(drawable: Drawable?): Bitmap? {
        return if (drawable == null) null
        else when (drawable::class) {
            VectorDrawableCompat::class -> getBitmapFromVector(drawable as VectorDrawableCompat)
            BitmapDrawable::class -> (drawable as BitmapDrawable).bitmap
            else -> throw ClassCastException(drawable.toString() + " is not supported!")
        }
    }

    private fun getBitmapFromVector(vectorDrawable: VectorDrawableCompat): Bitmap {
        val bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth,
                vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        vectorDrawable.draw(canvas)
        return bitmap
    }
}