package com.tao.mapboxdemo

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.aiforcetech.map.MapBox
import com.aiforcetech.map.entity.OptElement
import com.mapbox.geojson.Point
import com.tao.mapboxdemo.databinding.ActivityImageOptBinding
import com.tao.mapboxdemo.utils.PointUtil
import kotlin.random.Random

class ImageOptActivity : AppCompatActivity() {

    private val mapBox: MapBox by lazy { MapBox() }
    private var center = Point.fromLngLat(116.303273, 40.041439)

    private var imageMarker: OptElement.ImageOptElement? = null

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, ImageOptActivity::class.java))
        }
    }

    private lateinit var mViewBinding: ActivityImageOptBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewBinding = ActivityImageOptBinding.inflate(layoutInflater)
        setContentView(mViewBinding.root)
        mapBox.initMapBox(mViewBinding.mapView, scope = lifecycleScope, onMapClickListener = { result ->
            if (result.element==null){
                Log.d("ShowMap", "initView:  点击地图")
            }else{
                Log.d("ShowMap", "initView: result = ${result}")
                mapBox.updateImageSize(result.element as OptElement.ImageOptElement,10.0)
            }
        }) {
            mapBox.flyTo(center, 15.0)
        }

        mViewBinding.addBtn.setOnClickListener {
            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_marker_red)
            imageMarker = mapBox.addImage(center, bitmap)
        }
        mViewBinding.moveBtn.setOnClickListener {
            imageMarker?.let {
                val newPosition = PointUtil.generateRandomPointNear(center)
                mapBox.updateImagePosition(it, newPosition)
                mapBox.cameraTo(newPosition)
            }
        }
        mViewBinding.sizeBtn.setOnClickListener {
            imageMarker?.let {
                val newSize = Random.nextDouble(1.0, 8.0)
                mapBox.updateImageSize(it, newSize)
            }
        }
        mViewBinding.deleteBtn.setOnClickListener {
            imageMarker?.let {
                mapBox.removeElement(it)
                imageMarker = null
            }
        }

    }
}