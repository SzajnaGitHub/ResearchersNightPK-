package com.esspresso.nocnaukowcwpk.status

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import com.esspresso.nocnaukowcwpk.di.LocationState
import com.esspresso.nocnaukowcwpk.utils.DialogActivity
import com.jakewharton.rxrelay3.Relay
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

class LocationBroadCastReceiver @Inject constructor(private val locationManager: LocationManager, @LocationState private val locationRelay: Relay<Boolean>) : BroadcastReceiver() {
    private val disposables = CompositeDisposable()
    private val subject = BehaviorSubject.create<Boolean>()

    override fun onReceive(context: Context?, receivedIntent: Intent?) {
        subject.onNext(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
    }

    private fun subscribeToLocationChange(context: Activity) {
        subject.observeOn(AndroidSchedulers.mainThread())
            .distinctUntilChanged()
            .subscribe { isEnabled ->
                locationRelay.accept(isEnabled)
                if (!isEnabled) context.startActivity(DialogActivity.createNoLocationIntent(context))
            }.let(disposables::add)
    }

    fun register(activity: Activity) {
        subscribeToLocationChange(activity)
        activity.registerReceiver(this, IntentFilter(LOCATION_STATE_CHANGED_ACTION))
    }

    fun unregister(activity: Activity) {
        disposables.clear()
        activity.unregisterReceiver(this)
    }

    companion object {
        const val LOCATION_STATE_CHANGED_ACTION = LocationManager.PROVIDERS_CHANGED_ACTION
    }
}
