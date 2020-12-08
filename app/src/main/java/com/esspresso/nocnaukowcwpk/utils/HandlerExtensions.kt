package com.esspresso.nocnaukowcwpk.utils

import android.os.Handler
import android.os.Looper

fun postAction(delay: Long, action: () -> Unit) {
    Handler(Looper.getMainLooper()).postDelayed({
        action.invoke()
    }, delay)
}
