package lib

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.result.Result
import model.Botvac
import model.Day
import model.EDay
import model.EDirection
import model.EStatus
import model.Point
import model.Schedule
import model.Time
import java.lang.Thread.sleep
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt

class BotvacController() {
    var status: EStatus = EStatus.DISCONNECTED
        private set
    var address: String = ""
        private set
    private var username: String = ""
    private var password: String = ""
    var botvac: Botvac = Botvac()
    var minPointDistance: Int = 0
        set(value) {
            field = max(value, 0)
        }
    var inaccuracyFilterRatio: Double = 0.0
        set(value) {
            field = max(value, 0.0)
        }

    constructor(address: String, username: String, password: String) : this() {
        connect(address, username, password)
    }

    constructor(minPointDistance: Int) : this() {
        this.minPointDistance = minPointDistance
    }

    constructor(inaccuracyFilterRatio: Double) : this() {
        this.inaccuracyFilterRatio = inaccuracyFilterRatio
    }

    constructor(minPointDistance: Int, inaccuracyFilterRatio: Double) : this() {
        this.minPointDistance = minPointDistance
        this.inaccuracyFilterRatio = inaccuracyFilterRatio
    }

    constructor(
        address: String,
        username: String,
        password: String,
        minPointDistance: Int
    ) : this() {
        this.minPointDistance = minPointDistance
        connect(address, username, password)
    }

    constructor(
        address: String,
        username: String,
        password: String,
        inaccuracyFilterRatio: Double
    ) : this() {
        this.inaccuracyFilterRatio = inaccuracyFilterRatio
        connect(address, username, password)
    }

    constructor(
        address: String,
        username: String,
        password: String,
        minPointDistance: Int,
        inaccuracyFilterRatio: Double
    ) : this() {
        this.minPointDistance = minPointDistance
        this.inaccuracyFilterRatio = inaccuracyFilterRatio
        connect(address, username, password)
    }

    fun connect(address: String, username: String, password: String) {
        if (status == EStatus.DISCONNECTED) {
            this.address = address
            this.username = username
            this.password = password
            status = EStatus.CONNECTED
            sendCommand("TestMode On")
            sendCommand("SetLDSRotation On")
            sleep(3000)
        }
    }

    fun disconnect() {
        if (status == EStatus.CONNECTED) {
            sendCommand("SetLDSRotation Off")
            sendCommand("TestMode Off")
            sleep(3000)
        }
        status = EStatus.DISCONNECTED
        address = ""
        username = ""
        password = ""
        botvac = Botvac()
    }

    fun updateAccelerometer() {
        if (status == EStatus.CONNECTED) {
            val lines = sendCommand("GetAccel").lines()
            for (i in lines.indices) {
                when (i) {
                    1 -> {
                        botvac.pitch = lines[i].substring(16).toDouble()
                    }

                    2 -> {
                        botvac.roll = lines[i].substring(15).toDouble()
                    }
                }
            }
        }
    }

    fun updateCharge() {
        if (status == EStatus.CONNECTED) {
            val lines = sendCommand("GetCharger").lines()
            for (i in lines.indices) {
                when (i) {
                    1 -> {
                        botvac.charge = lines[i].substring(12).toInt()
                    }
                }
            }
        }
    }

    fun updateAnalogSensors() {
        if (status == EStatus.CONNECTED) {
            val lines = sendCommand("GetAnalogSensors").lines().dropLast(1)
            for (i in lines.indices) {
                when (i) {
                    10 -> {
                        botvac.leftMagnetStrength = lines[i].substring(18).dropLast(1).toInt()
                    }

                    11 -> {
                        botvac.rightMagnetStrength = lines[i].substring(19).dropLast(1).toInt()
                    }

                    12 -> {
                        botvac.wallDistance = lines[i].substring(14).dropLast(1).toInt()
                    }

                    13 -> {
                        botvac.leftDropDistance = lines[i].substring(18).dropLast(1).toInt()
                    }

                    14 -> {
                        botvac.rightDropDistance = lines[i].substring(19).dropLast(1).toInt()
                    }
                }
            }
        }
    }

    fun updateDigitalSensors() {
        if (status == EStatus.CONNECTED) {
            val lines = sendCommand("GetDigitalSensors").lines()
            for (i in lines.indices) {
                when (i) {
                    3 -> {
                        botvac.leftWheelExtended = lines[i].substring(25).toInt() != 0
                    }

                    4 -> {
                        botvac.rightWheelExtended = lines[i].substring(26).toInt() != 0
                    }

                    5 -> {
                        botvac.leftSideBumperPressed = lines[i].substring(9).toInt() != 0
                    }

                    6 -> {
                        botvac.leftFrontBumperPressed = lines[i].substring(10).toInt() != 0
                    }

                    8 -> {
                        botvac.rightSideBumperPressed = lines[i].substring(9).toInt() != 0
                    }

                    9 -> {
                        botvac.rightFrontBumperPressed = lines[i].substring(10).toInt() != 0
                    }
                }
            }
        }
    }

