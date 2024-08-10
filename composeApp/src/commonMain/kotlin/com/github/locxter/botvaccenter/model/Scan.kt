package com.github.locxter.botvaccenter.model

import java.io.Serializable
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

data class Scan(
    val points: MutableList<Point> = mutableListOf()
) : Serializable {
    fun toIcpPointCloud(): IcpPointCloud {
        return IcpPointCloud(points.map { it.toIcpPoint() }.toMutableList())
    }
}
