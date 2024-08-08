package com.github.locxter.botvaccenter.model

import java.io.Serializable
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

data class IcpPointCloud(
    val points: MutableList<IcpPoint> = mutableListOf()
): Serializable {
    fun moveBy(translation: IcpPoint) {
        points.replaceAll {
            IcpPoint(it.x + translation.x, it.y + translation.y)
        }
    }

    fun rawRotateBy(rotation: Double) {
        val radians = rotation * (PI / 180.0)
        points.replaceAll {
            IcpPoint(
                (it.x * cos(radians)) - (it.y * sin(radians)),
                (it.x * sin(radians)) + (it.y * cos(radians))
            )
        }
    }

    fun rotateBy(rotation: Double, mean: IcpPoint = getMean()) {
        moveBy(IcpPoint(-mean.x, -mean.y))
        rawRotateBy(rotation)
        moveBy(mean)
    }

    fun getMean(): IcpPoint {
        var mean = IcpPoint()
        for (point in points) {
            mean = IcpPoint(mean.x + point.x, mean.y + point.y)
        }
        mean = IcpPoint(mean.x / points.size.toDouble(), mean.y / points.size.toDouble())
        return mean
    }

    fun getClosestPointTo(otherPoint: IcpPoint): IcpPoint {
        var closestPoint = IcpPoint()
        var minDist = Double.MAX_VALUE
        for (point in points) {
            val dist = point.getDistanceTo(otherPoint)
            if (dist < minDist) {
                minDist = dist
                closestPoint = point
            }
        }
        return closestPoint
    }

    fun getDeepCopy(): IcpPointCloud {
        return IcpPointCloud(points.map { IcpPoint(it.x, it.y) }.toMutableList())
    }
}