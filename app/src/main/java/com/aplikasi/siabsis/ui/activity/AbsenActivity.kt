package com.aplikasi.siabsis.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.View
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet.VISIBLE
import androidx.core.content.ContextCompat
import com.aplikasi.siabsis.R
import com.aplikasi.siabsis.databinding.ActivityAbsenBinding
import com.aplikasi.siabsis.pref.UserPreference
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector

class AbsenActivity : AppCompatActivity(), OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks {
    private lateinit var binding: ActivityAbsenBinding

    private lateinit var pref: UserPreference

    private val requestCodeCameraPermission = 1001
    private lateinit var cameraSource: CameraSource
    private lateinit var barcodeDetector: BarcodeDetector
    private var scannedValue = ""

        private var mMap: GoogleMap? = null
    var mGoogleApiClient: GoogleApiClient? = null
    var mLocationRequest: LocationRequest? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAbsenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        pref = UserPreference(this)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) != android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(android.Manifest.permission.CAMERA),
                requestCodeCameraPermission
            )
        } else {
            setupControls()
        }

        binding.pbAbsensi1.visibility = View.VISIBLE
        binding.checklist.visibility = View.GONE


    }


    private fun setupControls() {
        barcodeDetector = BarcodeDetector.Builder(this)
            .setBarcodeFormats(Barcode.ALL_FORMATS)
            .build()

        cameraSource = CameraSource.Builder(this, barcodeDetector)
            .setRequestedPreviewSize(1920, 1024)
            .setAutoFocusEnabled(true)
            .build()

        binding.scanner.cameraSurfaceView.holder.addCallback(
            object : SurfaceHolder.Callback {
                override fun surfaceCreated(holder: SurfaceHolder) {
                    try {
                        if (ContextCompat.checkSelfPermission(
                                this@AbsenActivity,
                                android.Manifest.permission.CAMERA
                            ) != android.content.pm.PackageManager.PERMISSION_GRANTED
                        ) {
                            requestPermissions(
                                arrayOf(android.Manifest.permission.CAMERA),
                                requestCodeCameraPermission
                            )
                            return
                        }
                        cameraSource.start(binding.scanner.cameraSurfaceView.holder)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun surfaceChanged(
                    holder: SurfaceHolder,
                    format: Int,
                    width: Int,
                    height: Int
                ) {
                }

                override fun surfaceDestroyed(holder: SurfaceHolder) {
                    cameraSource.stop()
                }
            })
        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {
            }

            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                val barcodes = detections.detectedItems
                if (barcodes.size() == 1) {
                    scannedValue = barcodes.valueAt(0).rawValue
                    //Don't forget to add this line printing value or finishing activity must run on main thread
                    runOnUiThread {
                        cameraSource.stop()
                        Toast.makeText(
                            this@AbsenActivity,
                            "value- $scannedValue",
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.pbAbsensi1.visibility = android.view.View.GONE
                        binding.checklist.visibility = android.view.View.VISIBLE
                        binding.scanner.cameraSurfaceView.visibility = android.view.View.GONE
                        binding.framMaps.visibility = View.VISIBLE
                        check()
                    }
                } else {
                    check()
                }
            }
        })
    }

    @SuppressLint("ResourceAsColor")
    private fun check() {
        if (scannedValue != "") {
            binding.cvScanAbsensi.setCardBackgroundColor(ContextCompat.getColor(this, R.color.md_blue_400))
            binding.tvPindaiBarcode.setTextColor(ContextCompat.getColor(this, R.color.white))
        } else {
            binding.cvScanAbsensi.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white))
            binding.tvPindaiBarcode.setTextColor(ContextCompat.getColor(this, R.color.black))
//            Toast.makeText(this, "null", Toast.LENGTH_LONG).show()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap!!.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap!!.uiSettings.isZoomControlsEnabled = true
        mMap!!.uiSettings.isZoomGesturesEnabled = true
        mMap!!.uiSettings.isCompassEnabled = true
        val sydney = LatLng(-34.0, 151.0)
//        val lat = pref.getLatitude()
//        val long = pref.getLongitude()
//        val sydney = LatLng(lat.toDouble(), long.toDouble())
        mMap!!.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15f))

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
                mLocationRequest!!, this as com.google.android.gms.location.LocationListener
            )
        }
    }

    override fun onConnectionSuspended(i: Int) {}
}