package com.bitvale.lavafab

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import android.widget.ImageView


object BitmapUtil {

    fun getBitmapFromImageView(imageView: ImageView): Bitmap? {
        val drawable = imageView.drawable
        return if (drawable == null) null
        else when (drawable::class) {
            VectorDrawable::class -> getBitmapFromVector(drawable as VectorDrawable)
            BitmapDrawable::class -> (drawable as BitmapDrawable).bitmap
            else -> throw ClassCastException(drawable.toString() + " is not supported!")
        }
    }

    fun getBitmapFromDrawable(drawable: Drawable?): Bitmap? {
        return if (drawable == null) null
        else when (drawable::class) {
            VectorDrawable::class -> getBitmapFromVector(drawable as VectorDrawable)
            BitmapDrawable::class -> (drawable as BitmapDrawable).bitmap
            else -> throw ClassCastException(drawable.toString() + " is not supported!")
        }
    }

    private fun getBitmapFromVector(vectorDrawable: VectorDrawable): Bitmap {
        val bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth,
                vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        vectorDrawable.draw(canvas)
        return bitmap
    }

}