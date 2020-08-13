package com.esspresso.nocnaukowcwpk.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.esspresso.nocnaukowcwpk.R
import com.esspresso.nocnaukowcwpk.beacons.BeaconModel
import com.esspresso.nocnaukowcwpk.beacons.BeaconService
import com.esspresso.nocnaukowcwpk.config.RemoteConfigManager
import com.esspresso.nocnaukowcwpk.databinding.FragmentListBinding
import com.esspresso.nocnaukowcwpk.di.BeaconsInRange
import com.esspresso.nocnaukowcwpk.status.StatusManager
import com.jakewharton.rxrelay3.Relay
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.altbeacon.beacon.Beacon
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
    @BeaconsInRange
    internal lateinit var beaconsInRange: Relay<Collection<Beacon>>

    private val disposable = CompositeDisposable()
    private lateinit var binding: FragmentListBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false)
        beaconsInRange.distinctUntilChanged()
            .map {
                it.map { beacon ->
                    BeaconModel(beacon.id2.toString(), beacon.id3.toString())
                }
            }
            .observeOn(AndroidSchedulers.mainThread()).subscribe {
                println("TEKST BEACONS $it")
            }.let(disposable::add)
        return binding.root
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
