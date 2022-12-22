package com.altertravel;

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.SupportMapFragment

class PoiFormActivity: AppCompatActivity() {

    private var tag = "MainAct PoiForm"
    private lateinit var mapFragment: SupportMapFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.poi_form)

        mapFragment = SupportMapFragment.newInstance()
        supportFragmentManager.beginTransaction().add(R.id.poiLocationMap, mapFragment).commit()
    }
}
