package com.esspresso.nocnaukowcwpk.utils.recyclerview

import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.esspresso.nocnaukowcwpk.expiration.ExpirationHandler
import com.esspresso.nocnaukowcwpk.utils.itemIndex

class RecyclerAdapter<T : RecyclerModel>(
    var items: ArrayList<T>,
    private val itemLayout: Int,
    private val variableId: Int,
    private val clickHandler: ((T) -> Unit)? = null,
    private val onListEmptyAction: (() -> Unit)? = null,
    private val onListNoLongerEmptyAction: (() -> Unit)? = null
) : RecyclerView.Adapter<DataBindingViewHolder<ViewDataBinding>>() {

    private var expirationHandler = ExpirationHandler()

    override fun getItemCount() = items.size
    private fun isListEmpty() = itemCount == 0
    var currentItemPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder<ViewDataBinding> {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(parent.context), itemLayout, parent, false)
        return DataBindingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataBindingViewHolder<ViewDataBinding>, position: Int) {
        val item = items[position]
        val root = holder.binding.root
        println("TEKST LOL $position $item")
        holder.binding.setVariable(variableId, item)
        root.setOnClickListener {
            currentItemPosition = position
            clickHandler?.invoke(item)
        }
    }

    fun updateData(newItems: ArrayList<T>) {
        items = newItems
        notifyDataSetChanged()
    }

    fun removeAllItems() {
        items.clear()
        notifyDataSetChanged()
        if (isListEmpty()) onListEmptyAction?.invoke()
    }

    private fun addItem(item: T) {
        if (isListEmpty()) onListNoLongerEmptyAction?.invoke()
        items.add(item)
        notifyItemInserted(items.lastIndex)
        expirationHandler.setExpirationTimer(item.getId())
    }

    fun removeItem(item: T) {
        val index = items.indexOf(item)
        items.remove(item)
        notifyItemRemoved(index)
        if (isListEmpty()) onListEmptyAction?.invoke()
    }

    private fun updateItem(itemIndex: Int, item: T) {
        items[itemIndex] = item
        notifyItemChanged(itemIndex)
        expirationHandler.setExpirationTimer(item.getId())
    }

    private fun deleteExpiredItems() {
        expirationHandler.deleteExpiredItems { key -> removeItem(items[items.itemIndex(key)]) }
    }

    fun addOrUpdateItem(item: T) {
        println("TEKST ADD ITEM $item")
        Handler().postDelayed({ deleteExpiredItems() }, 10000)
        val itemIndex = items.itemIndex(item.getId())
        if (itemIndex != -1) {
            updateItem(itemIndex, item)
        } else {
            addItem(item)
            //    onItemAdditionAction?.invoke()
        }
    }

}
