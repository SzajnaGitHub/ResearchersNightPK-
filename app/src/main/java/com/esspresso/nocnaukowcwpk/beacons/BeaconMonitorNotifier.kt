package com.esspresso.nocnaukowcwpk.beacons

import com.esspresso.nocnaukowcwpk.di.EnterBeaconRange
import com.jakewharton.rxrelay3.Relay
import org.altbeacon.beacon.MonitorNotifier
import org.altbeacon.beacon.Region
import javax.inject.Inject

class BeaconMonitorNotifier @Inject constructor(@EnterBeaconRange private val enterRangeRelay: Relay<Boolean>) : MonitorNotifier {
    override fun didEnterRegion(region: Region?) {}
    override fun didExitRegion(region: Region?) {}
    override fun didDetermineStateForRegion(state: Int, region: Region?) = enterRangeRelay.accept(state == MonitorNotifier.INSIDE)
}
