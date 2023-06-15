package com.aplikasi.siabsis.helper

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.core.app.ActivityCompat.requestPermissions
import com.aplikasi.siabsis.ui.activity.AbsenActivity
import com.aplikasi.siabsis.ui.activity.LoginActivity
import com.google.android.gms.location.*


object GPSHelper {

    var moclat = 0.0
    var moclong = 0.0
    var instance: GPSHelper? = null

    fun getInstance(context: Context): GPSHelper? {
        if (instance == null) {
            Log.d("LOCATION", "Inisialisasi GPSHelper.")
            instance = GPSHelper
            instance!!.initCallback(context)
        }
        return instance
    }

    private val locationRequest: LocationRequest = LocationRequest.create().apply {
        interval = 7000
        fastestInterval =3000
        priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
    }

    var callback: LocationCallback? = null

    fun isLocationEnabled(mActivity: Activity) {
        // lokasi sudah diberikan izin
        val locationManager = mActivity.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager

        if (ActivityCompat.checkSelfPermission(
                mActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                mActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        } else {
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        }

        var lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if (lastKnownLocation == null){
            lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val mock = lastKnownLocation?.isMock
            Log.d("TAG", "isLocationEnabled1: ${lastKnownLocation?.isMock}")
            if (mock == true) {
                Toast.makeText(mActivity, "Anda Menggunakan Fake GPS", Toast.LENGTH_LONG).show()
                val intent = Intent(mActivity, LoginActivity::class.java)
                intent.putExtra("isMock", true)
                mActivity.startActivity(intent)
                mActivity.finish()
            }
        } else {
            val mock = lastKnownLocation?.isFromMockProvider
            Log.d("TAG", "isLocationEnabled2: ${lastKnownLocation?.isFromMockProvider}")
            if (mock == true) {
                Toast.makeText(mActivity, "Anda Menggunakan Fake GPS", Toast.LENGTH_LONG).show()
                val intent = Intent(mActivity, LoginActivity::class.java)
                intent.putExtra("isMock", true)
                mActivity.startActivity(intent)
                mActivity.finish()
            }
        }

    }

    fun initCallback(mContext: Context) {
        if (callback == null) {
            callback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    Log.d("TAG", "inisialisasi locationCallback")
                    LocationServices
                        .getFusedLocationProviderClient(mContext)
                        .removeLocationUpdates(this)

                    Log.d("TAG", "removeLocationUpdates : DONE")
                    Log.d("TAG", "onLocationResult: $locationResult")

                    if (locationResult != null) {
                        Log.d("TAG", "onLocationResult: have value")
                        val locations = locationResult.locations

                        for (lo in locations) {
                            if (lo.isFromMockProvider) {
                                Log.d("TAG", "onLocationResult: isFromMockProvider")
                                moclat = lo.latitude
                                moclong = lo.longitude
                                // delete mock dari list lokasi
                                locations.remove(lo)
                            }
                        }

                        if (locations.size <= 0) {
                            Log.d("TAG", "onLocationResult: null || spoofed")
                            Log.d("TAG", "onLocationResult: $moclat, $moclong")
                            Log.d("TAG", "spoof: ${locations.size}")
//                            TrackerReportAPI.reportSpoof(mContext, moclat, moclong)
                        }

                        else {
                            Log.d("TAG", "onLocationResult: has value")
                            val latestLocationIndex = locationResult.locations.size - 1
                            val liveLocation = locationResult.locations[latestLocationIndex]
                            val mLat = liveLocation.latitude
                            val mLng = liveLocation.longitude
                            Log.d("TAG", "onLocationResult: $mLat, $mLng")

                            AbsenActivity.latitude = mLat
                            AbsenActivity.longitude = mLng

                            if (mContext is AbsenActivity) {
                                Log.d("TAG", "onLocationResult: Context is of service")
                            }
                            else {
                                Log.d("TAG", "onLocationResult: Context is not of service")
                            }
                        }
                    }
                    else {
                        Log.d("TAG", "onLocationResult: null")
                    }
                }
            }
        } else {
            Log.d("TAG", "initCallback: callback is not null")
        }
    }

    fun initLocation(mActivity: Activity) {
        getInstance(mActivity)
        Log.d("TAG", "initLocation: ")
        if (ActivityCompat.checkSelfPermission(
                mActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                mActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("TAG", "initLocation: permission not granted")
            ActivityCompat.requestPermissions(
                mActivity as Activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                99
            )
        }
        else {
            Log.d("TAG", "initLocation: permission granted")
            LocationServices
                .getFusedLocationProviderClient(mActivity)
                .requestLocationUpdates(locationRequest, callback!!, Looper.getMainLooper())
            getLocationByDevice(mActivity)
        }
    }

    fun getLocationByDevice(mContext: Context) {
        Log.d("TAG", "getLocationByDevice: ")
        val locationManager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val lo = if (ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        } else {
            requestPermissions(
                mContext as Activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                99
            )
            return
        }


        if (lo != null) {
            if (lo.isFromMockProvider){
                Log.d("TAG", "getLocationByDevice: isFromMockProvider")
            }
            else {
                Log.d("TAG", "getLocationByDevice: is not from mock")
            }
        }
    }

}