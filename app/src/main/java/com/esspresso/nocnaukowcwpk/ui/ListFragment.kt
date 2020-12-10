package com.esspresso.nocnaukowcwpk.ui

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.esspresso.nocnaukowcwpk.R
import com.esspresso.nocnaukowcwpk.beacons.BeaconCardActivity
import com.esspresso.nocnaukowcwpk.beacons.BeaconManager
import com.esspresso.nocnaukowcwpk.beacons.BeaconModel
import com.esspresso.nocnaukowcwpk.beacons.BeaconService
import com.esspresso.nocnaukowcwpk.config.RemoteConfigManager
import com.esspresso.nocnaukowcwpk.databinding.FragmentListBinding
import com.esspresso.nocnaukowcwpk.status.BluetoothBroadcastReceiver
import com.esspresso.nocnaukowcwpk.status.LocationBroadCastReceiver
import com.esspresso.nocnaukowcwpk.status.StatusManager
import com.esspresso.nocnaukowcwpk.utils.PermissionUtility
import com.esspresso.nocnaukowcwpk.utils.postAction
import com.esspresso.nocnaukowcwpk.utils.recyclerview.RecyclerAdapter
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import javax.inject.Inject

@AndroidEntryPoint
class ListFragment : Fragment(), EasyPermissions.PermissionCallbacks {
    @Inject
    internal lateinit var beaconService: BeaconService
    @Inject
    internal lateinit var config: RemoteConfigManager
    @Inject
    internal lateinit var statusManager: StatusManager
    @Inject
    internal lateinit var beaconManager: BeaconManager
    @Inject
    internal lateinit var bluetoothReceiver: BluetoothBroadcastReceiver
    @Inject
    internal lateinit var locationReceiver: LocationBroadCastReceiver

    private val disposable = CompositeDisposable()
    private lateinit var binding: FragmentListBinding
    private val slideBarLayout by lazy(LazyThreadSafetyMode.NONE) { binding.slideBar.root as MotionLayout }
    private val scannerAdapter by lazy(LazyThreadSafetyMode.NONE) { (binding.recycler.adapter as? RecyclerAdapter<BeaconModel>) }
    private var currentItem: BeaconModel? = null
    private var canUpdateList = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false)
        binding.beaconClickHandler = ::clickHandler
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getNearbyBeacons()
        setupButtons()
        subscribeToStatus()
    }

    override fun onStart() {
        super.onStart()
        beaconService.bindService()
        bluetoothReceiver.register(requireActivity())
        locationReceiver.register(requireActivity())
        if (binding.list == null) {
            binding.list = beaconManager.cachedBeacons
        }
    }

    override fun onStop() {
        scannerAdapter?.items?.let { beaconManager.cachedBeacons = it }
        beaconService.unbindService()
        bluetoothReceiver.unregister(requireActivity())
        locationReceiver.unregister(requireActivity())
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
        canUpdateList = true

        postAction(200) {
            slideBarLayout.visibility = View.VISIBLE
        }
    }

    override fun onPause() {
        super.onPause()
        canUpdateList = false
    }

    override fun onDestroy() {
        statusManager.dispose()
        disposable.clear()
        super.onDestroy()
    }

    private fun setupButtons() {
        binding.slideBar.scanImage.setOnClickListener {
            if (PermissionUtility.hasBluetoothAndLocationPermissions(requireContext())) {
                manageStates()
            } else {
                checkLocationPermission()
            }
        }
    }

    private fun manageStates() {
        val currentState = slideBarLayout.currentState

        when (binding.statusModel?.isAllEnabled ?: true) {
            true -> {
                if (currentState == R.id.startState) {
                    slideBarLayout.transitionToEndState(R.id.scan_state)
                    startBeaconScan()
                } else {
                    slideBarLayout.transitionToEndState(R.id.startState)
                    stopBeaconScan()
                }
            }
            false -> {
                stopBeaconScan()
                if (currentState == R.id.startState) {
                    slideBarLayout.transitionToEndState(R.id.error_state)
                } else {
                    slideBarLayout.transitionToEndState(R.id.startState)
                }
            }
        }
    }

    private fun MotionLayout.transitionToEndState(id: Int) {
        this.transitionToState(id)
        this.transitionToEnd()
    }

    private fun checkLocationPermission() {
        PermissionUtility.requestLocationPermission(this)

    }

    private fun clickHandler(model: BeaconModel) {
        slideBarLayout.visibility = View.INVISIBLE
        currentItem = model
        val clickedItemPosition = scannerAdapter?.currentItemPosition ?: 0
        val sharedImage = binding.recycler.layoutManager?.findViewByPosition(clickedItemPosition)?.findViewById<ImageView>(R.id.icon_image)
        val activityOptions = ActivityOptions.makeSceneTransitionAnimation(requireActivity(), sharedImage, sharedImage?.transitionName)
        startActivityForResult(BeaconCardActivity.createIntent(requireContext(), model.id, model.categoryId), BEACON_CARD_ACTIVITY_REQUEST_CODE, activityOptions.toBundle())
    }


    private fun deleteCurrentItem() {
        currentItem?.let { scannerAdapter?.removeItem(it) }
    }

    private fun getNearbyBeacons() {
        beaconManager.getNearbyBeacons().observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (canUpdateList && it.isNotEmpty()) {
                    binding.list = it
                }
            }.let(disposable::add)
    }

    private fun subscribeToStatus() {
        statusManager.getCurrentStatus().subscribe({
            binding.statusModel = it
            if (it.isAllEnabled && slideBarLayout.currentState == R.id.error_state) slideBarLayout.transitionToEndState(R.id.startState)
            else if (!it.isAllEnabled) slideBarLayout.transitionToEndState(R.id.error_state)
        }, {}).let(disposable::add)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when {
            requestCode == BEACON_CARD_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK -> deleteCurrentItem()
        }
    }

    private fun startBeaconScan() {
        beaconService.startScanning()
        startRippleLoadingAnimation()
    }

    private fun stopBeaconScan() {
        beaconService.stopScanning()
        stopRippleLoadingAnimation()
    }

    private fun startRippleLoadingAnimation() {
        if (!binding.slideBar.rippleBackground.isRippleAnimationRunning) {
            binding.loading = true
            binding.slideBar.rippleBackground.startRippleAnimation()
        }
    }

    private fun stopRippleLoadingAnimation() {
        if (binding.slideBar.rippleBackground.isRippleAnimationRunning) {
            binding.slideBar.rippleBackground.stopRippleAnimation()
            binding.loading = false
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        manageStates()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).setRationale(R.string.permission_location_settings).setTitle(R.string.permission_location_header).build().show()
        } else {
            checkLocationPermission()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    companion object {
        private const val BEACON_CARD_ACTIVITY_REQUEST_CODE = 50
        fun newInstance() = ListFragment()
    }
}
