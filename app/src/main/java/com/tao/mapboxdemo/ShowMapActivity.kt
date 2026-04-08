package com.tao.mapboxdemo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.aiforcetech.map.MapBox
import com.mapbox.geojson.Point
import com.tao.mapboxdemo.databinding.ActivityShowMapBinding

class ShowMapActivity : AppCompatActivity() {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, ShowMapActivity::class.java))
        }
    }

    private val mapBox: MapBox by lazy { MapBox() }
    private var center1 = Point.fromLngLat(116.303273, 40.041439)
    private var center2 = Point.fromLngLat(117.303273, 40.041439)

    private lateinit var mViewBinding: ActivityShowMapBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewBinding = ActivityShowMapBinding.inflate(layoutInflater)
        setContentView(mViewBinding.root)

        mapBox.initMapBox(mViewBinding.mapView, scope = lifecycleScope)

        mViewBinding.moveBtn.setOnClickListener {
            mapBox.cameraTo(center1,15.5)
        }
        mViewBinding.flyBtn.setOnClickListener {
            mapBox.flyTo(center2,18.0,3000)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        mapBox.release()
    }
}