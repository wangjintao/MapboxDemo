package com.tao.mapboxdemo.utils

import com.mapbox.geojson.Point
import kotlin.math.PI
import kotlin.math.asin
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

/**
 *Author: WangJintao
 * Date: 2026/4/2 17:52
 **/
object PointUtil {

    /**
     * 在给定点附近 5 公里范围内随机生成一个点
     * @param center 中心点 (MapBox Point)
     * @param radiusKm 半径范围，默认 5 公里
     * @return 新生成的随机点
     */
    fun generateRandomPointNear(center: Point, radiusKm: Double = 5.0): Point {
        val earthRadiusMeters = 6371000.0 // 地球平均半径，单位：米
        val radiusMeters = radiusKm * 1000.0

        // 将中心点的经纬度转换为弧度
        val latRad = Math.toRadians(center.latitude())
        val lonRad = Math.toRadians(center.longitude())

        val random = Random.Default

        // 生成随机距离 (均匀分布在圆盘内，使用 sqrt 校正)
        val distance = radiusMeters * sqrt(random.nextDouble())
        // 生成随机方位角 [0, 2π)
        val bearing = random.nextDouble(0.0, 2 * PI)

        val delta = distance / earthRadiusMeters   // 角距离 (弧度)
        val sinDelta = sin(delta)
        val cosDelta = cos(delta)
        val sinLat = sin(latRad)
        val cosLat = cos(latRad)
        val sinBearing = sin(bearing)
        val cosBearing = cos(bearing)

        // 计算新纬度
        val newLatRad = asin(
            sinLat * cosDelta + cosLat * sinDelta * cosBearing
        )

        // 计算新经度
        val newLonRad = lonRad + atan2(
            sinBearing * sinDelta * cosLat,
            cosDelta - sinLat * sin(newLatRad)
        )

        // 转换回度数并返回 Point 对象
        return Point.fromLngLat(
            Math.toDegrees(newLonRad),
            Math.toDegrees(newLatRad)
        )
    }
}