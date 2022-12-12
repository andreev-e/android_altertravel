package com.altertravel

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.Marker

class MapsActivity : AppCompatActivity(),
    OnMapReadyCallback,
    LocationListener,
    GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener,
    ActivityCompat.OnRequestPermissionsResultCallback,
    GoogleMap.OnCameraMoveListener {

    private lateinit var mMap: GoogleMap
    private lateinit var locationManager: LocationManager
    private lateinit var currentLocation: LatLng
    private lateinit var currentLocationMarker: Marker
    private lateinit var mapFragment: MapFragment
    private var firstFixDone = false
    private var TAG = "MainAct"

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_BACKGROUND_LOCATION, false) -> {
                    Log.d(TAG, "Background location access granted.")
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    Log.d(TAG, "Precise location access granted.")
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    Log.d(TAG, "Only approximate location access granted.")
                }
                else -> {
                    Log.d(TAG, "No location access granted.")
                }
            }

            enableMap()
        }

        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        )

        Log.d(TAG, "onCreate")
    }

    private fun enableMap() {
        Log.d(TAG, "enableMap")
        val mapFragment = SupportMapFragment.newInstance()
        supportFragmentManager.beginTransaction().add(R.id.map, mapFragment)
            .commit()
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        Log.d(TAG, "onMapReady")
    }

    override fun onLocationChanged(location: Location) {
        Log.d(TAG, "onLocationChanged")
        currentLocation = LatLng(location.latitude, location.latitude)
        if (!firstFixDone) {
            currentLocationMarker = mMap.addMarker(
                MarkerOptions()
                    .position(currentLocation)
                    .title("Me")
            )!!
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation))
            firstFixDone = true
        }
        currentLocationMarker.position = currentLocation
    }

    override fun onCameraMove() {
        Log.d(TAG, "onCameraMove")
    }

    override fun onMyLocationButtonClick(): Boolean {
        Log.d(TAG, "onMyLocationButtonClick")
        return true
    }

    override fun onMyLocationClick(p0: Location) {
        Log.d(TAG, "onMyLocationClick")
    }
}