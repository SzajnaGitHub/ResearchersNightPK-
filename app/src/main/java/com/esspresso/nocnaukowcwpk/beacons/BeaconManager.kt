package com.esspresso.nocnaukowcwpk.beacons

import com.esspresso.db.userquestions.UserQuestionsDao
import com.esspresso.nocnaukowcwpk.config.RemoteConfigManager
import com.esspresso.nocnaukowcwpk.di.BeaconsInRange
import com.jakewharton.rxrelay3.Relay
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.Observables
import io.reactivex.rxjava3.schedulers.Schedulers
import org.altbeacon.beacon.Beacon
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BeaconManager @Inject constructor(
    @BeaconsInRange private val beaconsInRange: Relay<Collection<Beacon>>,
    private val remoteConfig: RemoteConfigManager,
    private val userQuestionsDao: UserQuestionsDao
) {
    var cachedBeacons = ArrayList<BeaconModel>()

    fun getNearbyBeacons(): Observable<ArrayList<BeaconModel>> = Observables.zip(beaconsInRange, getAnsweredQuestions())
        .map { (beacons, questions) ->
            Pair(questions, beacons.map { beacon -> BeaconModel.create(beacon, beaconList = remoteConfig.getBeacons()) })
        }
        .map { (questions, beacons) ->
            beacons.filter { model -> !questions.contains(model.getId()) }
        }
        .map { it.filter { model -> model.categoryId != null } }
        .map { ArrayList(it) }
        .observeOn(AndroidSchedulers.mainThread())

    private fun getAnsweredQuestions(): Observable<List<String>> = userQuestionsDao.getAllCorrectQuestionsIDS()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}
