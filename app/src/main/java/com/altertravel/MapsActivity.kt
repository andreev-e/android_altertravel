package com.altertravel

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions


class MapsActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener,
    ActivityCompat.OnRequestPermissionsResultCallback, GoogleMap.OnCameraMoveListener,
    LocationListener {

    private lateinit var mMap: GoogleMap
    private lateinit var currentLocation: LatLng
    private lateinit var currentLocationMarker: Marker
    private lateinit var mapFragment: SupportMapFragment
    private var firstFixDone = false
    private var TAG = "MainAct"
    private lateinit var permissionRequest: ActivityResultLauncher<Array<String>>

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        mapFragment = SupportMapFragment.newInstance()
        supportFragmentManager.beginTransaction().add(R.id.map, mapFragment).commit()
        mapFragment.getMapAsync(this)

        permissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    Log.d(TAG, "Precise location access granted.")
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    Log.d(TAG, "Only approximate location access granted.")
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_BACKGROUND_LOCATION, false) -> {
                    Log.d(TAG, "Background location access granted.")
                }
                else -> {
                    Log.d(TAG, "No location access granted.")
                }
            }

        }
        checkPermissions()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onResume() {
        super.onResume()
        checkPermissions()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun checkPermissions() {
        val locationPermissionsButton = findViewById<Button>(R.id.grant_permissions)
        val backgroundPermissionsButton = findViewById<Button>(R.id.grant_background_permissions)
        locationPermissionsButton.setVisibility(View.INVISIBLE)
        backgroundPermissionsButton.setVisibility(View.INVISIBLE)

        locationPermissionsButton.setOnClickListener {
            grantLocationPermissions()
        };

        backgroundPermissionsButton.setOnClickListener {
            grantBackgroundPermissions()
        };

        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionsButton.setVisibility(View.VISIBLE)
        } else {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                backgroundPermissionsButton.setVisibility(View.VISIBLE)
            }
        }

    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap.isMyLocationEnabled = true
            Toast.makeText(this, "isMyLocationEnabled", Toast.LENGTH_LONG).show()
        }

        Log.d(TAG, "onMapReady")
        //todo
        // LocationServices.FusedLocationApi.requestLocationUpdates(googleMap, mLocationRequest, this);
    }

    override fun onLocationChanged(location: Location) {
        Log.d(TAG, "onLocationChanged")
        currentLocation = LatLng(location.latitude, location.latitude)
        if (!firstFixDone) {
            currentLocationMarker = mMap.addMarker(
                MarkerOptions().position(currentLocation).title("Me")
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
        Toast.makeText(this, "onMyLocationButtonClick", Toast.LENGTH_LONG).show()
        return true
    }

    override fun onMyLocationClick(p0: Location) {
        Log.d(TAG, "onMyLocationClick")
        Toast.makeText(this, "onMyLocationClick", Toast.LENGTH_LONG).show()

    }

    fun addPoi(view: View) {
        Toast.makeText(this, "addPoi pressed", Toast.LENGTH_LONG).show()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun grantLocationPermissions() {
        permissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun grantBackgroundPermissions() {
        Log.d(TAG, "grantBackgroundPermissions")

        permissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        )

        Toast.makeText(this, "grantPermissions pressed", Toast.LENGTH_LONG).show()
    }

}