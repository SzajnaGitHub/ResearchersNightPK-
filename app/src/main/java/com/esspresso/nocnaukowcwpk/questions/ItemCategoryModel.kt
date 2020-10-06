package com.esspresso.nocnaukowcwpk.questions

import android.content.Context
import com.esspresso.nocnaukowcwpk.utils.CategoryResolver
import com.esspresso.nocnaukowcwpk.utils.recyclerview.RecyclerModel

data class ItemCategoryModel(
    private val category: String,
    val amount: Int
) : RecyclerModel {
    fun getIcon(context: Context) = CategoryResolver.resolveCategoryImage(category, context)
    override fun getId() = category
}
