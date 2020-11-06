package com.esspresso.nocnaukowcwpk.main.barcode

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.esspresso.nocnaukowcwpk.R
import com.esspresso.nocnaukowcwpk.beacons.BeaconCardActivity
import com.esspresso.nocnaukowcwpk.beacons.BeaconManager
import com.esspresso.nocnaukowcwpk.databinding.FragmentBarCodeReaderBinding
import com.esspresso.nocnaukowcwpk.di.QRCodeImageBitmap
import com.esspresso.nocnaukowcwpk.status.PermissionManager
import com.esspresso.nocnaukowcwpk.utils.DialogActivity
import com.jakewharton.rxrelay3.Relay
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

@AndroidEntryPoint
class BarCodeReaderFragment : Fragment() {
    @Inject
    internal lateinit var cameraManager: CameraManager
    @Inject
    internal lateinit var permissionManager: PermissionManager
    @Inject
    internal lateinit var beaconManager: BeaconManager
    @Inject
    @QRCodeImageBitmap
    internal lateinit var qrImageRelay: Relay<Bitmap>

    private lateinit var binding: FragmentBarCodeReaderBinding
    private val disposables = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bar_code_reader, container, false)
        setupBinding()
        subscribeToBeaconModelSubject()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        Handler().postDelayed({ setupCamera() }, 1000)
    }

    private fun setupCamera() {
        if (permissionManager.checkPermissionGranted(PermissionManager.CAMERA_PERMISSION)) {
            binding.missingPermission = false
            cameraManager.setupCamera(this, binding.viewFinder)
            subscribeToNewImageRelay()
        } else {
            binding.missingPermission = true
            checkCameraPermission()
        }
    }

    private fun setupBinding() {
        binding.missingPermissionImage.setOnClickListener {
            setupCamera()
        }
        binding.missingPermissionText.setOnClickListener {
            setupCamera()
        }
    }

    private fun checkCameraPermission() {
        permissionManager.requestPermission(PermissionManager.CAMERA_PERMISSION).subscribe { granted ->
            if (granted) {
                setupCamera()
            } else {
                permissionManager.shouldShowPermission(requireActivity(), PermissionManager.CAMERA_PERMISSION).subscribe { canAskAgain ->
                    if (!canAskAgain) startActivity(DialogActivity.createPermissionIntent(requireContext(), PermissionManager.CAMERA_PERMISSION, ::openSettings))
                }.let(disposables::add)
            }
        }.let(disposables::add)
    }

    private fun openSettings() {
        startActivityForResult(permissionManager.getApplicationSettingsIntent(requireContext()), SETTINGS_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SETTINGS_REQUEST_CODE && !permissionManager.checkPermissionGranted(PermissionManager.CAMERA_PERMISSION)) {
            Handler().postDelayed({ checkCameraPermission() }, 200)
        } else {
            setupCamera()
        }
    }

    private fun subscribeToNewImageRelay() {
        qrImageRelay.observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                binding.imageView.setImageBitmap(it)
            }.let(disposables::add)
    }

    private fun subscribeToBeaconModelSubject() {
        cameraManager.beaconModelSubject.observeOn(AndroidSchedulers.mainThread()).subscribe { model ->
            if (beaconManager.checkItemAnswered(model.id.toString())) {
                Toast.makeText(context, getString(R.string.text_points_already_collected), Toast.LENGTH_LONG).show()
                Handler().postDelayed({
                    cameraManager.closeCurrentImage()
                    binding.imageView.setImageDrawable(null)
                }, 2000)
            } else {
                startActivity(BeaconCardActivity.createIntent(requireContext(), model.id, model.categoryId))
            }
        }.let(disposables::add)
    }

    override fun onResume() {
        super.onResume()
        cameraManager.closeCurrentImage()
    }

    override fun onStop() {
        cameraManager.dispose()
        super.onStop()
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }

    companion object {
        private const val SETTINGS_REQUEST_CODE = 15
        fun newInstance() = BarCodeReaderFragment()
    }
}
