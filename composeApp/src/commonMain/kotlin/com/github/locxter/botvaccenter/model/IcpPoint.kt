package com.github.locxter.botvaccenter.model

import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

data class IcpPoint(
    val x: Double = 0.0,
    val y: Double = 0.0
) {
    fun toPoint(): Point {
        return Point(x.roundToInt(), y.roundToInt())
    }

    fun getDistanceTo(otherPoint: IcpPoint): Double {
        return sqrt((this.x - otherPoint.x).pow(2) + (this.y - otherPoint.y).pow(2))
    }
}