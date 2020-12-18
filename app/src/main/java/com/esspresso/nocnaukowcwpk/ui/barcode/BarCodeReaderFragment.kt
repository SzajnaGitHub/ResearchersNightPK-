package com.esspresso.nocnaukowcwpk.ui.barcode

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.esspresso.db.userquestions.UserQuestionsDao
import com.esspresso.nocnaukowcwpk.R
import com.esspresso.nocnaukowcwpk.ui.BeaconCardActivity
import com.esspresso.nocnaukowcwpk.beacons.BeaconManager
import com.esspresso.nocnaukowcwpk.databinding.FragmentBarCodeReaderBinding
import com.esspresso.nocnaukowcwpk.di.QRCodeImageBitmap
import com.esspresso.nocnaukowcwpk.utils.PermissionUtility
import com.esspresso.nocnaukowcwpk.utils.postAction
import com.jakewharton.rxrelay3.Relay
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import javax.inject.Inject

@AndroidEntryPoint
class BarCodeReaderFragment : Fragment(), EasyPermissions.PermissionCallbacks {
    @Inject
    internal lateinit var cameraManager: CameraManager
    @Inject
    internal lateinit var beaconManager: BeaconManager
    @Inject
    @QRCodeImageBitmap
    internal lateinit var qrImageRelay: Relay<Bitmap>
    @Inject
    internal lateinit var questionsDao: UserQuestionsDao

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
        setupCamera()
    }

    private fun setupCamera() {
        if (PermissionUtility.hasCameraPermission(requireContext())) {
            binding.missingPermission = false
            postAction(1000){
                cameraManager.setupCamera(this, binding.viewFinder)
                subscribeToNewImageRelay()
            }
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
        PermissionUtility.requestCameraPermission(this)
    }


    private fun subscribeToNewImageRelay() {
        qrImageRelay.observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                binding.imageView.setImageBitmap(it)
            }.let(disposables::add)
    }

    private fun subscribeToBeaconModelSubject() {
        cameraManager.beaconModelSubject.observeOn(AndroidSchedulers.mainThread()).subscribe { model ->
            println("TEKST MODEL FINAL RECEIVED")
            questionsDao.getSingleQuestion(model.id.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({
                    println("TEKST POINTS ALREADY COLLECTED")
                    Toast.makeText(context, getString(R.string.text_points_already_collected), Toast.LENGTH_LONG).show()
                    postAction(2000) {
                        cameraManager.closeCurrentImage()
                        binding.imageView.setImageDrawable(null)
                    }
                }, {
                    println("TEKST ERROR ")
                    startActivity(BeaconCardActivity.createIntent(requireContext(), model.id, model.categoryId))
                })
                .let(disposables::add)
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

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        setupCamera()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).setRationale(R.string.permission_camera_settings).setTitle(R.string.permission_camera_header).build().show()
            R.string.rationale_ask
        } else {
            setupCamera()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    companion object {
        fun newInstance() = BarCodeReaderFragment()
    }
}
