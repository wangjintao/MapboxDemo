package com.tao.mapboxsimple.utils

import com.mapbox.geojson.Point

/**
 *Author: WangJintao
 * Date: 2025/11/20 15:38
 **/
object PolygonUtil {

    val centerLng = 116.303273
    val centerLat = 40.041439

    fun generateRectanglePoints(offsetLng: Double, offsetLat: Double): List<Point> {
        val p1 = Point.fromLngLat(centerLng - offsetLng, centerLat - offsetLat)
        val p2 = Point.fromLngLat(centerLng + offsetLng, centerLat - offsetLat)
        val p3 = Point.fromLngLat(centerLng + offsetLng, centerLat + offsetLat)
        val p4 = Point.fromLngLat(centerLng - offsetLng, centerLat + offsetLat)
        return listOf(p1, p2, p3, p4, p1) // 闭合
    }

    fun generateTrianglePoints(offset: Double): List<Point> {
        val p1 = Point.fromLngLat(centerLng, centerLat + offset)
        val p2 = Point.fromLngLat(centerLng - offset, centerLat - offset)
        val p3 = Point.fromLngLat(centerLng + offset, centerLat - offset)
        return listOf(p1, p2, p3, p1)
    }
}