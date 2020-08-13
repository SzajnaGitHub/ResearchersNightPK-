package com.esspresso.nocnaukowcwpk.beacons

import com.esspresso.nocnaukowcwpk.di.BeaconsInRange
import com.jakewharton.rxrelay3.Relay
import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.RangeNotifier
import org.altbeacon.beacon.Region
import javax.inject.Inject

class BeaconRangeNotifier @Inject constructor(@BeaconsInRange private val beaconsInRange: Relay<Collection<Beacon>>) : RangeNotifier {
    override fun didRangeBeaconsInRegion(beacons: MutableCollection<Beacon>?, region: Region?) = beaconsInRange.accept(beacons)
}
