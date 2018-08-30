package com.bitvale.lavafab

import android.content.Context
import android.util.TypedValue
import androidx.annotation.ColorInt
import androidx.annotation.DimenRes

/**
 * Created by Alexander Kolpakov on 27.08.2018
 */

@ColorInt
fun Context.getThemeAccentColor(): Int {
    val value = TypedValue()
    theme.resolveAttribute(R.attr.colorAccent, value, true)
    return value.data
}

fun Context.getFloatDimen(@DimenRes res: Int) = resources.getDimension(res)