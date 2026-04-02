package com.tao.mapboxdemo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.aiforcetech.map.MapBox
import com.tao.mapboxdemo.databinding.ActivityShowMapBinding

class ShowMapActivity : AppCompatActivity() {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, ShowMapActivity::class.java))
        }
    }

    private val mapBox: MapBox by lazy { MapBox() }

    private lateinit var mViewBinding: ActivityShowMapBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewBinding = ActivityShowMapBinding.inflate(layoutInflater)
        setContentView(mViewBinding.root)

        mapBox.initMapBox(mViewBinding.mapView, scope = lifecycleScope)

    }
}