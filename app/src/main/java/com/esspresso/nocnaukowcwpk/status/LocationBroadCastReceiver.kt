package com.esspresso.nocnaukowcwpk.status

import android.app.Activity
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import javax.inject.Inject

class LocationBroadCastReceiver @Inject constructor() : BroadcastReceiver() {

    override fun onReceive(context: Context?, receivedIntent: Intent?) {
        val lm = context?.getSystemService(Service.LOCATION_SERVICE) as? LocationManager
        val isEnabled = lm?.isProviderEnabled(LocationManager.GPS_PROVIDER) == true
        println("TEKST $isEnabled")
    }

    fun register(activity: Activity) = activity.registerReceiver(this, IntentFilter(LOCATION_STATE_CHANGED_ACTION))
    fun unregister(activity: Activity) = activity.unregisterReceiver(this)

    companion object {
        const val LOCATION_STATE_CHANGED_ACTION = LocationManager.PROVIDERS_CHANGED_ACTION
    }
}
