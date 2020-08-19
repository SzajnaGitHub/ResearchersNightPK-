package com.esspresso.nocnaukowcwpk.beacons

import com.esspresso.nocnaukowcwpk.di.BeaconsInRange
import com.jakewharton.rxrelay3.Relay
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import org.altbeacon.beacon.Beacon
import javax.inject.Inject

class BeaconManager @Inject constructor(@BeaconsInRange private val beaconsInRange: Relay<Collection<Beacon>>) {

    fun getNearbyBeacons(): Observable<ArrayList<BeaconModel>> = beaconsInRange
        .map {
            it.map { beacon -> BeaconModel.create(beacon) }
        }
        .map {
            ArrayList(it)
        }
        .observeOn(AndroidSchedulers.mainThread())

}
