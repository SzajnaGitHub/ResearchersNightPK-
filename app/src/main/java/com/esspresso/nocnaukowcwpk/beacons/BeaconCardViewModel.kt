package com.esspresso.nocnaukowcwpk.beacons

import android.content.Context
import com.esspresso.nocnaukowcwpk.questions.QuestionModel
import com.esspresso.nocnaukowcwpk.utils.CategoryResolver
import com.esspresso.nocnaukowcwpk.utils.translateFromId


data class BeaconCardViewModel(
    val beaconId: BeaconId,
    val categoryId: String?,
    val questionModel: QuestionModel? = null,
) {
    fun getIcon(context: Context) = categoryId?.let { CategoryResolver.resolveCategoryImage(it, context) }
    fun getCategoryText(context: Context) = categoryId?.translateFromId(context) ?: ""
}
