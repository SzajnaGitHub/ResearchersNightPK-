package com.esspresso.nocnaukowcwpk.ui.map

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.esspresso.nocnaukowcwpk.R
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.ui.IconGenerator
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MarkerHelper @Inject constructor(@ApplicationContext val context: Context) {
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

    fun getSpecialMarkers(): List<MarkerOptions> {
        val pantographMarker = MarkerOptions().icon(ContextCompat.getDrawable(context, R.drawable.marker_diner_drawable)?.let { getMarkerIconFromDrawable(it) }).position(LatLng(50.074895, 19.995378))
        val parkingMarker = MarkerOptions().icon(ContextCompat.getDrawable(context, R.drawable.marker_parking_drawable)?.let { getMarkerIconFromDrawable(it) }).position(LatLng(50.074072, 19.996940))
        val tramMarker = MarkerOptions().icon(ContextCompat.getDrawable(context, R.drawable.marker_tram_drawable)?.let { getMarkerIconFromDrawable(it) }).position(LatLng(50.073989, 19.998912))
        return listOf(pantographMarker, tramMarker, parkingMarker)
    }

    private fun getMarkerIconFromDrawable(drawable: Drawable): BitmapDescriptor? {
        val canvas = Canvas()
        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        canvas.setBitmap(bitmap)
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        drawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    init {
        markerIconGenerator.setBackground(ContextCompat.getDrawable(context, R.drawable.marker_drawable))
    }
}
