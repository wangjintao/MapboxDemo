package com.tao.mapboxdemo

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.aiforcetech.map.MapBox
import com.aiforcetech.map.entity.OptElement
import com.mapbox.geojson.Point
import com.tao.mapboxdemo.databinding.ActivityPolygonOptBinding
import com.tao.mapboxdemo.utils.PointUtil
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
        mapBox.initMapBox(mViewBinding.mapView, scope = lifecycleScope, onMapClickListener = { result ->
            if (result.element == null) {
                Log.d("Polygon", "initView:  点击地图")
            } else {
                Log.d("Polygon", "initView: result = ${result}")
                when (result.element) {
                    is OptElement.PolygonOptElement -> {
                        mapBox.updatePolygonStrokeWidthAndColor(result.element as OptElement.PolygonOptElement, 15.0,
                            Color.RED)
//                        mapBox.removeElement(result.element)
                    }

                    else -> {}
                }
            }
        }) {
            mapBox.flyTo(center, 12.0)
        }
        mViewBinding.addBtn.setOnClickListener {
            val points =
                PolygonUtil.generateIrregularPolygonPoints(pointCount = 9, baseRadius = 0.002, irregularity = 0.5f)
            polygonOptElement = mapBox.addPolygon(points, strokeColor = Color.BLACK, fillColor = Color.GREEN)
        }
        mViewBinding.addMultipleBtn.setOnClickListener {

            val polygon1 =
                PolygonUtil.generateIrregularPolygonPoints(basePoint = PointUtil.generateRandomPointNear(center),
                    pointCount = 5, baseRadius = 0.002, irregularity = 0.5f)
            val polygon2 =
                PolygonUtil.generateIrregularPolygonPoints(basePoint = PointUtil.generateRandomPointNear(center),
                    pointCount = 7, baseRadius = 0.01, irregularity = 0.6f)
            val polygons: MutableList<List<Point>> = mutableListOf()
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

    override fun onDestroy() {
        super.onDestroy()
        mapBox.release()
    }
}