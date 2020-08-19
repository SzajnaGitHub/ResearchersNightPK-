package com.esspresso.nocnaukowcwpk.ultis.recyclerview

import androidx.databinding.BindingAdapter
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView

object RecyclerViewBindingAdapter {

    @JvmStatic
    @BindingAdapter("items", "itemLayout", "variableName", "clickHandler", requireAll = false)
    fun <T : RecyclerModel> bindRecyclerViewAdapter(recyclerView: RecyclerView, items: ArrayList<T>?, itemLayout: Int, variableName: String, clickHandler: ((T) -> Unit)? = null) {
        if (recyclerView.adapter == null) {
            val variableId = BR::class.java.getDeclaredField(variableName).getInt(null)
            items?.let { recyclerView.adapter = RecyclerAdapter(items, itemLayout, variableId, clickHandler) }
            recyclerView.scheduleLayoutAnimation()
        } else {
            val adapter = (recyclerView.adapter as RecyclerAdapter<T>)
            items?.let {
                it.forEach { item ->
                    adapter.addOrUpdateItem(item)
                }
            }
        }
    }
}
