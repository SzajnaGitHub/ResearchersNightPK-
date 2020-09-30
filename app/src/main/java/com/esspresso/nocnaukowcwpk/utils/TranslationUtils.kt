package com.esspresso.nocnaukowcwpk.utils

import android.content.Context

fun String.translateFromId(context: Context): String {
    val id = context.resources.getIdentifier(this, "string", context.packageName)
    return if (id != 0) context.resources.getString(id) else "????"
}
