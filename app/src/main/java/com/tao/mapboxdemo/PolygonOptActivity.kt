package com.tao.mapboxdemo

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import androidx.lifecycle.lifecycleScope
import com.aiforcetech.map.MapBox
import com.aiforcetech.map.entity.ObstacleCircle
import com.aiforcetech.map.entity.ObstaclePolygon
import com.aiforcetech.map.entity.OptElement
import com.aiforcetech.map.entity.PolygonData
import com.mapbox.geojson.Point
import com.tao.mapboxdemo.databinding.ActivityPolygonOptBinding
import com.tao.mapboxsimple.utils.PolygonUtil
import kotlin.random.Random

class PolygonOptActivity : AppCompatActivity() {
    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, PolygonOptActivity::class.java))
        }
    }

    private lateinit var mViewBinding: ActivityPolygonOptBinding
    private val mapBox: MapBox by lazy { MapBox() }
    private var center = Point.fromLngLat(116.303273, 40.041439)
    private var polygonOptElement: OptElement.PolygonOptElement? = null
    private val colors = intArrayOf(Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW,
        Color.CYAN, Color.MAGENTA, Color.DKGRAY)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewBinding = ActivityPolygonOptBinding.inflate(layoutInflater)
        setContentView(mViewBinding.root)
        mapBox.initMapBox(mViewBinding.mapView, scope = lifecycleScope, onMapClickListener = { result->
            if (result.element==null){
                Log.d("Polygon", "initView:  点击地图")
            }else{
                Log.d("Polygon", "initView: result = ${result}")
                when(result.element){
                    is OptElement.PolygonOptElement -> {
//                        mapBox.updatePolygonStrokeWidthAndColor(result.element as OptElement.PolygonOptElement,15.0,
//                            Color.RED)
                        mapBox.removeElement(result.element)
                    }
                    is OptElement.PolygonObstacleElement -> {
                        mapBox.removeElement(result.element)
                    }
                    else -> {}
                }
            }
        }) {
            mapBox.flyTo(center, 12.0)
        }
        mViewBinding.addBtn.setOnClickListener {
            val outer1 = PolygonUtil.generateRectanglePoints(0.006, 0.0009)
            val obs1 = ObstacleCircle(center = center, diameter = 1.5, color = Color.RED)
            val polygon = PolygonData(outer = outer1, strokeWidth = 5.0, fillColor = "#5544FF00".toColorInt(),
                obstacles = listOf(obs1))
            polygonOptElement = mapBox.addPolygon(polygon)
        }
        mViewBinding.addMultipleBtn.setOnClickListener {
            val outer1 = PolygonUtil.generateRectanglePoints(0.007, 0.0008)
            val obs1 = ObstacleCircle(center = center, diameter = 1.5, color = Color.RED)
            val polygon1 = PolygonData(outer = outer1, strokeWidth = 3.0, strokeColor = Color.BLACK,
                fillColor = "#5533FF00".toColorInt(),
                obstacles = listOf(obs1))

            val outer2 = PolygonUtil.generateTrianglePoints(0.001)
            val polygon2 = PolygonData(outer = outer2, strokeColor = Color.YELLOW, fillColor = "#FF4444FF".toColorInt())

            val polygons: MutableList<PolygonData> = mutableListOf()
            polygons.add(polygon1)
            polygons.add(polygon2)
            mapBox.addPolygons(polygons)

        }

        mViewBinding.changeFillBtn.setOnClickListener {
            polygonOptElement?.let {
                val index = Random.nextInt(0, colors.size)
                mapBox.updatePolygonFillColor(it, colors[index])
            } ?: run {
                Toast.makeText(this@PolygonOptActivity, "先添加一个多边形", Toast.LENGTH_SHORT).show()
            }
        }
        mViewBinding.changeStrokeBtn.setOnClickListener {
            polygonOptElement?.let {
                val index = Random.nextInt(0, colors.size)
                val with = Random.nextDouble(1.0, 5.0)
                mapBox.updatePolygonStrokeWidthAndColor(it, strokeWidth = with, strokeColor = colors[index])
            } ?: run {
                Toast.makeText(this@PolygonOptActivity, "先添加一个多边形", Toast.LENGTH_SHORT).show()
            }
        }
    }
}