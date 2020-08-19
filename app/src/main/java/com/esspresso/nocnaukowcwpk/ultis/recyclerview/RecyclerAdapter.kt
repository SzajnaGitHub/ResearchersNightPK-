package com.esspresso.nocnaukowcwpk.ultis.recyclerview

import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.esspresso.nocnaukowcwpk.expiration.ExpirationHandler
import com.esspresso.nocnaukowcwpk.ultis.itemIndex
import java.util.concurrent.TimeUnit

class RecyclerAdapter<T : RecyclerModel>(
    private var items: ArrayList<T>,
    private val itemLayout: Int,
    private val variableId: Int,
    private val clickHandler: ((T) -> Unit)? = null
) : RecyclerView.Adapter<DataBindingViewHolder<ViewDataBinding>>() {

    private var expirationHandler = ExpirationHandler()

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder<ViewDataBinding> {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(parent.context), itemLayout, parent, false)
        return DataBindingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataBindingViewHolder<ViewDataBinding>, position: Int) {
        val item = items[position]
        val root = holder.binding.root
        holder.binding.setVariable(variableId, item)
        root.setOnClickListener {
            clickHandler?.invoke(item)
        }
    }

    fun updateData(newItems: ArrayList<T>) {
        items = newItems
        notifyDataSetChanged()
    }

    private fun addItem(item: T) {
        items.add(item)
        notifyItemInserted(items.lastIndex)
        expirationHandler.setExpirationTimer(item.getId())
    }

    private fun removeItem(item: T) {
        val index = items.indexOf(item)
        items.remove(item)
        notifyItemRemoved(index)
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
        Handler().postDelayed({ deleteExpiredItems() }, 10000)
        val itemIndex = items.itemIndex(item.getId())
        if (itemIndex != -1) {
            updateItem(itemIndex, item)
        } else {
            addItem(item)
        }
    }

}
