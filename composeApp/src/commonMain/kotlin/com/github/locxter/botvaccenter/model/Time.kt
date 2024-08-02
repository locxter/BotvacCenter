package com.github.locxter.botvaccenter.model

import java.io.Serializable
import kotlin.math.max
import kotlin.math.min

class Time : Serializable {
    val hour: Int
    val minute: Int

    constructor() {
        hour = 0
        minute = 0
    }

    constructor(hour: Int, minute: Int) {
        this.hour = min(max(hour, 0), 23)
        this.minute = min(max(minute, 0), 59)
    }

    fun getFormated(): String {
        return "${if (hour < 10) "0$hour" else hour}:${if (minute < 10) "0$minute" else minute}"
    }
}
