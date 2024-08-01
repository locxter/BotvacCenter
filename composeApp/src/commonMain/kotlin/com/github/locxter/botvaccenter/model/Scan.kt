package com.github.locxter.botvaccenter.model

data class Scan(
    val points: MutableList<Point> = mutableListOf()
) {
    fun toICPPointCloud(): IcpPointCloud {
        return IcpPointCloud(points.map { it.toICPPoint() }.toMutableList())
    }
}
