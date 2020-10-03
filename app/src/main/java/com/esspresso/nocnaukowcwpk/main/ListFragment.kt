package com.esspresso.nocnaukowcwpk.main

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.os.Handler
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
import com.esspresso.nocnaukowcwpk.status.PermissionManager
import com.esspresso.nocnaukowcwpk.status.StatusManager
import com.esspresso.nocnaukowcwpk.utils.DialogActivity
import com.esspresso.nocnaukowcwpk.utils.recyclerview.RecyclerAdapter
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

@AndroidEntryPoint
class ListFragment : Fragment() {
    @Inject
    internal lateinit var beaconService: BeaconService
    @Inject
    internal lateinit var config: RemoteConfigManager
    @Inject
    internal lateinit var statusManager: StatusManager
    @Inject
    internal lateinit var beaconManager: BeaconManager
    @Inject
    internal lateinit var permissionManager: PermissionManager
    @Inject
    internal lateinit var bluetoothReceiver: BluetoothBroadcastReceiver
    @Inject
    internal lateinit var locationReceiver: LocationBroadCastReceiver

    private val disposable = CompositeDisposable()
    private lateinit var binding: FragmentListBinding
    private val slideBarLayout by lazy(LazyThreadSafetyMode.NONE) { binding.slideBar.root as MotionLayout }
    private val scannerAdapter by lazy(LazyThreadSafetyMode.NONE) { (binding.recycler.adapter as? RecyclerAdapter<BeaconModel>) }
    private var currentItem: BeaconModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false)
        binding.beaconClickHandler = ::clickHandler
        binding.onListEmptyAction = { binding.scanImage.visibility = View.VISIBLE }
        binding.onListNoLongerEmptyAction = { binding.scanImage.visibility = View.INVISIBLE }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButtons()
        subscribeToStatus()
        statusManager.register()
    }

    override fun onResume() {
        super.onResume()
        getNearbyBeacons()
    }

    override fun onPause() {
        disposable.clear()
        super.onPause()
    }

    override fun onStart() {
        super.onStart()
        beaconService.bindService()
        bluetoothReceiver.register(requireActivity())
        locationReceiver.register(requireActivity())
        if (binding.list == null) binding.list = beaconManager.cachedBeacons
    }

    override fun onStop() {
        scannerAdapter?.items?.let { beaconManager.cachedBeacons = it }
        beaconService.unbindService()
        bluetoothReceiver.unregister(requireActivity())
        locationReceiver.unregister(requireActivity())
        super.onStop()
    }

    private fun setupButtons() {
        binding.slideBar.rotate = true
        slideBarLayout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {}
            override fun onTransitionChange(p0: MotionLayout?, startState: Int, endState: Int, p3: Float) {}

            override fun onTransitionCompleted(p0: MotionLayout?, state: Int) {
                binding.slideBar.rotate = true
                if (state == R.id.startState && statusManager.isAllEnabled()) {
                    binding.statusModel = null
                }
            }

            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
                binding.slideBar.rotate = false
            }
        })

        binding.scanImage.setOnClickListener {
            if (permissionManager.checkIfAllPermissionsGranted()) {
                startBeaconScan()
            } else {
                stopRippleLoadingAnimation()
                shouldShowPermissions()
            }
        }
    }

    private fun shouldShowPermission(permission: String) {
        if (!permissionManager.checkPermissionGranted(permission)) {
            permissionManager.requestPermission(permission).subscribe({ granted ->
                if (!granted) {
                    permissionManager.shouldShowPermission(requireActivity(), PermissionManager.LOCATION_PERMISSION).subscribe { canAskAgain ->
                        if (!canAskAgain) startActivity(DialogActivity.createPermissionIntent(requireContext(), permission, ::openSettings))
                    }.let(disposable::add)
                }
            }, {}).let(disposable::add)
        }
    }

    private fun openSettings() {
        startActivityForResult(permissionManager.getApplicationSettingsIntent(this.requireContext()), SETTINGS_REQUEST_CODE)
    }

    private fun clickHandler(model: BeaconModel) {
        currentItem = model
        val clickedItemPosition = scannerAdapter?.getCurrentItemPosition() ?: 0
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
                if (it.isNotEmpty()) {
                    binding.list = it
                    stopRippleLoadingAnimation()
                }
            }.let(disposable::add)
    }

    private fun subscribeToStatus() {
        statusManager.getCurrentStatus().subscribe({
            if (it.isAllEnabled) {
                if (slideBarLayout.currentState == R.id.endState) {
                    binding.statusModel = it
                    slideBarLayout.transitionToState(R.id.startState)
                } else {
                    binding.statusModel = null
                }
            } else {
                binding.statusModel = it
            }
        }, {}).let(disposable::add)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when {
            requestCode == SETTINGS_REQUEST_CODE && !permissionManager.checkIfAllPermissionsGranted() -> Handler().postDelayed({ shouldShowPermissions() }, 200)
            requestCode == SETTINGS_REQUEST_CODE -> startBeaconScan()
            requestCode == BEACON_CARD_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK -> deleteCurrentItem()
        }
    }

    private fun startRippleLoadingAnimation() {
        if (!binding.rippleBackground.isRippleAnimationRunning) {
            binding.loading = true
            binding.rippleBackground.startRippleAnimation()
        }
    }

    private fun stopRippleLoadingAnimation() {
        if (binding.rippleBackground.isRippleAnimationRunning) {
            binding.rippleBackground.stopRippleAnimation()
            binding.loading = false
        }
    }

    private fun startBeaconScan() {
        beaconService.startScanning()
        startRippleLoadingAnimation()
    }

    private fun shouldShowPermissions() {
        shouldShowPermission(PermissionManager.LOCATION_PERMISSION)
        shouldShowPermission(PermissionManager.BLUETOOTH_PERMISSION)
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }

    companion object {
        private const val BEACON_CARD_ACTIVITY_REQUEST_CODE = 50
        private const val SETTINGS_REQUEST_CODE = 30
        fun newInstance() = ListFragment()
    }
}
