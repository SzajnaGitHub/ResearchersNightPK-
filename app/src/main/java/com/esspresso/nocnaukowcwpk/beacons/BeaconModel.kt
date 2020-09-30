package com.esspresso.nocnaukowcwpk.beacons

import android.content.Context
import androidx.annotation.Keep
import com.esspresso.nocnaukowcwpk.R
import com.esspresso.nocnaukowcwpk.utils.CategoryResolver
import com.esspresso.nocnaukowcwpk.utils.RssiResolver
import com.esspresso.nocnaukowcwpk.utils.recyclerview.RecyclerModel
import com.esspresso.nocnaukowcwpk.utils.translateFromId
import org.altbeacon.beacon.Beacon
import kotlin.math.roundToInt

typealias BeaconId = Pair<String, String>

@Keep
data class BeaconConfigModel(private val major: String, private val minor: String, val categoryId: String) {
    val id = BeaconId(major, minor)
}

@Keep
data class BeaconModel(
    val id: BeaconId,
    private val distance: Double = 0.0,
    private val signalStrength: Signal = Signal.NORMAL,
    private val expirationTimeInMinutes: Int = 0,
    val categoryId: String? = null
) : RecyclerModel {
    override fun getId() = id.toString()
    fun getIcon(context: Context) = categoryId?.let { CategoryResolver.resolveCategoryImage(it, context) }
    fun getCategoryText(context: Context) = categoryId?.translateFromId(context) ?: ""

    val distanceText get() = if (distance > 0) "${distance.toString().substring(0..2)}m" else "0m"
    val signalIcon = when (signalStrength) {
        Signal.STRONG -> R.drawable.ic_status_green
        Signal.NORMAL -> R.drawable.ic_status_yellow
        Signal.WEAK -> R.drawable.ic_status_red
    }

    companion object {
        fun create(beacon: Beacon) = BeaconModel(
            id = BeaconId(beacon.id2.toString(), beacon.id3.toString()),
            distance = beacon.distance,
            signalStrength = RssiResolver.resolve(beacon.runningAverageRssi.roundToInt()),
        )
    }
}