    fun updateLidar() {
        if (status == EStatus.CONNECTED) {
            val lines = sendCommand("GetLDSScan").lines().drop(1).dropLast(2)
            botvac.scan.points.clear()
            for (i in lines.indices) {
                var unique = true
                val distance = lines[i].substringAfter(',').substringBefore(',').toInt()
                if (distance > 7500 || distance == 0) {
                    continue
                }
                botvac.scan.points.add(
                    Point(
                        (distance * sin(-i * (PI / 180))).roundToInt(),
                        (distance * cos(-i * (PI / 180))).roundToInt()
                    )
                )
                val point = Point(
                    (botvac.x + (distance * sin((-i + botvac.angle) * (PI / 180))) +
                            (-92.5 * sin(botvac.angle * (PI / 180)))).roundToInt(),
                    (botvac.y + (distance * cos((-i + botvac.angle) * (PI / 180))) +
                            (-92.5 * cos(botvac.angle * (PI / 180)))).roundToInt()
                )
                val inaccuracyFilter =
                    (sqrt(
                        (point.x - botvac.x.toDouble()).pow(2) +
                                (point.y - botvac.y.toDouble()).pow(2)
                    ) * inaccuracyFilterRatio).roundToInt()
                for (mapPoint in botvac.map.points) {
                    if (point.x >= mapPoint.x - (minPointDistance + inaccuracyFilter) &&
                        point.x <= mapPoint.x + (minPointDistance + inaccuracyFilter) &&
                        point.y >= mapPoint.y - (minPointDistance + inaccuracyFilter) &&
                        point.y <= mapPoint.y + (minPointDistance + inaccuracyFilter)
                    ) {
                        unique = false
                    }
                }
                if (unique) {
                    botvac.map.points.add(point)
                }
            }
        }
    }

    fun updateCleaningTime() {
        if (status == EStatus.CONNECTED) {
            val lines = sendCommand("GetWarranty").lines()
            for (i in lines.indices) {
                when (i) {
                    1 -> {
                        botvac.cleaningTime = lines[i].substring(29).toLong(16)
                    }
                }
            }
        }
    }

    fun updateDayAndTime() {
        if (status == EStatus.CONNECTED) {
            val lines = sendCommand("GetTime").lines()
            for (i in lines.indices) {
                when (i) {
                    0 -> {
                        botvac.day =
                            Day(EDay.entries.first { lines[i].substringBefore(' ') == it.displayName })
                        botvac.time = Time(
                            lines[i].substringAfter(' ').substringBefore(':').toInt(),
                            lines[i].substringAfter(':').substringBefore(':').toInt()
                        )
                    }
                }
            }
        }
    }

    fun updateSchedule() {
        if (status == EStatus.CONNECTED) {
            val lines = sendCommand("GetSchedule").lines().dropLast(1)
            val schedule = Schedule()
            for (i in lines.indices) {
                when (i) {
                    0 -> {
                        schedule.isEnabled = lines[i].substring(12) == "Enabled"
                    }

                    else -> {
                        val time: Time? = if (lines[i].substring(10) == "H") {
                            Time(
                                lines[i].substring(4, 6).toInt(),
                                lines[i].substring(7, 9).toInt()
                            )
                        } else {
                            null
                        }
                        when (i) {
                            1 -> {
                                schedule.sunday = time
                            }

                            2 -> {
                                schedule.monday = time
                            }

                            3 -> {
                                schedule.tuesday = time
                            }

                            4 -> {
                                schedule.wednesday = time
                            }

                            5 -> {
                                schedule.thursday = time
                            }

                            6 -> {
                                schedule.friday = time
                            }

                            7 -> {
                                schedule.saturday = time
                            }
                        }
                    }
                }
            }
        }
    }

    fun updateAll() {
        updateAccelerometer()
        updateCharge()
        updateAnalogSensors()
        updateDigitalSensors()
        updateLidar()
        updateCleaningTime()
        updateDayAndTime()
        updateSchedule()
    }

    fun moveRobot(distance: Int, speed: Int) {
        if (status == EStatus.CONNECTED) {
            val validDistance = min(max(distance, -10000), 10000)
            val validSpeed = min(max(speed, 1), 350)
            sendCommand("SetMotor LWheelDist $validDistance RWheelDist $validDistance Speed $validSpeed")
            botvac.x += (distance * sin(botvac.angle * (PI / 180))).roundToInt()
            botvac.y += (distance * cos(botvac.angle * (PI / 180))).roundToInt()
            sleep((ceil(abs(distance).toDouble() / validSpeed) * 1250).toLong())
        }
    }

    fun rotateRobot(angle: Int, speed: Int) {
        if (status == EStatus.CONNECTED) {
            val validAngle = min(max(angle, -359), 359)
            val validSpeed = min(max(speed, 1), 350)
            val distance = (validAngle * ((250 * PI) / 360)).roundToInt()
            sendCommand("SetMotor LWheelDist $distance RWheelDist ${-1 * distance} Speed $validSpeed")
            botvac.angle = (botvac.angle + validAngle + 360) % 360
            sleep((ceil(abs(distance).toDouble() / validSpeed) * 1250).toLong())
        }
    }

