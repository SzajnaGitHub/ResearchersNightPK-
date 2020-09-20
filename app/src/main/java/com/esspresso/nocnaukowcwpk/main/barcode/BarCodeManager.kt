package com.esspresso.nocnaukowcwpk.main.barcode

import android.annotation.SuppressLint
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import javax.inject.Inject

class BarCodeManager @Inject constructor() {
    private val scanner = BarcodeScanning.getClient(BarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_QR_CODE).build())

    fun scanImage(input: InputImage, image: ImageProxy, action: () -> Unit) {
        scanner.process(input).addOnSuccessListener {
            decodeBarCodes(it, image, action)
        }
            .addOnFailureListener {
                image.close()
            }
    }

    @SuppressLint("UnsafeExperimentalUsageError")
    private fun decodeBarCodes(barCodes: List<Barcode>, image: ImageProxy, action: () -> Unit) {
        if (barCodes.isEmpty()) {
            image.close()
        }
        barCodes.forEach {
            when (it.valueType) {
                Barcode.TYPE_TEXT -> {
                    action.invoke()
                    println("TEKST TYPE TEXT VALUE ${it.displayValue}")
                }
                else -> image.close()
            }
        }
    }
}
