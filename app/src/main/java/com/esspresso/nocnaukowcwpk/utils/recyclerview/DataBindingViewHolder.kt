package com.esspresso.nocnaukowcwpk.utils.recyclerview

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class DataBindingViewHolder<out Binding : ViewDataBinding>(val binding: Binding): RecyclerView.ViewHolder(binding.root)
