package com.aplikasi.siabsis.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.Debug.getLocation
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.aplikasi.siabsis.R
import com.aplikasi.siabsis.databinding.ActivityMainBinding
import com.aplikasi.siabsis.helper.GPSHelper
import com.aplikasi.siabsis.pref.UserPreference
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val permissionId = 2

    private lateinit var pref: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        pref = UserPreference(this)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        getLocation()
        GPSHelper.getInstance(this)
        GPSHelper.getLocationByDevice(this)
        GPSHelper.initCallback(this)
        GPSHelper.isLocationEnabled(this)
    }



    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            permissionId
        )
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == permissionId) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLocation()
            }
        }
    }

//    @SuppressLint("MissingPermission", "SetTextI18n")
//    private fun getLocation() {
//        if (checkPermissions()) {
//            if (isLocationEnabled()) {
//                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
//                    val location: Location? = task.result
//                    if (location != null) {
//                        val geocoder = Geocoder(this, Locale.getDefault())
//                        val list: List<Address> =
//                            geocoder.getFromLocation(location.latitude, location.longitude, 1)
//                        binding.apply {
//                            val lat =  list[0].latitude
//                            val long = list[0].longitude
//                            pref.setLatitude(lat.toString())
//                            pref.setLongitude(long.toString())
//                            pref.setAddress(list[0].getAddressLine(0))
////                            tvLatitude.text = "Latitude\n${list[0].latitude}"
////                            tvLongitude.text = "Longitude\n${list[0].longitude}"
////                            tvCountryName.text = "Country Name\n${list[0].countryName}"
////                            tvLocality.text = "Locality\n${list[0].locality}"
////                            tvAddress.text = "Address\n${list[0].getAddressLine(0)}"
//                        }
//                    }
//                }
//            } else {
//                Toast.makeText(this, "Please turn on location", Toast.LENGTH_LONG).show()
//                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
//                startActivity(intent)
//            }
//        } else {
//            requestPermissions()
//        }
//    }
}