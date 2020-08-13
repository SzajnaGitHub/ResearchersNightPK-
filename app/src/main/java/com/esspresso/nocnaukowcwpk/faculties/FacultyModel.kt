package com.esspresso.nocnaukowcwpk.faculties

import androidx.annotation.Keep
import com.google.android.gms.maps.model.LatLng
import java.io.Serializable

@Keep
data class FacultyModel(
    val id: String,
    val visible: Boolean = true,
    private val lat: Double,
    private val lng: Double
) : Serializable {
    val latLng get() = LatLng(lat, lng)
}
