package com.esspresso.nocnaukowcwpk.ultis

import com.esspresso.nocnaukowcwpk.ultis.recyclerview.RecyclerModel

fun <T : RecyclerModel> ArrayList<T>.itemIndex(id: String): Int {
    this.forEachIndexed { index, item ->
        if (item.getId() == id) return index
    }
    return -1
}