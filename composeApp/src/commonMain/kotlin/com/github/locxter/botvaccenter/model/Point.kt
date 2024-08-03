package com.github.locxter.botvaccenter.model

import java.io.Serializable

data class Point(
    val x: Int = 0,
    val y: Int = 0
) : Serializable {
    fun toIcpPoint(): IcpPoint {
        return IcpPoint(x.toDouble(), y.toDouble())
    }
}
