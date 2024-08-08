package com.github.locxter.botvaccenter.model

import java.io.Serializable
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

data class Scan(
    val points: MutableList<Point> = mutableListOf()
) : Serializable {
    fun moveBy(translation: Point) {
        points.replaceAll {
            Point(it.x + translation.x, it.y + translation.y)
        }
    }

    fun rawRotateBy(rotation: Double) {
        val radians = rotation * (PI / 180.0)
        points.replaceAll {
            Point(
                ((it.x * cos(radians)) - (it.y * sin(radians))).roundToInt(),
                ((it.x * sin(radians)) + (it.y * cos(radians))).roundToInt()
            )
        }
    }

    fun rotateBy(rotation: Double, mean: Point = getMean()) {
        moveBy(Point(-mean.x, -mean.y))
        rawRotateBy(rotation)
        moveBy(mean)
    }

    fun getMean(): Point {
        var mean = Point()
        for (point in points) {
            mean = Point(mean.x + point.x, mean.y + point.y)
        }
        mean = Point(
            (mean.x / points.size.toDouble()).roundToInt(),
            (mean.y / points.size.toDouble()).roundToInt()
        )
        return mean
    }

    fun toIcpPointCloud(): IcpPointCloud {
        return IcpPointCloud(points.map { it.toIcpPoint() }.toMutableList())
    }
}
