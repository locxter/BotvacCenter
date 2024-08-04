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
    var location: Point = Point(),
    var angle: Double = 0.0,
    var cleaningTime: Long = 0,
    var day: Day = Day(),
    var time: Time = Time(),
    var schedule: Schedule = Schedule(),
    var brushRpm: Int = 0,
    var vacuumDutyCycle: Int = 0,
    var sideBrushEnabled: Boolean = false,
    var oldScan: Scan = Scan(),
    var oldLocation: Point = Point(),
    var oldAngle: Double = 0.0,
) : Serializable {
    fun getDeepCopy(): Botvac {
        return this.copy(
            scan = Scan(scan.points.map { Point(it.x, it.y) }.toMutableList()),
            map = Map(map.points.map { Point(it.x, it.y) }.toMutableList()),
            location = Point(location.x, location.y),
            day = Day(day.value),
            time = Time(time.hour, time.minute),
            schedule = Schedule(
                monday = schedule.monday?.let { Time(it.hour, it.minute) },
                tuesday = schedule.tuesday?.let { Time(it.hour, it.minute) },
                wednesday = schedule.wednesday?.let { Time(it.hour, it.minute) },
                thursday = schedule.thursday?.let { Time(it.hour, it.minute) },
                friday = schedule.friday?.let { Time(it.hour, it.minute) },
                saturday = schedule.saturday?.let { Time(it.hour, it.minute) },
                sunday = schedule.sunday?.let { Time(it.hour, it.minute) },
            ),
            oldScan = Scan(oldScan.points.map { Point(it.x, it.y) }.toMutableList()),
            oldLocation = Point(oldLocation.x, oldLocation.y),
        )
    }
}