    fun controlBrush(rpm: Int) {
        if (status == EStatus.CONNECTED) {
            sendCommand("SetMotor Brush RPM ${min(max(rpm, 0), 10000)}")
            botvac.brushRpm = min(max(rpm, 0), 10000)
        }
    }

    fun controlVacuum(dutyCycle: Int) {
        if (status == EStatus.CONNECTED) {
            sendCommand("SetMotor VacuumOn VacuumSpeed ${min(max(dutyCycle, 0), 100)}")
        }
    }

    fun controlSideBrush(enable: Boolean) {
        if (status == EStatus.CONNECTED) {
            if (enable) {
                sendCommand("SetMotor SideBrushOn SideBrushPower 5000")
            } else {
                sendCommand("SetMotor SideBrushOff")
            }
        }
    }

    fun moveToPoint(point: Point, speed: Int) {
        if (status == EStatus.CONNECTED) {
            var direction = EDirection.DIRECTION_UP
            var distance = abs(point.y - botvac.y)
            if (point.x < botvac.x) {
                distance = abs(point.x - botvac.x)
                direction = EDirection.DIRECTION_LEFT
            } else if (point.x > botvac.x) {
                distance = abs(point.x - botvac.x)
                direction = EDirection.DIRECTION_RIGHT
            } else if (point.y < botvac.y) {
                direction = EDirection.DIRECTION_DOWN
            }
            if (botvac.angle != (direction.ordinal * 90)) {
                var angleToGo = (direction.ordinal * 90) - botvac.angle
                if (abs(angleToGo) == 270) {
                    angleToGo /= -3
                }
                rotateRobot(angleToGo, speed)
            }
            moveRobot(distance, speed)
        }
    }

    fun uploadDayAndTime(day: Day, time: Time) {
        if (status == EStatus.CONNECTED) {
            sendCommand("SetTime Day ${(day.value.ordinal + 1) % 7} Hour ${time.hour} Min ${time.minute}")
            botvac.day = day
            botvac.time = time
        }
    }

    fun uploadSchedule(schedule: Schedule) {
        if (status == EStatus.CONNECTED) {
            if (schedule.isEnabled) {
                sendCommand("SetSchedule ON")
            } else {
                sendCommand("SetSchedule OFF")
            }
            for (i in 0..6) {
                val time = when (i) {
                    0 -> schedule.sunday
                    1 -> schedule.monday
                    2 -> schedule.tuesday
                    3 -> schedule.wednesday
                    4 -> schedule.thursday
                    5 -> schedule.friday
                    6 -> schedule.saturday
                    else -> null
                }
                time?.also {
                    sendCommand("SetSchedule Day $i Hour ${it.hour} Min ${it.minute}")
                } ?: run {
                    sendCommand("SetSchedule Day $i Hour 0 Min 0 None")
                }
            }
            botvac.schedule = schedule
        }
    }

    fun cleanHouse() {
        if (status == EStatus.CONNECTED) {
            sendCommand("SetLDSRotation Off")
            sendCommand("TestMode Off")
            sendCommand("Clean House")
            status = EStatus.CLEANING_HOUSE
        }
    }

    fun cleanSpot() {
        if (status == EStatus.CONNECTED) {
            sendCommand("SetLDSRotation Off")
            sendCommand("TestMode Off")
            sendCommand("Clean Spot")
            status = EStatus.CLEANING_SPOT
        }
    }

    fun cleanSpot(width: Int, height: Int) {
        if (status == EStatus.CONNECTED) {
            sendCommand("SetLDSRotation Off")
            sendCommand("TestMode Off")
            sendCommand(
                "Clean Spot Width ${min(max(width, 100), 500)}" +
                        " Height ${min(max(height, 100), 500)}"
            )
            status = EStatus.CLEANING_SPOT
        }
    }

    fun stopCleaning() {
        if (status == EStatus.CLEANING_HOUSE || status == EStatus.CLEANING_SPOT) {
            sendCommand("Clean Stop")
            sendCommand("TestMode On")
            sendCommand("SetLDSRotation On")
            sleep(3000)
            status = EStatus.CONNECTED
        }
    }

    fun runRawCommand(command: String): String {
        if (status == EStatus.CONNECTED) {
            return sendCommand(command)
        }
        return ""
    }

    private fun sendCommand(command: String): String {
        var rawResponse = ""
        if (status != EStatus.DISCONNECTED) {
            val (request, response, result) = Fuel.upload(
                address,
                parameters = listOf("command" to command)
            )
                .authentication()
                .basic(username, password)
                .responseString()
            when (result) {
                is Result.Failure -> {
                    throw result.getException()
                }

                is Result.Success -> {
                    rawResponse = response.body().asString("text/plain")
                }
            }
        }
        return rawResponse.substringAfter('\n').substringBeforeLast('\n')
    }
}
