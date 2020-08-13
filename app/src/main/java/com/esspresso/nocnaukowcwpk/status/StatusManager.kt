package com.esspresso.nocnaukowcwpk.status

import android.bluetooth.BluetoothAdapter
import com.esspresso.nocnaukowcwpk.di.EnterBeaconRange
import com.jakewharton.rxrelay3.Relay
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StatusManager @Inject constructor(@EnterBeaconRange private val enterRangeRelay: Relay<Boolean>) {

    private val disposables = CompositeDisposable()
    private val bluetooth by lazy(LazyThreadSafetyMode.NONE) { BluetoothAdapter.getDefaultAdapter() }

    fun isBluetoothEnabled() = bluetooth.isEnabled

    private fun subscribeToEnterRangeChanges() {
        enterRangeRelay.distinctUntilChanged().observeOn(AndroidSchedulers.mainThread()).subscribe { searching ->
            println("TEKST SEARCHING $searching")
        }.let(disposables::add)

    }

    fun dispose() {
        disposables.clear()
    }

    init {
        subscribeToEnterRangeChanges()
    }

}
