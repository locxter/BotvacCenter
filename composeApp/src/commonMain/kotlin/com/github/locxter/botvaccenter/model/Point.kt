package com.github.locxter.botvaccenter.model

data class Point(
    val x: Int = 0,
    val y: Int = 0
) {
    fun toIcpPoint(): IcpPoint {
        return IcpPoint(x.toDouble(), y.toDouble())
    }
}
