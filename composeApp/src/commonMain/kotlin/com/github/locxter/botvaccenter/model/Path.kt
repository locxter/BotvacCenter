package com.github.locxter.botvaccenter.model

data class Path(
    val points: MutableList<Point> = mutableListOf()
) {
    fun getDeepCopy(): Path {
        return Path(points.map { Point(it.x, it.y) }.toMutableList())
    }
}
