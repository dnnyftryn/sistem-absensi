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


class MapsActivity : FragmentActivity(), OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, LocationListener {
    var mGoogleApiClient: GoogleApiClient? = null
    var mLastLocation: Location? = null
    var mCurrLocationMarker: Marker? = null
    var mLocationRequest: LocationRequest? = null
    private var mMap: GoogleMap? = null
    private lateinit var binding: ActivityMapsBinding
    lateinit var geofencingClient: GeofencingClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        geofencingClient = LocationServices.getGeofencingClient(this)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap!!.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap!!.uiSettings.isZoomControlsEnabled = true
        mMap!!.uiSettings.isZoomGesturesEnabled = true
        mMap!!.uiSettings.isCompassEnabled = true
//        val sydney = LatLng(-34.0, 151.0)
        val indo = mMap!!.addCircle(
            CircleOptions()
                .center(LatLng(-6.1753924, 106.827153))
                .radius(100.0)
                .strokeColor(Color.RED)
                .fillColor(Color.TRANSPARENT)
        )
        mMap!!.addMarker(MarkerOptions().position(indo.center).title("Marker in Sydney"))
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(indo.center))

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            buildGoogleApiClient();
            mMap!!.isMyLocationEnabled = true;
        }
    }

    @Synchronized
    protected fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()
        mGoogleApiClient!!.connect()
    }

    override fun onConnected(bundle: Bundle?) {
        mLocationRequest = LocationRequest()
        mLocationRequest!!.interval = 1000
        mLocationRequest!!.fastestInterval = 1000
        mLocationRequest!!.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient!!,
                mLocationRequest!!, this
            )
        }
    }

    override fun onConnectionSuspended(i: Int) {}
    override fun onLocationChanged(location: Location) {
        mLastLocation = location
        mCurrLocationMarker?.remove()
        val latLng = LatLng(location.latitude, location.longitude)
        val markerOptions = MarkerOptions().position(latLng).title("Current Position")
//        markerOptions.position(latLng)
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val provider = locationManager.getBestProvider(Criteria(), true)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        val locations = locationManager.getLastKnownLocation(provider!!)
        val providerList = locationManager.allProviders
        if (null != locations && null != providerList && providerList.size > 0) {
            val longitude = locations.longitude
            val latitude = locations.latitude
            val geocoder = Geocoder(
                applicationContext,
                Locale.getDefault()
            )
            val results = FloatArray(1)
//            -6.471504, 106.889377
            val loc = Location.distanceBetween(
                location.latitude,
                location.longitude,
                -6.471504,
                106.889377,
                results
            )
            val radiusInMeters = 20.0 * 1.0 //1 KM = 1000 Meter
            val distance = results[0]
            mMap!!.addCircle(
                CircleOptions()
                    .center(LatLng(-6.471504, 106.889377))
                    .radius(radiusInMeters)
                    .strokeColor(Color.RED)
                    .fillColor(Color.TRANSPARENT)
            )
            try {
                val listAddresses: List<Address>? = geocoder.getFromLocation(
                    latitude,
                    longitude, 1
                )
                if (null != listAddresses && listAddresses.size > 0) {
                    val state: String = listAddresses[0].getAdminArea()
                    val country: String = listAddresses[0].getCountryName()
                    val subLocality: String = listAddresses[0].getSubLocality()
                    markerOptions.title(
                        "" + latLng + "," + subLocality + "," + state
                                + "," + country
                    )
//                    binding.btnAbsen.setOnClickListener {
//                        Toast.makeText(this,"$latLng, $subLocality, $state, $country" , Toast.LENGTH_SHORT).show()
//                    }
                    binding.btnAbsen.setOnClickListener {
                        if (distance <= radiusInMeters) {
                            Toast.makeText(this, "Anda Berada Di Dalam Radius", Toast.LENGTH_SHORT).show()
                            Toast.makeText(this, "lattitude: $latitude, longitude: $longitude", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Anda Berada Di Luar Radius", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        mCurrLocationMarker = mMap!!.addMarker(markerOptions)
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap!!.animateCamera(CameraUpdateFactory.zoomTo(11f))
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient!!,
                this
            )
        }
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {}
    fun checkLocationPermission(): Boolean {
        return if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION
                )
            } else {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION
                )
            }
            false
        } else {
            true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient()
                        }
                        mMap!!.isMyLocationEnabled = true
                    }
                } else {
                    Toast.makeText(
                        this, "permission denied",
                        Toast.LENGTH_LONG
                    ).show()
                }
                return
            }
        }
    }

    companion object {
        const val MY_PERMISSIONS_REQUEST_LOCATION = 99
    }
}