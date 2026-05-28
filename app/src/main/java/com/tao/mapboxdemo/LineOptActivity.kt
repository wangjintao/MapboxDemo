package com.tao.mapboxdemo

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.aiforcetech.map.MapBox
import com.aiforcetech.map.entity.OptElement
import com.mapbox.geojson.Point
import com.tao.mapboxdemo.databinding.ActivityLineOptBinding
import com.tao.mapboxdemo.utils.PointUtil
import com.tao.mapboxsimple.utils.LineUtil
import kotlin.math.log
import kotlin.random.Random

class LineOptActivity : AppCompatActivity() {

    private lateinit var mViewBinding: ActivityLineOptBinding

    private val mapBox: MapBox by lazy { MapBox() }
    private var center = Point.fromLngLat(116.303273, 40.041439)

    private var lineOptElement: OptElement.LineOptElement? = null
    private var linesOptElement: MutableList<OptElement.LineOptElement> = mutableListOf()
    private val colors = intArrayOf(Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW,
        Color.CYAN, Color.MAGENTA, Color.DKGRAY)

    companion object {
        private const val TAG = "LineOpt"
        fun start(context: Context) {
            context.startActivity(Intent(context, LineOptActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewBinding = ActivityLineOptBinding.inflate(layoutInflater)
        setContentView(mViewBinding.root)
        mapBox.initMapBox(mViewBinding.mapView, scope = lifecycleScope, onMapClickListener = { result->
            if (result.element==null){
                Log.d(TAG, "initView:  点击地图")
            }else{
                Log.d(TAG, "initView: result = ${result}")
                mapBox.updateLineStyle(result.element as OptElement.LineOptElement , width = 50.0)
            }
        }) {
            mapBox.flyTo(center, 11.8)
        }

        mViewBinding.addBtn.setOnClickListener {
            val line = LineUtil.generateTrajectory(center, 50)
            lineOptElement = mapBox.addLine(line, Color.GREEN, 2.5)

        }

        mViewBinding.addMultipleBtn.setOnClickListener {
            val lines: MutableList<List<Point>> = mutableListOf()
            for (i in 0 until 5) {
                val line = LineUtil.generateTrajectory(PointUtil.generateRandomPointNear(center = center))
                lines.add(line)
            }
            val opts = mapBox.addLines(lines = lines, color = Color.RED, width = 3.2)
            linesOptElement.addAll(opts)

        }

        mViewBinding.addPointBtn.setOnClickListener {
            lineOptElement?.let {
                val newPoint = PointUtil.generateRandomPointNear(center, 0.5)
                mapBox.appendLinePoint(it, newPoint)
            } ?: run {
                if (linesOptElement.isNotEmpty()) {
                    val index = Random.nextInt(0, linesOptElement.size)
                    linesOptElement[index].let { line ->
                        val newPoint = PointUtil.generateRandomPointNear(center, 0.5)
                        mapBox.appendLinePoint(line, newPoint)
                    }

                }
            }
        }

        mViewBinding.changeStyleBtn.setOnClickListener {

            lineOptElement?.let {
                val colorIndex = Random.nextInt(0, colors.size)
                mapBox.updateLineStyle(it, color = colors[colorIndex], width = Random.nextDouble(0.1, 5.0))
            } ?: run {
                if (linesOptElement.isNotEmpty()) {
                    val index = Random.nextInt(0, linesOptElement.size )
                    linesOptElement[index].let { line ->
                        val colorIndex = Random.nextInt(0, colors.size)
                        mapBox.updateLineStyle(line, color = colors[colorIndex], width = Random.nextDouble(0.1, 5.0))
                    }
                }

            }

        }
        mViewBinding.deleteBtn.setOnClickListener {
            lineOptElement?.let {
                mapBox.removeElement(it)
                lineOptElement=null
            }?:run {
                if (linesOptElement.isNotEmpty()){
                    val index = Random.nextInt(0, linesOptElement.size)
                    Log.i(TAG, "onCreate: size=${linesOptElement.size},index=$index")
                    linesOptElement[index].let {
                        mapBox.removeElement(it)
                        linesOptElement.remove(it)
                    }
                }
            }
        }

    }
}