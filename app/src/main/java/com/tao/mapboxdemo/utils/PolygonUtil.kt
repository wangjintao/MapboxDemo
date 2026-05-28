package com.tao.mapboxsimple.utils

import com.mapbox.geojson.Point
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/**
 *Author: WangJintao
 * Date: 2025/11/20 15:38
 **/
object PolygonUtil {

    private const val centerLng = 116.303273
    private const val centerLat = 40.041439

    fun generateRectanglePoints(basePoint: Point = Point.fromLngLat(centerLng, centerLat), offsetLng: Double,
        offsetLat: Double): List<Point> {
        val p1 = Point.fromLngLat(basePoint.longitude() - offsetLng, basePoint.latitude() - offsetLat)
        val p2 = Point.fromLngLat(basePoint.longitude() + offsetLng, basePoint.latitude() - offsetLat)
        val p3 = Point.fromLngLat(basePoint.longitude() + offsetLng, basePoint.latitude() + offsetLat)
        val p4 = Point.fromLngLat(basePoint.longitude() - offsetLng, basePoint.latitude() + offsetLat)
        return listOf(p1, p2, p3, p4, p1) // 闭合
    }

    fun generateTrianglePoints(basePoint: Point = Point.fromLngLat(centerLng, centerLat), offset: Double): List<Point> {
        val p1 = Point.fromLngLat(basePoint.longitude(), basePoint.latitude() + offset)
        val p2 = Point.fromLngLat(basePoint.longitude() - offset, basePoint.latitude() - offset)
        val p3 = Point.fromLngLat(basePoint.longitude() + offset, basePoint.latitude() - offset)
        return listOf(p1, p2, p3, p1)
    }

    /**
     * 生成不规则多边形边界点
     *
     * @param pointCount 顶点数量，最少3个
     * @param baseRadius 基础半径（越大图形越大）
     * @param irregularity 不规则程度 0f~1f
     * 不规则程度：
     * 0f 接近规则圆形
     * 0.2f 轻微不规则
     * 0.5f 明显不规则
     * 1f 非常随机
     *
     * @return 闭合多边形点集
     */
    fun generateIrregularPolygonPoints(basePoint: Point = Point.fromLngLat(centerLng, centerLat),
        pointCount: Int, baseRadius: Double, irregularity: Float = 0.3f): List<Point> {

        require(pointCount >= 3) {
            "pointCount 必须 >= 3"
        }

        val points = mutableListOf<Point>()

        // 每个点平均角度
        val angleStep = 360.0 / pointCount

        for (i in 0 until pointCount) {

            // 当前角度
            val angleDeg = i * angleStep
            val angleRad = Math.toRadians(angleDeg)

            // 随机半径
            val randomFactor = 1 +
                    Random.nextDouble(
                        -irregularity.toDouble(),
                        irregularity.toDouble()
                    )

            val radius = baseRadius * randomFactor

            // 经纬度偏移
            val lng = basePoint.longitude() + radius * cos(angleRad)
            val lat = basePoint.latitude() + radius * sin(angleRad)

            points.add(Point.fromLngLat(lng, lat))
        }

        // 闭合多边形
        points.add(points.first())

        return points
    }
}