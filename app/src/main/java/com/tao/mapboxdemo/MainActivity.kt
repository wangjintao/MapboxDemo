package com.tao.mapboxdemo

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tao.mapboxdemo.databinding.ActivityMainBinding
import com.tao.mapboxdemo.databinding.ItemFunctionListBinding

class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = "MainActivity"
        private val functionNames = listOf("显示地图", "图片", "圆点", "线", "多边形")

    }

    private lateinit var mViewBinding: ActivityMainBinding
    private lateinit var mAdapter: FunctionAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mViewBinding.root)
        initRecycleView()
    }

    private fun initRecycleView() {
        mAdapter = FunctionAdapter { name ->
            Log.d(TAG, "initRecycleView: $name")
            when (name) {
                functionNames[0] -> {
                    ShowMapActivity.start(this)

                }
            }

        }
        mViewBinding.functionList.layoutManager = GridLayoutManager(this, 2)
        mViewBinding.functionList.adapter = mAdapter
        mAdapter.submitList(functionNames)
    }

}

class FunctionAdapter(private val onButtonClick: (String) -> Unit) :
    ListAdapter<String, FunctionAdapter.FunctionViewHolder>(FunctionDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup,
        viewType: Int): FunctionViewHolder {
        val binding = ItemFunctionListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FunctionViewHolder(binding)

    }

    override fun onBindViewHolder(holder: FunctionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    inner class FunctionViewHolder(private val binding: ItemFunctionListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(content: String) {
            binding.funBtn.text = content
            binding.funBtn.setOnClickListener { onButtonClick(content) }
        }


    }

    private class FunctionDiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

    }
}

