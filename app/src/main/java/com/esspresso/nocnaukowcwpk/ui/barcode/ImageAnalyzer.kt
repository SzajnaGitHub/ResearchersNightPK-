package com.esspresso.nocnaukowcwpk.ui.barcode

import android.annotation.SuppressLint
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage

class ImageAnalyzer : ImageAnalysis.Analyzer {

    @SuppressLint("UnsafeExperimentalUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        imageProxy.image?.let { image ->
            InputImage.fromMediaImage(image,imageProxy.imageInfo.rotationDegrees)
            println("TEKST PIES PIES LOLOLOLOLOLOLOLOOLLO")
        }
    }

}
