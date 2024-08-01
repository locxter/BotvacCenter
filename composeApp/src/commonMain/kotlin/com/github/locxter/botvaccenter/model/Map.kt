package com.github.locxter.botvaccenter.model

data class Map(
    val points: MutableList<Point> = mutableListOf()
) {
    fun toIcpPointCloud(): IcpPointCloud {
        return IcpPointCloud(points.map { it.toIcpPoint() }.toMutableList())
    }
}
