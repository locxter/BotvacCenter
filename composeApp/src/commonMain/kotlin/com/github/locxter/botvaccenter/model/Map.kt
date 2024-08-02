package com.github.locxter.botvaccenter.model

import java.io.Serializable

data class Map(
    val points: MutableList<Point> = mutableListOf()
) : Serializable {
    fun toIcpPointCloud(): IcpPointCloud {
        return IcpPointCloud(points.map { it.toIcpPoint() }.toMutableList())
    }
}
