package com.esspresso.nocnaukowcwpk.main.map

import android.graphics.Color
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
import com.google.android.gms.maps.model.*
import com.google.maps.android.data.kml.KmlLayer
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

@AndroidEntryPoint
class MapFragment : Fragment(), GoogleMap.OnMarkerClickListener {
    @Inject
    internal lateinit var config: RemoteConfigManager

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
        val layer = KmlLayer(googleMap, R.raw.wmpk, requireContext())
        layer.addLayerToMap()
        map = googleMap
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style))
        googleMap.setOnMarkerClickListener(this)

/*
      val faculties = config.getFaculties()
        faculties.forEach { addMarker(it.latLng) }
        zoomToAllMarkers(faculties.map { it.latLng })
*/
/*        val polygon = map.addPolygon(
            PolygonOptions()
            .add(LatLng(50.07467944344386, 19.997579846059555), LatLng(50.074693214731255, 19.997799787198776), LatLng(50.075552116318185, 19.997684364724797), LatLng(50.07553604953736, 19.997464799122458))
            .strokeColor(Color.TRANSPARENT)
            .fillColor(Color.YELLOW)
            .strokeWidth(0f))*/

        val bounds = LatLngBounds.Builder()
            .include(LatLng(50.07507, 19.9967715))
            .include(LatLng(50.07507, 19.9967715))
            .include(LatLng(50.0755626, 19.995489))
            .include(LatLng(50.0757269, 19.9955914))
            .build()
        map.setLatLngBoundsForCameraTarget(bounds)
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(50.075219, 19.997690), 16.5f))

    }

    private fun addMarker(latLng: LatLng) {
        map.addMarker(MarkerOptions().position(latLng).title("Marker in Sydney"))
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        marker?.let {
            it.showInfoWindow()
            zoomToMarker(it)
        }
        return true
    }

    private fun zoomToMarker(marker: Marker) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.position, 17f))
    }

    private fun zoomToAllMarkers(markers: List<LatLng>) {
        val builder = LatLngBounds.Builder()
        markers.forEach {
            builder.include(it)
        }
        val cu = CameraUpdateFactory.newLatLngBounds(builder.build(), 100)
        map.animateCamera(cu)
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }

    companion object {
        fun newInstance() = MapFragment()
    }
}
