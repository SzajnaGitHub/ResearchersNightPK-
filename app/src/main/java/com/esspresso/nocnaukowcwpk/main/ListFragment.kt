package com.esspresso.nocnaukowcwpk.main

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.esspresso.nocnaukowcwpk.R
import com.esspresso.nocnaukowcwpk.beacons.BeaconManager
import com.esspresso.nocnaukowcwpk.beacons.BeaconModel
import com.esspresso.nocnaukowcwpk.beacons.BeaconService
import com.esspresso.nocnaukowcwpk.config.RemoteConfigManager
import com.esspresso.nocnaukowcwpk.databinding.FragmentListBinding
import com.esspresso.nocnaukowcwpk.status.StatusManager
import com.esspresso.nocnaukowcwpk.ultis.recyclerview.RecyclerAdapter
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

@AndroidEntryPoint
class ListFragment : Fragment() {
    @Inject
    internal lateinit var beaconHandler: BeaconService
    @Inject
    internal lateinit var config: RemoteConfigManager
    @Inject
    internal lateinit var statusManager: StatusManager
    @Inject
    internal lateinit var beaconManager: BeaconManager

    private val disposable = CompositeDisposable()
    private lateinit var binding: FragmentListBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false)
        startRippleLoadingAnimation()
        getNearbyBeacons()
        binding.list = ArrayList()
        binding.beaconClickHandler = ::clickHandler
        binding.onListEmptyAction = ::startRippleLoadingAnimation
        binding.onListNoLongerEmptyAction = ::stopRippleLoadingAnimation
        Handler().postDelayed({ (binding.recycler.adapter as RecyclerAdapter<BeaconModel>).removeAllItems() }, 20_000)
        return binding.root
    }

    private fun clickHandler(model: BeaconModel) {
    }

    private fun getNearbyBeacons() {
        beaconManager.getNearbyBeacons().subscribe {
            binding.list = it
        }.let(disposable::add)
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

    override fun onStart() {
        super.onStart()
        beaconHandler.bindService()
    }

    override fun onStop() {
        beaconHandler.unbindService()
        super.onStop()
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }

    companion object {
        fun newInstance() = ListFragment()
    }
}
