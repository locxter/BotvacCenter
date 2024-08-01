package com.github.locxter.botvaccenter.model

data class Map(
    val points: MutableList<Point> = mutableListOf()
) {
    fun toICPPointCloud(): IcpPointCloud {
        return IcpPointCloud(points.map { it.toICPPoint() }.toMutableList())
    }
}
