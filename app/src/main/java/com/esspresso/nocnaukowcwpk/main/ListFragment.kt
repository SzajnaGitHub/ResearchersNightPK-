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
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

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
        getNearbyBeacons()
        binding.list = ArrayList()
        binding.beaconClickHandler = ::clickHandler
        return binding.root
    }

    private fun clickHandler(model: BeaconModel) {

    }

    private fun getNearbyBeacons() {
        beaconManager.getNearbyBeacons().subscribe {
            binding.list = it
        }.let(disposable::add)
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
