package com.esspresso.nocnaukowcwpk.utils

import android.content.Context
import androidx.core.content.ContextCompat
import com.esspresso.nocnaukowcwpk.R

object CategoryResolver {

    @JvmStatic
    fun resolveCategoryImage(category: String, context: Context) = when (category) {
        CATEGORY_TECHNOLOGY -> ContextCompat.getDrawable(context, R.drawable.ic_technology)
        CATEGORY_SCIENCE -> ContextCompat.getDrawable(context, R.drawable.ic_science)
        CATEGORY_MECHANICS -> ContextCompat.getDrawable(context, R.drawable.ic_mechanics)
        CATEGORY_GAMES -> ContextCompat.getDrawable(context, R.drawable.ic_games)
        CATEGORY_VEHICLES -> ContextCompat.getDrawable(context, R.drawable.ic_car)
        CATEGORY_BUILDINGS -> ContextCompat.getDrawable(context, R.drawable.ic_building)
        else -> null
    }

    const val CATEGORY_TECHNOLOGY = "category_technology"
    const val CATEGORY_SCIENCE = "category_science"
    const val CATEGORY_MECHANICS = "category_mechanics"
    const val CATEGORY_GAMES = "category_games"
    const val CATEGORY_VEHICLES = "category_vehicles"
    const val CATEGORY_BUILDINGS = "category_buildings"
}
