package com.esspresso.nocnaukowcwpk.beacons

import androidx.annotation.Keep
import com.esspresso.nocnaukowcwpk.R
import com.esspresso.nocnaukowcwpk.utils.RssiResolver
import com.esspresso.nocnaukowcwpk.utils.recyclerview.RecyclerModel
import org.altbeacon.beacon.Beacon
import kotlin.math.roundToInt

@Keep
data class BeaconConfigModel(val major: String, val minor: String)

@Keep
data class BeaconModel(
    val id: BeaconId,
    private val distance: Double = 0.0,
    private val signalStrength: Signal = Signal.NORMAL,
    private val expirationTimeInMinutes: Int = 0
): RecyclerModel {
    override fun getId() = id.toString()

    val distanceText get() = if (distance > 0) distance.toString().substring(0..4) else "0"
    val signalIcon = when (signalStrength) {
        Signal.STRONG -> R.drawable.ic_status_green
        Signal.NORMAL -> R.drawable.ic_status_yellow
        Signal.WEAK -> R.drawable.ic_status_red
    }

    companion object {
        fun create(beacon: Beacon) = BeaconModel(
            id = BeaconId(beacon.id2.toString(), beacon.id3.toString()),
            distance = beacon.distance,
            signalStrength = RssiResolver.resolve(beacon.runningAverageRssi.roundToInt())
        )

        fun create(beacon: BeaconConfigModel) = BeaconModel(
            id = BeaconId(beacon.major, beacon.minor)
        )
    }
}

typealias BeaconId = Pair<String, String>