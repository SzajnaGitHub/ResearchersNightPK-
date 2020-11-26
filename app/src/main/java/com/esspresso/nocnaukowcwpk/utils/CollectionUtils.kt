package com.esspresso.nocnaukowcwpk.utils

import com.esspresso.nocnaukowcwpk.utils.recyclerview.RecyclerModel

fun <T : RecyclerModel> ArrayList<T>.itemIndex(id: String): Int {
    this.forEachIndexed { index, item ->
        if (item.getId() == id) return index
    }
    return -1
}