package com.tao.mapboxdemo

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.aiforcetech.map.MapBox
import com.aiforcetech.map.entity.OptElement
import com.mapbox.geojson.Point
import com.tao.mapboxdemo.databinding.ActivityLumpOptBinding
import com.tao.mapboxdemo.utils.PointUtil
import com.tao.mapboxsimple.utils.PolygonUtil
import kotlin.random.Random

class LumpOptActivity : AppCompatActivity() {

    private lateinit var mViewBinding: ActivityLumpOptBinding

    private val mapBox: MapBox by lazy { MapBox() }
    private var center = Point.fromLngLat(116.303273, 40.041439)
    private val colors = intArrayOf(Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW,
        Color.CYAN, Color.MAGENTA, Color.DKGRAY)

    private val element: MutableList<OptElement.LumpOptElement> = mutableListOf()

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, LumpOptActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewBinding = ActivityLumpOptBinding.inflate(layoutInflater)
        setContentView(mViewBinding.root)
        mapBox.initMapBox(mapView = mViewBinding.mapView, scope = lifecycleScope, finished = {
            mapBox.flyTo(center)
        },
            onMapClickListener = { result ->

            })

        mViewBinding.addBtn.setOnClickListener {
            val pointCount = Random.nextInt(5, 10)
            val d = Random.nextDouble(0.1, 8.0)
            val p = PointUtil.generateRandomPointNear(center, d)
            val colorIndex = Random.nextInt(0, colors.size)
            if (pointCount % 2 == 0) {
                val radius = Random.nextDouble(0.005, 0.02)
                val points = PolygonUtil.generateIrregularPolygonPoints(p, pointCount, radius, 0.6f)
                mapBox.addPolygonLump(points, colors[colorIndex])?.let { element.add(it)
                    mapBox.flyTo(p,14.0)}
            }else{
                val radius = Random.nextDouble(5.0, 30.0)
                mapBox.addCircleLump(p,radius,colors[colorIndex])?.let { element.add(it)
                mapBox.flyTo(p,17.0)}
            }


        }
        mViewBinding.changeBtn.setOnClickListener {
            val index = Random.nextInt(0,element.size)
            val colorIndex =  Random.nextInt(0, colors.size)
            mapBox.updateLumpColor(element[index],colors[colorIndex])
        }

    }

}