package com.esspresso.nocnaukowcwpk.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.esspresso.nocnaukowcwpk.R
import com.esspresso.nocnaukowcwpk.config.RemoteConfigManager
import com.esspresso.nocnaukowcwpk.databinding.FragmentMapBinding
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
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
        map = googleMap
        googleMap.setOnMarkerClickListener(this)
        val faculties = config.getFaculties()
        faculties.forEach { addMarker(it.latLng) }
        zoomToAllMarkers(faculties.map { it.latLng })
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
        markers.forEach{
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
