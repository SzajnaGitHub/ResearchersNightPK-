package com.esspresso.nocnaukowcwpk.status

import android.bluetooth.BluetoothAdapter
import android.location.LocationManager
import android.net.ConnectivityManager
import androidx.core.location.LocationManagerCompat
import com.esspresso.nocnaukowcwpk.di.BluetoothState
import com.esspresso.nocnaukowcwpk.di.EnterBeaconRange
import com.esspresso.nocnaukowcwpk.di.LocationState
import com.jakewharton.rxrelay3.Relay
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StatusManager @Inject constructor(
    @EnterBeaconRange private val enterRangeRelay: Relay<Boolean>,
    private val locationManager: LocationManager,
    private val connectivityManager: ConnectivityManager,
    @BluetoothState private val bluetoothRelay: Relay<Boolean>,
    @LocationState private val locationRelay: Relay<Boolean>
) {
    private val disposables = CompositeDisposable()
    private val bluetooth by lazy(LazyThreadSafetyMode.NONE) { BluetoothAdapter.getDefaultAdapter() }
    private val subject = BehaviorSubject.create<Unit>()

    private var statusModel = getInitialModel()

    private fun isBluetoothEnabled() = bluetooth.isEnabled
    private fun isLocationEnabled() = LocationManagerCompat.isLocationEnabled(locationManager)
    private fun isInternetEnabled() = connectivityManager.activeNetworkInfo?.isConnected == true
    fun isAllEnabled() = isBluetoothEnabled() && isLocationEnabled() && isInternetEnabled()

    private fun getInitialModel() = StatusModel(isInternetEnabled(), isBluetoothEnabled(), isLocationEnabled())
    fun register() {
        statusModel = getInitialModel()
    }

    private fun subscribeToEnterRangeChanges() {
        enterRangeRelay.distinctUntilChanged().observeOn(AndroidSchedulers.mainThread()).subscribe { searching ->
            println("TEKST SEARCHING $searching")
        }.let(disposables::add)

        locationRelay
            .map {
                println("TEKST STATUS MODEL b4 l $statusModel")
                statusModel = statusModel.copy(locationAvailable = it)
                println("TEKST STATUS MODEL aft l $statusModel")
            }
            .subscribe(subject::onNext)
            .let(disposables::add)

        bluetoothRelay
            .map {
                println("TEKST STATUS MODEL bf bt $statusModel")
                statusModel = statusModel.copy(bluetoothAvailable = it)
                println("TEKST STATUS MODEL aft bt $statusModel")
            }
            .subscribe(subject::onNext)
            .let(disposables::add)

        subject.onNext(Unit)
    }

    fun getCurrentStatus(): Observable<StatusModel> = subject.observeOn(AndroidSchedulers.mainThread())
        .map {
            statusModel
        }

    fun dispose() {
        disposables.clear()
    }

    init {
        subscribeToEnterRangeChanges()
    }
}
