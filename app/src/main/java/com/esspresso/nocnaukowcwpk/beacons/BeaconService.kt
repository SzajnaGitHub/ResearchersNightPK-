package com.esspresso.nocnaukowcwpk.beacons

import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.RemoteException
import com.esspresso.nocnaukowcwpk.utils.createRegion
import org.altbeacon.beacon.BeaconConsumer
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.BeaconParser
import javax.inject.Inject

class BeaconService @Inject constructor(
    private val context: Context,
    private val monitorNotifier: BeaconMonitorNotifier,
    private val rangeNotifier: BeaconRangeNotifier
) : BeaconConsumer {

    private val beaconManager by lazy(LazyThreadSafetyMode.NONE) { BeaconManager.getInstanceForApplication(context) }
    private val region by lazy { createRegion() }

    override fun getApplicationContext() = context
    override fun unbindService(p0: ServiceConnection?) {}
    override fun bindService(p0: Intent?, p1: ServiceConnection?, p2: Int) = true

    override fun onBeaconServiceConnect() {
        beaconManager.removeAllMonitorNotifiers()
        beaconManager.removeAllRangeNotifiers()
        beaconManager.backgroundMode = false
        beaconManager.foregroundScanPeriod = FOREGROUND_SCAN_PERIOD
        beaconManager.addMonitorNotifier(monitorNotifier)
        beaconManager.addRangeNotifier(rangeNotifier)
    }

    fun startScanning() {
        try {
            beaconManager.startMonitoringBeaconsInRegion(region)
            beaconManager.startRangingBeaconsInRegion(region)
        }
        catch (e: RemoteException) {}
    }

    fun stopScanning() {
        try {
            beaconManager.stopMonitoringBeaconsInRegion(region)
            beaconManager.stopRangingBeaconsInRegion(region)
        }
        catch (e: RemoteException) {}
    }

    fun bindService() {
        beaconManager.bind(this)
    }

    fun unbindService() {
        beaconManager.unbind(this)
    }

    init {
        beaconManager.beaconParsers.add(BeaconParser().setBeaconLayout(I_BEACON_LAYOUT))
    }

    companion object {
        private const val FOREGROUND_SCAN_PERIOD = 5500L
        private const val I_BEACON_LAYOUT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"
    }
}
