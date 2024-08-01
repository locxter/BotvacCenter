package com.github.locxter.botvaccenter.model

data class Scan(
    val points: MutableList<Point> = mutableListOf()
) {
    fun toIcpPointCloud(): IcpPointCloud {
        return IcpPointCloud(points.map { it.toIcpPoint() }.toMutableList())
    }
}
