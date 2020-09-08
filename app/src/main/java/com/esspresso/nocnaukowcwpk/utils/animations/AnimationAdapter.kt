package com.esspresso.nocnaukowcwpk.utils.animations

import android.view.View
import android.view.animation.AnimationUtils
import androidx.databinding.BindingAdapter
import com.esspresso.nocnaukowcwpk.R

object AnimationAdapter {

    @JvmStatic
    @BindingAdapter("rotate")
    fun rotateAdapter(view: View, rotate: Boolean) {
        if (rotate) {
            view.startAnimation(AnimationUtils.loadAnimation(view.context, R.anim.rotate))
        } else {
            view.clearAnimation()
        }
    }
}
