package com.esspresso.nocnaukowcwpk.utils

import android.app.Application
import android.content.Context
import android.util.Size
import kotlin.math.roundToInt


fun getDeviceSizeDp(context: Application): Size {
    val metrics = context.resources.displayMetrics
    return Size(toDp(context, metrics.widthPixels), toDp(context, metrics.heightPixels))
}

fun toDp(context: Context, pixel: Int) = (pixel / context.resources.displayMetrics.density).roundToInt()
