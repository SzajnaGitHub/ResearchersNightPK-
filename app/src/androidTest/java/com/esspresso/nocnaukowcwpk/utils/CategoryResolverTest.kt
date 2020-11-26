package com.esspresso.nocnaukowcwpk.utils

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.esspresso.nocnaukowcwpk.R
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CategoryResolverTest {
    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun categoryResolverReturnsTechnologyIconForTechnologyCategory() {
        val resolvedIconDrawable = CategoryResolver.resolveCategoryImage(CategoryResolver.CATEGORY_TECHNOLOGY, context)
        val expectedResultIconDrawable = ContextCompat.getDrawable(context, R.drawable.ic_technology)
        assertThat(resolvedIconDrawable?.constantState).isEqualTo(expectedResultIconDrawable?.constantState)
    }

    @Test
    fun categoryResolverReturnsNullForWrongCategory() {
        val resolvedIconDrawable = CategoryResolver.resolveCategoryImage("Wrong Category", context)
        assertThat(resolvedIconDrawable).isEqualTo(null)
    }

    @Test
    fun categoryResolverWillNotReturnsTechnologyIconForMechanicsCategory() {
        val resolvedIconDrawable = CategoryResolver.resolveCategoryImage(CategoryResolver.CATEGORY_MECHANICS, context)
        val expectedResultIconDrawable = ContextCompat.getDrawable(context, R.drawable.ic_technology)
        assertThat(resolvedIconDrawable?.constantState).isNotEqualTo(expectedResultIconDrawable?.constantState)
    }
}




