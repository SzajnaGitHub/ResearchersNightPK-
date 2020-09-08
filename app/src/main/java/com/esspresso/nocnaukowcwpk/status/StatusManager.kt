package com.esspresso.nocnaukowcwpk.status

import android.bluetooth.BluetoothAdapter
import android.location.LocationManager
import android.net.ConnectivityManager
import androidx.core.location.LocationManagerCompat
import com.esspresso.nocnaukowcwpk.di.EnterBeaconRange
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
    private val connectivityManager: ConnectivityManager
) {
    private val disposables = CompositeDisposable()
    private val bluetooth by lazy(LazyThreadSafetyMode.NONE) { BluetoothAdapter.getDefaultAdapter() }
    private val statusSubject = BehaviorSubject.create<StatusModel>()

    private fun isBluetoothEnabled() = bluetooth.isEnabled
    private fun isLocationEnabled() = LocationManagerCompat.isLocationEnabled(locationManager)
    private fun isInternetEnabled() = connectivityManager.activeNetworkInfo?.isConnected == true

    private fun subscribeToEnterRangeChanges() {
        enterRangeRelay.distinctUntilChanged().observeOn(AndroidSchedulers.mainThread()).subscribe { searching ->
            sendModel()
            println("TEKST SEARCHING $searching")
        }.let(disposables::add)
    }

    private fun sendModel() {
        statusSubject.onNext(
            StatusModel(
                connectionAvailable = isInternetEnabled(),
                bluetoothAvailable = isBluetoothEnabled(),
                locationAvailable = isLocationEnabled()
            )
        )
    }

    fun getCurrentStatus(): Observable<StatusModel> = statusSubject.observeOn(AndroidSchedulers.mainThread())
        .distinctUntilChanged()

    fun dispose() {
        disposables.clear()
    }

    init {
        subscribeToEnterRangeChanges()

    }
}
