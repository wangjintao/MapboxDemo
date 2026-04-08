package com.tao.mapboxsimple.utils

import com.mapbox.geojson.Point
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin
import kotlin.random.Random

/**
 *Author: WangJintao
 * Date: 2025/11/13 16:52
 **/
object LineUtil {
    fun generateTrajectory(
        startPoint: Point,
        numPoints: Int = 50
    ): List<Point> {
        val points = mutableListOf<Point>()
        var currentLon = startPoint.longitude()
        var currentLat = startPoint.latitude()

        // 初始方向（向东偏北，30度）
        var direction = Math.toRadians(30.0)

        for (i in 0 until numPoints) {
            points.add(Point.fromLngLat(currentLon, currentLat))

            // 根据行驶阶段调整参数
            val (distance, directionChange) = when {
                i < 15 -> {
                    Pair(0.001 + Random.nextDouble(0.0005), Random.nextDouble(-5.0, 5.0))
                }
                i < 35 -> {

                    Pair(0.0008 + Random.nextDouble(0.0003), Random.nextDouble(-15.0, 15.0))
                }
                else -> {
                    Pair(0.0005 + Random.nextDouble(0.0004), Random.nextDouble(-8.0, 8.0))
                }
            }

            // 更新方向
            direction += Math.toRadians(directionChange)

            // 限制方向变化范围（-90度到90度）
            direction = max(Math.toRadians(-90.0), min(Math.toRadians(90.0), direction))

            // 计算新坐标
            val deltaLon = distance * cos(direction)
            val deltaLat = distance * sin(direction)

            currentLon += deltaLon
            currentLat += deltaLat

            // 10%概率模拟停车（添加重复点）
            if (Random.nextDouble() < 0.08 && points.size < numPoints - 2) {
                points.add(Point.fromLngLat(currentLon, currentLat))
                points.add(Point.fromLngLat(currentLon, currentLat))
            }
        }

        // 确保返回指定数量的点
        return points.take(numPoints)
    }
}