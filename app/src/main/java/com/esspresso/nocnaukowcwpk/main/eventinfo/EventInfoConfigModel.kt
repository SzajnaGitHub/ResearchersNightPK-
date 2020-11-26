package com.esspresso.nocnaukowcwpk.main.eventinfo

import android.content.Context
import androidx.annotation.Keep
import com.esspresso.nocnaukowcwpk.utils.CategoryResolver
import com.esspresso.nocnaukowcwpk.utils.recyclerview.RecyclerModel
import com.squareup.moshi.JsonClass
import java.util.*

@Keep
@JsonClass(generateAdapter = true)
data class EventInfoConfigModel(
    val titlePl: String,
    val descriptionPl: String,
    val titleEng: String,
    val descriptionEng: String,
    val room: String,
    val startHour: String,
    val endHour: String,
    val categoryId: String
)

data class EventInfoItemModel(
    val title: String,
    val description: String,
    val room: String,
    val time: String,
    val categoryId: String

): RecyclerModel {
    fun getIcon(context: Context) = CategoryResolver.resolveCategoryImage(categoryId, context)

    companion object {
        fun create(configModel: EventInfoConfigModel): EventInfoItemModel {
            val isPolish = Locale.getDefault().language == "pl"
            return EventInfoItemModel(
                title = if (isPolish) configModel.titlePl else configModel.titleEng,
                description = if (isPolish) configModel.descriptionPl else configModel.descriptionEng,
                room = configModel.room,
                time = "${configModel.startHour} - ${configModel.endHour}",
                categoryId = configModel.categoryId
            )
        }
    }

    override fun getId() = title
}
