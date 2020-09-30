package com.esspresso.nocnaukowcwpk.beacons

import com.esspresso.nocnaukowcwpk.config.RemoteConfigManager
import com.esspresso.nocnaukowcwpk.di.BeaconsInRange
import com.esspresso.nocnaukowcwpk.store.KeyValueStore
import com.jakewharton.rxrelay3.Relay
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import org.altbeacon.beacon.Beacon
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BeaconManager @Inject constructor(
    @BeaconsInRange private val beaconsInRange: Relay<Collection<Beacon>>,
    private val remoteConfig: RemoteConfigManager,
    private val store: KeyValueStore
) {
    fun getNearbyBeacons(): Observable<ArrayList<BeaconModel>> = beaconsInRange
        .map { it.map { beacon -> BeaconModel.create(beacon) } }
        .map { it.map { beacon -> beacon.copy(categoryId = remoteConfig.getBeacons().find { it.id == beacon.id }?.categoryId) } }
        .map { it.filter { model -> model.categoryId != null } }
        .map { it.filter { model -> !checkItemAnswered(model.getId())  } }
        .map { ArrayList(it) }
        .observeOn(AndroidSchedulers.mainThread())

    private fun checkItemAnswered(id: String): Boolean {
        val questionAnswered = store.userQuestionAnsweredCorrectly + store.userQuestionAnsweredIncorrectly
        return questionAnswered.contains(id)
    }
}
