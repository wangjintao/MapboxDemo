package com.tao.mapboxdemo

import android.content.Context
import android.content.Intent
import android.graphics.Color
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
import com.tao.mapboxdemo.databinding.ActivityCircleOptBinding
import com.tao.mapboxdemo.utils.PointUtil
import kotlin.random.Random

class CircleOptActivity : AppCompatActivity() {
    private lateinit var mViewBinding: ActivityCircleOptBinding

    private val mapBox: MapBox by lazy { MapBox() }
    private var center = Point.fromLngLat(116.303273, 40.041439)

    private var circleMarker: OptElement.CircleOptElement? = null
    private var circleMarkerList: MutableList<OptElement.CircleOptElement> = mutableListOf()
    private val colors = intArrayOf(Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW,
        Color.CYAN, Color.MAGENTA, Color.DKGRAY)

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, CircleOptActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewBinding = ActivityCircleOptBinding.inflate(layoutInflater)
        setContentView(mViewBinding.root)
        mapBox.initMapBox(mViewBinding.mapView, scope = lifecycleScope, onMapClickListener = { result->
            if (result.element==null){
                Log.d("Circle", "initView:  点击地图")
            }else{
                Log.d("Circle", "initView: result = ${result}")
                mapBox.updateCircleColor(result.element as OptElement.CircleOptElement, Color.YELLOW)
            }
        }) {
            mapBox.flyTo(center, 15.0, 3000)
        }

        mViewBinding.addBtn.setOnClickListener {
            if (circleMarker == null)
                circleMarker = mapBox.addCircle(center, Color.RED, 6.0)
        }
        mViewBinding.addMultipleBtn.setOnClickListener {
            val tempPointList: MutableList<Point> = mutableListOf()
            for (i in 0 until 5) {
                tempPointList.add(PointUtil.generateRandomPointNear(center))
            }
            val circleMarkers = mapBox.addCircles(tempPointList, Color.GREEN, 5.0)
            circleMarkerList.addAll(circleMarkers)
        }
        mViewBinding.changeLocationBtn.setOnClickListener {
            circleMarker?.let {
                val newLocation = PointUtil.generateRandomPointNear(center, 2.0)
                mapBox.updateCirclePosition(it, newLocation)
                mapBox.cameraTo(newLocation)
            } ?: run {
                if (circleMarkerList.isNotEmpty()) {
                    val index = Random.nextInt(0, circleMarkerList.size)
                    val newLocation = PointUtil.generateRandomPointNear(center, 2.0)
                    mapBox.updateCirclePosition(circleMarkerList[index], newLocation)
                }

            }
        }
        mViewBinding.changeRadiusBtn.setOnClickListener {
            circleMarker?.let {
                val newRadius = Random.nextDouble(1.0, 10.0)
                mapBox.updateCircleRadius(it, newRadius)

            } ?: run {
                if (circleMarkerList.isNotEmpty()) {
                    val index = Random.nextInt(0, circleMarkerList.size)
                    val newRadius = Random.nextDouble(1.0, 10.0)
                    mapBox.updateCircleRadius(circleMarkerList[index], newRadius)
                }
            }
        }
        mViewBinding.changeColorBtn.setOnClickListener {
            circleMarker?.let {
                val index = Random.nextInt(0, colors.size)
                mapBox.updateCircleColor(it,colors[index])

            } ?: run {
                if (circleMarkerList.isNotEmpty()) {
                    val markerIndex = Random.nextInt(0, circleMarkerList.size)
                    val colorIndex = Random.nextInt(0, colors.size)
                    mapBox.updateCircleColor(circleMarkerList[markerIndex],colors[colorIndex])
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        mapBox.release()
    }
}