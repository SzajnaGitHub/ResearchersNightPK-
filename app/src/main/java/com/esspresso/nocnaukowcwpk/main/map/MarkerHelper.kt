package com.esspresso.nocnaukowcwpk.main.map

import android.content.Context
import androidx.core.content.ContextCompat
import com.esspresso.nocnaukowcwpk.R
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.ui.IconGenerator
import javax.inject.Inject

class MarkerHelper @Inject constructor(val context: Context) {
    private val markerIconGenerator by lazy(LazyThreadSafetyMode.NONE) { IconGenerator(context) }

    fun getBuildingMarkers(): List<MarkerOptions> {
        val buildingA = MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(markerIconGenerator.makeIcon("A"))).position(LatLng(50.074831, 19.997667))
        val buildingB = MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(markerIconGenerator.makeIcon("B"))).position(LatLng(50.074772, 19.996920))
        val buildingC = MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(markerIconGenerator.makeIcon("C"))).position(LatLng(50.074772, 19.996417))
        val buildingD = MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(markerIconGenerator.makeIcon("D"))).position(LatLng(50.074772, 19.995906))
        val buildingE = MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(markerIconGenerator.makeIcon("E"))).position(LatLng(50.075194, 19.996363))
        val buildingF = MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(markerIconGenerator.makeIcon("F"))).position(LatLng(50.075231, 19.994512))
        val buildingG = MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(markerIconGenerator.makeIcon("G"))).position(LatLng(50.075867, 19.994468))
        val buildingJ = MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(markerIconGenerator.makeIcon("J"))).position(LatLng(50.075632, 19.995276))
        val buildingK = MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(markerIconGenerator.makeIcon("K"))).position(LatLng(50.075688, 19.995789))
        val buildingH = MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(markerIconGenerator.makeIcon("H"))).position(LatLng(50.076185, 19.995574))
        return listOf(buildingA, buildingB, buildingC, buildingD, buildingE, buildingF, buildingG, buildingH, buildingJ, buildingK)
    }

    init {
        markerIconGenerator.setBackground(ContextCompat.getDrawable(context, R.drawable.marker_drawable))
    }
}
