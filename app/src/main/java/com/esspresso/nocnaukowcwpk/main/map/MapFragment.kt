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
        setInfoWindowAdapter()
        setupLatLngCameraMoveBoundaries()
        addBuildingMarkers()
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(50.075219, 19.997690), 16.5f))
    }

    private fun addBuildingMarkers() {
        markerHelper.getBuildingMarkers().forEach(map::addMarker)
    }

    private fun setupMap() {
        val layer = KmlLayer(map, R.raw.wmpk, requireContext())
        layer.addLayerToMap()
        map.isBuildingsEnabled = false
        map.uiSettings.setAllGesturesEnabled(true)
        map.setMaxZoomPreference(17.5f)
        map.setMinZoomPreference(15f)
        map.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style))
    }

    private fun setInfoWindowAdapter() {
       /* map.setInfoWindowAdapter(object: GoogleMap.InfoWindowAdapter {
            override fun getInfoContents(p0: Marker): View?  = null
            override fun getInfoWindow(marker: Marker): View {
                val binding = ViewMapInfoWindowBinding.inflate(layoutInflater, null, false)
                marker.showInfoWindow()
                return binding.root
            }
        })*/
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

    private fun zoomToMarker(marker: Marker) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.position, 17.5f))
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }

    companion object {
        fun newInstance() = MapFragment()
    }
}
