package com.esspresso.nocnaukowcwpk.main.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.esspresso.nocnaukowcwpk.R
import com.esspresso.nocnaukowcwpk.config.RemoteConfigManager
import com.esspresso.nocnaukowcwpk.databinding.FragmentMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.data.kml.KmlLayer
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

@AndroidEntryPoint
class MapFragment : Fragment() {
    @Inject
    internal lateinit var config: RemoteConfigManager
    @Inject
    internal lateinit var markerHelper: MarkerHelper

    private val disposable = CompositeDisposable()
    private lateinit var binding: FragmentMapBinding
    private lateinit var map: GoogleMap

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(::onMapReady)
    }

    private fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        setupMap()
        setupLatLngCameraMoveBoundaries()
        addMarkers()
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(DEFAULT_LAT_VALUE, DEFAULT_LONG_VALUE), DEFAULT_ZOOM_VALE))
    }

    private fun addMarkers() {
        (markerHelper.getSpecialMarkers() + markerHelper.getBuildingMarkers()).forEach(map::addMarker)
    }

    private fun setupMap() {
        addMapLayers()
        map.isBuildingsEnabled = false
        map.uiSettings.setAllGesturesEnabled(true)
        map.setMaxZoomPreference(MAX_ZOOM_VALE)
        map.setMinZoomPreference(MIN_ZOOM_VALE)
        map.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style))
    }

    private fun setupLatLngCameraMoveBoundaries() {
        val topRightBoundary = LatLng(50.07507, 19.9970)
        val topLeftBoundary = LatLng(50.07507, 19.9960)
        val bottomLeftBoundary = LatLng(50.07556, 19.9950)
        val bottomRightBoundary = LatLng(50.07572, 19.9970)
        map.setLatLngBoundsForCameraTarget(
            LatLngBounds.Builder()
                .include(topRightBoundary)
                .include(topLeftBoundary)
                .include(bottomLeftBoundary)
                .include(bottomRightBoundary)
                .build()
        )
    }

    private fun addMapLayers() {
        val layer = KmlLayer(map, R.raw.wmpk, requireContext())
        layer.addLayerToMap()
    }

    private fun zoomToMarker(marker: Marker) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.position, 17.5f))
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }

    companion object {
        private const val DEFAULT_LAT_VALUE = 50.075219
        private const val DEFAULT_LONG_VALUE = 19.997690
        private const val DEFAULT_ZOOM_VALE = 16.5f
        private const val MAX_ZOOM_VALE = 17.5f
        private const val MIN_ZOOM_VALE = 15f
        fun newInstance() = MapFragment()
    }
}
