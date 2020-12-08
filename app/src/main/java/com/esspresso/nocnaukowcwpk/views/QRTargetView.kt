package com.esspresso.nocnaukowcwpk.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class QRTargetView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val paint = Paint()
    private val leftRect = Rect(0, 0, 200, 200)
    private val rightRect = Rect(0, 0, 200, 200)
    private val clipPath = Path()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawLeftTopAndRightBottomSquare()
        canvas.drawRightTopAndLeftBottomSquare()
    }

    private fun Canvas.drawRightTopAndLeftBottomSquare() {
        save()
        clipPath.addRect(
            width - 200f,
            width * 0.03f,
            (width * 0.97f),
            (height * 0.2f), Path.Direction.CW
        )

        clipPath(clipPath, Region.Op.DIFFERENCE)

        drawRect(rightRect.apply {
            left = width - 200
            top = 0
            right = width
            bottom = 200
        }, paint)


        rotate(180f, width / 2f, height / 2f)
        clipPath(clipPath, Region.Op.DIFFERENCE)

        drawRect(rightRect.apply {
            left = width - 200
            top = 0
            right = width
            bottom = 200
        }, paint)

        restore()
    }

    private fun Canvas.drawLeftTopAndRightBottomSquare() {
        save()
        clipPath.addRect(
            width * 0.03f,
            width * 0.03f,
            (width * 0.2f),
            (height * 0.2f), Path.Direction.CW
        )

        clipPath(clipPath, Region.Op.DIFFERENCE)

        drawRect(leftRect.apply {
            right = (width * 0.2).toInt()
            bottom = (width * 0.2).toInt()
        }, paint)

        rotate(180f, width / 2f, height / 2f)

        clipPath(clipPath, Region.Op.DIFFERENCE)

        drawRect(leftRect.apply {
            right = (width * 0.2).toInt()
            bottom = (width * 0.2).toInt()
        }, paint)

        restore()
    }

    init {
        paint.color = Color.WHITE
    }
}
