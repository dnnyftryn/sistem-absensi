package com.aplikasi.siabsis.ui.activity.hrd

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.*
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.aplikasi.siabsis.R
import com.aplikasi.siabsis.data.model.Lokasi
import com.aplikasi.siabsis.databinding.ActivityMapsBinding
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import java.io.IOException
import java.util.*


class MapsActivity : FragmentActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityMapsBinding

    private var lokasi: MutableList<Lokasi> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

//        -6.762168, 108.436427
        lokasi.add(Lokasi("Kantor Polisi", -6.762122, 108.436427))
        lokasi.add(Lokasi("Kantor Polisi", -6.762133, 108.436421))
        lokasi.add(Lokasi("Kantor Polisi", -6.762143, 108.436423))
        lokasi.add(Lokasi("Kantor Polisi", -6.762165, 108.436422))

    }


    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isZoomGesturesEnabled = true
        googleMap.uiSettings.isScrollGesturesEnabled = true

        for (i in lokasi.indices) {
            val latLng = LatLng(lokasi[i].lat, lokasi[i].lng)
            googleMap.addMarker(MarkerOptions().position(latLng).title(lokasi[i].nama))
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15f))
        }
    }
}