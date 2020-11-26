package com.esspresso.nocnaukowcwpk.main.barcode

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
        canvas.save()
        drawTopSquares(canvas)

        canvas.save()
        clipPath.addRect(
            width - 200f,
            width * 0.03f,
            (width * 0.97f),
            (height * 0.2f), Path.Direction.CW
        )

        canvas.clipPath(clipPath, Region.Op.DIFFERENCE)

        canvas.drawRect(rightRect.apply {
            left = width - 200
            top = 0
            right = width
            bottom = 200
        }, paint)


        canvas.rotate(180f, width / 2f, height / 2f)
        canvas.clipPath(clipPath, Region.Op.DIFFERENCE)

        canvas.drawRect(rightRect.apply {
            left = width - 200
            top = 0
            right = width
            bottom = 200
        }, paint)


        canvas.restore()

    }

    private fun drawTopSquares(canvas: Canvas) {
        canvas.save()
        clipPath.addRect(
            width * 0.03f,
            width * 0.03f,
            (width * 0.2f),
            (height * 0.2f), Path.Direction.CW
        )

        canvas.clipPath(clipPath, Region.Op.DIFFERENCE)

        canvas.drawRect(leftRect.apply {
            right = (width * 0.2).toInt()
            bottom = (width * 0.2).toInt()
        }, paint)

        canvas.rotate(180f, width / 2f, height / 2f)


        canvas.clipPath(clipPath, Region.Op.DIFFERENCE)


        canvas.drawRect(leftRect.apply {
            right = (width * 0.2).toInt()
            bottom = (width * 0.2).toInt()
        }, paint)
        canvas.restore()
    }


    init {
        paint.color = Color.WHITE
    }
}
