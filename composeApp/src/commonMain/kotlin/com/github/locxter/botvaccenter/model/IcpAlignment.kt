package com.github.locxter.botvaccenter.model

import java.io.Serializable

data class IcpAlignment(
    val translation: IcpPoint = IcpPoint(),
    val rotation: Double = 0.0
) : Serializable
