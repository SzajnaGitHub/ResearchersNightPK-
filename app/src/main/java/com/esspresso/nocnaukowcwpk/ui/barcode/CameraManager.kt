package com.esspresso.nocnaukowcwpk.ui.barcode

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.util.Size
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.esspresso.nocnaukowcwpk.beacons.BeaconConfigModel
import com.esspresso.nocnaukowcwpk.di.QRCodeImageBitmap
import com.esspresso.nocnaukowcwpk.utils.toBitmap
import com.google.mlkit.vision.common.InputImage
import com.jakewharton.rxrelay3.Relay
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject

@SuppressLint("UnsafeExperimentalUsageError")
class CameraManager @Inject constructor(
    private val barCodeManager: BarCodeManager,
    @QRCodeImageBitmap private val qrImageRelay: Relay<Bitmap>,
    private val screenSize: Size,
    @ApplicationContext private val appContext: Context
) {

    private lateinit var cameraExecutor: ExecutorService
    val beaconModelSubject: BehaviorSubject<BeaconConfigModel> = BehaviorSubject.create()

    fun setupCamera(fragment: Fragment, view: PreviewView) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(fragment.requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider, fragment, view)
        }, ContextCompat.getMainExecutor(appContext))

        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    fun closeCurrentImage() = barCodeManager.clearImage()

    private fun takePhoto(imageCapture: ImageCapture, action: (ImageProxy) -> Unit) {
        imageCapture.takePicture(cameraExecutor, object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                action.invoke(image)
                image.close()
            }
            override fun onError(exception: ImageCaptureException) {}
        })
    }

    private fun bindPreview(cameraProvider: ProcessCameraProvider, context: Fragment, view: PreviewView) {
        val preview = Preview.Builder().build()
        val cameraSelector = getCameraSelector()
        val imageCapture = getImageCapture()
        val imageAnalysis = getImageAnalysis()
        setupImageAnalyzer(imageAnalysis, imageCapture)

        cameraProvider.bindToLifecycle(context, cameraSelector, preview, imageAnalysis, imageCapture)
        preview.setSurfaceProvider(view.surfaceProvider)
    }

    private fun getCameraSelector() = CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()
    private fun getImageCapture() = ImageCapture.Builder().setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
        .setTargetResolution(screenSize).build()

    private fun getImageAnalysis() = ImageAnalysis.Builder()
        .setTargetResolution(Size(1280, 1024))
        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
        .build()

    private fun setupImageAnalyzer(imageAnalysis: ImageAnalysis, imageCapture: ImageCapture) {
        imageAnalysis.setAnalyzer(cameraExecutor, { imageProxy ->
            imageProxy.image?.let { image ->
                takePhoto(imageCapture) { outputProxy ->
                    val outputImage = outputProxy.image?.toBitmap() ?: return@takePhoto
                    barCodeManager.scanImage(InputImage.fromMediaImage(image, imageProxy.imageInfo.rotationDegrees), imageProxy) {
                        beaconModelSubject.onNext(it)
                        //qrImageRelay.accept(outputImage)
                    }
                }
            }
        })
    }

    fun dispose() {
        if (::cameraExecutor.isInitialized && !cameraExecutor.isShutdown) cameraExecutor.shutdown()
    }
}
