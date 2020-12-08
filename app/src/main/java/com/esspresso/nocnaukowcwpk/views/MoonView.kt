package com.esspresso.nocnaukowcwpk.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Region
import android.util.AttributeSet
import android.util.Size
import android.view.View
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnRepeat
import androidx.core.content.ContextCompat
import com.esspresso.nocnaukowcwpk.R

class MoonView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val path = Path()
    private val moonPaint = Paint()
    private var isMoon = false
    private var circleX = CIRCLE_START_X
    private var circleY = CIRCLE_START_Y
    private var screenSize = Size(0, 0)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawYellowCircle(isMoon)
    }

    private fun Canvas.drawYellowCircle(isMoon: Boolean) {
        if (isMoon) {
            path.reset()
            save()
            path.addCircle(circleX - MOON_CLIP_FACTOR, circleY - MOON_CLIP_FACTOR, CLIP_CIRCLE_SIZE, Path.Direction.CW)
            clipPath(path, Region.Op.DIFFERENCE)
            drawCircle(circleX, circleY, CIRCLE_SIZE, moonPaint)
            restore()
        } else {
            drawCircle(circleX, circleY, CIRCLE_SIZE, moonPaint)
        }
    }

    fun startAnimation(onAnimationEnd: (() -> Unit)? = null) {
        ValueAnimator.ofFloat(circleY, screenSize.height.toFloat()).apply {
            duration = 1000
            startDelay = 300
            repeatCount = 1
            repeatMode = ValueAnimator.REVERSE
            doOnRepeat {
                isMoon = true
                circleX = screenSize.width - CIRCLE_START_X
            }
            addUpdateListener {
                circleY = it.animatedValue as Float
                postInvalidate()
            }
            doOnEnd { onAnimationEnd?.invoke() }
        }.start()
    }

    init {
        moonPaint.style = Paint.Style.FILL
        moonPaint.color = ContextCompat.getColor(context, R.color.moon_yellow)
        moonPaint.isAntiAlias = true
        context.resources.displayMetrics.let {
            screenSize = Size(it.widthPixels, it.heightPixels)
        }
    }

    companion object {
        private const val CIRCLE_SIZE = 120f
        private const val CLIP_CIRCLE_SIZE = 110f
        private const val CIRCLE_START_X = 240F
        private const val CIRCLE_START_Y = 240F
        private const val MOON_CLIP_FACTOR = 48F

    }
}
