package com.github.locxter.botvaccenter.model

import java.io.Serializable

data class Botvac(
    var pitch: Double = 0.0,
    var roll: Double = 0.0,
    var charge: Int = 0,
    var leftMagnetStrength: Int = 0,
    var rightMagnetStrength: Int = 0,
    var wallDistance: Int = 0,
    var leftDropDistance: Int = 0,
    var rightDropDistance: Int = 0,
    var leftWheelExtended: Boolean = false,
    var rightWheelExtended: Boolean = false,
    var leftFrontBumperPressed: Boolean = false,
    var rightFrontBumperPressed: Boolean = false,
    var leftSideBumperPressed: Boolean = false,
    var rightSideBumperPressed: Boolean = false,
    var scan: Scan = Scan(),
    var map: Map = Map(),
    var x: Int = 0,
    var y: Int = 0,
    var angle: Int = 0,
    var cleaningTime: Long = 0,
    var day: Day = Day(),
    var time: Time = Time(),
    var schedule: Schedule = Schedule(),
    var brushRpm: Int = 0,
    var vacuumDutyCycle: Int = 0,
    var sideBrushEnabled: Boolean = false,
) : Serializable
