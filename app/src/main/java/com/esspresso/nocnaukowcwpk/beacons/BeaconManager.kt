package com.esspresso.nocnaukowcwpk.beacons

import com.esspresso.nocnaukowcwpk.config.RemoteConfigManager
import com.esspresso.nocnaukowcwpk.di.BeaconsInRange
import com.jakewharton.rxrelay3.Relay
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import org.altbeacon.beacon.Beacon
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BeaconManager @Inject constructor(@BeaconsInRange private val beaconsInRange: Relay<Collection<Beacon>>, private val remoteConfig: RemoteConfigManager) {

    private lateinit varN

    private fun getBeaconsFromConfig() {
        remoteConfig.getBeacons().map {
            BeaconModel.create(it)
        }
    }

    fun getNearbyBeacons(): Observable<ArrayList<BeaconModel>> = beaconsInRange
        .map {
            val items = it.map { beacon -> BeaconModel.create(beacon) }
            items.filter {  }

        }
        .map {
            ArrayList(it)
        }
        .observeOn(AndroidSchedulers.mainThread())


    init {
        getBeaconsFromConfig()
    }
}
