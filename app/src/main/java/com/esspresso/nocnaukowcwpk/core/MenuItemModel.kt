package com.esspresso.nocnaukowcwpk.core

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

data class MenuItemModel(
    @DrawableRes private val backgroundDrawable: Int,
    @DrawableRes private val iconDrawable: Int,
    val title: String
) {
    fun getBackgroundDrawable(context: Context) = ContextCompat.getDrawable(context, backgroundDrawable)
    fun getIconDrawable(context: Context) = ContextCompat.getDrawable(context, iconDrawable)
}