package com.aplikasi.siabsis.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.SurfaceHolder
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.aplikasi.siabsis.R
import com.aplikasi.siabsis.databinding.ActivityAbsenBinding
import com.aplikasi.siabsis.helper.GPSHelper
import com.aplikasi.siabsis.pref.UserPreference
import com.google.android.gms.location.FusedLocationProviderClient
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
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.*

class AbsenActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityAbsenBinding

    private lateinit var pref: UserPreference

    private val requestCodeCameraPermission = 1001
    private lateinit var cameraSource: CameraSource
    private lateinit var barcodeDetector: BarcodeDetector
    private var scannedValue = ""
    private var mMap: GoogleMap? = null

    private var status: String? = null
    private var keterangan: String? = null
    private var current: String? = null

    companion object {
        var latitude = 0.0
        var longitude = 0.0
        var tempLocation: Location? = null
    }

    private lateinit var gpsHelper: GPSHelper

    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val permissionId = 2

    private lateinit var dbRef: DatabaseReference

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAbsenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val nama = binding.etNama.text.toString().trim()
        dbRef = FirebaseDatabase.getInstance().getReference("absen/$nama")

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        getLocation()
        gpsHelper = GPSHelper.getInstance(this)!!

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
        val bundle = intent.extras
        status = bundle?.getString("absen")
        binding.statusAbsen.setText("$status")
        binding.email.setText(pref.getUser())
        kirimData()

        val time = LocalTime.now()
        val time1 = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm")
        current = formatter.format(time1)

        keterangan = when(time.hour){
            in 8..16 -> {
                "terlambat"
            } else -> {
                "tepat waktu"
            }
        }

    }

    private fun kirimData() {
        binding.kirim.setOnClickListener {
            val qrCode = scannedValue.trim()
            val status = status
            val nama = binding.etNama.text.toString().trim()
            val email = binding.email.text.toString().trim()
            val divisi = binding.eteDivisi.text.toString().trim()

            Log.d("check data", "$nama, $divisi, $qrCode, $status $email, $current, $keterangan ")
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
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

    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun getLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    val location: Location = task.result
                    latitude  = location.latitude
                    longitude = location.longitude
                    Log.d("TAG", "latlng: $latitude $longitude")
                    Snackbar.make(
                        binding.root,
                        "Lat: ${location.latitude} Long: ${location.longitude}",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            } else {
                Toast.makeText(this, "Please turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
            Log.d("TAG", "requestLocationPermission: permission granted")
            GPSHelper.initLocation(this)
        }
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
                        binding.scanner.cameraSurfaceView.visibility = android.view.View.GONE
                        binding.framMaps.visibility = View.VISIBLE
//                        binding.qrCode.setText(scannedValue)
                    }
                }
            }
        })
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap!!.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap!!.uiSettings.isZoomControlsEnabled = true
        mMap!!.uiSettings.isZoomGesturesEnabled = true
        mMap!!.uiSettings.isCompassEnabled = true

        val sydney = LatLng(latitude, longitude)
//        val lat = pref.getLatitude()
//        val long = pref.getLongitude()
//        val sydney = LatLng(lat.toDouble(), long.toDouble())
        mMap!!.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15f))

    }
}