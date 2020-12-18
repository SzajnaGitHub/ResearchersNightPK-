package com.esspresso.nocnaukowcwpk.ui.barcode

import android.annotation.SuppressLint
import androidx.camera.core.ImageProxy
import com.esspresso.nocnaukowcwpk.beacons.BeaconConfigModel
import com.esspresso.nocnaukowcwpk.config.RemoteConfigManager
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import javax.inject.Inject

class BarCodeManager @Inject constructor(private val remoteConfig: RemoteConfigManager) {
    private val scanner = BarcodeScanning.getClient(BarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_QR_CODE).build())
    private var currentImage: ImageProxy? = null

    fun scanImage(input: InputImage, image: ImageProxy, action: (BeaconConfigModel) -> Unit) {
        scanner.process(input).addOnSuccessListener {
            decodeBarCodes(it, image, action)
        }
            .addOnFailureListener {
                image.close()
            }
    }

    @SuppressLint("UnsafeExperimentalUsageError")
    private fun decodeBarCodes(barCodes: List<Barcode>, image: ImageProxy, action: (BeaconConfigModel) -> Unit) {
        if (barCodes.isEmpty()) {
            image.close()
        }
        barCodes.forEach {
            when (it.valueType) {
                Barcode.TYPE_TEXT -> {
                    println("TEKST ${remoteConfig.getBeacons()[1].id}")
                    val model = remoteConfig.getBeacons().find { beacon -> beacon.id.toString() == it.displayValue } ?: return

                    println("TEKST TYPE TEXT VALUE ${it.displayValue} $model")
                    action.invoke(model)
                    currentImage = image
                }
                else -> image.close()
            }
        }
    }

    fun clearImage() {
        currentImage?.close()
        currentImage = null
    }
}
